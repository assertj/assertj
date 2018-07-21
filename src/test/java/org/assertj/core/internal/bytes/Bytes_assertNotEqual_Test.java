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
package org.assertj.core.internal.bytes;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldNotBeEqual.shouldNotBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;


import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.Test;


/**
 * Tests for <code>{@link Bytes#assertNotEqual(AssertionInfo, Byte, byte)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Bytes_assertNotEqual_Test extends BytesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertNotEqual(someInfo(), null, (byte) 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_bytes_are_not_equal() {
    bytes.assertNotEqual(someInfo(), (byte) 8, (byte) 6);
  }

  @Test
  public void should_fail_if_bytes_are_equal() {
    AssertionInfo info = someInfo();
    try {
      bytes.assertNotEqual(info, (byte) 6, (byte) 6);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual((byte) 6, (byte) 6));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertNotEqual(someInfo(), null, (byte) 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_bytes_are_not_equal_according_to_custom_comparison_strategy() {
    bytesWithAbsValueComparisonStrategy.assertNotEqual(someInfo(), (byte) 8, (byte) 6);
  }

  @Test
  public void should_fail_if_bytes_are_equal_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      bytesWithAbsValueComparisonStrategy.assertNotEqual(info, (byte) 6, (byte) -6);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotBeEqual((byte) 6, (byte) -6, absValueComparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
