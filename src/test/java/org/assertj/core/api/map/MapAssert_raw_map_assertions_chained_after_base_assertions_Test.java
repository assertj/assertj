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
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;

import java.util.ArrayList;
import java.util.Map;

import org.assertj.core.description.Description;
import org.junit.Ignore;
import org.junit.Test;

public class MapAssert_raw_map_assertions_chained_after_base_assertions_Test {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Ignore
  @Test
  public void raw_map_mixing_assertions_from_AbstractAssert_and_AbstractMapAssert() {
    Description description = emptyDescription();

    Map map1 = new java.util.HashMap();
    map1.put("Key1", "Value1");
    map1.put("Key2", "Value2");

    // try all base assertions followed by map specific ones using generics
    assertThat(map1).as("desc")
                    .containsOnlyKeys("Key1", "Key2")
                    .as(description)
                    .containsOnlyKeys("Key1", "Key2")
                    .describedAs(description)
                    .describedAs("describedAs")
                    .has(null)
                    .hasSameClassAs(map1)
                    .hasToString(map1.toString())
                    .is(null)
                    .isEqualTo(map1)
                    .isExactlyInstanceOf(Map.class)
                    .isIn(new ArrayList<>())
                    .isIn(Map.class)
                    .isInstanceOf(Map.class)
                    .isInstanceOfAny(Map.class, String.class)
                    .isNot(null)
                    .isNotEqualTo(null)
                    .isNotEmpty()
                    .isNotExactlyInstanceOf(String.class)
                    .isNotIn(new ArrayList<>())
                    .isNotIn(Map.class)
                    .isNotInstanceOf(Map.class)
                    .isNotInstanceOfAny(Map.class, String.class)
                    .isNotNull()
                    .isNotOfAnyClassIn(Map.class, String.class)
                    .isNotSameAs(null)
                    .isOfAnyClassIn(Map.class, String.class)
                    .isSameAs("")
                    .overridingErrorMessage("")
                    .withFailMessage("")
                    .withThreadDumpOnError()
                    .usingDefaultComparator()
                    .containsOnlyKeys("Key1", "Key2");
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Test
  public void test_bug_485() {
    // https://github.com/joel-costigliola/assertj-core/issues/485
    Map map1 = new java.util.HashMap<>();
    map1.put("Key1", "Value1");
    map1.put("Key2", "Value2");

    assertThat(map1).as("").containsOnlyKeys("Key1", "Key2");
  }

}
