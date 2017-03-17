/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.groups;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.groups.FieldsOrPropertiesExtractor.extract;
import static org.assertj.core.groups.Tuple.tuple;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class FieldsOrPropertiesExtractor_extract_tuples_Test {

  @Rule
  public ExpectedException thrown = none();

  private Employee yoda;
  private Employee luke;
  private List<Employee> employees;

  @Before
  public void setUp() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    yoda.surname = new Name("Master", "Jedi");
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = newArrayList(yoda, luke);
  }

  @Test
  public void should_extract_tuples_from_fields_or_properties() {
    List<Tuple> extractedValues = extract(employees, byName("id", "age"));
    assertThat(extractedValues).containsOnly(tuple(1L, 800), tuple(2L, 26));
  }

  @Test
  public void should_extract_tuples_with_consistent_iteration_order() {
    Set<Employee> employeeSet = new HashSet<>(employees);
    List<Tuple> extractedValues = extract(employeeSet, byName("id", "name.first", "age"));
    assertThat(extractedValues).containsOnly(tuple(1L, "Yoda", 800), tuple(2L, "Luke", 26));
  }

  @Test
  public void should_extract_tuples_with_null_value_for_null_nested_field_or_property() {
    luke.setName(null);
    assertThat(extract(employees, byName("id", "name.first", "age"))).containsOnly(tuple(1L, "Yoda", 800), tuple(2L, null, 26));
    assertThat(extract(employees, byName("name.first"))).containsOnly("Yoda", null);
    assertThat(extract(employees, byName("id", "surname.first"))).containsOnly(tuple(1L, "Master"), tuple(2L, null));
  }

  @Test
  public void should_throw_error_when_no_property_nor_public_field_match_one_of_given_names() {
    thrown.expectIntrospectionError();
    extract(employees, byName("id", "age", "unknown"));
  }

  @Test
  public void should_throw_exception_when_given_name_is_null() {
    thrown.expectIllegalArgumentException("The names of the fields/properties to read should not be null");
    extract(employees, byName((String[]) null));
  }

  @Test
  public void should_throw_exception_when_given_name_is_empty() {
    thrown.expectIllegalArgumentException("The names of the fields/properties to read should not be empty");
    extract(employees, byName(new String[0]));
  }
}
