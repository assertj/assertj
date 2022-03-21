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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.atomic.referencearray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.util.Arrays.array;

import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;

import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

@SuppressWarnings("deprecation")
class AtomicReferenceArrayAssert_extracting_Test {

  private static Employee yoda;
  private static Employee luke;
  private static AtomicReferenceArray<Employee> employees;

  @BeforeAll
  static void setUpOnce() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = new AtomicReferenceArray<>(array(yoda, luke));
  }

  @Test
  void should_allow_assertions_on_property_values_extracted_from_given_iterable() {
    assertThat(employees).extracting("age").containsOnly(800, 26);
  }

  @Test
  void should_allow_assertions_on_property_values_extracted_from_given_iterable_with_extracted_type_defined() {
    assertThat(employees).extracting("name", Name.class).containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  void should_allow_assertions_on_field_values_extracted_from_given_iterable() {
    // basic types
    assertThat(employees).extracting("id").containsOnly(1L, 2L);
    // object
    assertThat(employees).extracting("name").containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
    // nested property
    assertThat(employees).extracting("name.first").containsOnly("Yoda", "Luke");
  }

  @Test
  void should_throw_error_if_no_property_nor_field_with_given_name_can_be_extracted() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> assertThat(employees).extracting("unknown"));
  }

  @Test
  void should_allow_assertions_on_multiple_extracted_values_from_given_iterable() {
    assertThat(employees).extracting("name.first", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                             tuple("Luke", 26, 2L));
  }

  @Test
  void should_throw_error_if_one_property_or_field_can_not_be_extracted() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> {
      assertThat(employees).extracting("unknown", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                            tuple("Luke", 26, 2L));
    });
  }

  @Test
  void should_allow_assertions_on_extractor_assertions_extracted_from_given_array() {
    assertThat(employees).extracting(input -> input.getName().getFirst()).containsOnly("Yoda", "Luke");
  }

  @Test
  void should_use_property_field_names_as_description_when_extracting_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(employees).extracting("name.first").isEmpty())
                                                   .withMessageContaining("[Extracted: name.first]");
  }

  @Test
  void should_use_property_field_names_as_description_when_extracting_typed_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(employees).extracting("name.first", String.class)
                                                                                          .isEmpty())
                                                   .withMessageContaining("[Extracted: name.first]");
  }

  @Test
  void should_use_property_field_names_as_description_when_extracting_tuples_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(employees).extracting("name.first", "name.last")
                                                                                          .isEmpty())
                                                   .withMessageContaining("[Extracted: name.first, name.last]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_typed_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(employees).as("check employees first name")
                                                                                          .extracting("name.first", String.class)
                                                                                          .isEmpty())
                                                   .withMessageContaining("[check employees first name]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_tuples_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(employees).as("check employees name")
                                                                                          .extracting("name.first", "name.last")
                                                                                          .isEmpty())
                                                   .withMessageContaining("[check employees name]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(employees).as("check employees first name")
                                                                                          .extracting("name.first").isEmpty())
                                                   .withMessageContaining("[check employees first name]");
  }

  @Test
  void should_let_anonymous_class_extractor_runtime_exception_bubble_up() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(employees).extracting(new Extractor<Employee, String>() {
      @Override
      public String extract(Employee employee) {
        if (employee.getAge() > 100) throw new RuntimeException("age > 100");
        return employee.getName().getFirst();
      }
    })).withMessage("age > 100");
  }

  @Test
  void should_let_anonymous_class_function_runtime_exception_bubble_up() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(employees).extracting(new Function<Employee, String>() {
      @Override
      public String apply(Employee employee) {
        if (employee.getAge() > 100) throw new RuntimeException("age > 100");
        return employee.getName().getFirst();
      }
    })).withMessage("age > 100");
  }

  @Test
  void should_rethrow_throwing_extractor_checked_exception_as_a_runtime_exception() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(employees).extracting(employee -> {
      if (employee.getAge() > 100) throw new Exception("age > 100");
      return employee.getName().getFirst();
    })).withMessage("java.lang.Exception: age > 100");
  }

  @Test
  void should_let_throwing_extractor_runtime_exception_bubble_up() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(employees).extracting(employee -> {
      if (employee.getAge() > 100) throw new RuntimeException("age > 100");
      return employee.getName().getFirst();
    })).withMessage("age > 100");
  }

  @Test
  void should_allow_extracting_with_throwing_extractor() {
    assertThat(employees).extracting(employee -> {
      if (employee.getAge() < 20) throw new Exception("age < 20");
      return employee.getName().getFirst();
    }).containsOnly("Yoda", "Luke");
  }

  @Test
  void should_allow_extracting_with_anonymous_class_throwing_extractor() {
    assertThat(employees).extracting(new ThrowingExtractor<Employee, Object, Exception>() {
      @Override
      public Object extractThrows(Employee employee) throws Exception {
        if (employee.getAge() < 20) throw new Exception("age < 20");
        return employee.getName().getFirst();
      }
    }).containsOnly("Yoda", "Luke");
  }

}
