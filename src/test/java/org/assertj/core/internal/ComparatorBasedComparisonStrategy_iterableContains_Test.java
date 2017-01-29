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
package org.assertj.core.internal;

import static org.assertj.core.util.Lists.newArrayList;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#iterableContains(java.util.Collection, Object)}.
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_iterableContains_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void should_return_true_if_collections_contains_value_according_to_given_comparator() {
    List<String> hobbits = newArrayList("Merry", "Frodo", null, "Merry", "Sam");
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "Sam")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "SAM")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "sAm")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "sam")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, null)).isTrue();
  }

  @Test
  public void should_return_false_if_collections_does_not_contain_value_according_to_given_comparator() {
    List<String> hobbits = newArrayList("Merry", "Frodo", "Merry", null, "Sam");
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "Pippin")).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "SAM ")).isFalse();
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "Sam ")).isFalse();
  }

  @Test
  public void should_return_false_if_collections_is_empty_whatever_given_comparator_is() {
    assertThat(caseInsensitiveComparisonStrategy.iterableContains(newArrayList(), "anyone")).isFalse();
  }

}
