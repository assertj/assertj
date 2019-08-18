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
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;

import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.internal.Objects;
import org.assertj.core.internal.TypeComparators;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.PropertyOrFieldSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ObjectAssert#extracting(Function)}</code>.
 */
class ObjectAssert_extracting_with_Function_Test {

  private Employee luke;

  private static final Function<Employee, String> firstName = employee -> employee.getName().getFirst();

  @BeforeEach
  void setUp() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  }

  @Test
  void should_allow_extracting_a_value_with_the_corresponding_type_using_a_single_lambda() {
    // WHEN
    AbstractObjectAssert<?, String> result = assertThat(luke).extracting(firstName);
    // THEN
    result.isEqualTo("Luke")
          .extracting(String::length).isEqualTo(4);
  }

  @Test
  void should_allow_extracting_a_value_with_the_corresponding_type_using_a_single_method_reference() {
    // WHEN
    AbstractObjectAssert<?, Integer> result = assertThat(luke).extracting(Employee::getAge);
    // THEN
    result.isEqualTo(26)
          .extracting(Integer::longValue).isEqualTo(26L);
  }

  @Test
  void should_rethrow_any_extractor_function_exception() {
    // GIVEN
    RuntimeException explosion = new RuntimeException("boom!");
    Function<Employee, Object> bomb = employee -> {
      throw explosion;
    };
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(luke).extracting(bomb));
    // THEN
    then(error).isSameAs(explosion);
  }

  @Test
  void should_throw_a_NullPointerException_if_the_given_extractor_is_null() {
    // GIVEN
    Function<Employee, Object> extractor = null;
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(luke).extracting(extractor));
    // THEN
    then(error).isInstanceOf(NullPointerException.class)
               .hasMessage(shouldNotBeNull("extractor").create());
  }

  @Test
  void extracting_should_honor_registered_comparator() {
    // GIVEN
    ObjectAssert<Employee> assertion = assertThat(luke).usingComparator(ALWAY_EQUALS);
    // WHEN
    AbstractObjectAssert<?, String> result = assertion.extracting(firstName);
    // THEN
    result.isEqualTo("YODA");
  }

  @Test
  void extracting_should_keep_assertion_state() {
    // GIVEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractObjectAssert<?, Employee> assertion = assertThat(luke).as("test description")
                                                                  .withFailMessage("error message")
                                                                  .withRepresentation(UNICODE_REPRESENTATION)
                                                                  .usingComparator(ALWAY_EQUALS)
                                                                  .usingComparatorForFields(ALWAY_EQUALS_STRING, "foo")
                                                                  .usingComparatorForType(ALWAY_EQUALS_STRING, String.class);
    // WHEN
    AbstractObjectAssert<?, ?> result = assertion.extracting(firstName);
    // THEN
    then(result.descriptionText()).isEqualTo("test description");
    then(result.info.overridingErrorMessage()).isEqualTo("error message");
    then(result.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    then(comparatorsByTypeOf(result).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    then(comparatorByPropertyOrFieldOf(result).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
    then(comparatorOf(result).getComparator()).isSameAs(ALWAY_EQUALS);
  }

  private static Objects comparatorOf(AbstractObjectAssert<?, ?> assertion) {
    return (Objects) PropertyOrFieldSupport.EXTRACTION.getValueOf("objects", assertion);
  }

  private static TypeComparators comparatorsByTypeOf(AbstractObjectAssert<?, ?> assertion) {
    return (TypeComparators) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorByType", assertion);
  }

  @SuppressWarnings("unchecked")
  private static Map<String, Comparator<?>> comparatorByPropertyOrFieldOf(AbstractObjectAssert<?, ?> assertion) {
    return (Map<String, Comparator<?>>) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorByPropertyOrField", assertion);
  }
}
