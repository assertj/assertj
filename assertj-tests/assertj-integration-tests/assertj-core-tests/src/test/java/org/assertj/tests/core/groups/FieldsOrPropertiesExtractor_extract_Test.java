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
package org.assertj.tests.core.groups;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.groups.FieldsOrPropertiesExtractor.extract;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.tests.core.testkit.Employee;
import org.assertj.tests.core.testkit.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldsOrPropertiesExtractor_extract_Test {

  private List<Employee> employees;

  @BeforeEach
  public void setUp() {
    Employee yoda = new Employee(1L, new Name("Yoda"), 800);
    yoda.surname = new Name("Master", "Jedi");
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = list(yoda, luke);
  }

  @Test
  void should_extract_field_values_in_absence_of_properties() {
    // WHEN
    List<Object> extractedValues = extract(employees, byName("id"));
    // THEN
    then(extractedValues).containsOnly(1L, 2L);
  }

  @Test
  void should_extract_null_values_for_null_property_values() {
    // GIVEN
    employees.getFirst().setName(null);
    // WHEN
    List<Object> extractedValues = extract(employees, byName("name"));
    // THEN
    then(extractedValues).containsOnly(null, new Name("Luke", "Skywalker"));
  }

  @Test
  void should_extract_null_values_for_null_nested_property_values() {
    // GIVEN
    employees.getFirst().setName(null);
    // WHEN
    List<Object> extractedValues = extract(employees, byName("name.first"));
    // THEN
    then(extractedValues).containsOnly(null, "Luke");
  }

  @Test
  void should_extract_null_values_for_null_field_values() {
    // WHEN
    List<Object> extractedValues = extract(employees, byName("surname"));
    // THEN
    then(extractedValues).containsOnly(new Name("Master", "Jedi"), null);
  }

  @Test
  void should_extract_null_values_for_null_nested_field_values() {
    // WHEN
    List<Object> extractedValues = extract(employees, byName("surname.first"));
    // THEN
    then(extractedValues).containsOnly("Master", null);
  }

  @Test
  void should_extract_property_values_when_no_public_field_match_given_name() {
    // WHEN
    List<Object> extractedValues = extract(employees, byName("age"));
    // THEN
    then(extractedValues).containsOnly(800, 26);
  }

  @Test
  void should_extract_pure_property_values() {
    // WHEN
    List<Object> extractedValues = extract(employees, byName("adult"));
    // THEN
    then(extractedValues).containsOnly(true);
  }

  @Test
  void should_throw_error_when_no_property_nor_public_field_match_given_name() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> extract(employees, byName("unknown")));
  }

  @Test
  void should_throw_exception_when_given_name_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> extract(employees, byName((String) null)))
                                        .withMessage("The name of the property/field to read should not be null");
  }

  @Test
  void should_throw_exception_when_given_name_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> extract(employees, byName("")))
                                        .withMessage("The name of the property/field to read should not be empty");
  }

  @Test
  void should_fallback_to_field_if_exception_has_been_thrown_on_property_access() {
    // GIVEN
    List<Employee> employees = list(new EmployeeWithBrokenName("Name"));
    // WHEN
    List<Object> extractedValues = extract(employees, byName("name"));
    // THEN
    then(extractedValues).containsOnly(new Name("Name"));
  }

  @Test
  void should_prefer_properties_over_fields() {
    // GIVEN
    List<Employee> employees = list(new EmployeeWithOverriddenName("Overridden Name"));
    // WHEN
    List<Object> extractedValues = extract(employees, byName("name"));
    // THEN
    then(extractedValues).containsOnly(new Name("Overridden Name"));
  }

  @Test
  void should_throw_exception_if_property_cannot_be_extracted_due_to_runtime_exception_during_property_access() {
    // GIVEN
    List<Employee> employees = list(new BrokenEmployee());
    // WHEN/THEN
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> extract(employees, byName("adult")));
  }

  public static class EmployeeWithBrokenName extends Employee {

    public EmployeeWithBrokenName(String name) {
      super(1L, new Name(name), 0);
    }

    @Override
    public Name getName() {
      throw new IllegalStateException();
    }
  }

  public static class EmployeeWithOverriddenName extends Employee {

    private final String overriddenName;

    public EmployeeWithOverriddenName(final String overriddenName) {
      super(1L, new Name("Name"), 0);
      this.overriddenName = overriddenName;
    }

    @Override
    public Name getName() {
      return new Name(overriddenName);
    }
  }

  public static class BrokenEmployee extends Employee {

    @Override
    public boolean isAdult() {
      throw new IllegalStateException();
    }
  }
}
