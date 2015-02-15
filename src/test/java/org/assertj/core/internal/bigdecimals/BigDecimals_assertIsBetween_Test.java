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
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.*;

import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link BigDecimals#assertIsBetween(AssertionInfo, BigDecimal, BigDecimal, BigDecimal)}</code>.
 * 
 * @author William Delanoue
 */
public class BigDecimals_assertIsBetween_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    bigDecimals.assertIsBetween(someInfo(), null, ZERO, ONE);
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_start_is_null() {
    bigDecimals.assertIsBetween(someInfo(), ONE, null, ONE);
  }

  @Test(expected = NullPointerException.class)
  public void should_fail_if_end_is_null() {
    bigDecimals.assertIsBetween(someInfo(), ONE, ZERO, null);
  }

  @Test
  public void should_pass_if_actual_is_in_range() {
    bigDecimals.assertIsBetween(someInfo(), ONE, ZERO, TEN);
    bigDecimals.assertIsBetween(someInfo(), ONE, ONE, TEN);
    bigDecimals.assertIsBetween(someInfo(), ONE, new BigDecimal("1.00"), TEN);
    bigDecimals.assertIsBetween(someInfo(), ONE, ZERO, new BigDecimal("1.00"));
  }

  @Test
  public void should_pass_if_actual_is_equal_to_range_start() {
    bigDecimals.assertIsBetween(someInfo(), ONE, ONE, TEN);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_range_end() {
    bigDecimals.assertIsBetween(someInfo(), ONE, ZERO, ONE);
  }

  @Test
  public void should_fail_if_actual_is_not_in_range_start() {
    AssertionInfo info = someInfo();
    try {
        bigDecimals.assertIsBetween(info, ONE, new BigDecimal(2), TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(ONE, new BigDecimal(2), TEN, true, true));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_in_range_end() {
    AssertionInfo info = someInfo();
    try {
      bigDecimals.assertIsBetween(info, ONE, ZERO, ZERO);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(ONE, ZERO, ZERO, true, true));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
