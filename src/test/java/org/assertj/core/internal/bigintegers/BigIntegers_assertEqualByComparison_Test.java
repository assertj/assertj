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
package org.assertj.core.internal.bigintegers;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TEN;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BigIntegers;
import org.assertj.core.internal.BigIntegersBaseTest;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.Test;

/**
 * Tests for <code>{@link BigIntegers#assertEqualByComparison(AssertionInfo, BigInteger, BigInteger)}</code>.
 */
public class BigIntegers_assertEqualByComparison_Test extends BigIntegersBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    numbers.assertEqualByComparison(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_big_integers_are_equal_by_comparison() {
    numbers.assertEqualByComparison(someInfo(), new BigInteger("5"), new BigInteger("5"));
  }

  @Test
  public void should_fail_if_big_integers_are_not_equal_by_comparison() {
    AssertionInfo info = someInfo();
    try {
      numbers.assertEqualByComparison(info, TEN, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(TEN, ONE, info.representation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError(actualIsNull());
    numbersWithAbsValueComparisonStrategy.assertEqualByComparison(someInfo(), null, ONE);
  }

  @Test
  public void should_pass_if_big_integers_are_equal_by_comparison_whatever_custom_comparison_strategy_is() {
    numbersWithAbsValueComparisonStrategy.assertEqualByComparison(someInfo(), new BigInteger("5"), new BigInteger("5"));
  }

  @Test
  public void should_fail_if_big_integers_are_not_equal_by_comparison_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      numbersWithAbsValueComparisonStrategy.assertEqualByComparison(info, TEN, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeEqual(TEN, ONE, absValueComparisonStrategy,
                                                   new StandardRepresentation()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
