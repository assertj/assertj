/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class IterableAssert_satisfiesExactlyInAnyOrder_with_ThrowingConsumer_Test {

  private final List<String> actual = List.of("Luke", "Leia", "Yoda");

  @Test
  void should_pass_if_all_consumers_are_satisfied_by_different_elements_in_any_order() {
    // GIVEN
    ThrowingConsumer<String> consumer1 = s -> assertThat(s).contains("Y"); // Matches "Yoda"
    ThrowingConsumer<String> consumer2 = s -> assertThat(s).contains("L"); // Matches "Luke" and "Leia"
    ThrowingConsumer<String> consumer3 = s -> assertThat(s).doesNotContain("a"); // Matches "Luke"
    // WHEN/THEN
    then(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2, consumer3)
                .satisfiesExactlyInAnyOrder(consumer3, consumer1, consumer2);
  }

  @Test
  void should_fail_if_one_of_the_consumer_cannot_be_satisfied() {
    // GIVEN
    ThrowingConsumer<String> consumer1 = s -> assertThat(s).hasSize(5);
    ThrowingConsumer<String> consumer2 = s -> assertThat(s).hasSize(4);
    ThrowingConsumer<String> consumer3 = s -> assertThat(s).hasSize(4);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2,
                                                                                                  consumer3));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyExactlyInAnyOrder(actual).create());
  }

  @Test
  void should_fail_if_there_are_too_many_consumers() {
    // GIVEN
    ThrowingConsumer<String> consumer = s -> assertThat(s).doesNotContain("z");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer, consumer,
                                                                                                  consumer, consumer));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(actual, 3, 4).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    ThrowingConsumer<String> consumer = s -> assertThat(s).hasSize(4);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_consumer_array_is_null() {
    // GIVEN
    ThrowingConsumer<String>[] consumers = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumers));
    // THEN
    then(npe).hasMessage("The Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_fail_if_consumer_var_arg_is_null() {
    // GIVEN
    ThrowingConsumer<String> consumer = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(npe).hasMessage("Elements in the Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    IOException exception = new IOException("boom!");
    ThrowingConsumer<String> consumer = s -> {
      throw exception;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> consumer = s -> {
      throw runtimeException;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

}
