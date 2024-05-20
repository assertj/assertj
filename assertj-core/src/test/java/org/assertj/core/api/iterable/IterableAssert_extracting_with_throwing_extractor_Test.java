/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorsByTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.throwingFirstNameExtractor;
import static org.assertj.core.api.GroupAssertTestHelper.throwingLastNameExtractor;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_TUPLE;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.groups.Tuple;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.junit.jupiter.api.Test;

class IterableAssert_extracting_with_throwing_extractor_Test {

  private Iterable<Employee> jedis = list(new Employee(1L, new Name("Yoda"), 800),
                                          new Employee(2L, new Name("Luke", "Skywalker"), 26));

  private static final ThrowingExtractor<Employee, Object, Exception> throwingExtractor = employee -> {
    if (employee.getAge() < 20) throw new Exception("age < 20");
    return employee.getName().getFirst();
  };

  @Test
  void should_extract_tuples_according_to_given_throwing_extractor() {
    assertThat(jedis).extracting(throwingFirstNameExtractor)
                     .contains("Yoda", "Luke");
  }

  @Test
  void should_extract_tuples_according_to_given_throwing_extractors() {
    assertThat(jedis).extracting(throwingFirstNameExtractor, throwingLastNameExtractor)
                     .contains(tuple("Yoda", null), tuple("Luke", "Skywalker"));
  }

  @Test
  void should_allow_extracting_with_anonymous_class_throwing_extractor() {
    // GIVEN
    ThrowingExtractor<Employee, Object, Exception> nameThrowingExtractor = employee -> {
      if (employee.getAge() < 20) throw new Exception("age < 20");
      return employee.getName().getFirst();
    };
    // WHEN/THEN
    then(jedis).extracting(nameThrowingExtractor).containsOnly("Yoda", "Luke");
  }

  @Test
  void should_throw_assertion_error_if_actual_is_null() {
    // GIVEN
    jedis = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(jedis).extracting(throwingFirstNameExtractor));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_rethrow_throwing_extractor_checked_exception_as_a_runtime_exception() {
    // GIVEN
    ThrowingCallable code = () -> assertThat(jedis).extracting(employee -> {
      if (employee.getAge() > 100) throw new Exception("age > 100");
      return employee.getName().getFirst();
    });
    // WHEN
    RuntimeException runtimeException = catchThrowableOfType(RuntimeException.class, code);
    // THEN
    then(runtimeException).hasMessage("java.lang.Exception: age > 100");
  }

  @Test
  void should_let_throwing_extractor_runtime_exception_bubble_up() {
    // GIVEN
    ThrowingCallable code = () -> assertThat(jedis).extracting(employee -> {
      if (employee.getAge() > 100) throw new RuntimeException("age > 100");
      return employee.getName().getFirst();
    });
    // WHEN
    RuntimeException runtimeException = catchThrowableOfType(RuntimeException.class, code);
    // THEN
    then(runtimeException).hasMessage("age > 100");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_using_throwing_extractor() {
    // GIVEN
    String description = "expected exception";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(jedis).as(description)
                                                                                .extracting(throwingExtractor)
                                                                                .containsOnly("Luke"));
    // THEN
    then(assertionError).hasMessageContaining(description);
  }

  @Test
  void extracting_by_several_throwing_extractors_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForType(ALWAYS_EQUALS_TUPLE, Tuple.class)
                                                                .extracting(throwingFirstNameExtractor, throwingLastNameExtractor)
                                                                .contains(tuple("YODA", null), tuple("Luke", "Skywalker"));
    // THEN
    then(assertion.descriptionText()).isEqualTo("test description");
    then(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    then(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    then(comparatorsByTypeOf(assertion).get(Tuple.class)).isSameAs(ALWAYS_EQUALS_TUPLE);
  }

  @Test
  void extracting_by_throwing_extractor_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                                                .extracting(throwingFirstNameExtractor)
                                                                .contains("YODA", "Luke");
    // THEN
    then(assertion.descriptionText()).isEqualTo("test description");
    then(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    then(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    then(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAYS_EQUALS_STRING);
  }

}
