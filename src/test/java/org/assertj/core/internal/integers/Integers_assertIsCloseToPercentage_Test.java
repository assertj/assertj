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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.integers;

import static org.assertj.core.api.Assertions.withinPercentage;
import static org.assertj.core.data.Percentage.withPercentage;
import static org.assertj.core.error.ShouldBeEqualWithinPercentage.shouldBeEqualWithinPercentage;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.Test;

public class Integers_assertIsCloseToPercentage_Test extends IntegersBaseTest {

  private static final Integer ZERO = 0;
  private static final Integer ONE = 1;
  private static final Integer TWO = 2;
  private static final Integer TEN = 10;

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    integers.assertIsCloseToPercentage(someInfo(), null, ONE, withPercentage(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    integers.assertIsCloseToPercentage(someInfo(), ONE, null, withPercentage(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_percentage_is_null() {
    integers.assertIsCloseToPercentage(someInfo(), ONE, ZERO, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_fail_if_percentage_is_negative() {
    integers.assertIsCloseToPercentage(someInfo(), ONE, ZERO, withPercentage(-1));
  }

  @Test(expected = IllegalArgumentException.class)
  public void should_fail_if_percentage_is_greater_than_one_hundred() {
    integers.assertIsCloseToPercentage(someInfo(), ONE, ZERO, withPercentage(101));
  }

  @Test
  public void should_pass_if_difference_is_less_than_given_percentage() {
    integers.assertIsCloseToPercentage(someInfo(), ONE, ONE, withPercentage(1));
    integers.assertIsCloseToPercentage(someInfo(), ONE, TWO, withPercentage(100));
  }

  @Test
  public void should_pass_if_difference_is_equal_to_given_percentage() {
    integers.assertIsCloseToPercentage(someInfo(), ONE, ONE, withPercentage(ZERO));
    integers.assertIsCloseToPercentage(someInfo(), ONE, TWO, withPercentage(50));
  }

  @Test
  public void should_fail_if_actual_is_not_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      integers.assertIsCloseToPercentage(someInfo(), ONE, TEN, withPercentage(TEN));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqualWithinPercentage(ONE, TEN, withinPercentage(TEN),
                                                                   (TEN - ONE)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
