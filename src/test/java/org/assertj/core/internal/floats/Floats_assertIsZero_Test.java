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

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Floats;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Floats#assertIsNegative(AssertionInfo, Float)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Floats_assertIsZero_Test extends FloatsBaseTest {

  @Test
  public void should_succeed_since_actual_is_zero() {
    floats.assertIsZero(someInfo(), 0.0f);
  }

  @Test
  public void should_fail_since_actual_is_not_zero() {
    thrown.expectAssertionError("expected:<[0].0f> but was:<[2].0f>");
    floats.assertIsZero(someInfo(), 2.0f);
  }

  @Test
  public void should_fail_since_actual_is_negative_zero_and_not_primitive() {
    thrown.expectAssertionError("expected:<[]0.0f> but was:<[-]0.0f>");
    floats.assertIsZero(someInfo(), new Float(-0.0));
  }

  @Test
  public void should_succeed_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    floatsWithAbsValueComparisonStrategy.assertIsZero(someInfo(), 0.0f);
  }

  @Test
  public void should_fail_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError("expected:<[0].0f> but was:<[2].0f>");
    floatsWithAbsValueComparisonStrategy.assertIsZero(someInfo(), 2.0f);
  }

}
