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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import java.util.WeakHashMap;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

/**
 * Tests for <code>{@link Maps#newWeakHashMap()}</code>.
 * 
 * @author Christian Rösch
 */
public class Maps_newWeakHashMap_Test {
  @Test
  public void should_return_empty_mutable_WeakHashMap() {
    Map<String, Integer> map = Maps.newWeakHashMap();
    assertThat(map).isInstanceOf(WeakHashMap.class);
    assertThat(map).isEmpty();
    map.put("abc", 123);

    assertThat(map).containsExactly(MapEntry.entry("abc", 123));
  }

  @Test
  public void should_return_new_WeakHashMap() {
    Map<String, Integer> map1 = Maps.newWeakHashMap();
    Map<String, Integer> map2 = Maps.newWeakHashMap();
    assertThat(map2).isNotSameAs(map1);

    // be sure they have nothing in common
    map1.put("abc", 123);
    assertThat(map2).isEmpty();
  }
}
