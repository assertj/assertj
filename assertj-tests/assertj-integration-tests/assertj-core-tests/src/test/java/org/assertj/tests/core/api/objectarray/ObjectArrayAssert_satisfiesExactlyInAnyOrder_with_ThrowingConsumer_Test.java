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
package org.assertj.tests.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class ObjectArrayAssert_satisfiesExactlyInAnyOrder_with_ThrowingConsumer_Test {

  @Test
  void should_pass_if_all_consumers_are_satisfied_by_different_elements_in_any_order() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String> consumer1 = s -> {
      if (s.isEmpty()) throw new IOException("empty name");
      assertThat(s).contains("Y"); // matches "Yoda"
    };
    ThrowingConsumer<String> consumer2 = s -> assertThat(s).contains("L"); // matches "Luke" and "Leia"
    ThrowingConsumer<String> consumer3 = s -> assertThat(s).doesNotContain("a"); // matches "Luke"
    // WHEN/THEN
    then(actual).satisfiesExactlyInAnyOrder(consumer2, consumer3, consumer1);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] actual = null;
    ThrowingConsumer<String> consumer = s -> assertThat(s).hasSize(4);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_consumer_array_is_null() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String>[] consumers = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumers));
    // THEN
    then(exception).hasMessage("The Consumer<? super E>... expressing the assertions must not be null");
  }

  @Test
  void should_fail_if_one_of_the_consumers_cannot_be_satisfied() {
    // GIVEN
    String[] actual = { "Luke", "Leia", "Yoda" };
    ThrowingConsumer<String> consumer1 = s -> assertThat(s).isNotEmpty();
    ThrowingConsumer<String> consumer2 = s -> assertThat(s).isNotEmpty();
    ThrowingConsumer<String> consumer3 = s -> assertThat(s).isBlank();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer1, consumer2,
                                                                                                  consumer3));
    // THEN
    then(assertionError).hasMessage(shouldSatisfyExactlyInAnyOrder(List.of(actual)).create());
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    String[] actual = { "foo" };
    Throwable exception = new Throwable("boom!");
    ThrowingConsumer<String> consumer = s -> {
      throw exception;
    };
    // WHEN
    var throwable = catchThrowable(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    String[] actual = { "foo" };
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> consumer = s -> {
      throw runtimeException;
    };
    // WHEN
    var throwable = catchThrowable(() -> assertThat(actual).satisfiesExactlyInAnyOrder(consumer));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

  @Test
  void should_work_with_soft_assertions() {
    // GIVEN
    SoftAssertions softly = new SoftAssertions();
    ThrowingConsumer<Object> consumer = element -> assertThat(element).isNotNull();
    // WHEN
    softly.assertThat(new String[] { "Luke", "Yoda" })
          .overridingErrorMessage("error message")
          .satisfiesExactlyInAnyOrder(consumer);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement(THROWABLE).hasMessage("error message");
  }

}
