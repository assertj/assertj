/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.integers;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

@RunWith(DataProviderRunner.class)
public class Integers_assertIsNotCloseTo_Test extends IntegersBaseTest {

  private static final Integer ZERO = 0;
  private static final Integer ONE = 1;

  @Test
  @DataProvider({
      "1, 3, 1",
      "-1, -3, 1",
      "1, -2, 2",
      "-1, 2, 2"
  })
  public void should_pass_if_difference_is_greater_than_offset(int actual, int other, int offset) {
    integers.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
    integers.assertIsNotCloseTo(someInfo(), actual, other, within(offset));
  }

  @Test
  @DataProvider({
      "1, 0, 1",
      "1, 2, 1",
      "-1, 0, 1",
      "1, -1, 2",
      "-1, 1, 2"
  })
  public void should_pass_if_difference_is_equal_to_strict_offset(int actual, int other, int offset) {
    integers.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
  }

  @Test
  @DataProvider({
      "1, 2, 10",
      "1, 2, 2",
      "1, 0, 2",
      "0, 1, 2"
  })
  public void should_fail_if_actual_is_too_close_to_the_other_value(int actual, int other, int offset) {
    AssertionInfo info = someInfo();
    try {
      integers.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(actual, other, byLessThan(offset), abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  @DataProvider({
      "1, 2, 10",
      "1, 0, 2",
      "0, 1, 2"
  })
  public void should_fail_if_actual_is_too_close_to_the_other_value_with_strict_offset(int actual, int other,
                                                                                       int offset) {
    AssertionInfo info = someInfo();
    try {
      integers.assertIsNotCloseTo(info, actual, other, byLessThan(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(actual, other, byLessThan(offset), abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  @DataProvider({
      "1, 1, 0",
      "1, 0, 1",
      "1, 2, 1"
  })
  public void should_fail_if_difference_is_equal_to_given_offset(int actual, int other, int offset) {
    AssertionInfo info = someInfo();
    try {
      integers.assertIsNotCloseTo(someInfo(), actual, other, within(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(actual, other, within(offset), abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    integers.assertIsNotCloseTo(someInfo(), null, ONE, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    integers.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    integers.assertIsNotCloseTo(someInfo(), ONE, ZERO, null);
  }

}
