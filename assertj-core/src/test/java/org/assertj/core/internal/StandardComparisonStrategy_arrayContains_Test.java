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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.util.Arrays.array;

import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StandardComparisonStrategy#arrayContains(Object, Object)}.
 * 
 * @author Joel Costigliola
 */
class StandardComparisonStrategy_arrayContains_Test extends AbstractTest_StandardComparisonStrategy {

  @Test
  void should_return_true_if_array_contains_value() {
    String[] hobbits = array("Merry", "Frodo", null, "Merry", "Sam");
    assertThat(standardComparisonStrategy.arrayContains(hobbits, "Sam")).isTrue();
    assertThat(standardComparisonStrategy.arrayContains(hobbits, "Merry")).isTrue();
    assertThat(standardComparisonStrategy.arrayContains(hobbits, null)).isTrue();
  }

  @Test
  void should_return_false_if_array_does_not_contain_value() {
    String[] hobbits = array("Merry", "Frodo", "Merry", "Sam");
    assertThat(standardComparisonStrategy.arrayContains(hobbits, "Pippin")).isFalse();
    assertThat(standardComparisonStrategy.arrayContains(hobbits, "SAM ")).isFalse();
    assertThat(standardComparisonStrategy.arrayContains(hobbits, null)).isFalse();
  }

  @Test
  void should_return_false_if_array_is_empty() {
    assertThat(standardComparisonStrategy.arrayContains(new String[] {}, "Pippin")).isFalse();
  }

  @Test
  void should_fail_if_first_parameter_is_not_an_array() {
    assertThatIllegalArgumentException().isThrownBy(() -> standardComparisonStrategy.arrayContains("not an array", "Pippin"));
  }

}
