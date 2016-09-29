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
package org.assertj.core.internal.bytes;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.error.ShouldNotBeEqualWithinOffset.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

public class Bytes_assertIsNotCloseTo_Test extends BytesBaseTest {

  private static final Byte ZERO = 0;
  private static final Byte ONE = 1;
  private static final Byte TWO = 2;
  private static final Byte THREE = 3;
  private static final Byte TEN = 10;

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bytes.assertIsNotCloseTo(someInfo(), null, ONE, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if__expected_value_is_null() {
    bytes.assertIsNotCloseTo(someInfo(), ONE, null, within(ONE));
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_offset_is_null() {
    bytes.assertIsNotCloseTo(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_pass_if_difference_is_greater_than_given_offset() {
    bytes.assertIsNotCloseTo(someInfo(), ONE, THREE, within(ONE));
    bytes.assertIsNotCloseTo(someInfo(), ZERO, THREE, within(TWO));
  }

  @Test
  public void should_pass_if_difference_is_equal_to_given_offset() {
    bytes.assertIsNotCloseTo(someInfo(), ONE, ONE, within(ZERO));
    bytes.assertIsNotCloseTo(someInfo(), ONE, ZERO, within(ONE));
    bytes.assertIsNotCloseTo(someInfo(), ONE, TWO, within(ONE));
  }
  
  @Test
  public void should_fail_if_actual_is_too_close_enough_to_expected_value() {
    AssertionInfo info = someInfo();
    try {
      bytes.assertIsNotCloseTo(info, ONE, TWO, within(TEN));
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual(ONE, TWO, within(TEN), (byte) (TWO - ONE)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
