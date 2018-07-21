/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.junit.jupiter.api.Test;

public class MapAssert_contains_with_Java_Util_MapEntry_Test extends MapAssertBaseTest {

  final Map.Entry<String, String>[] entries = array(javaMapEntry("key1", "value1"), javaMapEntry("key2", "value2"));

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.contains(javaMapEntry("key1", "value1"), javaMapEntry("key2", "value2"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertContains(getInfo(assertions), getActual(assertions), entries);
  }
  
  @Test
  public void invoke_api_like_user() {
     assertThat(map("key1", "value1", "key2", "value2")).contains(javaMapEntry("key2", "value2"));
  }
}
