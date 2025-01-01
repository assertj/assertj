/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.isProtected;
import static net.bytebuddy.matcher.ElementMatchers.isPublic;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.namedOneOf;
import static net.bytebuddy.matcher.ElementMatchers.not;
import static org.assertj.core.api.ClassLoadingStrategyFactory.classLoadingStrategy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.stream.Stream;

import org.assertj.core.api.ClassLoadingStrategyFactory.ClassLoadingStrategyPair;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.TypeCache.SimpleKey;
import net.bytebuddy.TypeCache.Sort;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.matcher.ElementMatcher.Junction;

class SoftProxies {

  private static final Junction<MethodDescription> METHODS_CHANGING_THE_OBJECT_UNDER_TEST = methodsChangingTheObjectUnderTestNamed("asBase64Decoded",
                                                                                                                                   "asBase64Encoded",
                                                                                                                                   "asBoolean",
                                                                                                                                   "asByte",
                                                                                                                                   "asDouble",
                                                                                                                                   "asFloat",
                                                                                                                                   "asInstanceOf",
                                                                                                                                   "asInt",
                                                                                                                                   "asLong",
                                                                                                                                   "asShort",
                                                                                                                                   "asString",
                                                                                                                                   "asHexString",
                                                                                                                                   "binaryContent",
                                                                                                                                   "cause",
                                                                                                                                   "content",
                                                                                                                                   "extracting",
                                                                                                                                   "extractingByKey",
                                                                                                                                   "extractingByKeys",
                                                                                                                                   "extractingFromEntries",
                                                                                                                                   "extractingResultOf",
                                                                                                                                   "filteredOn",
                                                                                                                                   "filteredOnAssertions",
                                                                                                                                   "filteredOnNull",
                                                                                                                                   "flatExtracting",
                                                                                                                                   "flatMap",
                                                                                                                                   "get",
                                                                                                                                   "getCause", // deprecated
                                                                                                                                   "getRootCause", // deprecated
                                                                                                                                   "map",
                                                                                                                                   "message",
                                                                                                                                   "newAbstractIterableAssert",
                                                                                                                                   "rootCause",
                                                                                                                                   "scale",
                                                                                                                                   "size",
                                                                                                                                   "succeedsWithin",
                                                                                                                                   "toAssert",
                                                                                                                                   "usingRecursiveComparison");

  static final Junction<MethodDescription> METHODS_NOT_TO_PROXY = methodsNamed("as").or(named("clone"))
                                                                                    .or(named("describedAs"))
                                                                                    .or(named("descriptionText"))
                                                                                    .or(named("getWritableAssertionInfo"))
                                                                                    .or(named("inBinary"))
                                                                                    .or(named("inHexadecimal"))
                                                                                    .or(named("newAbstractIterableAssert"))
                                                                                    .or(named("newObjectArrayAssert"))
                                                                                    .or(named("overridingErrorMessage"))
                                                                                    .or(named("removeCustomAssertRelatedElementsFromStackTraceIfNeeded"))
                                                                                    .or(named("succeedsWithin"))
                                                                                    .or(named("failsWithin"))
                                                                                    .or(named("usingComparator"))
                                                                                    .or(named("usingDefaultComparator"))
                                                                                    .or(named("usingElementComparator"))
                                                                                    .or(named("withAssertionInfo"))
                                                                                    .or(named("withAssertionState"))
                                                                                    .or(named("withComparatorsForElementPropertyOrFieldNames"))
                                                                                    .or(named("withComparatorsForElementPropertyOrFieldTypes"))
                                                                                    .or(named("withFailMessage"))
                                                                                    .or(named("withIterables"))
                                                                                    .or(named("withRepresentation"))
                                                                                    .or(named("withThreadDumpOnError"))
                                                                                    .or(named("withTypeComparators"));

  private static final ByteBuddy BYTE_BUDDY = new ByteBuddy().with(new AuxiliaryType.NamingStrategy.SuffixingRandom("AssertJ$SoftProxies"))
                                                             .with(TypeValidation.DISABLED);

  private static final Implementation PROXIFY_METHOD_CHANGING_THE_OBJECT_UNDER_TEST = MethodDelegation.to(ProxifyMethodChangingTheObjectUnderTest.class);
  private static final Implementation ERROR_COLLECTOR = MethodDelegation.to(ErrorCollector.class);

  private static final TypeCache<TypeCache.SimpleKey> CACHE = new TypeCache.WithInlineExpunction<>(Sort.SOFT);

  private ErrorCollector collector;

  public SoftProxies(AssertionErrorCollector assertionErrorCollector) {
    collector = new ErrorCollector(assertionErrorCollector);
  }

  <SELF extends Assert<? extends SELF, ? extends ACTUAL>, ACTUAL> SELF createSoftAssertionProxy(Class<SELF> assertClass,
                                                                                                Class<ACTUAL> actualClass,
                                                                                                ACTUAL actual) {
    try {
      Class<? extends SELF> proxyClass = createSoftAssertionProxyClass(assertClass);
      Constructor<? extends SELF> constructor = proxyClass.getConstructor(actualClass);
      SELF proxiedAssert = constructor.newInstance(actual);
      // instance is a AssertJProxySetup since it is a generated proxy implementing it (see createProxy)
      ((AssertJProxySetup) proxiedAssert).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return proxiedAssert;
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <ASSERT extends Assert<?, ?>> Class<ASSERT> createSoftAssertionProxyClass(Class<ASSERT> assertClass) {
    SimpleKey cacheKey = new SimpleKey(assertClass);
    return (Class<ASSERT>) CACHE.findOrInsert(assertClass.getClassLoader(), cacheKey,
                                              () -> generateProxyClass(assertClass));
  }

  FileSizeAssert<?> createFileSizeAssertProxy(FileSizeAssert<?> fileSizeAssert) {
    Class<?> proxyClass = createSoftAssertionProxyClass(FileSizeAssert.class);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractFileAssert.class);
      FileSizeAssert<?> proxiedAssert = (FileSizeAssert<?>) constructor.newInstance(fileSizeAssert.returnToFile());
      ((AssertJProxySetup) proxiedAssert).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return proxiedAssert;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  BigDecimalScaleAssert<?> createBigDecimalScaleAssertProxy(BigDecimalScaleAssert<?> bigDecimalScaleAssert) {
    Class<?> proxyClass = createSoftAssertionProxyClass(BigDecimalScaleAssert.class);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractBigDecimalAssert.class);
      BigDecimalScaleAssert<?> proxiedAssert = (BigDecimalScaleAssert<?>) constructor.newInstance(bigDecimalScaleAssert.returnToBigDecimal());
      ((AssertJProxySetup) proxiedAssert).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return proxiedAssert;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  IterableSizeAssert<?> createIterableSizeAssertProxy(IterableSizeAssert<?> iterableSizeAssert) {
    Class<?> proxyClass = createSoftAssertionProxyClass(IterableSizeAssert.class);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractIterableAssert.class, Integer.class);
      IterableSizeAssert<?> proxiedAssert = (IterableSizeAssert<?>) constructor.newInstance(iterableSizeAssert.returnToIterable(),
                                                                                            iterableSizeAssert.actual);
      ((AssertJProxySetup) proxiedAssert).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return proxiedAssert;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  MapSizeAssert<?, ?> createMapSizeAssertProxy(MapSizeAssert<?, ?> mapSizeAssert) {
    Class<?> proxyClass = createSoftAssertionProxyClass(MapSizeAssert.class);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractMapAssert.class, Integer.class);
      MapSizeAssert<?, ?> proxiedAssert = (MapSizeAssert<?, ?>) constructor.newInstance(mapSizeAssert.returnToMap(),
                                                                                        mapSizeAssert.actual);
      ((AssertJProxySetup) proxiedAssert).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return proxiedAssert;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  RecursiveComparisonAssert<?> createRecursiveComparisonAssertProxy(RecursiveComparisonAssert<?> recursiveComparisonAssert) {
    Class<?> proxyClass = createSoftAssertionProxyClass(RecursiveComparisonAssert.class);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(Object.class, RecursiveComparisonConfiguration.class);
      RecursiveComparisonAssert<?> proxiedAssert = (RecursiveComparisonAssert<?>) constructor.newInstance(recursiveComparisonAssert.actual,
                                                                                                          recursiveComparisonAssert.getRecursiveComparisonConfiguration());
      ((AssertJProxySetup) proxiedAssert).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return proxiedAssert;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  static <V> Class<? extends V> generateProxyClass(Class<V> assertClass) {
    ClassLoadingStrategyPair strategy = classLoadingStrategy(assertClass);
    return BYTE_BUDDY.subclass(assertClass)
                     .defineField(ProxifyMethodChangingTheObjectUnderTest.FIELD_NAME,
                                  ProxifyMethodChangingTheObjectUnderTest.class,
                                  Visibility.PRIVATE)
                     .method(METHODS_CHANGING_THE_OBJECT_UNDER_TEST)
                     .intercept(PROXIFY_METHOD_CHANGING_THE_OBJECT_UNDER_TEST)
                     .defineField(ErrorCollector.FIELD_NAME, ErrorCollector.class, Visibility.PRIVATE)
                     .method(any().and(not(METHODS_CHANGING_THE_OBJECT_UNDER_TEST))
                                  .and(not(METHODS_NOT_TO_PROXY)))
                     .intercept(ERROR_COLLECTOR)
                     .implement(AssertJProxySetup.class)
                     // set ProxifyMethodChangingTheObjectUnderTest and ErrorCollector fields on the generated proxy
                     .intercept(FieldAccessor.ofField(ProxifyMethodChangingTheObjectUnderTest.FIELD_NAME).setsArgumentAt(0)
                                             .andThen(FieldAccessor.ofField(ErrorCollector.FIELD_NAME).setsArgumentAt(1)))
                     .make()
                     .load(strategy.getClassLoader(), strategy.getClassLoadingStrategy())
                     .getLoaded();
  }

  private static Junction<MethodDescription> methodsNamed(String... names) {
    return namedOneOf(names);
  }

  private static Junction<MethodDescription> methodsChangingTheObjectUnderTestNamed(String... names) {
    Junction<MethodDescription> publicMethods = namedOneOf(names).and(isPublic());

    String[] forProxyMethodNames = Stream.of(names)
                                         .map(name -> name + "ForProxy")
                                         .toArray(String[]::new);
    Junction<MethodDescription> forProxyProtectedMethods = namedOneOf(forProxyMethodNames).and(isProtected());

    return publicMethods.or(forProxyProtectedMethods);

  }

}
