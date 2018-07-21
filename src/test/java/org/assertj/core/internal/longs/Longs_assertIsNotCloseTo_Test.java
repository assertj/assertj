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
package org.assertj.core.internal.longs;

import static java.lang.Math.abs;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class Longs_assertIsNotCloseTo_Test extends LongsBaseTest {

  private static final Long ZERO = 0L;
  private static final Long ONE = 1L;

  @ParameterizedTest
  @CsvSource({
      "1, 3, 1",
      "-1, -3, 1",
      "1, -2, 2",
      "-1, 2, 2"
  })
  public void should_pass_if_difference_is_greater_than_offset(long actual, long other, long offset) {
    longs.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
    longs.assertIsNotCloseTo(someInfo(), actual, other, within(offset));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 0, 1",
      "1, 2, 1",
      "-1, 0, 1",
      "1, -1, 2",
      "-1, 1, 2"
  })
  public void should_pass_if_difference_is_equal_to_strict_offset(long actual, long other, long offset) {
    longs.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
  }

  @ParameterizedTest
  @CsvSource({
      "1, 2, 10",
      "1, 2, 2",
      "1, 0, 2",
      "0, 1, 2"
  })
  public void should_fail_if_actual_is_too_close_to_the_other_value(long actual, long other, long offset) {
    AssertionInfo info = someInfo();
    try {
      longs.assertIsNotCloseTo(someInfo(), actual, other, byLessThan(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(actual, other, byLessThan(offset), abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @ParameterizedTest
  @CsvSource({
      "1, 2, 10",
      "1, 0, 2",
      "0, 1, 2"
  })
  public void should_fail_if_actual_is_too_close_to_the_other_value_with_strict_offset(long actual, long other,
                                                                                       long offset) {
    AssertionInfo info = someInfo();
    try {
      longs.assertIsNotCloseTo(info, actual, other, byLessThan(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(actual, other, byLessThan(offset), abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @ParameterizedTest
  @CsvSource({
      "1, 1, 0",
      "1, 0, 1",
      "1, 2, 1"
  })
  public void should_fail_if_difference_is_equal_to_given_offset(long actual, long other, long offset) {
    AssertionInfo info = someInfo();
    try {
      longs.assertIsNotCloseTo(someInfo(), actual, other, within(offset));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(actual, other, within(offset), abs(actual - other)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertIsNotCloseTo(someInfo(), null, ONE, byLessThan(ONE)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_expected_value_is_null() {
    assertThatNullPointerException().isThrownBy(() -> longs.assertIsNotCloseTo(someInfo(), ONE, null, byLessThan(ONE)));
  }

  @Test
  public void should_fail_if_offset_is_null() {
    assertThatNullPointerException().isThrownBy(() -> longs.assertIsNotCloseTo(someInfo(), ONE, ZERO, null));
  }

}
