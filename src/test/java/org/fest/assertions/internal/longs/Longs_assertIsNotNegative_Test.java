/*
 * Created on May 28, 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.fest.assertions.internal.longs;

import static org.fest.assertions.test.TestData.someInfo;

import org.junit.Test;

import org.fest.assertions.internal.LongsBaseTest;

/**
 * Tests for <code>{@link Longs#assertIsNotNegative(AssertionInfo, Longs))}</code>.
 * 
 * @author Nicolas François
 */
public class Longs_assertIsNotNegative_Test extends LongsBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_negative() {
    longs.assertIsNotNegative(someInfo(), 6L);
  }

  @Test
  public void should_succeed_since_actual_is_zero() {
    longs.assertIsNotNegative(someInfo(), 0L);
  }

  @Test
  public void should_fail_since_actual_is_negative() {
    thrown.expectAssertionError("expected:<-6L> to be greater than or equal to:<0L>");
    longs.assertIsNotNegative(someInfo(), -6L);
  }

  @Test
  public void should_succeed_since_actual_negative_is_not_negative_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), -1L);
  }

  @Test
  public void should_succeed_since_actual_positive_is_not_negative_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), 1L);
  }

}
