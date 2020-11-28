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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.util.Preconditions.checkState;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

public class ProxifyMethodChangingTheObjectUnderTest {

  public static final String FIELD_NAME = "dispatcher";

  private final SoftProxies proxies;

  ProxifyMethodChangingTheObjectUnderTest(SoftProxies proxies) {
    this.proxies = proxies;
  }

  @RuntimeType
  public static AbstractAssert<?, ?> intercept(@FieldValue(FIELD_NAME) ProxifyMethodChangingTheObjectUnderTest dispatcher,
                                               @SuperCall Callable<AbstractAssert<?, ?>> assertionMethod,
                                               @This AbstractAssert<?, ?> currentAssertInstance) throws Exception {
    AbstractAssert<?, ?> result = assertionMethod.call();
    return dispatcher.createAssertProxy(result).withAssertionState(currentAssertInstance);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private AbstractAssert<?, ?> createAssertProxy(AbstractAssert<?, ?> currentAssert) {
    if (currentAssert instanceof IterableSizeAssert) return createIterableSizeAssertProxy(currentAssert);
    if (currentAssert instanceof MapSizeAssert) return createMapSizeAssertProxy(currentAssert);
    if (currentAssert instanceof RecursiveComparisonAssert)
      return createRecursiveComparisonAssertProxy((RecursiveComparisonAssert) currentAssert);
    return (AbstractAssert) proxies.createSoftAssertionProxy(currentAssert.getClass(), actualClass(currentAssert),
                                                             actual(currentAssert));
  }

  private RecursiveComparisonAssert<?> createRecursiveComparisonAssertProxy(RecursiveComparisonAssert<?> currentAssert) {
    return proxies.createRecursiveComparisonAssertProxy(currentAssert);
  }

  private MapSizeAssert<?, ?> createMapSizeAssertProxy(Object currentAssert) {
    MapSizeAssert<?, ?> mapSizeAssert = (MapSizeAssert<?, ?>) currentAssert;
    // can't use the usual way of building soft proxy since MapSizeAssert takes 2 parameters
    return proxies.createMapSizeAssertProxy(mapSizeAssert);
  }

  private IterableSizeAssert<?> createIterableSizeAssertProxy(Object currentAssert) {
    IterableSizeAssert<?> iterableSizeAssert = (IterableSizeAssert<?>) currentAssert;
    // can't use the usual way of building soft proxy since IterableSizeAssert takes 2 parameters
    return proxies.createIterableSizeAssertProxy(iterableSizeAssert);
  }

  @SuppressWarnings("rawtypes")
  private static Class actualClass(Object currentAssert) {
    if (currentAssert instanceof AbstractObjectArrayAssert) return Array.newInstance(Object.class, 0).getClass();
    if (currentAssert instanceof StringAssert) return String.class;
    if (currentAssert instanceof RecursiveComparisonAssert) return Object.class;
    if (currentAssert instanceof AtomicIntegerFieldUpdaterAssert) return AtomicIntegerFieldUpdater.class;
    if (currentAssert instanceof AtomicLongFieldUpdaterAssert) return AtomicLongFieldUpdater.class;
    if (currentAssert instanceof AtomicMarkableReferenceAssert) return AtomicMarkableReference.class;
    if (currentAssert instanceof AtomicReferenceAssert) return AtomicReference.class;
    if (currentAssert instanceof AtomicReferenceArrayAssert) return AtomicReferenceArray.class;
    if (currentAssert instanceof AtomicReferenceFieldUpdaterAssert) return AtomicReferenceFieldUpdater.class;
    if (currentAssert instanceof AtomicStampedReferenceAssert) return AtomicStampedReference.class;
    if (currentAssert instanceof BigDecimalAssert) return BigDecimal.class;
    if (currentAssert instanceof BigIntegerAssert) return BigInteger.class;
    if (currentAssert instanceof BooleanAssert) return Boolean.class;
    if (currentAssert instanceof BooleanArrayAssert) return boolean[].class;
    if (currentAssert instanceof ByteAssert) return Byte.class;
    if (currentAssert instanceof ByteArrayAssert) return byte[].class;
    if (currentAssert instanceof CharArrayAssert) return char[].class;
    if (currentAssert instanceof CharSequenceAssert) return CharSequence.class;
    if (currentAssert instanceof CharacterAssert) return Character.class;
    if (currentAssert instanceof ClassAssert) return Class.class;
    if (currentAssert instanceof CompletableFutureAssert) return CompletionStage.class;
    if (currentAssert instanceof DateAssert) return Date.class;
    if (currentAssert instanceof DoubleAssert) return Double.class;
    if (currentAssert instanceof DoubleArrayAssert) return double[].class;
    if (currentAssert instanceof DoublePredicateAssert) return DoublePredicate.class;
    if (currentAssert instanceof DurationAssert) return Duration.class;
    if (currentAssert instanceof PeriodAssert) return Period.class;
    if (currentAssert instanceof FileAssert) return File.class;
    if (currentAssert instanceof FloatAssert) return Float.class;
    if (currentAssert instanceof FloatArrayAssert) return float[].class;
    if (currentAssert instanceof FutureAssert) return Future.class; // must be after CompletionStage / CompletableFuture
    if (currentAssert instanceof InputStreamAssert) return InputStream.class;
    if (currentAssert instanceof InstantAssert) return Instant.class;
    if (currentAssert instanceof IntArrayAssert) return int[].class;
    if (currentAssert instanceof IntPredicateAssert) return IntPredicate.class;
    if (currentAssert instanceof IntegerAssert) return Integer.class;
    if (currentAssert instanceof IteratorAssert) return Iterator.class;
    if (currentAssert instanceof LocalDateAssert) return LocalDate.class;
    if (currentAssert instanceof LocalDateTimeAssert) return LocalDateTime.class;
    if (currentAssert instanceof LongAdderAssert) return LongAdder.class;
    if (currentAssert instanceof LongArrayAssert) return long[].class;
    if (currentAssert instanceof LongAssert) return Long.class;
    if (currentAssert instanceof LongPredicateAssert) return LongPredicate.class;
    if (currentAssert instanceof MapAssert) return Map.class;
    if (currentAssert instanceof ObjectAssert || currentAssert instanceof ProxyableObjectAssert) return Object.class;
    if (currentAssert instanceof OffsetDateTimeAssert) return OffsetDateTime.class;
    if (currentAssert instanceof OffsetTimeAssert) return OffsetTime.class;
    if (currentAssert instanceof OptionalAssert) return Optional.class;
    if (currentAssert instanceof OptionalDoubleAssert) return OptionalDouble.class;
    if (currentAssert instanceof OptionalIntAssert) return OptionalInt.class;
    if (currentAssert instanceof OptionalLongAssert) return OptionalLong.class;
    if (currentAssert instanceof PathAssert) return Path.class;
    if (currentAssert instanceof PredicateAssert) return Predicate.class;
    if (currentAssert instanceof ShortAssert) return Short.class;
    if (currentAssert instanceof ShortArrayAssert) return short[].class;
    if (currentAssert instanceof ThrowableAssert) return Throwable.class;
    if (currentAssert instanceof ThrowableAssertAlternative) return Throwable.class;
    if (currentAssert instanceof UriAssert) return URI.class;
    if (currentAssert instanceof UrlAssert) return URL.class;
    if (currentAssert instanceof ZonedDateTimeAssert) return ZonedDateTime.class;
    // Trying to create a proxy will only match exact constructor argument types.
    // To initialize one for ListAssert for example we can't use an ArrayList, we have to use a List.
    // So we can't just return actual.getClass() as we could read a concrete class whereas
    // *Assert classes define a constructor using interface (@see ListAssert for example).
    // Instead we can read generic types from *Assert definition.
    // Inspecting: class ListAssert<T> extends AbstractListAssert<ListAssert<T>, List<? extends T>, T>
    // will return the generic defined by the super class AbstractListAssert at index 1, which is a List<? extends T>
    Type actualType = ((ParameterizedType) currentAssert.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
    if (actualType instanceof ParameterizedType) return (Class<?>) ((ParameterizedType) actualType).getRawType();
    if (actualType instanceof TypeVariable) return (Class<?>) ((TypeVariable) actualType).getGenericDeclaration();
    return (Class<?>) actualType;
  }

  private static Object actual(Object result) {
    checkState(result instanceof AbstractAssert,
               "We should be trying to make a proxy of an *Assert class but it was: %s", result.getClass());
    return ((AbstractAssert<?, ?>) result).actual;
  }

}
