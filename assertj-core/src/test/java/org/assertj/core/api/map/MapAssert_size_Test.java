/*
 * Copyright 2012-2026 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Map;

import org.junit.jupiter.api.Test;

class MapAssert_size_Test {

  @Test
  void should_be_able_to_use_integer_assertions_on_size_the_map_size() {
    // GIVEN
    Map<String, String> stringToString = mapOf(entry("a", "1"), entry("b", "2"));
    // WHEN/THEN
    then(stringToString).size().isGreaterThan(0).isLessThanOrEqualTo(3)
                        .returnToMap().contains(entry("a", "1"));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Map<Object, Object> nullMap = null;
    // WHEN
    var error = expectAssertionError(() -> assertThat(nullMap).size().isGreaterThan(1));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

}
