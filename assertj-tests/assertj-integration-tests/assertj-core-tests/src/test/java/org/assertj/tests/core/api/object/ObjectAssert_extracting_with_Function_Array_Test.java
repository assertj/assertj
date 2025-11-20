/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;

import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.assertj.tests.core.testkit.Employee;
import org.assertj.tests.core.testkit.Name;
import org.assertj.tests.core.testkit.NavigationMethodBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectAssert_extracting_with_Function_Array_Test implements NavigationMethodBaseTest<ObjectAssert<Employee>> {

  private Employee luke;

  private static final Function<Employee, String> FIRST_NAME = employee -> employee.getName().getFirst();

  @BeforeEach
  void setUp() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  }

  @Test
  void should_allow_extracting_values_using_multiple_lambda_extractors() {
    // GIVEN
    Function<Employee, Object> lastName = employee -> employee.getName().getLast();
    // WHEN
    AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> result = assertThat(luke).extracting(FIRST_NAME, lastName);
    // THEN
    result.hasSize(2)
          .containsExactly("Luke", "Skywalker");
  }

  @Test
  void should_allow_extracting_values_using_multiple_lambda_extractors_with_type() {
    // GIVEN
    Function<Employee, String> lastName = employee -> employee.getName().getLast();
    // WHEN
    AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> result = assertThat(luke).extracting(FIRST_NAME, lastName);
    // THEN
    result.hasSize(2)
          .containsExactly("Luke", "Skywalker");
  }

  @Test
  void should_allow_extracting_values_using_multiple_method_reference_extractors() {
    // WHEN
    AbstractListAssert<?, ?, Object, ?> result = assertThat(luke).extracting(Employee::getName, Employee::getAge);
    // THEN
    result.hasSize(2)
          .doesNotContainNull();
  }

  @Test
  void should_rethrow_any_extractor_function_exception() {
    // GIVEN
    RuntimeException explosion = new RuntimeException("boom!");
    Function<Employee, Object> bomb = _ -> {
      throw explosion;
    };
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(luke).extracting(FIRST_NAME, bomb));
    // THEN
    then(throwable).isSameAs(explosion);
  }

  @Test
  void should_throw_a_NullPointerException_if_the_given_extractor_array_is_null() {
    // GIVEN
    Function<? super Employee, ?>[] extractors = null;
    // WHEN
    var nullPointerException = catchNullPointerException(() -> assertThat(luke).extracting(extractors));
    // THEN
    then(nullPointerException).hasMessage(shouldNotBeNull("extractors").create());
  }

  @Test
  void extracting_should_keep_assertion_state() {
    // GIVEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    ObjectAssert<Employee> assertion = assertThat(luke).as("test description")
                                                       .withFailMessage("error message")
                                                       .withRepresentation(UNICODE_REPRESENTATION)
                                                       .usingComparator(ALWAYS_EQUALS)
                                                       .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class);
    // WHEN
    AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> result = assertion.extracting(FIRST_NAME, Employee::getName);
    // THEN
    then(result.descriptionText()).isEqualTo("test description");
    then(result.info.overridingErrorMessage()).isEqualTo("error message");
    then(result.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    then(comparatorOf(result).getComparator()).isSameAs(ALWAYS_EQUALS);
  }

  private static Objects comparatorOf(AbstractListAssert<?, ?, ?, ?> assertion) {
    return (Objects) PropertyOrFieldSupport.EXTRACTION.getValueOf("objects", assertion);
  }

  @Override
  public ObjectAssert<Employee> getAssertion() {
    return new ObjectAssert<>(luke);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ObjectAssert<Employee> assertion) {
    return assertion.extracting(Employee::getName, Employee::getAge);
  }

}
