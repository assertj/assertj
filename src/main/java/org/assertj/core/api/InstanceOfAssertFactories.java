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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.*;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * Static {@link InstanceOfAssertFactory InstanceOfAssertFactories} for {@link Assert#asInstanceOf(InstanceOfAssertFactory)}.
 *
 * @author Stefano Cordio
 * @since 3.13.0
 */
public interface InstanceOfAssertFactories {

  /**
   * {@link InstanceOfAssertFactory} for a {@link Predicate}, assuming {@code Object} as input type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Predicate, PredicateAssert<Object>> predicate() {
    return predicate(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Predicate}.
   *
   * @param <T>  the {@code Predicate} input type.
   * @param type the input type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <T> InstanceOfAssertFactory<Predicate, PredicateAssert<T>> predicate(Class<T> type) {
    return new InstanceOfAssertFactory<>(Predicate.class, Assertions::<T> assertThat);
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
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<Object>> completableFuture() {
    return completableFuture(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link CompletableFuture}.
   *
   * @param <RESULT>   the {@code CompletableFuture} result type.
   * @param resultType the result type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<RESULT>> completableFuture(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletableFuture.class, Assertions::<RESULT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link CompletionStage}, assuming {@code Object} as result type.
   *    
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<Object>> completionStage() {
    return completionStage(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link CompletionStage}.
   *
   * @param <RESULT>   the {@code CompletionStage} result type.
   * @param resultType the result type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<RESULT>> completionStage(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletionStage.class, Assertions::<RESULT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Optional}, assuming {@code Object} as value type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Optional, OptionalAssert<Object>> optional() {
    return optional(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Optional}.
   *
   * @param <VALUE>    the {@code Optional} value type.
   * @param resultType the value type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<Optional, OptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
    return new InstanceOfAssertFactory<>(Optional.class, Assertions::<VALUE> assertThat);
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
   */
  InstanceOfAssertFactory<URI, AbstractUriAssert<?>> URI = new InstanceOfAssertFactory<>(URI.class,
                                                                                         Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link URL}.
   */
  InstanceOfAssertFactory<URL, AbstractUrlAssert<?>> URL = new InstanceOfAssertFactory<>(URL.class,
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
   * {@link InstanceOfAssertFactory} for a {@code byte} or its corresponding boxed type {@link Byte}.
   */
  InstanceOfAssertFactory<Byte, AbstractByteAssert<?>> BYTE = new InstanceOfAssertFactory<>(Byte.class,
                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@code byte} aray.
   */
  InstanceOfAssertFactory<byte[], AbstractByteArrayAssert<?>> BYTE_ARRAY = new InstanceOfAssertFactory<>(byte[].class,
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
   * {@link InstanceOfAssertFactory} for a {@link File}.
   */
  InstanceOfAssertFactory<File, AbstractFileAssert<?>> FILE = new InstanceOfAssertFactory<>(File.class,
                                                                                            Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Future}, assuming {@code Object} as result type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Future, FutureAssert<Object>> future() {
    return future(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Future}.
   *
   * @param <RESULT>    the {@code Future} result type.
   * @param resultType  the result type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<Future, FutureAssert<RESULT>> future(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(Future.class, Assertions::<RESULT> assertThat);
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
   * {@link InstanceOfAssertFactory} for an array of {@link Object}.
   *
   * @return the factory instance.
   */
  static InstanceOfAssertFactory<Object[], ObjectArrayAssert<Object>> array() {
    return array(Object[].class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an array of elements.
   *
   * @param <ELEMENT> the element type.
   * @param arrayType the element type instance.
   * @return the factory instance.
   */
  static <ELEMENT> InstanceOfAssertFactory<ELEMENT[], ObjectArrayAssert<ELEMENT>> array(Class<ELEMENT[]> arrayType) {
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
   * {@link InstanceOfAssertFactory} for a {@link Date}.
   */
  InstanceOfAssertFactory<Date, AbstractDateAssert<?>> DATE = new InstanceOfAssertFactory<>(Date.class,
                                                                                            Assertions::assertThat);

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
   * {@link InstanceOfAssertFactory} for an {@link Instant}.
   */
  InstanceOfAssertFactory<Instant, AbstractInstantAssert<?>> INSTANT = new InstanceOfAssertFactory<>(Instant.class,
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
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicIntegerFieldUpdater, AtomicIntegerFieldUpdaterAssert<Object>> atomicIntegerFieldUpdater() {
    return atomicIntegerFieldUpdater(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicIntegerFieldUpdater}.
   *
   * @param <OBJECT>   the {@code AtomicIntegerFieldUpdater} object type.
   * @param objectType the object type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <OBJECT> InstanceOfAssertFactory<AtomicIntegerFieldUpdater, AtomicIntegerFieldUpdaterAssert<OBJECT>> atomicIntegerFieldUpdater(Class<OBJECT> objectType) {
    return new InstanceOfAssertFactory<>(AtomicIntegerFieldUpdater.class, Assertions::<OBJECT> assertThat);
  }

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
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicLongFieldUpdater, AtomicLongFieldUpdaterAssert<Object>> atomicLongFieldUpdater() {
    return atomicLongFieldUpdater(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicIntegerFieldUpdater}.
   *
   * @param <OBJECT>   the {@code AtomicLongFieldUpdater} object type.
   * @param objectType the object type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <OBJECT> InstanceOfAssertFactory<AtomicLongFieldUpdater, AtomicLongFieldUpdaterAssert<OBJECT>> atomicLongFieldUpdater(Class<OBJECT> objectType) {
    return new InstanceOfAssertFactory<>(AtomicLongFieldUpdater.class, Assertions::<OBJECT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReference}, assuming {@code Object} as value type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicReference, AtomicReferenceAssert<Object>> atomicReference() {
    return atomicReference(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReference}.
   *
   * @param <VALUE>   the {@code AtomicReference} value type.
   * @param valueType the value type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<AtomicReference, AtomicReferenceAssert<VALUE>> atomicReference(Class<VALUE> valueType) {
    return new InstanceOfAssertFactory<>(AtomicReference.class, Assertions::<VALUE> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceArray}, assuming {@code Object} as element type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicReferenceArray, AtomicReferenceArrayAssert<Object>> atomicReferenceArray() {
    return atomicReferenceArray(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceArray}.
   *
   * @param <ELEMENT>   the {@code AtomicReferenceArray} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<AtomicReferenceArray, AtomicReferenceArrayAssert<ELEMENT>> atomicReferenceArray(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(AtomicReferenceArray.class, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceFieldUpdater}, assuming {@code Object} as field and object types.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicReferenceFieldUpdater, AtomicReferenceFieldUpdaterAssert<Object, Object>> atomicReferenceFieldUpdater() {
    return atomicReferenceFieldUpdater(Object.class, Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicReferenceFieldUpdater}.
   *
   * @param <FIELD>    the {@code AtomicReferenceFieldUpdater} field type.
   * @param <OBJECT>   the {@code AtomicReferenceFieldUpdater} object type.
   * @param fieldType  the field type instance.
   * @param objectType the object type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <FIELD, OBJECT> InstanceOfAssertFactory<AtomicReferenceFieldUpdater, AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT>> atomicReferenceFieldUpdater(Class<FIELD> fieldType,
                                                                                                                                                            Class<OBJECT> objectType) {
    return new InstanceOfAssertFactory<>(AtomicReferenceFieldUpdater.class, Assertions::<FIELD, OBJECT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicMarkableReference}, assuming {@code Object} as value type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicMarkableReference, AtomicMarkableReferenceAssert<Object>> atomicMarkableReference() {
    return atomicMarkableReference(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicMarkableReference}.
   *
   * @param <VALUE>   the {@code AtomicMarkableReference} value type.
   * @param valueType the value type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<AtomicMarkableReference, AtomicMarkableReferenceAssert<VALUE>> atomicMarkableReference(Class<VALUE> valueType) {
    return new InstanceOfAssertFactory<>(AtomicMarkableReference.class, Assertions::<VALUE> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicStampedReference}, assuming {@code Object} as value type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicStampedReference, AtomicStampedReferenceAssert<Object>> atomicStampedReference() {
    return atomicStampedReference(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link AtomicStampedReference}.
   *
   * @param <VALUE>   the {@code AtomicStampedReference} value type.
   * @param valueType the value type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<AtomicStampedReference, AtomicStampedReferenceAssert<VALUE>> atomicStampedReference(Class<VALUE> valueType) {
    return new InstanceOfAssertFactory<>(AtomicStampedReference.class, Assertions::<VALUE> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Throwable}.
   */
  InstanceOfAssertFactory<Throwable, AbstractThrowableAssert<?, ? extends Throwable>> THROWABLE = new InstanceOfAssertFactory<>(Throwable.class,
                                                                                                                                Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link CharSequence}.
   */
  InstanceOfAssertFactory<CharSequence, AbstractCharSequenceAssert<?, ? extends CharSequence>> CHAR_SEQUENCE = new InstanceOfAssertFactory<>(CharSequence.class,
                                                                                                                                             Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link String}.
   */
  InstanceOfAssertFactory<String, AbstractStringAssert<?>> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                                  Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterable}, assuming {@code Object} as element type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Iterable, IterableAssert<Object>> iterable() {
    return iterable(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterable}.
   *
   * @param <ELEMENT>   the {@code Iterable} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Iterable, IterableAssert<ELEMENT>> iterable(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Iterable.class, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterator}, assuming {@code Object} as element type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Iterator, IteratorAssert<Object>> iterator() {
    return iterator(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Iterator}.
   *
   * @param <ELEMENT>   the {@code Iterator} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Iterator, IteratorAssert<ELEMENT>> iterator(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Iterator.class, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link List}, assuming {@code Object} as element type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<List, ListAssert<Object>> list() {
    return list(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link List}.
   *
   * @param <ELEMENT>   the {@code List} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<List, ListAssert<ELEMENT>> list(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(List.class, Assertions::<ELEMENT> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Stream}, assuming {@code Object} as element type.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Stream, ListAssert<Object>> stream() {
    return stream(Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Stream}.
   *
   * @param <ELEMENT>   the {@code Stream} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Stream, ListAssert<ELEMENT>> stream(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Stream.class, Assertions::<ELEMENT> assertThat);
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
   * {@link InstanceOfAssertFactory} for a {@link Map}, assuming {@code Object} as key and value types.
   *
   * @return the factory instance.
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Map, MapAssert<Object, Object>> map() {
    return map(Object.class, Object.class);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Map}.
   *
   * @param <K>       the {@code Map} key type.
   * @param <V>       the {@code Map} value type.
   * @param keyType   the key type instace.
   * @param valueType the value type instace.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <K, V> InstanceOfAssertFactory<Map, MapAssert<K, V>> map(Class<K> keyType, Class<V> valueType) {
    return new InstanceOfAssertFactory<>(Map.class, Assertions::<K, V> assertThat);
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
