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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.ObjectChangingMethodTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link MapAssert#extractingByKeys(Object[])}</code>.
 *
 * @author Stefano Cordio
 */
@DisplayName("MapAssert extractingByKeys(KEY...)")
class MapAssert_extractingByKeys_Test implements ObjectChangingMethodTest<MapAssert<Object, Object>> {

  private static final Object NAME = "name";
  private Map<Object, Object> map;

  @BeforeEach
  void setup() {
    map = new HashMap<>();
    map.put(NAME, "kawhi");
    map.put("age", 25);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Map<Object, Object> map = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKeys(NAME, "age"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_allow_list_assertions_on_values_extracted_from_given_map_keys() {
    // WHEN
    AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> result = assertThat(map).extractingByKeys(NAME, "age");
    // THEN
    result.containsExactly("kawhi", 25);
  }

  @Test
  void should_extract_null_element_from_unknown_key() {
    // WHEN
    AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> result = assertThat(map).extractingByKeys(NAME, "unknown");
    // THEN
    result.containsExactly("kawhi", null);
  }

  @Test
  void should_extract_empty_list_if_no_keys_are_provided() {
    // WHEN
    AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> result = assertThat(map).extractingByKeys();
    // THEN
    result.isEmpty();
  }

  @Test
  void should_use_key_name_as_description() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(map).extractingByKeys(NAME, "age").isEmpty());
    // THEN
    then(error).hasMessageContaining("[Extracted: name, age]");
  }

  @Override
  public MapAssert<Object, Object> getAssertion() {
    return assertThat(map);
  }

  @Override
  public AbstractAssert<?, ?> invoke_object_changing_method(MapAssert<Object, Object> assertion) {
    return assertion.extractingByKeys(NAME, "age");
  }

}
