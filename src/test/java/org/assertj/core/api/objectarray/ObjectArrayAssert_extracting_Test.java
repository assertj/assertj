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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.test.ExpectedException.*;
import static org.assertj.core.util.Arrays.*;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#extracting(String)}</code>.
 * 
 * @author Joel Costigliola
 * @author Mateusz Haligowski
 */
public class ObjectArrayAssert_extracting_Test {

  private static Employee yoda;
  private static Employee luke;
  private static Employee[] employees;

  @Rule
  public ExpectedException thrown = none();

  @BeforeClass
  public static void setUpOnce() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = array(yoda, luke);
  }

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable() {
    assertThat(employees).extracting("age").containsOnly(800, 26);
  }

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable_with_extracted_type_defined()
       {
    assertThat(employees).extracting("name", Name.class).containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_allow_assertions_on_field_values_extracted_from_given_iterable() {
    // basic types
    assertThat(employees).extracting("id").containsOnly(1L, 2L);
    // object
    assertThat(employees).extracting("name").containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
    // nested property
    assertThat(employees).extracting("name.first").containsOnly("Yoda", "Luke");
  }

  @Test
  public void should_throw_error_if_no_property_nor_field_with_given_name_can_be_extracted() {
    thrown.expect(IntrospectionError.class);
    assertThat(employees).extracting("unknown");
  }

  @Test
  public void should_allow_assertions_on_multiple_extracted_values_from_given_iterable() {
    assertThat(employees).extracting("name.first", "age", "id").containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
  }

  @Test
  public void should_throw_error_if_one_property_or_field_can_not_be_extracted() {
    thrown.expect(IntrospectionError.class);
    assertThat(employees).extracting("unknown", "age", "id").containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
  }

  @Test
  public void should_allow_assertions_on_extractor_assertions_extracted_from_given_array() {
    assertThat(employees).extracting(new Extractor<Employee, String>() {
      @Override
      public String extract(Employee input) {
        return input.getName().getFirst();
      }
    }).containsOnly("Yoda", "Luke");
  }
}
