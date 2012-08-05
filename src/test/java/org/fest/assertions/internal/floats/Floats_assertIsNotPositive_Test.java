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
package org.fest.assertions.internal.floats;

import static org.fest.assertions.test.TestData.someInfo;

import org.junit.Test;

import org.fest.assertions.internal.FloatsBaseTest;

/**
 * Tests for <code>{@link Floats#assertIsNotPositive(org.fest.assertions.core.AssertionInfo, Float))}</code>.
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
    thrown.expectAssertionError("expected:<6.0f> to be less than or equal to:<0.0f>");
    floats.assertIsNotPositive(someInfo(), 6f);
  }

  @Test
  public void should_fail_since_actual_can_be_positive_according_to_custom_comparison_strategy() {
    thrown
        .expectAssertionError("expected:<-1.0f> to be less than or equal to:<0.0f> according to 'AbsValueComparator' comparator");
    floatsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), -1f);
  }

  @Test
  public void should_fail_since_actual_is_positive_according_to_custom_comparison_strategy() {
    thrown
        .expectAssertionError("expected:<1.0f> to be less than or equal to:<0.0f> according to 'AbsValueComparator' comparator");
    floatsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), 1f);
  }

}
