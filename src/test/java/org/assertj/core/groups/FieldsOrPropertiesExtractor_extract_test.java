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
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;

public class FieldsOrPropertiesExtractor_extract_test {
  
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
  public void should_extract_field_values_even_if_property_exist() {
    List<Object> extractedValues = extract("id", employees);
    assertThat(extractedValues).containsOnly(1L, 2L);
  }
  
  @Test
  public void should_extract_property_values_when_no_public_field_match_given_name() {
    List<Object> extractedValues = extract("age", employees);
    assertThat(extractedValues).containsOnly(800, 26);
  }
  
  @Test
  public void should_extract_pure_property_values() {
    List<Object> extractedValues = extract("adult", employees);
    assertThat(extractedValues).containsOnly(true);
  }
  
  @Test
  public void should_throw_error_when_no_property_nor_public_field_match_given_name() {
    thrown.expect(IntrospectionError.class);
    extract("unknown", employees);
  }
  
  @Test
  public void should_throw_exception_when_given_name_is_null() {
    thrown.expectIllegalArgumentException("The name of the field/property to read should not be null");
    extract((String)null, employees);
  }
  
  @Test
  public void should_throw_exception_when_given_name_is_empty() {
    thrown.expectIllegalArgumentException("The name of the field/property to read should not be empty");
    extract("", employees);
  }
  
  @Test
  public void should_fallback_to_field_if_exception_has_been_thrown_on_property_access() throws Exception {

    List<Employee> employees = Arrays.<Employee>asList(employeeWithBrokenName("Name"));
    List<Object> extractedValues = extract("name", employees);
    assertThat(extractedValues).containsOnly(new Name("Name"));
  }


  @Test
  public void should_prefer_properties_over_fields() throws Exception {
    
    List<Employee> employees = Arrays.<Employee>asList(employeeWithOverridenName("Overriden Name"));
    List<Object> extractedValues = extract("name", employees);
    assertThat(extractedValues).containsOnly(new Name("Overriden Name"));
  }

  @Test
  public void should_throw_exception_if_property_cannot_be_extracted_due_to_runtime_exception_during_property_access() throws Exception {
    
    thrown.expect(IntrospectionError.class);
    
    List<Employee> employees = Arrays.<Employee>asList(brokenEmployee());
    extract("adult", employees);
  }

  // --
  
  private Employee employeeWithBrokenName(String name) {
    return new Employee(1L, new Name(name), 0){
      
      @Override
      public Name getName() {
        throw new IllegalStateException();
      }
    };
  }
  
  private Employee employeeWithOverridenName(final String overridenName) {
    return new Employee(1L, new Name("Name"), 0){
      
      @Override
      public Name getName() {
        return new Name(overridenName);
      }
    };
  }

  private Employee brokenEmployee() {
    return new Employee(){
      
      @Override
      public boolean isAdult() {
        throw new IllegalStateException();
      }
    };
  }
}
