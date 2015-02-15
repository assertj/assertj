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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.test.ExpectedException.*;
import static org.assertj.core.util.Lists.*;

import java.util.Comparator;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.groups.Tuple;
import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#extracting(String)}</code> and
 * <code>{@link AbstractIterableAssert#extracting(String...)}</code>.
 * 
 * @author Joel Costigliola
 * @author Mateusz Haligowski
 */
public class IterableAssert_extracting_Test {

  private Employee yoda;
  private Employee luke;
  private Iterable<Employee> employees;

  private static final Extractor<Employee, String> firstName = new Extractor<Employee, String>() {
    @Override
    public String extract(Employee input) {
      return input.getName().getFirst();
    }
  };
  
  private static final Extractor<Employee, Integer> age = new Extractor<Employee, Integer>() {
    @Override
    public Integer extract(Employee input) {
      return input.getAge();
    }
  };
  
  @Before
  public void setUp() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = newArrayList(yoda, luke);
  }

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable() throws Exception {
    assertThat(employees).as("extract property backed by a private field")
                         .extracting("age")
                         .containsOnly(800, 26);
    assertThat(employees).as("extract pure property")
                         .extracting("adult")
                         .containsOnly(true, true);
    assertThat(employees).as("nested property")
                         .extracting("name.first")
                         .containsOnly("Yoda", "Luke");
    assertThat(employees).as("extract field that is also a property")
                         .extracting("name")
                         .containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
    assertThat(employees).as("extract field that is also a property but specifiying the extracted type")
                         .extracting("name", Name.class)
                         .containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_allow_assertions_on_null_property_values_extracted_from_given_iterable() throws Exception {
    yoda.name.setFirst(null);
    assertThat(employees).as("not null property but null nested property")
                         .extracting("name.first")
                         .containsOnly(null, "Luke");
    yoda.setName(null);
    assertThat(employees).as("extract nested property when top property is null")
                         .extracting("name.first")
                         .containsOnly(null, "Luke");
    assertThat(employees).as("null property")
                         .extracting("name")
                         .containsOnly(null, new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_allow_assertions_on_field_values_extracted_from_given_iterable() throws Exception {
    assertThat(employees).as("extract field")
                         .extracting("id")
                         .containsOnly(1L, 2L);
    assertThat(employees).as("null field")
                         .extracting("surname")
                         .containsNull();
    assertThat(employees).as("null nested field")
                         .extracting("surname.first")
                         .containsNull();
    yoda.surname = new Name();
    assertThat(employees).as("not null field but null nested field")
                         .extracting("surname.first")
                         .containsNull();
    yoda.surname = new Name("Master");
    assertThat(employees).as("nested field")
                         .extracting("surname.first")
                         .containsOnly("Master", null);
    assertThat(employees).as("extract field specifiying the extracted type")
                         .extracting("surname", Name.class)
                         .containsOnly(new Name("Master"), null);
  }

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable_with_extracted_type_defined()
      throws Exception {
    // extract field that is also a property and check generic for comparator.
    assertThat(employees).extracting("name", Name.class).usingElementComparator(new Comparator<Name>() {
      @Override
      public int compare(Name o1, Name o2) {
        return o1.getFirst().compareTo(o2.getFirst());
      }
    }).containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_throw_error_if_no_property_nor_field_with_given_name_can_be_extracted() throws Exception {
    thrown.expect(IntrospectionError.class);
    assertThat(employees).extracting("unknown");
  }

  @Test
  public void should_allow_assertions_on_multiple_extracted_values_from_given_iterable() throws Exception {
    assertThat(employees).extracting("name.first", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
        tuple("Luke", 26, 2L));
  }

  @Test
  public void should_throw_error_if_one_property_or_field_can_not_be_extracted() throws Exception {
    thrown.expect(IntrospectionError.class);
    assertThat(employees).extracting("unknown", "age", "id")
        .containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
  }

  @Test
  public void should_allow_extracting_single_values_using_extractor() throws Exception {
    assertThat(employees).extracting(firstName).containsOnly("Yoda", "Luke");
    assertThat(employees).extracting(age).containsOnly(26, 800);
  }

  @Test
  public void sohuld_allow_extracting_multiple_values_using_extractor() throws Exception {
    assertThat(employees).extracting(new Extractor<Employee, Tuple>() {
      @Override
      public Tuple extract(Employee input) {
        return new Tuple(input.getName().getFirst(), input.getAge(), input.id);
      }
    }).containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
  }
  
}
