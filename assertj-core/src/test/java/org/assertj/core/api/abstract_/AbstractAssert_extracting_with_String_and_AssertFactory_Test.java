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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.INTEGER;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.api.AssertFactory;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.NavigationMethodBaseTest;
import org.assertj.core.api.abstract_.AbstractAssert_extracting_with_String_and_AssertFactory_Test.TestAssert;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Stefano Cordio
 */
class AbstractAssert_extracting_with_String_and_AssertFactory_Test implements NavigationMethodBaseTest<TestAssert> {

  private TestAssert underTest;

  @BeforeEach
  void setup() {
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    underTest = new TestAssert(luke);
  }

  @Test
  void should_throw_npe_if_the_given_propertyOrField_is_null() {
    // GIVEN
    String propertyOrField = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.extracting(propertyOrField, Assertions::assertThat));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("propertyOrField").create());
  }

  @Test
  void should_throw_npe_if_the_given_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.extracting("age", null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("assertFactory").create());
  }

  @Test
  void should_throw_IntrospectionError_if_given_field_name_cannot_be_read() {
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.extracting("foo", Assertions::assertThat));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessageContaining("Can't find any field or property with name 'foo'.");
  }

  @Test
  void should_pass_allowing_assertions_on_property_value() {
    // WHEN
    AbstractAssert<?, ?> result = underTest.extracting("age", Assertions::assertThat);
    // THEN
    result.isEqualTo(26);
  }

  @Test
  void should_pass_allowing_assertions_on_inner_property_value() {
    // WHEN
    AbstractAssert<?, ?> result = underTest.extracting("name.first", Assertions::assertThat);
    // THEN
    result.isEqualTo("Luke");
  }

  @Test
  void should_pass_allowing_narrowed_assertions_on_property_value_extracted_with_instanceOfAssertFactory() {
    // WHEN
    AbstractIntegerAssert<?> result = underTest.extracting("age", INTEGER);
    // THEN
    result.isNotZero();
  }

  @Test
  void should_use_property_field_name_as_description_when_extracting_single_property() {
    // WHEN
    AssertionError error = expectAssertionError(() -> underTest.extracting("name.first", Assertions::assertThat)
                                                               .isNull());
    // THEN
    then(error).hasMessageContaining("[Extracted: name.first]");
  }

  @Test
  void should_throw_assertion_error_if_actual_is_null() {
    // GIVEN
    TestAssert underTest = new TestAssert(null);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> underTest.extracting("age", Assertions::assertThat));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Override
  public TestAssert getAssertion() {
    return underTest;
  }

  @Override
  public AbstractAssert<?, ?> invoke_navigation_method(TestAssert assertion) {
    return assertion.extracting("age", Assertions::assertThat);
  }

  static class TestAssert extends AbstractAssert<TestAssert, Object> {

    TestAssert(Object actual) {
      super(actual, TestAssert.class);
    }

    // re-declare to allow test access
    @Override
    protected <ASSERT extends AbstractAssert<?, ?>> ASSERT extracting(String propertyOrField,
                                                                      AssertFactory<Object, ASSERT> assertFactory) {
      return super.extracting(propertyOrField, assertFactory);
    }

  }

}
