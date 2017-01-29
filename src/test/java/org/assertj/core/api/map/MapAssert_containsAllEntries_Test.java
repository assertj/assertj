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
import static org.assertj.core.util.Arrays.array;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.assertj.core.api.MapAssert;
import org.assertj.core.api.MapAssertBaseTest;
import org.junit.Test;

public class MapAssert_containsAllEntries_Test extends MapAssertBaseTest {

  final Map.Entry<String, String>[] entries = array(javaMapEntry("firstKey", "firstValue"),
                                                    javaMapEntry("secondKey", "secondValue"));

  @Override
  protected MapAssert<Object, Object> invoke_api_method() {
    return assertions.containsAllEntriesOf(map("firstKey", "firstValue", "secondKey", "secondValue"));
  }

  @Override
  protected void verify_internal_effects() {
    verify(maps).assertContains(getInfo(assertions), getActual(assertions), entries);
  }

  @Test
  public void invoke_api_like_user() {
    Map<String, String> actual = map("firstKey", "firstValue", "secondKey", "secondValue");
    assertThat(actual).containsAllEntriesOf(map("secondKey", "secondValue", "firstKey", "firstValue"));
  }
}
