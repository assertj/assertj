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
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_SEQUENCE;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.function.Function;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Stefano Cordio
 */
class ObjectAssert_extracting_with_Function_and_InstanceOfAssertFactory_Test
    implements NavigationMethodBaseTest<ObjectAssert<Employee>> {

  private Employee luke;

  private static final Function<Employee, String> FIRST_NAME = employee -> employee.getName().getFirst();

  @BeforeEach
  void setUp() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  }

  @Test
  void should_throw_npe_if_the_given_extractor_is_null() {
    // GIVEN
    Function<Employee, String> extractor = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(luke).extracting(extractor, STRING));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class);
  }

  @Test
  void should_throw_npe_if_the_given_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(luke).extracting(Employee::getName, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_on_value_extracted_with_lambda() {
    // WHEN
    AbstractStringAssert<?> result = assertThat(luke).extracting(FIRST_NAME, STRING);
    // THEN
    result.startsWith("Lu");
  }

  @Test
  void should_pass_allowing_parent_type_narrowed_assertions_on_value_extracted_with_parent_type_factory() {
    // WHEN
    AbstractCharSequenceAssert<?, ?> result = assertThat(luke).extracting(FIRST_NAME, CHAR_SEQUENCE);
    // THEN
    result.startsWith("Lu");
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_on_value_extracted_with_method_reference() {
    // WHEN
    AbstractIntegerAssert<?> result = assertThat(luke).extracting(Employee::getAge, INTEGER);
    // THEN
    result.isPositive();
  }

  @Test
  void should_pass_allowing_actual_type_narrowed_assertions_on_value_extracted_as_an_object() {
    // GIVEN
    Function<Employee, Object> ageAsObject = Employee::getAge;
    // WHEN
    AbstractIntegerAssert<?> result = assertThat(luke).extracting(ageAsObject, INTEGER);
    // THEN
    result.isPositive();
  }

  @Test
  void should_fail_if_the_extracted_value_is_not_an_instance_of_the_assert_factory_type() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(luke).extracting(Employee::getAge, STRING));
    // THEN
    then(error).hasMessageContainingAll("Expecting actual:", "to be an instance of:", "but was instance of:");
  }

  @Test
  void should_rethrow_any_extractor_function_exception() {
    // GIVEN
    RuntimeException explosion = new RuntimeException("boom!");
    Function<Employee, String> bomb = employee -> {
      throw explosion;
    };
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(luke).extracting(bomb, STRING));
    // THEN
    then(error).isSameAs(explosion);
  }

  @Override
  public ObjectAssert<Employee> getAssertion() {
    return new ObjectAssert<>(luke);
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(ObjectAssert<Employee> assertion) {
    return assertion.extracting(Employee::getAge, INTEGER);
  }

}
