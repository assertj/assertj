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
package org.assertj.core.internal.shorts;

import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Shorts;
import org.assertj.core.internal.ShortsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Shorts#assertNotEqual(AssertionInfo, Short, short)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Shorts_assertNotEqual_Test extends ShortsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    shorts.assertNotEqual(someInfo(), null, (short) 8);
  }

  @Test
  public void should_pass_if_shorts_are_not_equal() {
    shorts.assertNotEqual(someInfo(), (short) 8, (short) 6);
  }

  @Test
  public void should_fail_if_shorts_are_equal() {
    AssertionInfo info = someInfo();
    try {
      shorts.assertNotEqual(info, (short) 6, (short) 6);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual((short) 6, (short) 6));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    shortsWithAbsValueComparisonStrategy.assertNotEqual(someInfo(), null, (short) 8);
  }

  @Test
  public void should_pass_if_shorts_are_not_equal_according_to_custom_comparison_strategy() {
    shortsWithAbsValueComparisonStrategy.assertNotEqual(someInfo(), (short) 8, (short) 6);
  }

  @Test
  public void should_fail_if_shorts_are_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      shortsWithAbsValueComparisonStrategy.assertNotEqual(info, (short) 6, (short) -6);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual((short) 6, (short) -6, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
