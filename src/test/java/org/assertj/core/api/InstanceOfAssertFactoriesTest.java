package org.assertj.core.api;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.*;

import java.security.Certificate;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;

import org.assertj.core.util.Strings;
import org.junit.jupiter.api.Test;

class InstanceOfAssertFactoriesTest {

  @Test
  void predicate_factory_should_allow_predicate_assertions() {
    Predicate<Object> predicate = Objects::isNull;
    assertThat((Object) predicate).asInstanceOf(PREDICATE).accepts((Object) null);
  }

  @Test
  void typed_predicate_factory_should_allow_typed_predicate_assertions() {
    Predicate<String> predicate = Strings::isNullOrEmpty;
    assertThat((Object) predicate).asInstanceOf(predicate(String.class)).accepts("");
  }

  @Test
  void int_predicate_factory_should_allow_int_predicate_assertions() {
    IntPredicate predicate = i -> i == 0;
    assertThat((Object) predicate).asInstanceOf(INT_PREDICATE).accepts(0);
  }

  @Test
  void long_predicate_factory_should_allow_long_predicate_assertions() {
    LongPredicate predicate = l -> l == 0L;
    assertThat((Object) predicate).asInstanceOf(LONG_PREDICATE).accepts(0L);
  }

  @Test
  void double_predicate_factory_should_allow_double_predicate_assertions() {
    DoublePredicate predicate = d -> d == 0.0;
    assertThat((Object) predicate).asInstanceOf(DOUBLE_PREDICATE).accepts(0.0);
  }

  @Test
  void completable_future_factory_should_allow_completable_future_assertions() {
    CompletableFuture<Object> completableFuture = completedFuture("done");
    assertThat((Object) completableFuture).asInstanceOf(COMPLETABLE_FUTURE).isDone();
  }

  @Test
  void typed_completable_future_factory_should_allow_typed_completable_future_assertions() {
    CompletableFuture<String> completableFuture = completedFuture("done");
    assertThat((Object) completableFuture).asInstanceOf(completableFuture(String.class)).isDone();
  }

  @Test
  void completion_stage_factory_should_allow_completable_future_assertions() {
    CompletionStage<Object> completableFuture = completedFuture("done");
    assertThat((Object) completableFuture).asInstanceOf(COMPLETION_STAGE).isDone();
  }

  @Test
  void typed_completion_stage_factory_should_allow_typed_completable_future_assertions() {
    CompletionStage<String> completableFuture = completedFuture("done");
    assertThat((Object) completableFuture).asInstanceOf(completionStage(String.class)).isDone();
  }

  @Test
  void string_factory_should_allow_string_assertions() {
    assertThat((Object) "string").asInstanceOf(STRING).startsWith("str");
  }

  @Test
  void integer_factory_should_allow_integer_assertions() {
    assertThat((Object) 0).asInstanceOf(INTEGER).isZero();
  }

  @Test
  @SuppressWarnings("deprecation")
  void class_factory_should_allow_class_assertions() {
    assertThat((Object) Certificate.class).asInstanceOf(CLASS).hasAnnotations(Deprecated.class);
  }

  @Test
  void list_factory_should_allow_list_assertions() {
    List<Object> list = asList("Homer", "Marge", "Bart", "Lisa", "Maggie");
    assertThat((Object) list).asInstanceOf(LIST).contains("Bart", "Lisa");
  }

  @Test
  void typed_list_factory_should_allow_typed_list_assertions() {
    List<String> list = asList("Homer", "Marge", "Bart", "Lisa", "Maggie");
    assertThat((Object) list).asInstanceOf(list(String.class)).contains("Bart", "Lisa");
  }

}
