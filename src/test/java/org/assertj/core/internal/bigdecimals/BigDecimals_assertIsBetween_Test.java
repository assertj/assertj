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
package org.assertj.core.internal.bigdecimals;

import static java.math.BigDecimal.*;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigDecimals;
import org.assertj.core.internal.BigDecimalsBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link BigDecimals#assertIsBetween(AssertionInfo, BigDecimal, BigDecimal, BigDecimal)}</code>.
 * 
 * @author William Delanoue
 */
public class BigDecimals_assertIsBetween_Test extends BigDecimalsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> numbers.assertIsBetween(someInfo(), null, ZERO, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_start_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsBetween(someInfo(), ONE, null, ONE));
  }

  @Test
  public void should_fail_if_end_is_null() {
    assertThatNullPointerException().isThrownBy(() -> numbers.assertIsBetween(someInfo(), ONE, ZERO, null));
  }

  @Test
  public void should_pass_if_actual_is_in_range() {
    numbers.assertIsBetween(someInfo(), ONE, ZERO, TEN);
    numbers.assertIsBetween(someInfo(), ONE, ONE, TEN);
    numbers.assertIsBetween(someInfo(), ONE, new BigDecimal("1.00"), TEN);
    numbers.assertIsBetween(someInfo(), ONE, ZERO, new BigDecimal("1.00"));
  }

  @Test
  public void should_pass_if_actual_is_equal_to_range_start() {
    numbers.assertIsBetween(someInfo(), ONE, ONE, TEN);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_range_end() {
    numbers.assertIsBetween(someInfo(), ONE, ZERO, ONE);
  }

  @Test
  public void should_fail_if_actual_is_not_in_range_start() {
    AssertionInfo info = someInfo();
    try {
        numbers.assertIsBetween(info, ONE, new BigDecimal(2), TEN);
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
      numbers.assertIsBetween(info, ONE, ZERO, ZERO);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(ONE, ZERO, ZERO, true, true));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
