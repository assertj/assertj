/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.CHAR_SEQUENCE;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import java.util.function.Function;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectChangingMethodTest;
import org.assertj.core.api.abstract_.AbstractAssert_extracting_with_Function_and_AssertFactory_Test.TestAssert;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractAssert#extracting(Function, AssertFactory)}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("AbstractAssert extracting(Function, AssertFactory)")
class AbstractAssert_extracting_with_Function_and_AssertFactory_Test implements ObjectChangingMethodTest<TestAssert> {

  private final Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  private final TestAssert underTest = new TestAssert(luke);

  @Test
  void should_throw_npe_if_the_given_extractor_is_null() {
    // GIVEN
    Function<Employee, Object> extractor = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.extracting(extractor, Assertions::assertThat));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("extractor").create());
  }

  @Test
  void should_throw_npe_if_the_given_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.extracting(Employee::getAge, null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("assertFactory").create());
  }

  @Test
  void should_pass_allowing_assertions_on_value_extracted_with_method_reference() {
    // WHEN
    AbstractAssert<?, ?> result = underTest.extracting(Employee::getAge, Assertions::assertThat);
    // THEN
    result.isEqualTo(26);
  }

  @Test
  void should_pass_allowing_assertions_on_value_extracted_with_lambda() {
    // GIVEN
    Function<Employee, String> firstName = employee -> employee.getName().getFirst();
    // WHEN
    AbstractAssert<?, ?> result = underTest.extracting(firstName, Assertions::assertThat);
    // THEN
    result.isEqualTo("Luke");
  }

  @Test
  void should_pass_allowing_narrowed_assertions_on_value_extracted_with_instanceOfAssertFactory() {
    // WHEN
    AbstractIntegerAssert<?> result = underTest.extracting(Employee::getAge, INTEGER);
    // THEN
    result.isNotZero();
  }

  @Test
  void should_pass_allowing_parent_type_narrowed_assertions_on_value_extracted_with_parent_type_factory() {
    // GIVEN
    Function<Employee, String> firstName = employee -> employee.getName().getFirst();
    // WHEN
    AbstractCharSequenceAssert<?, ?> result = underTest.extracting(firstName, CHAR_SEQUENCE);
    // THEN
    result.startsWith("Lu");
  }

  @Test
  void should_rethrow_any_extractor_function_exception() {
    // GIVEN
    RuntimeException explosion = new RuntimeException("boom!");
    Function<Employee, String> bomb = employee -> {
      throw explosion;
    };
    // WHEN
    Throwable error = catchThrowable(() -> underTest.extracting(bomb, Assertions::assertThat));
    // THEN
    then(error).isSameAs(explosion);
  }

  @Override
  public TestAssert getAssertion() {
    return underTest;
  }

  @Override
  public AbstractAssert<?, ?> invoke_object_changing_method(TestAssert assertion) {
    return assertion.extracting(Employee::getAge, Assertions::assertThat);
  }

  static class TestAssert extends AbstractAssert<TestAssert, Employee> {

    TestAssert(Employee actual) {
      super(actual, TestAssert.class);
    }

    // re-declare to allow test access
    @Override
    protected <T, ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(Function<? super Employee, ? extends T> extractor,
                                                                         AssertFactory<T, ASSERT> assertFactory) {
      return super.extracting(extractor, assertFactory);
    }

  }

}
