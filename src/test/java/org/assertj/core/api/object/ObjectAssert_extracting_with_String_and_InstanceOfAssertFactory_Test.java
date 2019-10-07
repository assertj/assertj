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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.BIG_DECIMAL;
import static org.assertj.core.api.InstanceOfAssertFactories.LONG;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.BigDecimalComparator.BIG_DECIMAL_COMPARATOR;

import java.math.BigDecimal;
import java.util.Comparator;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.assertj.core.api.AbstractLongAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractStringAssert;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#extracting(String, InstanceOfAssertFactory)}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("ObjectAssert extracting(String, InstanceOfAssertFactory)")
class ObjectAssert_extracting_with_String_and_InstanceOfAssertFactory_Test {

  private Employee luke;

  @BeforeEach
  void setup() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  }

  @Test
  void should_throw_npe_if_the_given_assert_factory_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(luke).extracting("id", null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("instanceOfAssertFactory").create());;
  }

  @Test
  void should_throw_IntrospectionError_if_given_field_name_cannot_be_read() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(luke).extracting("foo", LONG));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class)
                .hasMessageContaining("Can't find any field or property with name 'foo'.");
  }

  @Test
  void should_pass_allowing_type_narrowed_assertions_on_property_extracted_by_name() {
    // WHEN
    AbstractLongAssert<?> result = assertThat(luke).extracting("id", LONG);
    // THEN
    result.isPositive();
  }

  @Test
  void should_pass_allowing_narrowed_assertions_on_inner_property_extracted_by_name() {
    // WHEN
    AbstractStringAssert<?> result = assertThat(luke).extracting("name.first", STRING);
    // THEN
    result.startsWith("Lu");
  }

  @Test
  void should_fail_if_the_extracted_value_is_not_an_instance_of_the_assert_factory_type() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(luke).extracting("name.first", LONG));
    // THEN
    then(error).hasMessageContainingAll("Expecting:", "to be an instance of:", "but was instance of:");
  }

  @Test
  void should_use_property_field_name_as_description_when_extracting_single_property() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(luke).extracting("name.first", STRING)
                                                                      .isNull());
    // THEN
    then(error).hasMessageContaining("[Extracted: name.first]");
  }

  @Test
  void should_keep_existing_assertion_state() {
    // GIVEN
    AbstractObjectAssert<?, Employee> assertion = assertThat(luke).as("description")
                                                                  .withFailMessage("error message")
                                                                  .withRepresentation(UNICODE_REPRESENTATION)
                                                                  .usingComparator(ALWAY_EQUALS)
                                                                  .usingComparatorForFields(ALWAY_EQUALS_STRING, "foo")
                                                                  .usingComparatorForType(ALWAY_EQUALS_STRING, String.class);
    // WHEN
    AbstractStringAssert<?> result = assertion.extracting("name.first", STRING);
    // THEN
    then(result).hasFieldOrPropertyWithValue("objects", extractObjectField(assertion))
                .extracting(AbstractAssert::getWritableAssertionInfo)
                .isEqualToComparingFieldByField(assertion.info);
  }

  private static Object extractObjectField(AbstractAssert<?, ?> assertion) {
    return PropertyOrFieldSupport.EXTRACTION.getValueOf("objects", assertion);
  }

  @Test
  void should_allow_to_specify_type_comparator_after_using_extracting_with_single_parameter_on_object() {
    // GIVEN
    Person obiwan = new Person("Obi-Wan");
    obiwan.setHeight(new BigDecimal("1.820"));

    Comparator<Object> heightComparator = (o1, o2) -> {
      if (o1 instanceof BigDecimal) return BIG_DECIMAL_COMPARATOR.compare((BigDecimal) o1, (BigDecimal) o2);
      throw new IllegalStateException("only supported for BigDecimal");
    };
    // WHEN
    AbstractBigDecimalAssert<?> result = assertThat(obiwan).extracting("height", BIG_DECIMAL)
                                                           .usingComparator(heightComparator);
    // THEN
    result.isEqualTo(new BigDecimal("1.82"));
  }

  @SuppressWarnings("unused")
  private static class Person {

    private final String name;
    private BigDecimal height;

    public Person(String name) {
      this.name = name;
    }

    public void setHeight(BigDecimal height) {
      this.height = height;
    }
  }
}
