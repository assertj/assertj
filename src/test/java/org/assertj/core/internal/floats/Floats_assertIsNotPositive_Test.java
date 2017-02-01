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
package org.assertj.core.internal.floats;

import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.internal.FloatsBaseTest;

import org.junit.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Floats#assertIsNotPositive(org.assertj.core.api.AssertionInfo,
 * Comparable)} ())}</code>.
 *
 * @author Nicolas François
 */
public class Floats_assertIsNotPositive_Test extends FloatsBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_positive() {
	floats.assertIsNotPositive(someInfo(), -6f);
  }

  @Test
  public void should_succeed_since_actual_is_zero() {
	floats.assertIsNotPositive(someInfo(), 0f);
  }

  @Test
  public void should_fail_since_actual_is_positive() {
	thrown.expectAssertionError("%nExpecting:%n <6.0f>%nto be less than or equal to:%n <0.0f> ");
	floats.assertIsNotPositive(someInfo(), 6f);
  }

  @Test
  public void should_fail_since_actual_can_be_positive_according_to_custom_comparison_strategy() {
	thrown.expectAssertionError("%nExpecting:%n <-1.0f>%nto be less than or equal to:%n <0.0f> when comparing values using 'AbsValueComparator'");
	floatsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), -1f);
  }

  @Test
  public void should_fail_since_actual_is_positive_according_to_custom_comparison_strategy() {
	thrown.expectAssertionError("%nExpecting:%n <1.0f>%nto be less than or equal to:%n <0.0f> when comparing values using 'AbsValueComparator'");
	floatsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), 1f);
  }

}
