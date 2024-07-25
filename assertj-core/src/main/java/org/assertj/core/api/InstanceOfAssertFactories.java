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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Set;
import java.util.Spliterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * {@link InstanceOfAssertFactory} instances for Java types.
 *
 * @author Stefano Cordio
 * @see Assert#asInstanceOf(InstanceOfAssertFactory)
 * @see AbstractObjectAssert#extracting(String, InstanceOfAssertFactory)
 * @see AbstractObjectAssert#extracting(Function, InstanceOfAssertFactory)
 * @see AbstractMapAssert#extractingByKey(Object, InstanceOfAssertFactory)
 * @see AbstractOptionalAssert#get(InstanceOfAssertFactory)
 * @see AbstractIterableAssert#first(InstanceOfAssertFactory)
 * @see AbstractIterableAssert#last(InstanceOfAssertFactory)
 * @see AbstractIterableAssert#element(int, InstanceOfAssertFactory)
 * @since 3.13.0
 */
public interface InstanceOfAssertFactories {

  /**
   * {@link InstanceOfAssertFactory} for a {@link Predicate}, assuming {@code Object} as input type.
   *
   * @see #predicate(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Predicate, PredicateAssert<Object>> PREDICATE = predicate(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Predicate}.
   *
   * @param <T>  the {@code Predicate} input type.
   * @param type the input type instance.
   * @return the factory instance.
   *
   * @see #PREDICATE
   */
  @SuppressWarnings("rawtypes")
  static <T> InstanceOfAssertFactory<Predicate, PredicateAssert<T>> predicate(Class<T> type) {
    return new InstanceOfAssertFactory<>(Predicate.class, new Class[] { type }, Assertions::<T> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link IntPredicate}.
   */
  InstanceOfAssertFactory<IntPredicate, IntPredicateAssert> INT_PREDICATE = new InstanceOfAssertFactory<>(IntPredicate.class,
                                                                                                          Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link LongPredicate}.
   */
  InstanceOfAssertFactory<LongPredicate, LongPredicateAssert> LONG_PREDICATE = new InstanceOfAssertFactory<>(LongPredicate.class,
                                                                                                             Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link DoublePredicate}.
   */
  InstanceOfAssertFactory<DoublePredicate, DoublePredicateAssert> DOUBLE_PREDICATE = new InstanceOfAssertFactory<>(DoublePredicate.class,
                                                                                                                   Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link CompletableFuture}, assuming {@code Object} as result type.
   *
   * @see #completableFuture(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<Object>> COMPLETABLE_FUTURE = completableFuture(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link CompletableFuture}.
   *
   * @param <RESULT>   the {@code CompletableFuture} result type.
   * @param resultType the result type instance.
   * @return the factory instance.
   *
   * @see #COMPLETABLE_FUTURE
   */
  @SuppressWarnings("rawtypes")
  static <RESULT> InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<RESULT>> completableFuture(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletableFuture.class, new Class[] { resultType }, Assertions::<RESULT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link CompletionStage}, assuming {@code Object} as result type.
   *
   * @see #completionStage(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<Object>> COMPLETION_STAGE = completionStage(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link CompletionStage}.
   *
   * @param <RESULT>   the {@code CompletionStage} result type.
   * @param resultType the result type instance.
   * @return the factory instance.
   *
   * @see #COMPLETION_STAGE
   */
  @SuppressWarnings("rawtypes")
  static <RESULT> InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<RESULT>> completionStage(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletionStage.class, new Class[] { resultType }, Assertions::<RESULT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Optional}, assuming {@code Object} as value type.
   *
   * @see #optional(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Optional, OptionalAssert<Object>> OPTIONAL = optional(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Optional}.
   *
   * @param <VALUE>    the {@code Optional} value type.
   * @param resultType the value type instance.
   * @return the factory instance.
   *
   * @see #OPTIONAL
   */
  @SuppressWarnings("rawtypes")
  static <VALUE> InstanceOfAssertFactory<Optional, OptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
    return new InstanceOfAssertFactory<>(Optional.class, new Class[] { resultType }, Assertions::<VALUE> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link OptionalDouble}.
   */
  InstanceOfAssertFactory<OptionalDouble, OptionalDoubleAssert> OPTIONAL_DOUBLE = new InstanceOfAssertFactory<>(OptionalDouble.class,
                                                                                                                Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link OptionalInt}.
   */
  InstanceOfAssertFactory<OptionalInt, OptionalIntAssert> OPTIONAL_INT = new InstanceOfAssertFactory<>(OptionalInt.class,
                                                                                                       Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link OptionalLong}.
   */
  InstanceOfAssertFactory<OptionalLong, OptionalLongAssert> OPTIONAL_LONG = new InstanceOfAssertFactory<>(OptionalLong.class,
                                                                                                          Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Matcher}.
   */
  InstanceOfAssertFactory<Matcher, MatcherAssert> MATCHER = new InstanceOfAssertFactory<>(Matcher.class,
                                                                                          Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link BigDecimal}.
   */
  InstanceOfAssertFactory<BigDecimal, AbstractBigDecimalAssert<?>> BIG_DECIMAL = new InstanceOfAssertFactory<>(BigDecimal.class,
                                                                                                               Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link BigInteger}.
   */
  InstanceOfAssertFactory<BigInteger, AbstractBigIntegerAssert<?>> BIG_INTEGER = new InstanceOfAssertFactory<>(BigInteger.class,
                                                                                                               Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link URI}.
   * <p>
   * Note: The {@code TYPE} suffix prevents the shadowing of {@code java.net.URI} when the factory is statically imported.
   * </p>
   *
   * @since 3.13.2
   */
  InstanceOfAssertFactory<URI, AbstractUriAssert<?>> URI_TYPE = new InstanceOfAssertFactory<>(URI.class,
                                                                                              Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link URL}.
   * <p>
   * Note: The {@code TYPE} suffix prevents the shadowing of {@code java.net.URL} when the factory is statically imported.
   * </p>
   *
   * @since 3.13.2
   */
  InstanceOfAssertFactory<URL, AbstractUrlAssert<?>> URL_TYPE = new InstanceOfAssertFactory<>(URL.class,
                                                                                              Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code boolean} or its corresponding boxed type {@link Boolean}.
   */
  InstanceOfAssertFactory<Boolean, AbstractBooleanAssert<?>> BOOLEAN = new InstanceOfAssertFactory<>(Boolean.class,
                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code boolean} array.
   */
  InstanceOfAssertFactory<boolean[], AbstractBooleanArrayAssert<?>> BOOLEAN_ARRAY = new InstanceOfAssertFactory<>(boolean[].class,
                                                                                                                  Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code boolean} two-dimensional array.
   */
  InstanceOfAssertFactory<boolean[][], Boolean2DArrayAssert> BOOLEAN_2D_ARRAY = new InstanceOfAssertFactory<>(boolean[][].class,
                                                                                                              Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code byte} or its corresponding boxed type {@link Byte}.
   */
  InstanceOfAssertFactory<Byte, AbstractByteAssert<?>> BYTE = new InstanceOfAssertFactory<>(Byte.class,
                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code byte} array.
   */
  InstanceOfAssertFactory<byte[], AbstractByteArrayAssert<?>> BYTE_ARRAY = new InstanceOfAssertFactory<>(byte[].class,
                                                                                                         Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code byte} two-dimensional array.
   */
  InstanceOfAssertFactory<byte[][], Byte2DArrayAssert> BYTE_2D_ARRAY = new InstanceOfAssertFactory<>(byte[][].class,
                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code char} or its corresponding boxed type {@link Character}.
   */
  InstanceOfAssertFactory<Character, AbstractCharacterAssert<?>> CHARACTER = new InstanceOfAssertFactory<>(Character.class,
                                                                                                           Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code char} array.
   */
  InstanceOfAssertFactory<char[], AbstractCharArrayAssert<?>> CHAR_ARRAY = new InstanceOfAssertFactory<>(char[].class,
                                                                                                         Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code char} two-dimensional array.
   */
  InstanceOfAssertFactory<char[][], Char2DArrayAssert> CHAR_2D_ARRAY = new InstanceOfAssertFactory<>(char[][].class,
                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Class}.
   */
  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<Class, ClassAssert> CLASS = new InstanceOfAssertFactory<>(Class.class,
                                                                                    Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code double} or its corresponding boxed type {@link Double}.
   */
  InstanceOfAssertFactory<Double, AbstractDoubleAssert<?>> DOUBLE = new InstanceOfAssertFactory<>(Double.class,
                                                                                                  Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code double} array.
   */
  InstanceOfAssertFactory<double[], AbstractDoubleArrayAssert<?>> DOUBLE_ARRAY = new InstanceOfAssertFactory<>(double[].class,
                                                                                                               Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code double} two-dimensional array.
   */
  InstanceOfAssertFactory<double[][], Double2DArrayAssert> DOUBLE_2D_ARRAY = new InstanceOfAssertFactory<>(double[][].class,
                                                                                                           Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link File}.
   */
  InstanceOfAssertFactory<File, AbstractFileAssert<?>> FILE = new InstanceOfAssertFactory<>(File.class,
                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Future}, assuming {@code Object} as result type.
   *
   * @see #future(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Future, FutureAssert<Object>> FUTURE = future(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Future}.
   *
   * @param <RESULT>   the {@code Future} result type.
   * @param resultType the result type instance.
   * @return the factory instance.
   *
   * @see #FUTURE
   */
  @SuppressWarnings("rawtypes")
  static <RESULT> InstanceOfAssertFactory<Future, FutureAssert<RESULT>> future(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(Future.class, new Class[] { resultType }, Assertions::<RESULT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link InputStream}.
   */
  InstanceOfAssertFactory<InputStream, AbstractInputStreamAssert<?, ?>> INPUT_STREAM = new InstanceOfAssertFactory<>(InputStream.class,
                                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code float} or its corresponding boxed type {@link Float}.
   */
  InstanceOfAssertFactory<Float, AbstractFloatAssert<?>> FLOAT = new InstanceOfAssertFactory<>(Float.class,
                                                                                               Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code float} array.
   */
  InstanceOfAssertFactory<float[], AbstractFloatArrayAssert<?>> FLOAT_ARRAY = new InstanceOfAssertFactory<>(float[].class,
                                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code float} two-dimensional array.
   */
  InstanceOfAssertFactory<float[][], Float2DArrayAssert> FLOAT_2D_ARRAY = new InstanceOfAssertFactory<>(float[][].class,
                                                                                                        Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@code integer} or its corresponding boxed type {@link Integer}.
   */
  InstanceOfAssertFactory<Integer, AbstractIntegerAssert<?>> INTEGER = new InstanceOfAssertFactory<>(Integer.class,
                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@code int} array.
   */
  InstanceOfAssertFactory<int[], AbstractIntArrayAssert<?>> INT_ARRAY = new InstanceOfAssertFactory<>(int[].class,
                                                                                                      Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@code int} two-dimensional array.
   */
  InstanceOfAssertFactory<int[][], Int2DArrayAssert> INT_2D_ARRAY = new InstanceOfAssertFactory<>(int[][].class,
                                                                                                  Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code long} or its corresponding boxed type {@link Long}.
   */
  InstanceOfAssertFactory<Long, AbstractLongAssert<?>> LONG = new InstanceOfAssertFactory<>(Long.class,
                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code long} array.
   */
  InstanceOfAssertFactory<long[], AbstractLongArrayAssert<?>> LONG_ARRAY = new InstanceOfAssertFactory<>(long[].class,
                                                                                                         Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code long} two-dimensional array.
   */
  InstanceOfAssertFactory<long[][], Long2DArrayAssert> LONG_2D_ARRAY = new InstanceOfAssertFactory<>(long[][].class,
                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an object of a specific type.
   * <p>
   * <b>While this factory ensures that {@code actual} is an instance of the input type, it creates always
   * an {@link ObjectAssert} with the corresponding type.</b>
   *
   * @param <T>  the object type.
   * @param type the object type instance.
   * @return the factory instance.
   */
  static <T> InstanceOfAssertFactory<T, ObjectAssert<T>> type(Class<T> type) {
    return new InstanceOfAssertFactory<>(type, Assertions::assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an array of {@link Object}.
   *
   * @see #array(Class)
   */
  InstanceOfAssertFactory<Object[], ObjectArrayAssert<Object>> ARRAY = array(Object[].class);

  /**
   * {@link InstanceOfAssertFactory} for an array of elements.
   *
   * @param <ELEMENT> the element type.
   * @param arrayType the element type instance.
   * @return the factory instance.
   *
   * @see #ARRAY
   */
  static <ELEMENT> InstanceOfAssertFactory<ELEMENT[], ObjectArrayAssert<ELEMENT>> array(Class<ELEMENT[]> arrayType) {
    return new InstanceOfAssertFactory<>(arrayType, Assertions::assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a two-dimensional array of {@link Object}.
   *
   * @see #array(Class)
   */
  InstanceOfAssertFactory<Object[][], Object2DArrayAssert<Object>> ARRAY_2D = array2D(Object[][].class);

  /**
   * {@link InstanceOfAssertFactory} for a two-dimensional array of elements.
   *
   * @param <ELEMENT> the element type.
   * @param arrayType the element type instance.
   * @return the factory instance.
   *
   * @see #ARRAY
   */
  static <ELEMENT> InstanceOfAssertFactory<ELEMENT[][], Object2DArrayAssert<ELEMENT>> array2D(Class<ELEMENT[][]> arrayType) {
    return new InstanceOfAssertFactory<>(arrayType, Assertions::assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@code short} or its corresponding boxed type {@link Short}.
   */
  InstanceOfAssertFactory<Short, AbstractShortAssert<?>> SHORT = new InstanceOfAssertFactory<>(Short.class,
                                                                                               Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code short} array.
   */
  InstanceOfAssertFactory<short[], AbstractShortArrayAssert<?>> SHORT_ARRAY = new InstanceOfAssertFactory<>(short[].class,
                                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code short} two-dimensional array.
   */
  InstanceOfAssertFactory<short[][], Short2DArrayAssert> SHORT_2D_ARRAY = new InstanceOfAssertFactory<>(short[][].class,
                                                                                                        Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Date}.
   */
  InstanceOfAssertFactory<Date, AbstractDateAssert<?>> DATE = new InstanceOfAssertFactory<>(Date.class,
                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Temporal}.
   *
   * @since 3.26.0
   */
  InstanceOfAssertFactory<Temporal, TemporalAssert> TEMPORAL = new InstanceOfAssertFactory<>(Temporal.class,
                                                                                             Assertions::assertThatTemporal);

  /**
   * {@link InstanceOfAssertFactory} for a {@link ZonedDateTime}.
   */
  InstanceOfAssertFactory<ZonedDateTime, AbstractZonedDateTimeAssert<?>> ZONED_DATE_TIME = new InstanceOfAssertFactory<>(ZonedDateTime.class,
                                                                                                                         Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link LocalDateTime}.
   */
  InstanceOfAssertFactory<LocalDateTime, AbstractLocalDateTimeAssert<?>> LOCAL_DATE_TIME = new InstanceOfAssertFactory<>(LocalDateTime.class,
                                                                                                                         Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link OffsetDateTime}.
   */
  InstanceOfAssertFactory<OffsetDateTime, AbstractOffsetDateTimeAssert<?>> OFFSET_DATE_TIME = new InstanceOfAssertFactory<>(OffsetDateTime.class,
                                                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link OffsetTime}.
   */
  InstanceOfAssertFactory<OffsetTime, AbstractOffsetTimeAssert<?>> OFFSET_TIME = new InstanceOfAssertFactory<>(OffsetTime.class,
                                                                                                               Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link LocalTime}.
   */
  InstanceOfAssertFactory<LocalTime, AbstractLocalTimeAssert<?>> LOCAL_TIME = new InstanceOfAssertFactory<>(LocalTime.class,
                                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link LocalDate}.
   */
  InstanceOfAssertFactory<LocalDate, AbstractLocalDateAssert<?>> LOCAL_DATE = new InstanceOfAssertFactory<>(LocalDate.class,
                                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link YearMonth}.
   *
   * @since 3.26.0
   */
  InstanceOfAssertFactory<YearMonth, AbstractYearMonthAssert<?>> YEAR_MONTH = new InstanceOfAssertFactory<>(YearMonth.class,
                                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Instant}.
   */
  InstanceOfAssertFactory<Instant, AbstractInstantAssert<?>> INSTANT = new InstanceOfAssertFactory<>(Instant.class,
                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Duration}.
   *
   * @since 3.15.0
   */
  InstanceOfAssertFactory<Duration, AbstractDurationAssert<?>> DURATION = new InstanceOfAssertFactory<>(Duration.class,
                                                                                                        Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Period}.
   *
   * @since 3.17.0
   */
  InstanceOfAssertFactory<Period, AbstractPeriodAssert<?>> PERIOD = new InstanceOfAssertFactory<>(Period.class,
                                                                                                  Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicBoolean}.
   */
  InstanceOfAssertFactory<AtomicBoolean, AtomicBooleanAssert> ATOMIC_BOOLEAN = new InstanceOfAssertFactory<>(AtomicBoolean.class,
                                                                                                             Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicInteger}.
   */
  InstanceOfAssertFactory<AtomicInteger, AtomicIntegerAssert> ATOMIC_INTEGER = new InstanceOfAssertFactory<>(AtomicInteger.class,
                                                                                                             Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicIntegerArray}.
   */
  InstanceOfAssertFactory<AtomicIntegerArray, AtomicIntegerArrayAssert> ATOMIC_INTEGER_ARRAY = new InstanceOfAssertFactory<>(AtomicIntegerArray.class,
                                                                                                                             Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicIntegerFieldUpdater}, assuming {@code Object} as object type.
   *
   * @see #atomicIntegerFieldUpdater(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<AtomicIntegerFieldUpdater, AtomicIntegerFieldUpdaterAssert<Object>> ATOMIC_INTEGER_FIELD_UPDATER = atomicIntegerFieldUpdater(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicIntegerFieldUpdater}.
   *
   * @param <T>        the {@code AtomicIntegerFieldUpdater} object type.
   * @param objectType the object type instance.
   * @return the factory instance.
   *
   * @see #ATOMIC_INTEGER_FIELD_UPDATER
   */
  @SuppressWarnings("rawtypes")
  static <T> InstanceOfAssertFactory<AtomicIntegerFieldUpdater, AtomicIntegerFieldUpdaterAssert<T>> atomicIntegerFieldUpdater(Class<T> objectType) {
    return new InstanceOfAssertFactory<>(AtomicIntegerFieldUpdater.class, new Class[] { objectType }, Assertions::<T> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link LongAdder}.
   */
  InstanceOfAssertFactory<LongAdder, LongAdderAssert> LONG_ADDER = new InstanceOfAssertFactory<>(LongAdder.class,
                                                                                                 Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicLong}.
   */
  InstanceOfAssertFactory<AtomicLong, AtomicLongAssert> ATOMIC_LONG = new InstanceOfAssertFactory<>(AtomicLong.class,
                                                                                                    Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicLongArray}.
   */
  InstanceOfAssertFactory<AtomicLongArray, AtomicLongArrayAssert> ATOMIC_LONG_ARRAY = new InstanceOfAssertFactory<>(AtomicLongArray.class,
                                                                                                                    Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicLongFieldUpdater}, assuming {@code Object} as object type.
   *
   * @see #atomicLongFieldUpdater(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<AtomicLongFieldUpdater, AtomicLongFieldUpdaterAssert<Object>> ATOMIC_LONG_FIELD_UPDATER = atomicLongFieldUpdater(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicIntegerFieldUpdater}.
   *
   * @param <T>        the {@code AtomicLongFieldUpdater} object type.
   * @param objectType the object type instance.
   * @return the factory instance.
   *
   * @see #ATOMIC_LONG_FIELD_UPDATER
   */
  @SuppressWarnings("rawtypes")
  static <T> InstanceOfAssertFactory<AtomicLongFieldUpdater, AtomicLongFieldUpdaterAssert<T>> atomicLongFieldUpdater(Class<T> objectType) {
    return new InstanceOfAssertFactory<>(AtomicLongFieldUpdater.class, new Class[] { objectType }, Assertions::<T> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReference}, assuming {@code Object} as value type.
   *
   * @see #atomicReference(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<AtomicReference, AtomicReferenceAssert<Object>> ATOMIC_REFERENCE = atomicReference(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReference}.
   *
   * @param <V>       the {@code AtomicReference} value type.
   * @param valueType the value type instance.
   * @return the factory instance.
   *
   * @see #ATOMIC_REFERENCE
   */
  @SuppressWarnings("rawtypes")
  static <V> InstanceOfAssertFactory<AtomicReference, AtomicReferenceAssert<V>> atomicReference(Class<V> valueType) {
    return new InstanceOfAssertFactory<>(AtomicReference.class, new Class[] { valueType }, Assertions::<V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceArray}, assuming {@code Object} as element type.
   *
   * @see #atomicReferenceArray(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<AtomicReferenceArray, AtomicReferenceArrayAssert<Object>> ATOMIC_REFERENCE_ARRAY = atomicReferenceArray(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceArray}.
   *
   * @param <E>         the {@code AtomicReferenceArray} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #ATOMIC_REFERENCE_ARRAY
   */
  @SuppressWarnings("rawtypes")
  static <E> InstanceOfAssertFactory<AtomicReferenceArray, AtomicReferenceArrayAssert<E>> atomicReferenceArray(Class<E> elementType) {
    return new InstanceOfAssertFactory<>(AtomicReferenceArray.class, new Class[] { elementType }, Assertions::<E> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceFieldUpdater}, assuming {@code Object} as field and object types.
   *
   * @see #atomicReferenceFieldUpdater(Class, Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<AtomicReferenceFieldUpdater, AtomicReferenceFieldUpdaterAssert<Object, Object>> ATOMIC_REFERENCE_FIELD_UPDATER = atomicReferenceFieldUpdater(Object.class,
                                                                                                                                                                       Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceFieldUpdater}.
   *
   * @param <T>        the {@code AtomicReferenceFieldUpdater} field type.
   * @param <V>        the {@code AtomicReferenceFieldUpdater} object type.
   * @param fieldType  the field type instance.
   * @param objectType the object type instance.
   * @return the factory instance.
   *
   * @see #ATOMIC_REFERENCE_FIELD_UPDATER
   */
  @SuppressWarnings("rawtypes")
  static <T, V> InstanceOfAssertFactory<AtomicReferenceFieldUpdater, AtomicReferenceFieldUpdaterAssert<T, V>> atomicReferenceFieldUpdater(Class<T> fieldType,
                                                                                                                                          Class<V> objectType) {
    return new InstanceOfAssertFactory<>(AtomicReferenceFieldUpdater.class, new Class[] { fieldType, objectType },
                                         Assertions::<T, V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicMarkableReference}, assuming {@code Object} as value type.
   *
   * @see #atomicMarkableReference(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<AtomicMarkableReference, AtomicMarkableReferenceAssert<Object>> ATOMIC_MARKABLE_REFERENCE = atomicMarkableReference(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicMarkableReference}.
   *
   * @param <V>       the {@code AtomicMarkableReference} value type.
   * @param valueType the value type instance.
   * @return the factory instance.
   *
   * @see #ATOMIC_MARKABLE_REFERENCE
   */
  @SuppressWarnings("rawtypes")
  static <V> InstanceOfAssertFactory<AtomicMarkableReference, AtomicMarkableReferenceAssert<V>> atomicMarkableReference(Class<V> valueType) {
    return new InstanceOfAssertFactory<>(AtomicMarkableReference.class, new Class[] { valueType }, Assertions::<V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicStampedReference}, assuming {@code Object} as value type.
   *
   * @see #atomicStampedReference(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<AtomicStampedReference, AtomicStampedReferenceAssert<Object>> ATOMIC_STAMPED_REFERENCE = atomicStampedReference(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicStampedReference}.
   *
   * @param <V>       the {@code AtomicStampedReference} value type.
   * @param valueType the value type instance.
   * @return the factory instance.
   *
   * @see #ATOMIC_STAMPED_REFERENCE
   */
  @SuppressWarnings("rawtypes")
  static <V> InstanceOfAssertFactory<AtomicStampedReference, AtomicStampedReferenceAssert<V>> atomicStampedReference(Class<V> valueType) {
    return new InstanceOfAssertFactory<>(AtomicStampedReference.class, new Class[] { valueType }, Assertions::<V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Throwable}.
   */
  InstanceOfAssertFactory<Throwable, AbstractThrowableAssert<?, Throwable>> THROWABLE = new InstanceOfAssertFactory<>(Throwable.class,
                                                                                                                      Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Throwable}.
   *
   * @param <T>  the {@code Throwable} type.
   * @param type the element type instance.
   * @return the factory instance.
   *
   * @see #THROWABLE
   * @since 3.21.0
   */
  static <T extends Throwable> InstanceOfAssertFactory<T, AbstractThrowableAssert<?, T>> throwable(Class<T> type) {
    return new InstanceOfAssertFactory<>(type, Assertions::assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link CharSequence}.
   */
  InstanceOfAssertFactory<CharSequence, AbstractCharSequenceAssert<?, ? extends CharSequence>> CHAR_SEQUENCE = new InstanceOfAssertFactory<>(CharSequence.class,
                                                                                                                                             Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link StringBuilder}.
   */
  InstanceOfAssertFactory<StringBuilder, AbstractCharSequenceAssert<?, ? extends CharSequence>> STRING_BUILDER = new InstanceOfAssertFactory<>(StringBuilder.class,
                                                                                                                                               Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link StringBuffer}.
   */
  InstanceOfAssertFactory<StringBuffer, AbstractCharSequenceAssert<?, ? extends CharSequence>> STRING_BUFFER = new InstanceOfAssertFactory<>(StringBuffer.class,
                                                                                                                                             Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link String}.
   */
  InstanceOfAssertFactory<String, AbstractStringAssert<?>> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                                  Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterable}, assuming {@code Object} as element type.
   *
   * @see #iterable(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Iterable, IterableAssert<Object>> ITERABLE = iterable(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterable}.
   *
   * @param <ELEMENT>   the {@code Iterable} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #ITERABLE
   */
  @SuppressWarnings("rawtypes")
  static <ELEMENT> InstanceOfAssertFactory<Iterable, IterableAssert<ELEMENT>> iterable(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Iterable.class, new Class[] { elementType }, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterator}, assuming {@code Object} as element type.
   *
   * @see #iterator(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Iterator, IteratorAssert<Object>> ITERATOR = iterator(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterator}.
   *
   * @param <ELEMENT>   the {@code Iterator} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #ITERATOR
   */
  @SuppressWarnings("rawtypes")
  static <ELEMENT> InstanceOfAssertFactory<Iterator, IteratorAssert<ELEMENT>> iterator(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Iterator.class, new Class[] { elementType }, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Collection}, assuming {@code Object} as element type.
   *
   * @see #collection(Class)
   * @since 3.21.0
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Collection, AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>>> COLLECTION = collection(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Collection}.
   *
   * @param <E>   the {@code Collection} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #COLLECTION
   * @since 3.21.0
   */
  @SuppressWarnings("rawtypes")
  static <E> InstanceOfAssertFactory<Collection, AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>>> collection(Class<E> elementType) {
    return new InstanceOfAssertFactory<>(Collection.class, new Class[] { elementType }, Assertions::<E> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link List}, assuming {@code Object} as element type.
   *
   * @see #list(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<List, ListAssert<Object>> LIST = list(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link List}.
   *
   * @param <ELEMENT>   the {@code List} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #LIST
   */
  @SuppressWarnings("rawtypes")
  static <ELEMENT> InstanceOfAssertFactory<List, ListAssert<ELEMENT>> list(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(List.class, new Class[] { elementType }, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Set}, assuming {@code Object} as element type.
   *
   * @see #set(Class)
   * @since 3.26.0
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Set, AbstractCollectionAssert<?, Collection<?>, Object, ObjectAssert<Object>>> SET = set(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Set}.
   *
   * @param <E>   the {@code Set} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #SET
   * @since 3.26.0
   */
  @SuppressWarnings("rawtypes")
  static <E> InstanceOfAssertFactory<Set, AbstractCollectionAssert<?, Collection<? extends E>, E, ObjectAssert<E>>> set(Class<E> elementType) {
    return new InstanceOfAssertFactory<>(Set.class, new Class[] { elementType }, Assertions::<E> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Stream}, assuming {@code Object} as element type.
   *
   * @see #stream(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Stream, ListAssert<Object>> STREAM = stream(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Stream}.
   *
   * @param <ELEMENT>   the {@code Stream} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #STREAM
   */
  @SuppressWarnings("rawtypes")
  static <ELEMENT> InstanceOfAssertFactory<Stream, ListAssert<ELEMENT>> stream(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Stream.class, new Class[] { elementType }, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link DoubleStream}.
   */
  InstanceOfAssertFactory<DoubleStream, ListAssert<Double>> DOUBLE_STREAM = new InstanceOfAssertFactory<>(DoubleStream.class,
                                                                                                          Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link LongStream}.
   */
  InstanceOfAssertFactory<LongStream, ListAssert<Long>> LONG_STREAM = new InstanceOfAssertFactory<>(LongStream.class,
                                                                                                    Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link IntStream}.
   */
  InstanceOfAssertFactory<IntStream, ListAssert<Integer>> INT_STREAM = new InstanceOfAssertFactory<>(IntStream.class,
                                                                                                     Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Path}.
   */
  InstanceOfAssertFactory<Path, AbstractPathAssert<?>> PATH = new InstanceOfAssertFactory<>(Path.class,
                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Spliterator}, assuming {@code Object} as element type.
   *
   * @see #spliterator(Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Spliterator, SpliteratorAssert<Object>> SPLITERATOR = spliterator(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Spliterator}.
   *
   * @param <ELEMENT>   the {@code Spliterator} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #SPLITERATOR
   */
  @SuppressWarnings("rawtypes")
  static <ELEMENT> InstanceOfAssertFactory<Spliterator, SpliteratorAssert<ELEMENT>> spliterator(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Spliterator.class, new Class[] { elementType }, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Map}, assuming {@code Object} as key and value types.
   *
   * @see #map(Class, Class)
   */
  @SuppressWarnings("rawtypes")
  InstanceOfAssertFactory<Map, MapAssert<Object, Object>> MAP = map(Object.class, Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Map}.
   *
   * @param <K>       the {@code Map} key type.
   * @param <V>       the {@code Map} value type.
   * @param keyType   the key type instance.
   * @param valueType the value type instance.
   * @return the factory instance.
   *
   * @see #MAP
   */
  @SuppressWarnings("rawtypes")
  static <K, V> InstanceOfAssertFactory<Map, MapAssert<K, V>> map(Class<K> keyType, Class<V> valueType) {
    return new InstanceOfAssertFactory<>(Map.class, new Class[] { keyType, valueType }, Assertions::<K, V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Comparable}.
   *
   * @param <T>            the {@code Comparable} type.
   * @param comparableType the comparable type instance.
   * @return the factory instance.
   */
  static <T extends Comparable<? super T>> InstanceOfAssertFactory<T, AbstractComparableAssert<?, T>> comparable(Class<T> comparableType) {
    return new InstanceOfAssertFactory<>(comparableType, Assertions::assertThat);
  }

}
