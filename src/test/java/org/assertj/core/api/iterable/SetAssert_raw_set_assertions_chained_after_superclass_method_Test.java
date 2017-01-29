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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;

import java.util.HashSet;
import java.util.Set;
import java.util.Map;

import org.assertj.core.description.Description;
import org.assertj.core.description.EmptyTextDescription;
import org.junit.Ignore;
import org.junit.Test;

public class SetAssert_raw_set_assertions_chained_after_superclass_method_Test {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Ignore
  @Test
  public void raw_set_assertions_mixed_with_inherited_methods() {
    Description description = EmptyTextDescription.emptyText();

    Set set = new java.util.HashSet<>();
    set.add("Key1");
    set.add("Key2");

    assertThat(set).as("desc")
                    .containsOnly("Key1", "Key2");

    // try all base assertions followed by set specific ones using generics
    assertThat(set).as("desc")
                    .usingDefaultComparator()
                    .as(description)
                    .describedAs(description)
                    .describedAs("describedAs")
                    .has(null)
                    .hasSameClassAs(set)
                    .hasToString(set.toString())
                    .is(null)
                    .isEqualTo(set)
                    .isExactlyInstanceOf(Map.class)
                    .isIn(new HashSet<>())
                    .isIn(Map.class)
                    .isInstanceOf(Map.class)
                    .isInstanceOfAny(Map.class, String.class)
                    .isNot(null)
                    .isNotEqualTo(null)
                    .isNotEmpty()
                    .isNotExactlyInstanceOf(String.class)
                    .isNotIn(new HashSet<>())
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
    Set set = new java.util.HashSet<>();
    set.add("Key1");
    set.add("Key2");

    assertThat(set).as("")
                    .contains("Key1", "Key2");

    assertThat(set).as("")
                    .containsOnly("Key1", "Key2");
  }

}
