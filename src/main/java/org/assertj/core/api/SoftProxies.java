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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.TypeCache.SimpleKey;
import net.bytebuddy.TypeCache.Sort;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.implementation.MethodDelegation;
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

  private static ByteBuddy byteBuddy = new ByteBuddy().with(TypeValidation.DISABLED);

  private final ErrorCollector collector = new ErrorCollector();
  private final TypeCache<TypeCache.SimpleKey> cache = new TypeCache.WithInlineExpunction<>(Sort.SOFT);

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
  @SuppressWarnings("unchecked")
  <V, T> V create(final Class<V> assertClass, Class<T> actualClass, T actual) {

    try {
      ClassLoader classLoader = getClass().getClassLoader();
      SimpleKey key = new SimpleKey(assertClass);
      Class<V> proxyClass = (Class<V>) cache.findOrInsert(classLoader, key, () -> createProxy(assertClass, collector));

      Constructor<? extends V> constructor = proxyClass.getConstructor(actualClass);
      return constructor.newInstance(actual);
    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }

  private <V> Class<?> createProxy(Class<V> assertClass, ErrorCollector collector) {

    return byteBuddy.subclass(assertClass)
                    .method(METHODS_CHANGING_THE_OBJECT_UNDER_TEST)
                    .intercept(MethodDelegation.to(new ProxifyMethodChangingTheObjectUnderTest(this)))
                    .method(any().and(not(METHODS_CHANGING_THE_OBJECT_UNDER_TEST))
                                 .and(not(METHODS_NOT_TO_PROXY)))
                    .intercept(MethodDelegation.to(collector))
                    .make()
                    // Use ClassLoader of soft assertion class to allow ByteBuddy to always find it.
                    // This is needed in OSGI runtime when custom soft assertion is defined outside of assertj bundle.
                    .load(assertClass.getClassLoader())
                    .getLoaded();
  }

  private static Junction<MethodDescription> methodsNamed(String name) {
    return ElementMatchers.<MethodDescription> named(name);
  }

  IterableSizeAssert<?> createIterableSizeAssertProxy(IterableSizeAssert<?> iterableSizeAssert) {
    Class<?> proxyClass = createProxy(IterableSizeAssert.class, collector);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractIterableAssert.class, Integer.class);
      return (IterableSizeAssert<?>) constructor.newInstance(iterableSizeAssert.returnToIterable(), iterableSizeAssert.actual);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  MapSizeAssert<?, ?> createMapSizeAssertProxy(MapSizeAssert<?, ?> mapSizeAssert) {
    Class<?> proxyClass = createProxy(MapSizeAssert.class, collector);
    try {
      Constructor<?> constructor = proxyClass.getConstructor(AbstractMapAssert.class, Integer.class);
      return (MapSizeAssert<?, ?>) constructor.newInstance(mapSizeAssert.returnToMap(), mapSizeAssert.actual);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
