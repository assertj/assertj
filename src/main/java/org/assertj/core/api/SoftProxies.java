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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api;

import static net.bytebuddy.matcher.ElementMatchers.any;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.TypeCache.SimpleKey;
import net.bytebuddy.TypeCache.Sort;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassInjector;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.auxiliary.AuxiliaryType;
import net.bytebuddy.matcher.ElementMatcher.Junction;
import net.bytebuddy.matcher.ElementMatchers;

class SoftProxies {

  private static final Junction<MethodDescription> METHODS_CHANGING_THE_OBJECT_UNDER_TEST = methodsNamed("extracting").or(named("filteredOn"))
                                                                                                                      .or(named("filteredOnNull"))
                                                                                                                      .or(named("map"))
                                                                                                                      .or(named("asString"))
                                                                                                                      .or(named("asList"))
                                                                                                                      .or(named("size"))
                                                                                                                      .or(named("toAssert"))
                                                                                                                      .or(named("flatMap"))
                                                                                                                      .or(named("extractingResultOf"))
                                                                                                                      .or(named("flatExtracting"));

  private static final Junction<MethodDescription> METHODS_NOT_TO_PROXY = methodsNamed("as").or(named("clone"))
                                                                                            .or(named("describedAs"))
                                                                                            .or(named("descriptionText"))
                                                                                            .or(named("getWritableAssertionInfo"))
                                                                                            .or(named("inBinary"))
                                                                                            .or(named("inHexadecimal"))
                                                                                            .or(named("newAbstractIterableAssert"))
                                                                                            .or(named("newObjectArrayAssert"))
                                                                                            .or(named("removeCustomAssertRelatedElementsFromStackTraceIfNeeded"))
                                                                                            .or(named("overridingErrorMessage"))
                                                                                            .or(named("usingDefaultComparator"))
                                                                                            .or(named("usingElementComparator"))
                                                                                            .or(named("withComparatorsForElementPropertyOrFieldNames"))
                                                                                            .or(named("withComparatorsForElementPropertyOrFieldTypes"))
                                                                                            .or(named("withIterables"))
                                                                                            .or(named("withFailMessage"))
                                                                                            .or(named("withAssertionInfo"))
                                                                                            .or(named("withAssertionState"))
                                                                                            .or(named("withRepresentation"))
                                                                                            .or(named("withTypeComparators"))
                                                                                            .or(named("withThreadDumpOnError"));

  private static final ByteBuddy BYTE_BUDDY = new ByteBuddy().with(new AuxiliaryType.NamingStrategy.SuffixingRandom("AssertJ$SoftProxies"))
                                                             .with(TypeValidation.DISABLED);

  private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();
  private static final Method PRIVATE_LOOKUP_IN;

  private static final TypeCache<TypeCache.SimpleKey> CACHE = new TypeCache.WithInlineExpunction<>(Sort.SOFT);

  static {
    Method privateLookupIn;
    try {
      privateLookupIn = MethodHandles.class.getMethod("privateLookupIn", Class.class, MethodHandles.Lookup.class);
    } catch (Exception e) {
      privateLookupIn = null;
    }
    PRIVATE_LOOKUP_IN = privateLookupIn;
  }

  private final ErrorCollector collector = new ErrorCollector();

  public boolean wasSuccess() {
    return collector.wasSuccess();
  }

  void collectError(Throwable error) {
    collector.addError(error);
  }

  List<Throwable> errorsCollected() {
    return collector.errors();
  }

  // TODO V extends AbstractAssert ?
  <V, T> V create(Class<V> assertClass, Class<T> actualClass, T actual) {
    try {
      Class<? extends V> proxyClass = createProxy(assertClass);
      Constructor<? extends V> constructor = proxyClass.getConstructor(actualClass);
      V proxyInstance = constructor.newInstance(actual);
      // instance is a AssertJProxySetup since it is a generated proxy implementing it (see createProxy)
      ((AssertJProxySetup) proxyInstance).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return proxyInstance;
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  private static <V> Class<? extends V> createProxy(Class<V> assertClass) {
    return (Class<V>) CACHE.findOrInsert(SoftProxies.class.getClassLoader(),
                                         new SimpleKey(assertClass),
                                         () -> BYTE_BUDDY.subclass(assertClass)
                                                         .defineField(ProxifyMethodChangingTheObjectUnderTest.FIELD_NAME,
                                                                      ProxifyMethodChangingTheObjectUnderTest.class,
                                                                      Visibility.PRIVATE)
                                                         .method(METHODS_CHANGING_THE_OBJECT_UNDER_TEST)
                                                         .intercept(MethodDelegation.to(ProxifyMethodChangingTheObjectUnderTest.class))
                                                         .defineField(ErrorCollector.FIELD_NAME, ErrorCollector.class,
                                                                      Visibility.PRIVATE)
                                                         .method(any().and(not(METHODS_CHANGING_THE_OBJECT_UNDER_TEST))
                                                                      .and(not(METHODS_NOT_TO_PROXY)))
                                                         .intercept(MethodDelegation.to(ErrorCollector.class))
                                                         //
                                                         .implement(AssertJProxySetup.class)
                                                         .intercept(FieldAccessor.ofField(ProxifyMethodChangingTheObjectUnderTest.FIELD_NAME)
                                                                                 .setsArgumentAt(0)
                                                                                 .andThen(FieldAccessor.ofField(ErrorCollector.FIELD_NAME)
                                                                                                       .setsArgumentAt(1)))
                                                         .make()
                                                         // Use ClassLoader of soft assertion class to allow ByteBuddy to always
                                                         // find it. This is needed in OSGI runtime when custom soft assertion is
                                                         // defined outside of assertj bundle.
                                                         .load(assertClass.getClassLoader(), classLoadingStrategy(assertClass))
                                                         .getLoaded());
  }

  private static ClassLoadingStrategy<ClassLoader> classLoadingStrategy(Class<?> assertClass) {
    if (ClassInjector.UsingReflection.isAvailable()) {
      return ClassLoadingStrategy.Default.INJECTION;
    } else if (ClassInjector.UsingLookup.isAvailable()) {
      try {
        return ClassLoadingStrategy.UsingLookup.of(PRIVATE_LOOKUP_IN.invoke(null, assertClass, LOOKUP));
      } catch (Exception e) {
        throw new IllegalStateException("Could not access package of " + assertClass, e);
      }
    } else {
      throw new IllegalStateException("No code generation strategy available");
    }
  }

  private static Junction<MethodDescription> methodsNamed(String name) {
    return ElementMatchers.<MethodDescription>named(name);
  }

  IterableSizeAssert<?> createIterableSizeAssertProxy(IterableSizeAssert<?> iterableSizeAssert) {
    Class<?> proxyClass = createProxy(IterableSizeAssert.class);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractIterableAssert.class, Integer.class);
      IterableSizeAssert<?> instance = (IterableSizeAssert<?>) constructor.newInstance(iterableSizeAssert.returnToIterable(),
                                                                                       iterableSizeAssert.actual);
      ((AssertJProxySetup) instance).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return instance;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  MapSizeAssert<?, ?> createMapSizeAssertProxy(MapSizeAssert<?, ?> mapSizeAssert) {
    Class<?> proxyClass = createProxy(MapSizeAssert.class);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractMapAssert.class, Integer.class);
      MapSizeAssert<?, ?> instance = (MapSizeAssert<?, ?>) constructor.newInstance(mapSizeAssert.returnToMap(),
                                                                                   mapSizeAssert.actual);
      ((AssertJProxySetup) instance).assertj$setup(new ProxifyMethodChangingTheObjectUnderTest(this), collector);
      return instance;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
