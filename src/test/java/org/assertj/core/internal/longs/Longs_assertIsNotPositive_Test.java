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
package org.assertj.core.internal.longs;

import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.internal.LongsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Longs#assertIsNotPositive(AssertionInfo, Long))}</code>.
 * 
 * @author Nicolas François
 */
public class Longs_assertIsNotPositive_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_positive() {
    longs.assertIsNotPositive(someInfo(), -6L);
  }

  @Test
  public void should_succeed_since_actual_is_zero() {
    longs.assertIsNotPositive(someInfo(), 0L);
  }

  @Test
  public void should_fail_since_actual_is_positive() {
    thrown.expectAssertionError("%nExpecting:%n <6L>%nto be less than or equal to:%n <0L> ");
    longs.assertIsNotPositive(someInfo(), 6L);
  }

  @Test
  public void should_fail_since_actual_can_be_positive_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError("%nExpecting:%n <-1L>%nto be less than or equal to:%n <0L> when comparing values using 'AbsValueComparator'");
    longsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), -1L);
  }

  @Test
  public void should_fail_since_actual_is_positive_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError("%nExpecting:%n <1L>%nto be less than or equal to:%n <0L> when comparing values using 'AbsValueComparator'");
    longsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), 1L);
  }

}
