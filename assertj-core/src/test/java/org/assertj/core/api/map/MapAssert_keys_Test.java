/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;

import java.util.Map;

import org.junit.jupiter.api.Test;

class MapAssert_keys_Test {

  @Test
  void should_return_collection_assertion_after_calling_keys_for_map_assertions() {
    // GIVEN
    Map<String, Integer> map = Map.of("first", 1, "second", 2);
    // WHEN/THEN
    then(map).keys().contains("first", "second");
  }

  @Test
  void should_allow_chaining_collection_assertions_on_keys() {
    // GIVEN
    Map<String, Integer> map = Map.of("first", 1, "second", 2, "third", 3);
    // WHEN/THEN
    then(map).keys()
             .containsAnyOf("first", "fourth")
             .hasSize(3);
  }

  @Test
  void should_return_collection_assertion_with_right_generic_type() {
    // GIVEN
    Map<String, String> map = Map.of("first", "one");
    // WHEN/THEN
    then(map).keys()
             .singleElement(STRING)
             .startsWith("f");
  }

  @Test
  void should_have_an_helpful_error_message_when_keys_is_used_on_a_null_map() {
    // GIVEN
    Map<String, String> nullMap = null;
    // WHEN/THEN
    thenNullPointerException().isThrownBy(() -> then(nullMap).keys().contains("nothing"))
                              .withMessage("Can not extract keys from a null map.");
  }
}
