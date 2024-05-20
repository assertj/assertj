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
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.from;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Map;
import org.assertj.core.testkit.jdk11.Jdk11;
import org.junit.jupiter.api.Test;

class MapAssert_values_Test {

  @Test
  void should_return_collection_assertion_after_calling_values_for_map_assertions() {
    // GIVEN
    Map<String, Integer> map = Jdk11.Map.of("first", 1, "second", 2);
    // WHEN/THEN
    then(map).values().contains(1, 2);
  }

  @Test
  void should_return_collection_assertion_with_right_generic_type() {
    // GIVEN
    Map<String, String> map = Jdk11.Map.of("first", "one");
    // WHEN/THEN
    then(map).values()
             .singleElement()
             .returns('o', from(s -> s.charAt(0)));
  }

  @Test
  void should_have_an_helpful_error_message_when_values_is_used_on_a_null_map() {
    // GIVEN
    Map<String, String> nullMap = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(nullMap).values().contains("nothing"))
                                    .withMessage("Can not extract values from a null map.");
  }
}
