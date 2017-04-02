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
package org.assertj.core.api.iterable;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.extractor.Extractors;
import org.assertj.core.groups.Tuple;
import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Lists.newArrayList;

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
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable() {
    assertThat(employees).extracting("age")
                         .as("extract property backed by a private field")
                         .containsOnly(800, 26);
    assertThat(employees).extracting("adult")
                         .as("extract pure property")
                         .containsOnly(true, true);
    assertThat(employees).extracting("name.first")
                         .as("nested property")
                         .containsOnly("Yoda", "Luke");
    assertThat(employees).extracting("name")
                         .as("extract field that is also a property")
                         .containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
    assertThat(employees).extracting("name", Name.class)
                         .as("extract field that is also a property but specifying the extracted type")
                         .containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_allow_assertions_on_null_property_values_extracted_from_given_iterable() {
    yoda.name.setFirst(null);
    assertThat(employees).extracting("name.first")
                         .as("not null property but null nested property")
                         .containsOnly(null, "Luke");
    yoda.setName(null);
    assertThat(employees).extracting("name.first")
                         .as("extract nested property when top property is null")
                         .containsOnly(null, "Luke");
    assertThat(employees).extracting("name")
                         .as("null property")
                         .containsOnly(null, new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_allow_assertions_on_field_values_extracted_from_given_iterable() {
    assertThat(employees).extracting("id")
                         .as("extract field")
                         .containsOnly(1L, 2L);
    assertThat(employees).extracting("surname")
                         .as("null field")
                         .containsNull();
    assertThat(employees).extracting("surname.first")
                         .as("null nested field")
                         .containsNull();
    yoda.surname = new Name();
    assertThat(employees).extracting("surname.first")
                         .as("not null field but null nested field")
                         .containsNull();
    yoda.surname = new Name("Master");
    assertThat(employees).extracting("surname.first")
                         .as("nested field")
                         .containsOnly("Master", null);
    assertThat(employees).extracting("surname", Name.class)
                         .as("extract field specifying the extracted type")
                         .containsOnly(new Name("Master"), null);
  }

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable_with_extracted_type_defined()
       {
    // extract field that is also a property and check generic for comparator.
    assertThat(employees).extracting("name", Name.class).usingElementComparator(new Comparator<Name>() {
      @Override
      public int compare(Name o1, Name o2) {
        return o1.getFirst().compareTo(o2.getFirst());
      }
    }).containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_throw_error_if_no_property_nor_field_with_given_name_can_be_extracted() {
    thrown.expectIntrospectionError();
    assertThat(employees).extracting("unknown");
  }

  @Test
  public void should_allow_assertions_on_multiple_extracted_values_from_given_iterable() {
    assertThat(employees).extracting("name.first", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                             tuple("Luke", 26, 2L));
  }

  @Test
  public void should_throw_error_if_one_property_or_field_can_not_be_extracted() {
    thrown.expectIntrospectionError();
    assertThat(employees).extracting("unknown", "age", "id")
                         .containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
  }

  @Test
  public void should_allow_extracting_single_values_using_extractor() {
    assertThat(employees).extracting(firstName).containsOnly("Yoda", "Luke");
    assertThat(employees).extracting(age).containsOnly(26, 800);
  }

  @Test
  public void should_allow_extracting_multiple_values_using_extractor() {
    assertThat(employees).extracting(new Extractor<Employee, Tuple>() {
      @Override
      public Tuple extract(Employee input) {
        return new Tuple(input.getName().getFirst(), input.getAge(), input.id);
      }
    }).containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
  }

  @Test
  public void should_allow_extracting_by_toString_method() {
    assertThat(employees).extracting(Extractors.toStringMethod()).containsOnly(
        "Employee[id=1, name=Name[first='Yoda', last='null'], age=800]",
        "Employee[id=2, name=Name[first='Luke', last='Skywalker'], age=26]");
  }

  @Test
  public void should_use_property_field_names_as_description_when_extracting_list() {
    thrown.expectAssertionErrorWithMessageContaining("[Extracted: name.first]");

    assertThat(employees).extracting("name.first").isEmpty();
  }

  @Test
  public void should_use_property_field_names_as_description_when_extracting_typed_list() {
    thrown.expectAssertionErrorWithMessageContaining("[Extracted: name.first]");

    assertThat(employees).extracting("name.first", String.class).isEmpty();
  }

  @Test
  public void should_use_property_field_names_as_description_when_extracting_tuples() {
    thrown.expectAssertionErrorWithMessageContaining("[Extracted: name.first, name.last]");

    assertThat(employees).extracting("name.first", "name.last").isEmpty();
  }
}
