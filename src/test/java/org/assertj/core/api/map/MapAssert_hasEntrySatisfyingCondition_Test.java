/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.Condition;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.assertj.core.data.MapEntry;
import org.junit.Test;

public class MapAssert_hasEntrySatisfyingCondition_Test extends MapAssertBaseTest {

  final MapEntry<String, String>[] entries = array(entry("key1", "value1"), entry("key2", "value2"));
  Condition<Object> valueCondition = new Condition<>($ -> true, "");

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.hasEntrySatisfying("key1", valueCondition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertHasEntrySatisfying(getInfo(assertions), getActual(assertions), "key1", valueCondition);
  }

  @Test
  public void invoke_api_like_user() {
    Map<String, String> map = map("key1", "value1", "key2", "value2");
    assertThat(map).hasEntrySatisfying("key1", new Condition<>(v -> v.contains("val"), "test condition"));
  }
}
