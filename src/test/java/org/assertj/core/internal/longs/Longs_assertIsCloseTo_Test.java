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

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldBeEqualWithinOffset.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

@RunWith(DataProviderRunner.class)
public class Longs_assertIsCloseTo_Test extends LongsBaseTest {

  private static final Long ZERO = 0L;
  private static final Long ONE = 1L;

  @Test
  @DataProvider({
      "1, 1, 1",
      "1, 2, 10",
      "-2, 0, 3",
      "-1, 1, 3",
      "0, 2, 5"
  })
  public void should_pass_if_difference_is_less_than_given_offset(long actual, long expected, long offset) {
    longs.assertIsCloseTo(someInfo(), actual, expected, within(offset));
  }

  @Test
  @DataProvider({
      "1, 3, 2",
      "3, 1, 2",
      "-2, 0, 2",
      "-1, 1, 2",
      "0, 2, 2"
  })
  public void should_pass_if_difference_is_equal_to_given_offset(long actual, long expected, long offset) {
    longs.assertIsCloseTo(someInfo(), actual, expected, within(offset));
  }

  @Test
  @DataProvider({
      "1, 3, 1",
      "3, 1, 1",
      "-2, 0, 1",
      "-1, 1, 1",
      "0, 2, 1"
  })
  public void should_fail_if_actual_is_not_close_enough_to_expected(long actual, long expected, long offset) {
    AssertionInfo info = someInfo();
    try {
      longs.assertIsCloseTo(info, actual, expected, within(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, expected, within(offset), abs(actual - expected)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  @DataProvider({
      "1, 2, 1",
      "3, 2, 1",
      "-2, -1, 1",
      "-1, 1, 2",
      "0, 2, 2"
  })
  public void should_fail_if_difference_is_equal_to_the_given_strict_offset(long actual, long expected, long offset) {
    AssertionInfo info = someInfo();
    try {
      longs.assertIsCloseTo(info, actual, expected, byLessThan(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(actual, expected, byLessThan(offset), abs(actual - expected)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    longs.assertIsCloseTo(someInfo(), null, ONE, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    longs.assertIsCloseTo(someInfo(), ONE, null, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    longs.assertIsCloseTo(someInfo(), ONE, ZERO, null);
  }

}
