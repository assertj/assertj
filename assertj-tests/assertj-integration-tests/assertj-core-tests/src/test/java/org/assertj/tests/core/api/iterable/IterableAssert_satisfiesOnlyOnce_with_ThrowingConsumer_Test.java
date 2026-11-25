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
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class IterableAssert_satisfiesOnlyOnce_with_ThrowingConsumer_Test {

  private static final ThrowingConsumer<String> REQUIREMENTS = value -> assertThat(value).isEqualTo("Luke");

  private final List<String> actual = List.of("Luke", "Yoda", "Leia");

  @Test
  void should_pass_if_only_one_actual_elements_satisfies_the_requirements() {
    assertThat(actual).satisfiesOnlyOnce(REQUIREMENTS);
  }

  @Test
  void should_fail_if_more_than_once_actual_elements_satisfy_the_requirements() {
    // GIVEN
    List<String> actual = List.of("Luke", "Yoda", "Leia", "Luke");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesOnlyOnce(REQUIREMENTS));
    // THEN
    then(assertionError).hasMessageContaining("Expecting exactly one element of actual:")
                        .hasMessageContaining("to satisfy the requirements but these 2 elements did:")
                        .hasMessageContaining("[\"Luke\", \"Luke\"]");
  }

  @Test
  void should_fail_if_no_actual_elements_satisfy_the_requirements() {
    // GIVEN
    ThrowingConsumer<String> requirements = value -> assertThat(value).isEqualTo("Vader");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesOnlyOnce(requirements));
    // THEN
    then(assertionError).hasMessageContaining("Expecting exactly one element of actual:")
                        .hasMessageContaining("to satisfy the requirements but none did");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesOnlyOnce(REQUIREMENTS));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_consumer_is_null() {
    // GIVEN
    ThrowingConsumer<String> requirements = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).satisfiesOnlyOnce(requirements));
    // THEN
    then(npe).hasMessage("The Consumer<? super E> expressing the requirements must not be null");
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    IOException exception = new IOException("boom!");
    ThrowingConsumer<String> requirements = s -> {
      throw exception;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).satisfiesOnlyOnce(requirements));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> requirements = s -> {
      throw runtimeException;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).satisfiesOnlyOnce(requirements));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

}
