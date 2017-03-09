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
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.assertj.core.api.Condition;
import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link MapAssert#hasEntrySatisfying(Condition)}</code>.
 */
public class MapAssert_hasEntrySatisfying_with_condition_Test extends MapAssertBaseTest {


  private final Condition<Object> condition = new Condition<Object>() {
    @Override
    public boolean matches(Object value) {
      //return is not important as we are testing the invoking and the internal effects
      return false;
    }
  };

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.hasEntrySatisfying(condition);
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertHasEntrySatisfying(getInfo(assertions), getActual(assertions), condition);
  }

  @Test
  public void invoke_like_user() {
    Map<String, Object> map = new HashMap<>();
    map.put("joe", "cool");
    Condition<? super Entry<String, Object>> cond2 = new Condition<Entry<String, Object>>() {

      @Override
      public boolean matches(Entry<String, Object> entry) {
        return !entry.getKey().isEmpty() && entry.getValue() != null;
      }
    };
    assertThat(map).hasEntrySatisfying(cond2);
  }

}
