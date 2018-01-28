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
package org.assertj.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.assertj.core.description.Description;
import org.junit.Ignore;
import org.junit.Test;

public class ListAssert_raw_list_assertions_chained_after_superclass_method_Test {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Ignore
  @Test
  public void raw_list_assertions_mixed_with_inherited_methods() {
    Description description = emptyDescription();

    List list = new java.util.ArrayList<>();
    list.add("Key1");
    list.add("Key2");

    assertThat(list).as("desc")
                    .containsOnly("Key1", "Key2");

    // try all base assertions followed by list specific ones using generics
    assertThat(list).as("desc")
                    .usingDefaultComparator()
                    .isSorted()
                    .as(description)
                    .isSorted()
                    .describedAs(description)
                    .describedAs("describedAs")
                    .has(null)
                    .hasSameClassAs(list)
                    .hasToString(list.toString())
                    .is(null)
                    .isEqualTo(list)
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
                    .contains("Key1", atIndex(0));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Test
  public void test_bug_485() {
    // https://github.com/joel-costigliola/assertj-core/issues/485
    List list = new java.util.ArrayList<>();
    list.add("Key1");
    list.add("Key2");

    assertThat(list).as("")
                    .isSorted()
                    .contains("Key1", "Key2");

    assertThat(list).as("")
                    .containsOnly("Key1", "Key2");
  }

}
