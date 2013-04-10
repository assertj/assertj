/*
 * Created on Jul 27, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.assertj.core.groups;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.FieldsOrPropertiesExtractor.extract;
import static org.assertj.core.groups.Tuple.tuple;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;

public class FieldsOrPropertiesExtractor_extract_tuples_test {
  
  @Rule
  public ExpectedException thrown = none();
  
  private static Employee yoda;
  private static Employee luke;
  private static List<Employee> employees;

  @BeforeClass
  public static void setUpOnce() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = newArrayList(yoda, luke);
  }

  @Test
  public void should_extract_tuples_from_fields_or_properties() {
    List<Tuple> extractedValues = extract(employees, "id", "age");
    assertThat(extractedValues).containsOnly(tuple(1L, 800), tuple(2L, 26));
  }
  
  @Test
  public void should_extract_tuples_with_consistent_iteration_order() {
    Set<Employee> employeeSet =  new HashSet<Employee>(employees);
    List<Tuple> extractedValues = extract(employeeSet, "id", "name.first", "age");
    assertThat(extractedValues).containsOnly(tuple(1L, "Yoda", 800), tuple(2L,"Luke", 26));
  }
  
  @Test
  public void should_throw_error_when_no_property_nor_public_field_match_one_of_given_names() {
    thrown.expect(IntrospectionError.class);
    extract(employees, "id", "age", "unknown");
  }
  
  @Test
  public void should_throw_exception_when_given_name_is_null() {
    thrown.expectIllegalArgumentException("The names of the fields/properties to read should not be null");
    extract(employees, (String[])null);
  }
  
  @Test
  public void should_throw_exception_when_given_name_is_empty() {
    thrown.expectIllegalArgumentException("The names of the fields/properties to read should not be empty");
    extract(employees, new String[0]);
  }
}
