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
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.testkit.Maps.mapOf;

import java.util.Map;

import org.assertj.core.groups.Tuple;
import org.assertj.core.testkit.Employee;
import org.assertj.core.testkit.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ByNameMultipleExtractor")
class ByNameMultipleExtractorTest {

  private static final Employee YODA = new Employee(1L, new Name("Yoda"), 800);

  @Test
  void should_extract_tuples_from_fields_or_properties() {
    // GIVEN
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor("id", "age");
    // WHEN
    Tuple result = underTest.apply(YODA);
    // THEN
    then(result).isEqualTo(tuple(1L, 800));
  }

  @Test
  void should_extract_tuples_with_consistent_iteration_order() {
    // GIVEN
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor("id", "name.first", "age");
    // WHEN
    Tuple result = underTest.apply(YODA);
    // THEN
    then(result).isEqualTo(tuple(1L, "Yoda", 800));
  }

  @Test
  void should_throw_error_when_no_property_nor_public_field_match_one_of_given_names() {
    // GIVEN
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor("id", "name.first", "unknown");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(YODA));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_throw_exception_when_given_name_is_null() {
    // GIVEN
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor((String[]) null);
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(YODA));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The names of the fields/properties to read should not be null");
  }

  @Test
  void should_throw_exception_when_given_name_is_empty() {
    // GIVEN
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor(); // empty vararg array
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(YODA));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The names of the fields/properties to read should not be empty");
  }

  @Test
  void should_throw_exception_when_no_object_is_given() {
    // GIVEN
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor("id", "name.first", "age");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(null));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("The object to extract fields/properties from should not be null");
  }

  @Test
  void should_extract_multiple_values_from_map_by_keys() {
    // GIVEN
    Employee luke = new Employee(2L, new Name("Luke"), 22);
    Map<String, Employee> map = mapOf(entry("key1", YODA), entry("key2", luke), entry("key3", null));
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor("key1", "key2", "key3");
    // WHEN
    Tuple result = underTest.apply(map);
    // THEN
    then(result).isEqualTo(tuple(YODA, luke, null));
  }

  @Test
  void should_throw_error_with_map_when_non_existing_key_is_given() {
    // GIVEN
    Employee luke = new Employee(2L, new Name("Luke"), 22);
    Map<String, Employee> map = mapOf(entry("key1", YODA), entry("key2", luke));
    ByNameMultipleExtractor underTest = new ByNameMultipleExtractor("key1", "key2", "bad key");
    // WHEN
    Throwable thrown = catchThrowable(() -> underTest.apply(YODA));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

}
