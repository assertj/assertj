/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.extractor;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.testkit.Maps.mapOf;

import java.util.Map;
import java.util.OptionalInt;

import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.util.introspection.Introspection;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ByNameSingleExtractor")
class ByNameSingleExtractorTest {

  private static final Employee YODA = new Employee(1L, new Name("Yoda"), 800);

  @Test
  void should_extract_field_values_even_if_property_does_not_exist() {
    // GIVEN
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("id");
    // WHEN
    Object result = underTest.apply(YODA);
    // THEN
    then(result).isEqualTo(1L);
  }

  @Test
  void should_extract_property_values_when_no_public_field_match_given_name() {
    // GIVEN
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("age");
    // WHEN
    Object result = underTest.apply(YODA);
    // THEN
    then(result).isEqualTo(800);
  }

  @Test
  void should_extract_pure_property_values() {
    // GIVEN
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("adult");
    // WHEN
    Object result = underTest.apply(YODA);
    // THEN
    then(result).isEqualTo(true);
  }

  @Test
  void should_throw_error_when_no_property_nor_public_field_match_given_name() {
    // GIVEN
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("unknown");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(YODA));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_throw_exception_when_given_name_is_null() {
    // GIVEN
    ByNameSingleExtractor underTest = new ByNameSingleExtractor(null);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(YODA));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The name of the property/field to read should not be null");
  }

  @Test
  void should_throw_exception_when_given_name_is_empty() {
    // GIVEN
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(YODA));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The name of the property/field to read should not be empty");
  }

  @Test
  void should_fallback_to_field_if_exception_has_been_thrown_on_property_access() {
    // GIVEN
    Employee employee = new Employee(1L, new Name("Name"), 0) {
      @Override
      public Name getName() {
        throw new RuntimeException();
      }
    };
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("name");
    // WHEN
    Object result = underTest.apply(employee);
    // THEN
    then(result).isEqualTo(new Name("Name"));
  }

  @Test
  void should_prefer_properties_over_fields() {
    // GIVEN
    Employee employee = new Employee(1L, new Name("Name"), 0) {
      @Override
      public Name getName() {
        return new Name("Overridden Name");
      }
    };
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("name");
    // WHEN
    Object result = underTest.apply(employee);
    // THEN
    then(result).isEqualTo(new Name("Overridden Name"));
  }

  @Test
  void should_throw_exception_if_property_cannot_be_extracted_due_to_runtime_exception_during_property_access() {
    // GIVEN
    Employee employee = new Employee() {
      @Override
      public boolean isAdult() {
        throw new RuntimeException();
      }
    };
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("adult");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(employee));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_throw_exception_if_no_object_is_given() {
    // GIVEN
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("id");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(null));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void should_extract_single_value_from_map_by_key() {
    // GIVEN
    Map<String, Employee> map = mapOf(entry("key", YODA));
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("key");
    // WHEN
    Object result = underTest.apply(map);
    // THEN
    then(result).isEqualTo(YODA);
  }

  @Test
  void should_throw_error_from_map_by_non_existing_key() {
    // GIVEN
    Map<String, Employee> map = mapOf(entry("key", YODA));
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("non-existing");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(map));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_extract_null_from_map_by_key_with_null_value() {
    // GIVEN
    Map<String, Employee> map = mapOf(entry("key", null));
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("key");
    // WHEN
    Object result = underTest.apply(map);
    // THEN
    then(result).isNull();
  }

  @Test
  void should_extract_property_field_combinations() {
    // GIVEN
    Employee darth = new Employee(1L, new Name("Darth", "Vader"), 100);
    Employee luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    darth.field = luke;
    luke.field = darth;
    luke.surname = new Name("Young", "Padawan");
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("me.field.me.field.me.field.surname.name");
    // WHEN
    Object result = underTest.apply(darth);
    // THEN
    then(result).isEqualTo("Young Padawan");
  }

  @Test
  void should_extract_property_with_bare_name_method() {
    // GIVEN
    BareOptionalIntHolder holder = new BareOptionalIntHolder(42);
    ByNameSingleExtractor underTest = new ByNameSingleExtractor("value");
    // WHEN
    Object result = underTest.apply(holder);
    // THEN
    then(result).isEqualTo(OptionalInt.of(42));
  }

  @Test
  void should_ignore_property_with_bare_name_method_when_disabled() {
    try {
      // GIVEN
      Introspection.setExtractBareNamePropertyMethods(false);
      BareOptionalIntHolder holder = new BareOptionalIntHolder(42);
      ByNameSingleExtractor underTest = new ByNameSingleExtractor("value");
      // WHEN
      Object result = underTest.apply(holder);
      // THEN
      then(result).isEqualTo(42);
    } finally {
      Introspection.setExtractBareNamePropertyMethods(true);
    }
  }

  /** This style of Optional handling is emitted by Immutables code gen library. */
  static class BareOptionalIntHolder {
    private final Integer value;

    BareOptionalIntHolder() {
      value = null;
    }

    BareOptionalIntHolder(int value) {
      this.value = value;
    }

    public OptionalInt value() {
      return OptionalInt.of(value);
    }

    // ensure setter-like methods don't distract us
    public BareOptionalIntHolder value(@SuppressWarnings("unused") int value) {
      throw new AssertionError("unreached");
    }
  }

}
