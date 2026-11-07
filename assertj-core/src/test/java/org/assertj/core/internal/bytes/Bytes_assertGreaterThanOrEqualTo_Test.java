/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.bytes;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Bytes#assertGreaterThanOrEqualTo(AssertionInfo, Byte, byte)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Bytes_assertGreaterThanOrEqualTo_Test extends BytesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertGreaterThanOrEqualTo(someInfo(), null, (byte) 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other() {
    bytes.assertGreaterThanOrEqualTo(someInfo(), (byte) 8, (byte) 6);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other() {
    bytes.assertGreaterThanOrEqualTo(someInfo(), (byte) 6, (byte) 6);
  }

  @Test
  void should_fail_if_actual_is_less_than_other() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> bytes.assertGreaterThanOrEqualTo(info, (byte) 6, (byte) 8));
    // THEN
    then(error).hasMessage(shouldBeGreaterOrEqual((byte) 6, (byte) 8).create());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(),
                                                                                                                                    null,
                                                                                                                                    (byte) 8))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    bytesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), (byte) -8, (byte) 6);
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    bytesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), (byte) -6, (byte) 6);
  }

  @Test
  void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> bytesWithAbsValueComparisonStrategy.assertGreaterThanOrEqualTo(info, (byte) 6,
                                                                                                          (byte) -8));
    // THEN
    then(error).hasMessage(shouldBeGreaterOrEqual((byte) 6, (byte) -8, absValueComparisonStrategy).create());
  }
}
