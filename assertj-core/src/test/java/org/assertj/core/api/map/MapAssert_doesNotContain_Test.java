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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.assertj.core.data.MapEntry;
import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Nicolas François
 */
class MapAssert_doesNotContain_Test extends MapAssertBaseTest {

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.doesNotContain(entry("key1", "value1"), entry("key2", "value2"));
  }

  @Override
  protected void verify_internal_effects() {
    MapEntry<String, String>[] entries = array(entry("key1", "value1"), entry("key2", "value2"));
    verify(maps).assertDoesNotContain(getInfo(assertions), getActual(assertions), entries, null);
  }

  @Test
  void invoke_api_like_user() {
    assertThat(map("key1", "value1")).doesNotContain(entry("key2", "value2"), entry("key3", "value3"));
  }

  @Test
  void should_honor_custom_value_equals_when_comparing_entry_values() {
    // GIVEN
    var map = Map.of("key", "value");
    // WHEN/THEN
    then(map).usingEqualsForValues(String::equalsIgnoreCase)
             .doesNotContain(entry("otherKey", "value"), entry("key", "otherValue"));
    expectAssertionError(() -> assertThat(map).usingEqualsForValues(String::equalsIgnoreCase)
                                              .doesNotContain(entry("key", "Value")));
  }
}
