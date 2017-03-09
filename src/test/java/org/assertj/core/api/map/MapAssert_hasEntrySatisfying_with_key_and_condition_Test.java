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

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.Condition;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.assertj.core.data.MapEntry;

/**
 * Tests for <code>{@link MapAssert#hasEntrySatisfying(Object, Condition)}</code>.
 *
 * @author Filip Hrisafov
 */
public class MapAssert_hasEntrySatisfying_with_key_and_condition_Test extends MapAssertBaseTest {

  final MapEntry<String, String>[] entries = array(entry("key1", "value1"));

  private final Condition<Object> condition = new Condition<Object>() {
    @Override
    public boolean matches(Object value) {
      //return is not important as we are testing the invoking and the internal effects
      return false;
    }
  };

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.hasEntrySatisfying("key1", condition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertHasEntrySatisfying(getInfo(assertions), getActual(assertions), "key1", condition);
  }
}
