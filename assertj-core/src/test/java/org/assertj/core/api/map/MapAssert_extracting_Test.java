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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.HashMap;
import java.util.Map;

import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Deprecated
class MapAssert_extracting_Test {

  private static final Object NAME = "name";
  private Map<Object, Object> map;

  @BeforeEach
  void setup() {
    map = new HashMap<>();
    map.put(NAME, "kawhi");
    map.put("age", 25);
    map.put("title", null);
  }

  @Test
  void should_allow_assertions_on_values_extracted_from_given_map_keys() {
    // WHEN/THEN
    assertThat(map).extracting(NAME, "age")
                   .containsExactly("kawhi", 25);
  }

  @Test
  void should_allow_object_assertions_on_value_extracted_from_given_map_key() {
    // WHEN/THEN
    assertThat(map).extracting(NAME)
                   .isEqualTo("kawhi");
  }

  @Test
  void should_allow_assertions_on_values_extracted_from_given_extractors() {
    // WHEN/THEN
    assertThat(map).extracting(m -> m.get(NAME), m -> m.get("age"))
                   .containsExactly("kawhi", 25);
  }

  @Test
  void should_allow_object_assertions_on_value_extracted_from_given_extractor() {
    // WHEN/THEN
    assertThat(map).extracting(m -> m.get(NAME))
                   .isEqualTo("kawhi");
  }

  @Test
  void should_extract_null_element_from_unknown_key() {
    // WHEN/THEN
    assertThat(map).extracting(NAME, "unknown")
                   .containsExactly("kawhi", null);
  }

  @Test
  void should_extract_null_object_from_key_with_null_value() {
    // WHEN/THEN
    assertThat(map).extracting("title")
                   .isNull();
  }

  @Test
  void should_fail_with_unknown_key() {
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(map).extracting("unknown"));
    // THEN
    then(thrown).isInstanceOf(IntrospectionError.class);
  }

  @Test
  void should_use_key_names_as_description() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extracting(NAME, "age").isEmpty());
    // THEN
    then(error).hasMessageContaining("[Extracted: name, age]");
  }

  @Test
  void should_use_key_name_as_description() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extracting(NAME).isNull());
    // THEN
    then(error).hasMessageContaining("[Extracted: name]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_values_list() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).as("check name and age")
                                                                     .extracting(NAME, "age").isEmpty());
    // THEN
    then(error).hasMessageContaining("[check name and age]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_value_object() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).as("check name")
                                                                     .extracting(NAME).isNull());
    // THEN
    then(error).hasMessageContaining("[check name]");
  }

  @Test
  void should_fail_with_key_list_if_actual_is_null() {
    // GIVEN
    map = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extracting(NAME, "age"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_with_single_key_if_actual_is_null() {
    // GIVEN
    map = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extracting(NAME));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

}
