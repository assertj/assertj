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

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#areEqual(Object, Object)}.<br>
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_areEqual_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void should_return_true_if_objects_are_equal_according_to_comparison_strategy() {
    assertThat(caseInsensitiveComparisonStrategy.areEqual("Yoda", "Yoda")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.areEqual("Yoda", "YODA")).isTrue();
    assertThat(caseInsensitiveComparisonStrategy.areEqual("YOda", "YodA")).isTrue();
  }

  @Test
  public void should_return_true_if_both_objects_are_null() {
    assertThat(caseInsensitiveComparisonStrategy.areEqual(null, null)).isTrue();
  }

  @Test
  public void should_return_false_if_first_object_is_null_and_second_is_not() {
    assertThat(caseInsensitiveComparisonStrategy.areEqual(null, "Yoda")).isFalse();
  }

  @Test
  public void should_return_false_if_second_object_is_null_and_first_is_not() {
    assertThat(caseInsensitiveComparisonStrategy.areEqual("Yoda", null)).isFalse();
  }

  @Test
  public void should_return_false_if_objects_are_not_equal_according_to_comparison_strategy() {
    assertThat(caseInsensitiveComparisonStrategy.areEqual("Yoda", "Yod")).isFalse();
  }

  @Test
  public void should_fail_if_objects_are_not_mutually_comparable() {
    thrown.expect(ClassCastException.class);
    assertThat(caseInsensitiveComparisonStrategy.areEqual("Yoda", 5)).isFalse();
  }

}
