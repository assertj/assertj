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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal.longs;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

@RunWith(DataProviderRunner.class)
public class Longs_assertIsNotCloseTo_Test extends LongsBaseTest {

  private static final Long ZERO = 0l;
  private static final Long ONE = 1l;
  private static final Long TWO = 2l;
  private static final Long THREE = 3l;
  private static final Long TEN = 10l;

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    longs.assertIsNotCloseTo(someInfo(), null, ONE, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_expected_value_is_null() {
    longs.assertIsNotCloseTo(someInfo(), ONE, null, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    longs.assertIsNotCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_pass_if_difference_is_greater_than_given_offset() {
    longs.assertIsNotCloseTo(someInfo(), ONE, THREE, within(ONE));
    longs.assertIsNotCloseTo(someInfo(), ONE, TEN, within(TWO));
  }

  // @format:off
  @Test
  @DataProvider({
    "1, 1, 0",
    "1, 0, 1",
    "-1, 0, 1",
    "-1, -1, 0",
    "-1, 1, 2",
    "0, 9223372036854775807, 9223372036854775807",
    "9223372036854775807, 9223372036854775807, 0",
    "-9223372036854775808, -9223372036854775808, 0"
  })
  // @format:on
  public void should_pass_if_difference_is_equal_to_given_offset(Long actual, Long expected, Long offset) {
    longs.assertIsNotCloseTo(someInfo(), actual, expected, within(offset));
  }
  
  @Test
  public void should_fail_if_actual_is_too_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      longs.assertIsNotCloseTo(info, ONE, TWO, within(TEN));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE, TWO, within(TEN), TWO - ONE));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
