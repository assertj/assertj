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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.util.introspection;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link PropertyOrFieldSupport#getSimpleValue(String, Object)}</code> with {@code Map} input.
  *
  * @author Stefano Cordio
  */
@DisplayName("PropertyOrFieldSupport getSimpleValue(String, Map)")
class PropertyOrFieldSupport_getSimpleValue_with_Map_input_Test {

  private final PropertyOrFieldSupport underTest = PropertyOrFieldSupport.EXTRACTION;
  private final AbstractMap<String, String> map = new HashMap<>();

  @Test
  void should_extract_property_value_even_if_map_key_matches_given_name() {
    // GIVEN
    map.put("empty", "value"); // key clashes with AbstractMap#isEmpty()
    // WHEN
    Object value = underTest.getSimpleValue("empty", map);
    // THEN
    then(value).isInstanceOf(Boolean.class);
  }

  @Test
  void should_extract_field_value_even_if_map_key_matches_given_name() {
    // GIVEN
    map.put("keySet", "value"); // key clashes with AbstractMap#keySet
    // WHEN
    Object value = underTest.getSimpleValue("keySet", map);
    // THEN
    then(value).isInstanceOf(Collection.class);
  }

  @Test
  void should_extract_map_value_when_no_property_or_field_matches_given_name() {
    // GIVEN
    map.put("key", "value");
    // WHEN
    Object value = underTest.getSimpleValue("key", map);
    // THEN
    then(value).isEqualTo("value");
  }

  @Test
  void should_extract_null_when_given_name_is_not_found() {
    // WHEN
    Object value = underTest.getSimpleValue("unknown", map);
    // THEN
    then(value).isNull();
  }

}
