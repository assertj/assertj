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
package org.assertj.core.internal.comparables;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ComparablesBaseTest;
import org.junit.jupiter.api.Test;

public class Comparables_isBetween_Test extends ComparablesBaseTest {

  @Test
  public void succeeds_if_actual_is_between_start_and_end() {
    assertThat(BigInteger.ONE).isBetween(BigInteger.ZERO, BigInteger.TEN);
  }

  @Test
  public void succeeds_if_actual_is_equal_to_start() {
    comparables.assertIsBetween(someInfo(), 8, 8, 10, true, true);
  }

  @Test
  public void succeeds_if_actual_is_equal_to_end() {
    comparables.assertIsBetween(someInfo(), 10, 8, 10, true, true);
  }

  @Test
  public void fails_if_actual_is_less_than_start() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> comparables.assertIsBetween(someInfo(), 6, 8, 10, true, true))
                                                   .withMessage(format("%nExpecting:%n <6>%nto be between:%n [8, 10]"));
  }

  @Test
  public void fails_if_actual_is_greater_than_end() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> comparables.assertIsBetween(someInfo(), 12, 8, 10, true, true))
                                                   .withMessage(format("%nExpecting:%n <12>%nto be between:%n [8, 10]"));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> comparables.assertIsBetween(someInfo(), null, 8, 10, true, true))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_start_is_null() {
    assertThatNullPointerException().isThrownBy(() -> comparables.assertIsBetween(someInfo(), 8, null, 10, true, true))
                                    .withMessage("The start range to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_end_is_null() {
    assertThatNullPointerException().isThrownBy(() -> comparables.assertIsBetween(someInfo(), 8, 10, null, true, true))
                                    .withMessage("The end range to compare actual with should not be null");
  }

  @Test
  public void should_fail_if_end_is_less_than_start() {
    assertThatIllegalArgumentException().isThrownBy(() -> comparables.assertIsBetween(someInfo(), 8, 10, 8, true, true))
                                        .withMessage("The end value <8> must not be less than the start value <10>!");
  }

  @Test
  public void succeeds_if_end_is_equal_to_start() {
    comparables.assertIsBetween(someInfo(), 8, 8, 8, true, true);
    comparables.assertIsBetween(someInfo(), BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, true, true);
    comparables.assertIsBetween(someInfo(), BigDecimal.TEN, new BigDecimal("10.000"), new BigDecimal("10.000"), true, true);
    comparables.assertIsBetween(someInfo(), BigDecimal.TEN, new BigDecimal("10.000"), new BigDecimal("10.0"), true, true);
    comparables.assertIsBetween(someInfo(), BigDecimal.TEN, new BigDecimal("10.00"), new BigDecimal("10.0000"), true, true);
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void succeeds_if_actual_is_between_start_and_end_according_to_custom_comparison_strategy() {
    comparablesWithCustomComparisonStrategy.assertIsBetween(someInfo(), -7, 6, 8, true, true);
  }

  @Test
  public void fails_if_actual_is_is_greater_than_end_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      comparablesWithCustomComparisonStrategy.assertIsBetween(someInfo(), -12, 8, 10, true, true);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(-12, 8, 10, true, true, customComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void fails_if_actual_is_is_less_than_start_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      comparablesWithCustomComparisonStrategy.assertIsBetween(someInfo(), 6, -8, 10, true, true);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(6, -8, 10, true, true, customComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void fails_if_end_is_less_than_start_according_to_custom_comparison_strategy() {
    assertThatIllegalArgumentException().isThrownBy(() -> comparablesWithCustomComparisonStrategy.assertIsBetween(someInfo(),
                                                                                                                  8,
                                                                                                                  -10,
                                                                                                                  8,
                                                                                                                  true,
                                                                                                                  true))
                                        .withMessage("The end value <8> must not be less than the start value <-10> (using AbsValueComparator)!");
  }

}
