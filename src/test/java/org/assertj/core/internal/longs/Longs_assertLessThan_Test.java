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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeLess.shouldBeLess;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Longs#assertLessThan(AssertionInfo, Long, long)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Longs_assertLessThan_Test extends LongsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longs.assertLessThan(someInfo(), null, 8L))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_less_than_other() {
    longs.assertLessThan(someInfo(), 6L, 8L);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other() {
    AssertionInfo info = someInfo();
    try {
      longs.assertLessThan(info, 6L, 6L);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(6L, 6L));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_greater_than_other() {
    AssertionInfo info = someInfo();
    try {
      longs.assertLessThan(info, 8L, 6L);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(8L, 6L));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> longsWithAbsValueComparisonStrategy.assertLessThan(someInfo(), null, 8L))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    longsWithAbsValueComparisonStrategy.assertLessThan(someInfo(), 6L, -8L);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      longsWithAbsValueComparisonStrategy.assertLessThan(info, 6L, -6L);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(6L, -6L, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      longsWithAbsValueComparisonStrategy.assertLessThan(info, -8L, 6L);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeLess(-8L, 6L, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
