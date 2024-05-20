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
package org.assertj.core.util.introspection;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PropertyOrFieldSupport_getSimpleValue_Test {

  private final PropertyOrFieldSupport underTest = PropertyOrFieldSupport.EXTRACTION;

  @Nested
  class With_Map_input {

    private final AbstractMap<String, String> input = new HashMap<>();

    @Test
    void should_extract_property_value_even_if_map_key_matches_given_name() {
      // GIVEN
      input.put("empty", "value"); // key clashes with AbstractMap#isEmpty()
      // WHEN
      Object value = underTest.getSimpleValue("empty", input);
      // THEN
      then(value).isInstanceOf(Boolean.class);
    }

    @Test
    void should_extract_field_value_even_if_map_key_matches_given_name() {
      // GIVEN
      input.put("keySet", "value"); // key clashes with AbstractMap#keySet
      // WHEN
      Object value = underTest.getSimpleValue("keySet", input);
      // THEN
      then(value).isInstanceOf(Collection.class);
    }

    @ParameterizedTest
    @CsvSource({
        "key, value",
        "key, ",
    })
    void should_extract_map_value_when_no_property_or_field_matches_given_name(String key, String expected) {
      // GIVEN
      input.put(key, expected);
      // WHEN
      Object value = underTest.getSimpleValue(key, input);
      // THEN
      then(value).isEqualTo(expected);
    }

    @Test
    void should_fail_when_given_name_is_not_found() {
      // WHEN
      Throwable thrown = catchThrowable(() -> underTest.getSimpleValue("unknown", input));
      // THEN
      then(thrown).isInstanceOf(IntrospectionError.class);
    }

  }

  @Nested
  class With_Optional_input {

    @Test
    void should_return_null_if_optional_is_empty() {
      // GIVEN
      Optional<?> input = Optional.empty();
      // WHEN
      Object value = underTest.getSimpleValue("value", input);
      // THEN
      then(value).isNull();
    }

    @Test
    void should_return_optional_value_if_optional_is_not_empty() {
      // GIVEN
      Optional<?> input = Optional.of("string");
      // WHEN
      Object value = underTest.getSimpleValue("value", input);
      // THEN
      then(value).isEqualTo("string");
    }

  }

}
