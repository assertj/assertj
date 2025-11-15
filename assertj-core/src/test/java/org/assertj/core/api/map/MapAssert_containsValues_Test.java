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
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.api.MapAssert#containsValue(Object)}</code>.
 *
 * @author Alexander Bischof
 */
class MapAssert_containsValues_Test extends MapAssertBaseTest {

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.containsValues("value1", "value2");
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertContainsValues(getInfo(assertions), getActual(assertions), array("value1", "value2"), null);
  }

  @Test
  void should_honor_custom_value_equals_when_comparing_entry_values() {
    // GIVEN
    var map = map("key1", "value1", "key2", "value2");
    // WHEN/THEN
    then(map).usingEqualsForValues(String::equalsIgnoreCase)
             .containsValues("VALUE1")
             .containsValues("VALUE2", "VALUE1");
  }
}
