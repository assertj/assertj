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
 * @since 3.13.0
 */
public interface InstanceOfAssertFactories {

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
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

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<Object>> completableFuture() {
    return completableFuture(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletableFuture, CompletableFutureAssert<RESULT>> completableFuture(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletableFuture.class, Assertions::<RESULT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<Object>> completionStage() {
    return completionStage(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <RESULT> InstanceOfAssertFactory<CompletionStage, CompletableFutureAssert<RESULT>> completionStage(Class<RESULT> resultType) {
    return new InstanceOfAssertFactory<>(CompletionStage.class, Assertions::<RESULT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
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

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
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

  InstanceOfAssertFactory<AtomicIntegerArray, AtomicIntegerArrayAssert> ATOMIC_INTEGER_ARRAY = new InstanceOfAssertFactory<>(AtomicIntegerArray.class,
                                                                                                                             Assertions::assertThat);

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicIntegerFieldUpdater, AtomicIntegerFieldUpdaterAssert<Object>> atomicIntegerFieldUpdater() {
    return atomicIntegerFieldUpdater(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <OBJECT> InstanceOfAssertFactory<AtomicIntegerFieldUpdater, AtomicIntegerFieldUpdaterAssert<OBJECT>> atomicIntegerFieldUpdater(Class<OBJECT> objectType) {
    return new InstanceOfAssertFactory<>(AtomicIntegerFieldUpdater.class, Assertions::<OBJECT> assertThat);
  }

  InstanceOfAssertFactory<AtomicLong, AtomicLongAssert> ATOMIC_LONG = new InstanceOfAssertFactory<>(AtomicLong.class,
                                                                                                    Assertions::assertThat);

  InstanceOfAssertFactory<AtomicLongArray, AtomicLongArrayAssert> ATOMIC_LONG_ARRAY = new InstanceOfAssertFactory<>(AtomicLongArray.class,
                                                                                                                    Assertions::assertThat);

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicLongFieldUpdater, AtomicLongFieldUpdaterAssert<Object>> atomicLongFieldUpdater() {
    return atomicLongFieldUpdater(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <OBJECT> InstanceOfAssertFactory<AtomicLongFieldUpdater, AtomicLongFieldUpdaterAssert<OBJECT>> atomicLongFieldUpdater(Class<OBJECT> objectType) {
    return new InstanceOfAssertFactory<>(AtomicLongFieldUpdater.class, Assertions::<OBJECT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicReference, AtomicReferenceAssert<Object>> atomicReference() {
    return atomicReference(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<AtomicReference, AtomicReferenceAssert<VALUE>> atomicReference(Class<VALUE> valueType) {
    return new InstanceOfAssertFactory<>(AtomicReference.class, Assertions::<VALUE> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicReferenceArray, AtomicReferenceArrayAssert<Object>> atomicReferenceArray() {
    return atomicReferenceArray(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<AtomicReferenceArray, AtomicReferenceArrayAssert<ELEMENT>> atomicReferenceArray(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(AtomicReferenceArray.class, Assertions::<ELEMENT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicReferenceFieldUpdater, AtomicReferenceFieldUpdaterAssert<Object, Object>> atomicReferenceFieldUpdater() {
    return atomicReferenceFieldUpdater(Object.class, Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <FIELD, OBJECT> InstanceOfAssertFactory<AtomicReferenceFieldUpdater, AtomicReferenceFieldUpdaterAssert<FIELD, OBJECT>> atomicReferenceFieldUpdater(Class<FIELD> fieldType,
                                                                                                                                                            Class<OBJECT> objectType) {
    return new InstanceOfAssertFactory<>(AtomicReferenceFieldUpdater.class, Assertions::<FIELD, OBJECT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicMarkableReference, AtomicMarkableReferenceAssert<Object>> atomicMarkableReference() {
    return atomicMarkableReference(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<AtomicMarkableReference, AtomicMarkableReferenceAssert<VALUE>> atomicMarkableReference(Class<VALUE> valueType) {
    return new InstanceOfAssertFactory<>(AtomicMarkableReference.class, Assertions::<VALUE> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<AtomicStampedReference, AtomicStampedReferenceAssert<Object>> atomicStampedReference() {
    return atomicStampedReference(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<AtomicStampedReference, AtomicStampedReferenceAssert<VALUE>> atomicStampedReference(Class<VALUE> valueType) {
    return new InstanceOfAssertFactory<>(AtomicStampedReference.class, Assertions::<VALUE> assertThat);
  }

  InstanceOfAssertFactory<Throwable, AbstractThrowableAssert<?, ? extends Throwable>> THROWABLE = new InstanceOfAssertFactory<>(Throwable.class,
                                                                                                                                Assertions::assertThat);

  InstanceOfAssertFactory<CharSequence, AbstractCharSequenceAssert<?, ? extends CharSequence>> CHAR_SEQUENCE = new InstanceOfAssertFactory<>(CharSequence.class,
                                                                                                                                             Assertions::assertThat);

  InstanceOfAssertFactory<String, AbstractStringAssert<?>> STRING = new InstanceOfAssertFactory<>(String.class,
                                                                                                  Assertions::assertThat);

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Iterable, IterableAssert<Object>> iterable() {
    return iterable(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Iterable, IterableAssert<ELEMENT>> iterable(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Iterable.class, Assertions::<ELEMENT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Iterator, IteratorAssert<Object>> iterator() {
    return iterator(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Iterator, IteratorAssert<ELEMENT>> iterator(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Iterator.class, Assertions::<ELEMENT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<List, ListAssert<Object>> list() {
    return list(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<List, ListAssert<ELEMENT>> list(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(List.class, Assertions::<ELEMENT> assertThat);
  }

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Stream, ListAssert<Object>> stream() {
    return stream(Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Stream, ListAssert<ELEMENT>> stream(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Stream.class, Assertions::<ELEMENT> assertThat);
  }

  InstanceOfAssertFactory<DoubleStream, ListAssert<Double>> DOUBLE_STREAM = new InstanceOfAssertFactory<>(DoubleStream.class,
                                                                                                          Assertions::assertThat);

  InstanceOfAssertFactory<LongStream, ListAssert<Long>> LONG_STREAM = new InstanceOfAssertFactory<>(LongStream.class,
                                                                                                    Assertions::assertThat);

  InstanceOfAssertFactory<IntStream, ListAssert<Integer>> INT_STREAM = new InstanceOfAssertFactory<>(IntStream.class,
                                                                                                     Assertions::assertThat);

  InstanceOfAssertFactory<Path, AbstractPathAssert<?>> PATH = new InstanceOfAssertFactory<>(Path.class,
                                                                                            Assertions::assertThat);

  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  static InstanceOfAssertFactory<Map, MapAssert<Object, Object>> map() {
    return map(Object.class, Object.class);
  }

  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <K, V> InstanceOfAssertFactory<Map, MapAssert<K, V>> map(Class<K> keyType, Class<V> valueType) {
    return new InstanceOfAssertFactory<>(Map.class, Assertions::<K, V> assertThat);
  }

  static <T extends Comparable<? super T>> InstanceOfAssertFactory<T, AbstractComparableAssert<?, T>> comparable(Class<T> comparableType) {
    return new InstanceOfAssertFactory<>(comparableType, Assertions::assertThat);
  }

}
