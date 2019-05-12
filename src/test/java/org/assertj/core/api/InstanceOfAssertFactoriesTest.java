package org.assertj.core.api;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import java.security.Certificate;
import java.util.Objects;
import java.util.function.DoublePredicate;
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
  void string_factory_should_allow_string_assertions() {
    // GIVEN
    Object value = "string";
    // WHEN
    StringAssert result = assertThat(value).asInstanceOf(STRING);
    // THEN
    result.startsWith("str");
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
  void class_factory_should_allow_class_assertions() {
    // GIVEN
    @SuppressWarnings("deprecation")
    Object value = Certificate.class;
    // WHEN
    ClassAssert result = assertThat(value).asInstanceOf(CLASS);
    // THEN
    result.hasAnnotations(Deprecated.class);
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
