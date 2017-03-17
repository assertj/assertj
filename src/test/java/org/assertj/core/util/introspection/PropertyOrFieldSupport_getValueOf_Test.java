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
package org.assertj.core.util.introspection;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class PropertyOrFieldSupport_getValueOf_Test {
  private static final Employee yoda = new Employee(1L, new Name("Yoda"), 800);
  private PropertyOrFieldSupport propertyOrFieldSupport;

  @Before
  public void setup() {
    propertyOrFieldSupport = PropertyOrFieldSupport.EXTRACTION;
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_extract_property_value() {
    Object value = propertyOrFieldSupport.getValueOf("age", yoda);

    assertThat(value).isEqualTo(800);
  }

  @Test
  public void should_extract_property_with_no_corresponding_field() {
    Object value = propertyOrFieldSupport.getValueOf("adult", yoda);

    assertThat(value).isEqualTo(true);
  }

  @Test
  public void should_prefer_properties_over_fields() {
    Object extractedValue = propertyOrFieldSupport.getValueOf("name", employeeWithOverriddenName("Overridden Name"));

    assertThat(extractedValue).isEqualTo(new Name("Overridden Name"));
  }

  @Test
  public void should_extract_public_field_values_as_no_property_matches_given_name() {
    Object value = propertyOrFieldSupport.getValueOf("id", yoda);

    assertThat(value).isEqualTo(1L);
  }

  @Test
  public void should_extract_private_field_values_as_no_property_matches_given_name() {
    Object value = propertyOrFieldSupport.getValueOf("city", yoda);

    assertThat(value).isEqualTo("New York");
  }

  @Test
  public void should_fallback_to_field_if_exception_has_been_thrown_on_property_access() {
    Object extractedValue = propertyOrFieldSupport.getValueOf("name", employeeWithBrokenName("Name"));

    assertThat(extractedValue).isEqualTo(new Name("Name"));
  }

  @Test
  public void should_return_null_if_one_of_nested_property_or_field_value_is_null() {
    Object value = propertyOrFieldSupport.getValueOf("surname.first", yoda);

    assertThat(value).isNull();
  }

  @Test
  public void should_extract_nested_property_field_combinations() {
    Employee darth = new Employee(1L, new Name("Darth", "Vader"), 100);
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    darth.field = luke;
    luke.field = darth;
    luke.surname = new Name("Young", "Padawan");
    Object value = propertyOrFieldSupport.getValueOf("me.field.me.field.me.field.surname.name", darth);
    assertThat(value).isEqualTo("Young Padawan");
  }

  @Test
  public void should_throw_error_when_no_property_nor_field_match_given_name() {
    thrown.expectIntrospectionError();

    propertyOrFieldSupport.getValueOf("unknown", yoda);
  }

  @Test
  public void should_throw_error_when_no_property_nor_public_field_match_given_name_if_extraction_is_limited_to_public_fields() {
    thrown.expectIntrospectionError();

    propertyOrFieldSupport = new PropertyOrFieldSupport(new PropertySupport(),
                                                        FieldSupport.EXTRACTION_OF_PUBLIC_FIELD_ONLY);

    propertyOrFieldSupport.getValueOf("city", yoda);
  }

  @Test
  public void should_throw_exception_when_given_property_or_field_name_is_null() {
    thrown.expectIllegalArgumentException("The name of the property/field to read should not be null");
    propertyOrFieldSupport.getValueOf(null, yoda);
  }

  @Test
  public void should_throw_exception_when_given_name_is_empty() {
    thrown.expectIllegalArgumentException("The name of the property/field to read should not be empty");
    propertyOrFieldSupport.getValueOf("", yoda);
  }

  @Test
  public void should_throw_exception_if_property_cannot_be_extracted_due_to_runtime_exception_during_property_access() {
    thrown.expectIntrospectionError();

    propertyOrFieldSupport.getValueOf("adult", brokenEmployee());
  }

  @Test
  public void should_throw_exception_if_no_object_is_given() {
    thrown.expectIllegalArgumentException();
    propertyOrFieldSupport.getValueOf("name", null);
  }

  @Test
  public void should_extract_single_value_from_maps_by_key() {
    String key1 = "key1";
    String key2 = "key2";
    Map<String, Employee> map1 = new HashMap<>();
    map1.put(key1, yoda);
    Employee luke = new Employee(2L, new Name("Luke"), 22);
    map1.put(key2, luke);

    Map<String, Employee> map2 = new HashMap<>();
    map2.put(key1, yoda);
    Employee han = new Employee(3L, new Name("Han"), 31);
    map2.put(key2, han);

    List<Map<String, Employee>> maps = asList(map1, map2);
    assertThat(maps).extracting(key2).containsExactly(luke, han);
    assertThat(maps).extracting(key2, Employee.class).containsExactly(luke, han);
    assertThat(maps).extracting(key1).containsExactly(yoda, yoda);
    assertThat(maps).extracting("bad key").containsExactly(null, null);
  }

  private Employee employeeWithBrokenName(String name) {
    return new Employee(1L, new Name(name), 0) {
      @Override
      public Name getName() {
        throw new IllegalStateException();
      }
    };
  }

  private Employee employeeWithOverriddenName(final String overriddenName) {
    return new Employee(1L, new Name("Name"), 0) {
      @Override
      public Name getName() {
        return new Name(overriddenName);
      }
    };
  }

  private Employee brokenEmployee() {
    return new Employee() {
      @Override
      public boolean isAdult() {
        throw new IllegalStateException();
      }
    };
  }

}
