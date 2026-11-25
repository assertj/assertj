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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.io.IOException;
import java.util.List;

import org.assertj.core.api.ThrowingConsumer;
import org.junit.jupiter.api.Test;

class IterableAssert_satisfiesExactly_with_ThrowingConsumer_Test {

  private final List<String> actual = List.of("Luke", "Leia", "Yoda");

  @Test
  void should_pass_when_each_element_satisfies_its_given_requirements() {
    // GIVEN
    ThrowingConsumer<String>[] requirements = array(name -> {
      if (name.isBlank()) throw new IOException("blank name");
    },
                                                    name -> assertThat(name).startsWith("Lei"),
                                                    name -> assertThat(name).endsWith("da"));
    // WHEN/THEN
    then(actual).satisfiesExactly(requirements);
  }

  @Test
  void should_fail_when_any_element_is_not_satisfying_its_requirements() {
    // GIVEN
    ThrowingConsumer<String>[] requirements = array(name -> assertThat(name).isNotBlank(),
                                                    name -> assertThat(name).startsWith("Han"),
                                                    name -> assertThat(name).endsWith("da"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    // can't build the exact error message due to internal stack traces
    then(assertionError).hasMessageStartingWith(format("%n" +
                                                       "Expecting each element of:%n" +
                                                       "  %s%n" +
                                                       "to satisfy the requirements at its index, but these elements did not:%n%n",
                                                       STANDARD_REPRESENTATION.toStringOf(actual)))
                        .hasMessageContaining(shouldStartWith("Leia", "Han").create());
  }

  @Test
  void should_fail_when_actual_and_requirements_have_different_sizes() {
    // GIVEN
    ThrowingConsumer<Object>[] requirements = array(element -> assertThat(element).isNotNull());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, requirements, actual.size(), requirements.length).create());
  }

  @Test
  void should_fail_if_requirements_is_null() {
    // GIVEN
    ThrowingConsumer<Object>[] requirements = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(assertionError).hasMessage("%nExpecting an array but was: null".formatted());
  }

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    ThrowingConsumer<Object>[] requirements = array(element -> assertThat(element).isNotNull());
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).satisfiesExactly(requirements));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_rethrow_throwables_as_runtime_exceptions() {
    // GIVEN
    Exception exception = new Exception("boom!");
    ThrowingConsumer<String> requirement = s -> {
      throw exception;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).satisfiesExactly(requirement));
    // THEN
    then(throwable).isInstanceOf(RuntimeException.class)
                   .cause().isSameAs(exception);
  }

  @Test
  void should_propagate_RuntimeException_as_is() {
    // GIVEN
    RuntimeException runtimeException = new RuntimeException("boom!");
    ThrowingConsumer<String> requirement = s -> {
      throw runtimeException;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(List.of("foo")).satisfiesExactly(requirement));
    // THEN
    then(throwable).isSameAs(runtimeException);
  }

}
