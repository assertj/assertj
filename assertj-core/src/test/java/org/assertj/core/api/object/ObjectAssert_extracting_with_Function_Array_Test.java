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
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS;
import static org.assertj.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;

import java.util.List;
import java.util.function.Function;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.internal.Objects;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ObjectAssert_extracting_with_Function_Array_Test implements NavigationMethodBaseTest<ObjectAssert<Employee>> {

  private Employee luke;

  private static final Function<Employee, String> firstName = employee -> employee.getName().getFirst();

  @BeforeEach
  void setUp() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  }

  @Test
  void should_allow_extracting_values_using_multiple_lambda_extractors() {
    // GIVEN
    Function<Employee, Object> lastName = employee -> employee.getName().getLast();
    // WHEN
    AbstractListAssert<?, ?, Object, ?> result = assertThat(luke).extracting(firstName, lastName);
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
    Function<Employee, Object> bomb = employee -> {
      throw explosion;
    };
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(luke).extracting(firstName, bomb));
    // THEN
    then(error).isSameAs(explosion);
  }

  @Test
  void should_throw_a_NullPointerException_if_the_given_extractor_array_is_null() {
    // GIVEN
    Function<? super Employee, ?>[] extractors = null;
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(luke).extracting(extractors));
    // THEN
    then(error).isInstanceOf(NullPointerException.class)
               .hasMessage(shouldNotBeNull("extractors").create());
  }

  @Test
  void extracting_should_keep_assertion_state() {
    // GIVEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    ObjectAssert<Employee> assertion = assertThat(luke).as("test description")
                                                       .withFailMessage("error message")
                                                       .withRepresentation(UNICODE_REPRESENTATION)
                                                       .usingComparator(ALWAYS_EQUALS)
                                                       .usingComparatorForFields(ALWAYS_EQUALS_STRING, "foo")
                                                       .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class);
    // WHEN
    AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> result = assertion.extracting(firstName, Employee::getName);
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
