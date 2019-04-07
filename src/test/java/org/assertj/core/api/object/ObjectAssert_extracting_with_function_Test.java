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
 * Tests for <code>{@link ObjectAssert#extracting(Function)}</code> and <code>{@link ObjectAssert#extracting(Function[])}</code>.
 */
public class ObjectAssert_extracting_with_function_Test {

  private Employee luke;

  private static final Function<Employee, String> firstName = employee -> employee.getName().getFirst();

  @BeforeEach
  public void setUp() {
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
  }

  @Test
  public void should_allow_extracting_a_value_using_a_single_extractor() {
    assertThat(luke).extracting(firstName).isEqualTo("Luke");
    assertThat(luke).extracting(Employee::getAge).isEqualTo(26);
  }

  @Test
  public void should_allow_extracting_values_using_multiple_extractors() {
    assertThat(luke).extracting(Employee::getName, Employee::getAge)
      .hasSize(2)
      .doesNotContainNull();
    assertThat(luke).extracting(firstName, employee -> employee.getName().getLast())
      .hasSize(2)
      .containsExactly("Luke", "Skywalker");
  }

  @Test
  public void should_rethrow_any_extractor_function_exception() {
    // GIVEN
    RuntimeException explosion = new RuntimeException("boom!");
    // WHEN
    Throwable error = catchThrowable(() -> {
      throw explosion;
    });
    // THEN
    assertThat(error).isSameAs(explosion);
  }

  @Test
  public void should_throw_a_NullPointerException_if_the_given_extractor_is_null() {
    // GIVEN
    Function<Employee, Object> extractor = null;
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(luke).extracting(extractor));
    // THEN
    assertThat(error).isInstanceOf(NullPointerException.class)
                     .hasMessage("The given java.util.function.Function extractor must not be null");
  }

  @Test
  public void extracting_should_honor_registered__comparator() {
    assertThat(luke).usingComparator(ALWAY_EQUALS)
                    .extracting(firstName)
                    .isEqualTo("YODA");
  }

  @Test
  public void extracting_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractObjectAssert<?, ?> assertion = assertThat(luke).as("test description")
                                                           .withFailMessage("error message")
                                                           .withRepresentation(UNICODE_REPRESENTATION)
                                                           .usingComparator(ALWAY_EQUALS)
                                                           .usingComparatorForFields(ALWAY_EQUALS_STRING, "foo")
                                                           .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                           .extracting(firstName)
                                                           .isEqualTo("LUKE");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorByPropertyOrFieldOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorOf(assertion).getComparator()).isSameAs(ALWAY_EQUALS);
  }

  public static Objects comparatorOf(AbstractObjectAssert<?, ?> assertion) {
    return (Objects) PropertyOrFieldSupport.EXTRACTION.getValueOf("objects", assertion);
  }

  public static TypeComparators comparatorsByTypeOf(AbstractObjectAssert<?, ?> assertion) {
    return (TypeComparators) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorByType", assertion);
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Comparator<?>> comparatorByPropertyOrFieldOf(AbstractObjectAssert<?, ?> assertion) {
    return (Map<String, Comparator<?>>) PropertyOrFieldSupport.EXTRACTION.getValueOf("comparatorByPropertyOrField", assertion);
  }
}
