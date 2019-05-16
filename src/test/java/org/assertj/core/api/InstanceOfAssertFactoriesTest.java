package org.assertj.core.api;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
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
    PredicateAssert<Object> result = assertThat(value).asInstanceOf(PREDICATE);
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
    CompletableFutureAssert<Object> result = assertThat(value).asInstanceOf(COMPLETABLE_FUTURE);
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
    CompletableFutureAssert<Object> result = assertThat(value).asInstanceOf(COMPLETION_STAGE);
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
    OptionalAssert<Object> result = assertThat(value).asInstanceOf(OPTIONAL);
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
    BigDecimalAssert result = assertThat(value).asInstanceOf(BIG_DECIMAL);
    // THEN
    result.isEqualTo("0.0");
  }

  @Test
  void big_integer_factory_should_allow_big_integer_assertions() {
    // GIVEN
    Object value = BigInteger.valueOf(0L);
    // WHEN
    BigIntegerAssert result = assertThat(value).asInstanceOf(BIG_INTEGER);
    // THEN
    result.isEqualTo(0L);
  }

  @Test
  void uri_factory_should_allow_uri_assertions() {
    // GIVEN
    Object value = java.net.URI.create("http://localhost");
    // WHEN
    UriAssert result = assertThat(value).asInstanceOf(URI);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void url_factory_should_allow_url_assertions() throws MalformedURLException {
    // GIVEN
    Object value = new java.net.URL("http://localhost");
    // WHEN
    UrlAssert result = assertThat(value).asInstanceOf(URL);
    // THEN
    result.hasHost("localhost");
  }

  @Test
  void boolean_factory_should_allow_boolean_assertions() {
    // GIVEN
    Object value = true;
    // WHEN
    BooleanAssert result = assertThat(value).asInstanceOf(BOOLEAN);
    // THEN
    result.isTrue();
  }

  @Test
  void boolean_array_factory_should_allow_boolean_array_assertions() {
    // GIVEN
    Object value = new boolean[] { true, false };
    // WHEN
    BooleanArrayAssert result = assertThat(value).asInstanceOf(BOOLEAN_ARRAY);
    // THEN
    result.containsExactly(true, false);
  }

  @Test
  void byte_factory_should_allow_byte_assertions() {
    // GIVEN
    Object value = (byte) 0;
    // WHEN
    ByteAssert result = assertThat(value).asInstanceOf(BYTE);
    // THEN
    result.isEqualTo((byte) 0);
  }

  @Test
  void byte_array_factory_should_allow_byte_array_assertions() {
    // GIVEN
    Object value = new byte[] { 0, 1 };
    // WHEN
    ByteArrayAssert result = assertThat(value).asInstanceOf(BYTE_ARRAY);
    // THEN
    result.containsExactly(0, 1);
  }

  @Test
  void character_factory_should_allow_character_assertions() {
    // GIVEN
    Object value = 'a';
    // WHEN
    CharacterAssert result = assertThat(value).asInstanceOf(CHARACTER);
    // THEN
    result.isLowerCase();
  }

  @Test
  void char_array_factory_should_allow_char_array_assertions() {
    // GIVEN
    Object value = new char[] { 'a', 'b' };
    // WHEN
    CharArrayAssert result = assertThat(value).asInstanceOf(CHAR_ARRAY);
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
    DoubleAssert result = assertThat(value).asInstanceOf(DOUBLE);
    // THEN
    result.isZero();
  }

  @Test
  void double_array_factory_should_allow_double_array_assertions() {
    // GIVEN
    Object value = new double[] { 0.0, 1.0 };
    // WHEN
    DoubleArrayAssert result = assertThat(value).asInstanceOf(DOUBLE_ARRAY);
    // THEN
    result.containsExactly(0.0, 1.0);
  }

  @Test
  void file_factory_should_allow_file_assertions() {
    // GIVEN
    Object value = new File("random-file-which-does-not-exist");
    // WHEN
    FileAssert result = assertThat(value).asInstanceOf(FILE);
    // THEN
    result.doesNotExist();
  }

  @Test
  void integer_factory_should_allow_integer_assertions() {
    // GIVEN
    Object value = 0;
    // WHEN
    IntegerAssert result = assertThat(value).asInstanceOf(INTEGER);
    // THEN
    result.isZero();
  }

  @Test
  void string_factory_should_allow_string_assertions() {
    // GIVEN
    Object value = "string";
    // WHEN
    StringAssert result = assertThat(value).asInstanceOf(STRING);
    // THEN
    result.startsWith("str");
  }

  @Test
  void list_factory_should_allow_list_assertions() {
    // GIVEN
    Object value = asList("Homer", "Marge", "Bart", "Lisa", "Maggie");
    // WHEN
    ListAssert<Object> result = assertThat(value).asInstanceOf(LIST);
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
