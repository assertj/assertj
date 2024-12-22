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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.tests.core.groups;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.groups.FieldsOrPropertiesExtractor.extract;
import static org.assertj.core.groups.Tuple.tuple;
import static org.assertj.core.util.Lists.list;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.groups.Tuple;
import org.assertj.core.util.introspection.IntrospectionError;
import org.assertj.tests.core.testkit.Employee;
import org.assertj.tests.core.testkit.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldsOrPropertiesExtractor_extract_tuples_Test {

  private List<Employee> employees;

  @BeforeEach
  public void setUp() {
    Employee yoda = new Employee(1L, new Name("Yoda"), 800);
    yoda.surname = new Name("Master", "Jedi");
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = list(yoda, luke);
  }

  @Test
  void should_extract_tuples_from_fields_or_properties() {
    // WHEN
    List<Tuple> extractedValues = extract(employees, byName("id", "age"));
    // THEN
    then(extractedValues).containsOnly(tuple(1L, 800), tuple(2L, 26));
  }

  @Test
  void should_extract_tuples_with_consistent_iteration_order() {
    // GIVEN
    Set<Employee> employeeSet = new HashSet<>(employees);
    // WHEN
    List<Tuple> extractedValues = extract(employeeSet, byName("id", "name.first", "age"));
    // THEN
    then(extractedValues).containsOnly(tuple(1L, "Yoda", 800), tuple(2L, "Luke", 26));
  }

  @Test
  void should_extract_tuples_with_null_value_for_null_nested_field_or_property() {
    // GIVEN
    employees.get(1).setName(null);
    // WHEN/THEN
    then(extract(employees, byName("id", "name.first", "age"))).containsOnly(tuple(1L, "Yoda", 800), tuple(2L, null, 26));
    then(extract(employees, byName("name.first"))).containsOnly("Yoda", null);
    then(extract(employees, byName("id", "surname.first"))).containsOnly(tuple(1L, "Master"), tuple(2L, null));
  }

  @Test
  void should_throw_error_when_no_property_nor_public_field_match_one_of_given_names() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> extract(employees, byName("id", "age", "unknown")));
  }

  @Test
  void should_throw_exception_when_given_name_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> extract(employees, byName((String[]) null)))
                                        .withMessage("The names of the fields/properties to read should not be null");
  }

  @Test
  void should_throw_exception_when_given_name_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> extract(employees, byName()))
                                        .withMessage("The names of the fields/properties to read should not be empty");
  }
}
