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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Integers#assertLessThanOrEqualTo(AssertionInfo, Integer, int)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Integers_assertLessThanOrEqualTo_Test extends IntegersBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integers.assertLessThanOrEqualTo(someInfo(), null, 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_less_than_other() {
    integers.assertLessThanOrEqualTo(someInfo(), 6, 8);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other() {
    integers.assertLessThanOrEqualTo(someInfo(), 6, 6);
  }

  @Test
  public void should_fail_if_actual_is_greater_than_other() {
    AssertionInfo info = someInfo();
    try {
      integers.assertLessThanOrEqualTo(info, 8, 6);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLessOrEqual(8, 6));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> integersWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), null, 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    integersWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), 6, -8);
  }

  @Test
  public void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    integersWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(someInfo(), 6, -6);
  }

  @Test
  public void should_fail_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      integersWithAbsValueComparisonStrategy.assertLessThanOrEqualTo(info, -8, 6);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLessOrEqual(-8, 6, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
