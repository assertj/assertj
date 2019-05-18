package org.assertj.core.api;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.DoublePredicate;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Test;

class InstanceOfAssertFactoriesTest {

  @Test
  void predicate_factory_should_allow_predicate_assertions() {
    // GIVEN
    Object value = (Predicate<Object>) Objects::isNull;
    // WHEN
    PredicateAssert<Object> result = assertThat(value).asInstanceOf(predicate());
    // THEN
    result.accepts((Object) null);
  }

  @Test
  void typed_predicate_factory_should_allow_typed_predicate_assertions() {
    // GIVEN
    Object value = (Predicate<String>) Strings::isNullOrEmpty;
    // WHEN
    PredicateAssert<String> result = assertThat(value).asInstanceOf(predicate(String.class));
    // THEN
    result.accepts("");
  }

  @Test
  void int_predicate_factory_should_allow_int_predicate_assertions() {
    // GIVEN
    Object value = (IntPredicate) i -> i == 0;
    // WHEN
    IntPredicateAssert result = assertThat(value).asInstanceOf(INT_PREDICATE);
    // THEN
    result.accepts(0);
  }

  @Test
  void long_predicate_factory_should_allow_long_predicate_assertions() {
    // GIVEN
    Object value = (LongPredicate) l -> l == 0L;
    // WHEN
    LongPredicateAssert result = assertThat(value).asInstanceOf(LONG_PREDICATE);
    // THEN
    result.accepts(0L);
  }

  @Test
  void double_predicate_factory_should_allow_double_predicate_assertions() {
    // GIVEN
    Object value = (DoublePredicate) d -> d == 0.0;
    // WHEN
    DoublePredicateAssert result = assertThat(value).asInstanceOf(DOUBLE_PREDICATE);
    // THEN
    result.accepts(0.0);
  }

  @Test
  void completable_future_factory_should_allow_completable_future_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<Object> result = assertThat(value).asInstanceOf(completableFuture());
    // THEN
    result.isDone();
  }

  @Test
  void typed_completable_future_factory_should_allow_typed_completable_future_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<String> result = assertThat(value).asInstanceOf(completableFuture(String.class));
    // THEN
    result.isDone();
  }

  @Test
  void completion_stage_factory_should_allow_completable_future_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<Object> result = assertThat(value).asInstanceOf(completionStage());
    // THEN
    result.isDone();
  }

  @Test
  void typed_completion_stage_factory_should_allow_typed_completable_future_assertions() {
    // GIVEN
    Object value = completedFuture("done");
    // WHEN
    CompletableFutureAssert<String> result = assertThat(value).asInstanceOf(completionStage(String.class));
    // THEN
    result.isDone();
  }

  @Test
  void optional_factory_should_allow_optional_assertions() {
    // GIVEN
    Object value = Optional.of("something");
    // WHEN
    OptionalAssert<Object> result = assertThat(value).asInstanceOf(optional());
    // THEN
    result.isPresent();
  }

  @Test
  void typed_optional_factory_should_allow_typed_optional_assertions() {
    // GIVEN
    Object value = Optional.of("something");
    // WHEN
    OptionalAssert<String> result = assertThat(value).asInstanceOf(optional(String.class));
    // THEN
    result.isPresent();
  }

  @Test
  void optional_double_factory_should_allow_optional_double_assertions() {
    // GIVEN
    Object value = OptionalDouble.of(0.0);
    // WHEN
    OptionalDoubleAssert result = assertThat(value).asInstanceOf(OPTIONAL_DOUBLE);
    // THEN
    result.isPresent();
  }

  @Test
  void optional_int_factory_should_allow_optional_int_assertions() {
    // GIVEN
    Object value = OptionalInt.of(0);
    // WHEN
    OptionalIntAssert result = assertThat(value).asInstanceOf(OPTIONAL_INT);
    // THEN
    result.isPresent();
  }

  @Test
  void optional_long_factory_should_allow_optional_long_assertions() {
    // GIVEN
    Object value = OptionalLong.of(0L);
    // WHEN
    OptionalLongAssert result = assertThat(value).asInstanceOf(OPTIONAL_LONG);
    // THEN
    result.isPresent();
  }

  @Test
  void big_decimal_factory_should_allow_big_decimal_assertions() {
    // GIVEN
    Object value = BigDecimal.valueOf(0.0);
    // WHEN
    AbstractBigDecimalAssert<?> result = assertThat(value).asInstanceOf(BIG_DECIMAL);
    // THEN
    result.isEqualTo("0.0");
  }

  @Test
  void big_integer_factory_should_allow_big_integer_assertions() {
    // GIVEN
    Object value = BigInteger.valueOf(0L);
    // WHEN
    AbstractBigIntegerAssert<?> result = assertThat(value).asInstanceOf(BIG_INTEGER);
    // THEN
    result.isEqualTo(0L);
  }

  @Test
  void uri_factory_should_allow_uri_assertions() {
    // GIVEN
    Object value = java.net.URI.create("http://localhost");
    // WHEN
    AbstractUriAssert<?> result = assertThat(value).asInstanceOf(URI);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void url_factory_should_allow_url_assertions() throws MalformedURLException {
    // GIVEN
    Object value = new java.net.URL("http://localhost");
    // WHEN
    AbstractUrlAssert<?> result = assertThat(value).asInstanceOf(URL);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void boolean_factory_should_allow_boolean_assertions() {
    // GIVEN
    Object value = true;
    // WHEN
    AbstractBooleanAssert<?> result = assertThat(value).asInstanceOf(BOOLEAN);
    // THEN
    result.isTrue();
  }

  @Test
  void boolean_array_factory_should_allow_boolean_array_assertions() {
    // GIVEN
    Object value = new boolean[] { true, false };
    // WHEN
    AbstractBooleanArrayAssert<?> result = assertThat(value).asInstanceOf(BOOLEAN_ARRAY);
    // THEN
    result.containsExactly(true, false);
  }

  @Test
  void byte_factory_should_allow_byte_assertions() {
    // GIVEN
    Object value = (byte) 0;
    // WHEN
    AbstractByteAssert<?> result = assertThat(value).asInstanceOf(BYTE);
    // THEN
    result.isEqualTo((byte) 0);
  }

  @Test
  void byte_array_factory_should_allow_byte_array_assertions() {
    // GIVEN
    Object value = new byte[] { 0, 1 };
    // WHEN
    AbstractByteArrayAssert<?> result = assertThat(value).asInstanceOf(BYTE_ARRAY);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void character_factory_should_allow_character_assertions() {
    // GIVEN
    Object value = 'a';
    // WHEN
    AbstractCharacterAssert<?> result = assertThat(value).asInstanceOf(CHARACTER);
    // THEN
    result.isLowerCase();
  }

  @Test
  void char_array_factory_should_allow_char_array_assertions() {
    // GIVEN
    Object value = new char[] { 'a', 'b' };
    // WHEN
    AbstractCharArrayAssert<?> result = assertThat(value).asInstanceOf(CHAR_ARRAY);
    // THEN
    result.doesNotHaveDuplicates();
  }

  @Test
  void class_factory_should_allow_class_assertions() {
    // GIVEN
    Object value = Function.class;
    // WHEN
    ClassAssert result = assertThat(value).asInstanceOf(CLASS);
    // THEN
    result.hasAnnotations(FunctionalInterface.class);
  }

  @Test
  void double_factory_should_allow_double_assertions() {
    // GIVEN
    Object value = 0.0;
    // WHEN
    AbstractDoubleAssert<?> result = assertThat(value).asInstanceOf(DOUBLE);
    // THEN
    result.isZero();
  }

  @Test
  void double_array_factory_should_allow_double_array_assertions() {
    // GIVEN
    Object value = new double[] { 0.0, 1.0 };
    // WHEN
    AbstractDoubleArrayAssert<?> result = assertThat(value).asInstanceOf(DOUBLE_ARRAY);
    // THEN
    result.containsExactly(0.0, 1.0);
  }

  @Test
  void file_factory_should_allow_file_assertions() {
    // GIVEN
    Object value = new File("random-file-which-does-not-exist");
    // WHEN
    AbstractFileAssert<?> result = assertThat(value).asInstanceOf(FILE);
    // THEN
    result.doesNotExist();
  }

  @Test
  void future_factory_should_allow_future_assertions() {
    // GIVEN
    Object value = mock(Future.class);
    // WHEN
    FutureAssert<Object> result = assertThat(value).asInstanceOf(future());
    // THEN
    result.isNotDone();
  }

  @Test
  void typed_future_factory_should_allow_typed_future_assertions() {
    // GIVEN
    Object value = mock(Future.class);
    // WHEN
    FutureAssert<String> result = assertThat(value).asInstanceOf(future(String.class));
    // THEN
    result.isNotDone();
  }

  @Test
  void input_stream_factory_should_allow_input_stream_assertions() {
    // GIVEN
    Object value = new ByteArrayInputStream("stream".getBytes());
    // WHEN
    AbstractInputStreamAssert<?, ?> result = assertThat(value).asInstanceOf(INPUT_STREAM);
    // THEN
    result.hasContent("stream");
  }

  @Test
  void float_factory_should_allow_float_assertions() {
    // GIVEN
    Object value = 0.0f;
    // WHEN
    AbstractFloatAssert<?> result = assertThat(value).asInstanceOf(FLOAT);
    // THEN
    result.isZero();
  }

  @Test
  void float_array_factory_should_allow_float_array_assertions() {
    // GIVEN
    Object value = new float[] { 0.0f, 1.0f };
    // WHEN
    AbstractFloatArrayAssert<?> result = assertThat(value).asInstanceOf(FLOAT_ARRAY);
    // THEN
    result.containsExactly(0.0f, 1.0f);
  }

  @Test
  void integer_factory_should_allow_integer_assertions() {
    // GIVEN
    Object value = 0;
    // WHEN
    AbstractIntegerAssert<?> result = assertThat(value).asInstanceOf(INTEGER);
    // THEN
    result.isZero();
  }

  @Test
  void int_array_factory_should_allow_int_array_assertions() {
    // GIVEN
    Object value = new int[] { 0, 1 };
    // WHEN
    AbstractIntArrayAssert<?> result = assertThat(value).asInstanceOf(INT_ARRAY);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void long_factory_should_allow_long_assertions() {
    // GIVEN
    Object value = 0L;
    // WHEN
    AbstractLongAssert<?> result = assertThat(value).asInstanceOf(LONG);
    // THEN
    result.isZero();
  }

  @Test
  void long_array_factory_should_allow_long_array_assertions() {
    // GIVEN
    Object value = new long[] { 0L, 1L };
    // WHEN
    AbstractLongArrayAssert<?> result = assertThat(value).asInstanceOf(LONG_ARRAY);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void array_factory_should_allow_array_assertions() {
    // GIVEN
    Object value = new Object[] { 0, "" };
    // WHEN
    ObjectArrayAssert<Object> result = assertThat(value).asInstanceOf(array());
    // THEN
    result.containsExactly(0, "");
  }

  @Test
  void typed_array_factory_should_allow_typed_array_assertions() {
    // GIVEN
    Object value = new Integer[] { 0, 1 };
    // WHEN
    ObjectArrayAssert<Integer> result = assertThat(value).asInstanceOf(array(Integer[].class));
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void short_factory_should_allow_short_assertions() {
    // GIVEN
    Object value = (short) 0;
    // WHEN
    AbstractShortAssert<?> result = assertThat(value).asInstanceOf(SHORT);
    // THEN
    result.isZero();
  }

  @Test
  void short_array_factory_should_allow_short_array_assertions() {
    // GIVEN
    Object value = new short[] { 0, 1 };
    // WHEN
    AbstractShortArrayAssert<?> result = assertThat(value).asInstanceOf(SHORT_ARRAY);
    // THEN
    result.containsExactly((short) 0, (short) 1);
  }

  @Test
  void date_factory_should_allow_date_assertions() {
    // GIVEN
    Object value = new Date();
    // WHEN
    AbstractDateAssert<?> result = assertThat(value).asInstanceOf(DATE);
    // THEN
    result.isBeforeOrEqualsTo(new Date());
  }

  @Test
  void zoned_date_time_factory_should_allow_zoned_date_time_assertions() {
    // GIVEN
    Object value = ZonedDateTime.now();
    // WHEN
    AbstractZonedDateTimeAssert<?> result = assertThat(value).asInstanceOf(ZONED_DATE_TIME);
    // THEN
    result.isBeforeOrEqualTo(ZonedDateTime.now());
  }

  @Test
  void local_date_time_factory_should_allow_local_date_time_assertions() {
    // GIVEN
    Object value = LocalDateTime.now();
    // WHEN
    AbstractLocalDateTimeAssert<?> result = assertThat(value).asInstanceOf(LOCAL_DATE_TIME);
    // THEN
    result.isBeforeOrEqualTo(LocalDateTime.now());
  }

  @Test
  void offset_date_time_factory_should_allow_offset_date_time_assertions() {
    // GIVEN
    Object value = OffsetDateTime.now();
    // WHEN
    AbstractOffsetDateTimeAssert<?> result = assertThat(value).asInstanceOf(OFFSET_DATE_TIME);
    // THEN
    result.isBeforeOrEqualTo(OffsetDateTime.now());
  }

  @Test
  void offset_time_factory_should_allow_offset_time_assertions() {
    // GIVEN
    Object value = OffsetTime.now();
    // WHEN
    AbstractOffsetTimeAssert<?> result = assertThat(value).asInstanceOf(OFFSET_TIME);
    // THEN
    result.isBeforeOrEqualTo(OffsetTime.now());
  }

  @Test
  void local_time_factory_should_allow_local_time_assertions() {
    // GIVEN
    Object value = LocalTime.now();
    // WHEN
    AbstractLocalTimeAssert<?> result = assertThat(value).asInstanceOf(LOCAL_TIME);
    // THEN
    result.isBeforeOrEqualTo(LocalTime.now());
  }

  @Test
  void local_date_factory_should_allow_local_date_assertions() {
    // GIVEN
    Object value = LocalDate.now();
    // WHEN
    AbstractLocalDateAssert<?> result = assertThat(value).asInstanceOf(LOCAL_DATE);
    // THEN
    result.isBeforeOrEqualTo(LocalDate.now());
  }

  @Test
  void instant_factory_should_allow_instant_assertions() {
    // GIVEN
    Object value = Instant.now();
    // WHEN
    AbstractInstantAssert<?> result = assertThat(value).asInstanceOf(INSTANT);
    // THEN
    result.isBeforeOrEqualTo(Instant.now());
  }

  @Test
  void atomic_boolean_factory_should_allow_atomic_boolean_assertions() {
    // GIVEN
    Object value = new AtomicBoolean();
    // WHEN
    AtomicBooleanAssert result = assertThat(value).asInstanceOf(ATOMIC_BOOLEAN);
    // THEN
    result.isFalse();
  }

  @Test
  void atomic_integer_factory_should_allow_atomic_integer_assertions() {
    // GIVEN
    Object value = new AtomicInteger();
    // WHEN
    AtomicIntegerAssert result = assertThat(value).asInstanceOf(ATOMIC_INTEGER);
    // THEN
    result.hasValue(0);
  }

  @Test
  void string_factory_should_allow_string_assertions() {
    // GIVEN
    Object value = "string";
    // WHEN
    AbstractStringAssert<?> result = assertThat(value).asInstanceOf(STRING);
    // THEN
    result.startsWith("str");
  }

  @Test
  void iterable_factory_should_allow_iterable_assertions() {
    // GIVEN
    Object value = asList("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    IterableAssert<Object> result = assertThat(value).asInstanceOf(iterable());
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void typed_iterable_factory_should_allow_typed_iterable_assertions() {
    // GIVEN
    Object value = asList("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    IterableAssert<String> result = assertThat(value).asInstanceOf(iterable(String.class));
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void list_factory_should_allow_list_assertions() {
    // GIVEN
    Object value = asList("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    ListAssert<Object> result = assertThat(value).asInstanceOf(list());
    // THEN
    result.contains("Bart", "Lisa");
  }

  @Test
  void typed_list_factory_should_allow_typed_list_assertions() {
    // GIVEN
    Object value = asList("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    ListAssert<String> result = assertThat(value).asInstanceOf(list(String.class));
    // THEN
    result.contains("Bart", "Lisa");
  }

}
