package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

/**
 * Static {@link InstanceOfAssertFactory InstanceOfAssertFactories} for {@link Assert#asInstanceOf(InstanceOfAssertFactory)}.
 *
 * @since 3.13.0
 */
public interface InstanceOfAssertFactories {

  static InstanceOfAssertFactory<Predicate, PredicateAssert<Object>> predicate() {
    return predicate(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <T> InstanceOfAssertFactory<Predicate, PredicateAssert<T>> predicate(Class<T> type) {
    return new InstanceOfAssertFactory<>(Predicate.class, Assertions::<T> assertThat);
  }

  InstanceOfAssertFactory<IntPredicate, IntPredicateAssert> INT_PREDICATE = new InstanceOfAssertFactory<>(IntPredicate.class,
                                                                                                          Assertions::assertThat);

  InstanceOfAssertFactory<LongPredicate, LongPredicateAssert> LONG_PREDICATE = new InstanceOfAssertFactory<>(LongPredicate.class,
                                                                                                             Assertions::assertThat);

  InstanceOfAssertFactory<DoublePredicate, DoublePredicateAssert> DOUBLE_PREDICATE = new InstanceOfAssertFactory<>(DoublePredicate.class,
                                                                                                                   Assertions::assertThat);

  static InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<Object>> completableFuture() {
    return completableFuture(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<RESULT>> completableFuture(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletableFuture.class, Assertions::<RESULT> assertThat);
  }

  static InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<Object>> completionStage() {
    return completionStage(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<RESULT>> completionStage(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletionStage.class, Assertions::<RESULT> assertThat);
  }

  static InstanceOfAssertFactory<Optional, OptionalAssert<Object>> optional() {
    return optional(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<Optional, OptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
    return new InstanceOfAssertFactory<>(Optional.class, Assertions::<VALUE> assertThat);
  }

  InstanceOfAssertFactory<OptionalDouble, OptionalDoubleAssert> OPTIONAL_DOUBLE = new InstanceOfAssertFactory<>(OptionalDouble.class,
                                                                                                                Assertions::assertThat);

  InstanceOfAssertFactory<OptionalInt, OptionalIntAssert> OPTIONAL_INT = new InstanceOfAssertFactory<>(OptionalInt.class,
                                                                                                       Assertions::assertThat);

  InstanceOfAssertFactory<OptionalLong, OptionalLongAssert> OPTIONAL_LONG = new InstanceOfAssertFactory<>(OptionalLong.class,
                                                                                                          Assertions::assertThat);

  InstanceOfAssertFactory<BigDecimal, AbstractBigDecimalAssert<?>> BIG_DECIMAL = new InstanceOfAssertFactory<>(BigDecimal.class,
                                                                                                               Assertions::assertThat);

  InstanceOfAssertFactory<BigInteger, AbstractBigIntegerAssert<?>> BIG_INTEGER = new InstanceOfAssertFactory<>(BigInteger.class,
                                                                                                               Assertions::assertThat);

  InstanceOfAssertFactory<URI, AbstractUriAssert<?>> URI = new InstanceOfAssertFactory<>(URI.class,
                                                                                         Assertions::assertThat);

  InstanceOfAssertFactory<URL, AbstractUrlAssert<?>> URL = new InstanceOfAssertFactory<>(URL.class,
                                                                                         Assertions::assertThat);

  InstanceOfAssertFactory<Boolean, AbstractBooleanAssert<?>> BOOLEAN = new InstanceOfAssertFactory<>(Boolean.class,
                                                                                                     Assertions::assertThat);

  InstanceOfAssertFactory<boolean[], AbstractBooleanArrayAssert<?>> BOOLEAN_ARRAY = new InstanceOfAssertFactory<>(boolean[].class,
                                                                                                                  Assertions::assertThat);

  InstanceOfAssertFactory<Byte, AbstractByteAssert<?>> BYTE = new InstanceOfAssertFactory<>(Byte.class,
                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<byte[], AbstractByteArrayAssert<?>> BYTE_ARRAY = new InstanceOfAssertFactory<>(byte[].class,
                                                                                                         Assertions::assertThat);

  InstanceOfAssertFactory<Character, AbstractCharacterAssert<?>> CHARACTER = new InstanceOfAssertFactory<>(Character.class,
                                                                                                           Assertions::assertThat);

  InstanceOfAssertFactory<char[], AbstractCharArrayAssert<?>> CHAR_ARRAY = new InstanceOfAssertFactory<>(char[].class,
                                                                                                         Assertions::assertThat);

  @SuppressWarnings("rawtypes") // using Class instance
  InstanceOfAssertFactory<Class, ClassAssert> CLASS = new InstanceOfAssertFactory<>(Class.class,
                                                                                    Assertions::assertThat);

  InstanceOfAssertFactory<Double, AbstractDoubleAssert<?>> DOUBLE = new InstanceOfAssertFactory<>(Double.class,
                                                                                                  Assertions::assertThat);

  InstanceOfAssertFactory<double[], AbstractDoubleArrayAssert<?>> DOUBLE_ARRAY = new InstanceOfAssertFactory<>(double[].class,
                                                                                                               Assertions::assertThat);

  InstanceOfAssertFactory<File, AbstractFileAssert<?>> FILE = new InstanceOfAssertFactory<>(File.class,
                                                                                            Assertions::assertThat);

  static InstanceOfAssertFactory<Future, FutureAssert<Object>> future() {
    return future(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<Future, FutureAssert<RESULT>> future(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(Future.class, Assertions::<RESULT> assertThat);
  }

  InstanceOfAssertFactory<InputStream, AbstractInputStreamAssert<?, ?>> INPUT_STREAM = new InstanceOfAssertFactory<>(InputStream.class,
                                                                                                                     Assertions::assertThat);

  InstanceOfAssertFactory<Float, AbstractFloatAssert<?>> FLOAT = new InstanceOfAssertFactory<>(Float.class,
                                                                                               Assertions::assertThat);

  InstanceOfAssertFactory<float[], AbstractFloatArrayAssert<?>> FLOAT_ARRAY = new InstanceOfAssertFactory<>(float[].class,
                                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<Integer, AbstractIntegerAssert<?>> INTEGER = new InstanceOfAssertFactory<>(Integer.class,
                                                                                                     Assertions::assertThat);

  InstanceOfAssertFactory<int[], AbstractIntArrayAssert<?>> INT_ARRAY = new InstanceOfAssertFactory<>(int[].class,
                                                                                                      Assertions::assertThat);

  InstanceOfAssertFactory<Long, AbstractLongAssert<?>> LONG = new InstanceOfAssertFactory<>(Long.class,
                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<long[], AbstractLongArrayAssert<?>> LONG_ARRAY = new InstanceOfAssertFactory<>(long[].class,
                                                                                                         Assertions::assertThat);

  static InstanceOfAssertFactory<Object[], ObjectArrayAssert<Object>> array() {
    return array(Object[].class);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<ELEMENT[], ObjectArrayAssert<ELEMENT>> array(Class<ELEMENT[]> arrayType) {
    return new InstanceOfAssertFactory<>(arrayType, Assertions::assertThat);
  }

  InstanceOfAssertFactory<Short, AbstractShortAssert<?>> SHORT = new InstanceOfAssertFactory<>(Short.class,
                                                                                               Assertions::assertThat);

  InstanceOfAssertFactory<short[], AbstractShortArrayAssert<?>> SHORT_ARRAY = new InstanceOfAssertFactory<>(short[].class,
                                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<Date, AbstractDateAssert<?>> DATE = new InstanceOfAssertFactory<>(Date.class,
                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<ZonedDateTime, AbstractZonedDateTimeAssert<?>> ZONED_DATE_TIME = new InstanceOfAssertFactory<>(ZonedDateTime.class,
                                                                                                                         Assertions::assertThat);

  InstanceOfAssertFactory<LocalDateTime, AbstractLocalDateTimeAssert<?>> LOCAL_DATE_TIME = new InstanceOfAssertFactory<>(LocalDateTime.class,
                                                                                                                         Assertions::assertThat);

  InstanceOfAssertFactory<OffsetDateTime, AbstractOffsetDateTimeAssert<?>> OFFSET_DATE_TIME = new InstanceOfAssertFactory<>(OffsetDateTime.class,
                                                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<OffsetTime, AbstractOffsetTimeAssert<?>> OFFSET_TIME = new InstanceOfAssertFactory<>(OffsetTime.class,
                                                                                                               Assertions::assertThat);

  InstanceOfAssertFactory<LocalTime, AbstractLocalTimeAssert<?>> LOCAL_TIME = new InstanceOfAssertFactory<>(LocalTime.class,
                                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<LocalDate, AbstractLocalDateAssert<?>> LOCAL_DATE = new InstanceOfAssertFactory<>(LocalDate.class,
                                                                                                            Assertions::assertThat);

  InstanceOfAssertFactory<Instant, AbstractInstantAssert<?>> INSTANT = new InstanceOfAssertFactory<>(Instant.class,
                                                                                                     Assertions::assertThat);

  InstanceOfAssertFactory<AtomicBoolean, AtomicBooleanAssert> ATOMIC_BOOLEAN = new InstanceOfAssertFactory<>(AtomicBoolean.class,
                                                                                                             Assertions::assertThat);

  InstanceOfAssertFactory<AtomicInteger, AtomicIntegerAssert> ATOMIC_INTEGER = new InstanceOfAssertFactory<>(AtomicInteger.class,
                                                                                                             Assertions::assertThat);

  InstanceOfAssertFactory<String, AbstractStringAssert<?>> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                                  Assertions::assertThat);

  static InstanceOfAssertFactory<Iterable, IterableAssert<Object>> iterable() {
    return iterable(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Iterable, IterableAssert<ELEMENT>> iterable(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Iterable.class, Assertions::<ELEMENT> assertThat);
  }

  static InstanceOfAssertFactory<List, ListAssert<Object>> list() {
    return list(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<List, ListAssert<ELEMENT>> list(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(List.class, Assertions::<ELEMENT> assertThat);
  }

}
