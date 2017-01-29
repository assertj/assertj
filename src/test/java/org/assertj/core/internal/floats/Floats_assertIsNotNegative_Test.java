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
 * Tests for <code>{@link Floats#assertIsNotNegative(AssertionInfo, Float))}</code>.
 * 
 * @author Nicolas François
 */
public class Floats_assertIsNotNegative_Test extends FloatsBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_negative() {
    floats.assertIsNotNegative(someInfo(), 6f);
  }

  @Test
  public void should_succeed_since_actual_is_zero() {
    floats.assertIsNotNegative(someInfo(), 0f);
  }

  @Test
  public void should_fail_since_actual_is_negative() {
    thrown.expectAssertionError("%nExpecting:%n <-6.0f>%nto be greater than or equal to:%n <0.0f> ");
    floats.assertIsNotNegative(someInfo(), -6f);
  }

  @Test
  public void should_succeed_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    floatsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), -1f);
  }

  @Test
  public void should_succeed_since_actual_positive_is_not_negative_according_to_custom_comparison_strategy() {
    floatsWithAbsValueComparisonStrategy.assertIsNotNegative(someInfo(), 1f);
  }

}
