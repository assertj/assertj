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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.assertj.core.data.MapEntry;
import org.junit.Test;

/**
 * Tests for <code>{@link Maps#newConcurrentHashMap()}</code>.
 * 
 * @author Christian Rösch
 */
public class Maps_newConcurrentHashMap_Test {
  @Test
  public void should_return_empty_mutable_ConcurrentHashMap() {
    ConcurrentMap<String, Integer> map = Maps.newConcurrentHashMap();
    assertThat(map).isInstanceOf(ConcurrentHashMap.class);
    assertThat(map).isEmpty();
    map.put("abc", 123);

    assertThat(map).containsExactly(MapEntry.entry("abc", 123));
  }

  @Test
  public void should_return_new_ConcurrentHashMap() {
    ConcurrentMap<String, Integer> map1 = Maps.newConcurrentHashMap();
    ConcurrentMap<String, Integer> map2 = Maps.newConcurrentHashMap();
    assertThat(map2).isNotSameAs(map1);

    // be sure they have nothing in common
    map1.put("abc", 123);
    assertThat(map2).isEmpty();
  }
}
