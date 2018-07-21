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
package org.assertj.core.internal.floats;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeBetween.shouldBeBetween;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Floats;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Floats#assertIsStrictlyBetween(AssertionInfo, Float, Float, Float)}</code>.
 * 
 * @author William Delanoue
 */
public class Floats_assertIsStrictlyBetween_Test extends FloatsBaseTest {

  private static final Float ZERO = 0f;
  private static final Float ONE = 1f;
  private static final Float TWO = 2f;
  private static final Float TEN = 10f;
  
  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floats.assertIsStrictlyBetween(someInfo(), null, ZERO, ONE))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_start_is_null() {
    assertThatNullPointerException().isThrownBy(() -> floats.assertIsStrictlyBetween(someInfo(), ONE, null, ONE));
  }

  @Test
  public void should_fail_if_end_is_null() {
    assertThatNullPointerException().isThrownBy(() -> floats.assertIsStrictlyBetween(someInfo(), ONE, ZERO, null));
  }

  @Test
  public void should_pass_if_actual_is_in_range() {
    floats.assertIsStrictlyBetween(someInfo(), ONE, ZERO, TEN);
  }

  @Test
  public void should_fail_if_actual_is_equal_to_range_start() {
    AssertionInfo info = someInfo();
    try {
        floats.assertIsStrictlyBetween(info, ONE, ONE, TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(ONE, ONE, TEN, false, false));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_equal_to_range_end() {
    AssertionInfo info = someInfo();
    try {
      floats.assertIsStrictlyBetween(info, ONE, ZERO, ONE);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(ONE, ZERO, ONE, false, false));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_in_range_start() {
    AssertionInfo info = someInfo();
    try {
        floats.assertIsStrictlyBetween(info, ONE, TWO, TEN);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldBeBetween(ONE, TWO, TEN, false, false));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_not_in_range_end() {
    assertThatIllegalArgumentException().isThrownBy(() -> floats.assertIsStrictlyBetween(someInfo(), ONE, ZERO, ZERO))
                                        .withMessage("The end value <0.0> must not be less than or equal to the start value <0.0>!");
  }
}
