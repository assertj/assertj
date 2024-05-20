/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.bytes;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.core.testkit.TestData.someHexInfo;
import static org.assertj.core.testkit.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Bytes#assertIsOne(AssertionInfo, Comparable)}</code>.
 *
 * @author Drummond Dawson
 */
class Bytes_assertIsOne_Test extends BytesBaseTest {

  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    resetFailures();
  }

  @Test
  void should_succeed_since_actual_is_one() {
    bytes.assertIsOne(someInfo(), (byte) 1);
  }

  @Test
  void should_fail_since_actual_is_not_one() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsOne(someInfo(), (byte) 0))
                                                   .withMessage(shouldBeEqualMessage("0", "1"));
  }

  @Test
  void should_fail_since_actual_is_not_one_in_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsOne(someHexInfo(), (byte) 0x00))
                                                   .withMessage(shouldBeEqualMessage("0x00", "0x01"));
  }

  @Test
  void should_succeed_since_actual_is_one_whatever_custom_comparison_strategy_is() {
    bytesWithAbsValueComparisonStrategy.assertIsOne(someInfo(), (byte) 1);
  }

  @Test
  void should_succeed_since_actual_is_one_whatever_custom_comparison_strategy_is_in_hex_representation() {
    bytesWithAbsValueComparisonStrategy.assertIsOne(someHexInfo(), (byte) 0x01);
  }

  @Test
  void should_fail_since_actual_is_not_one_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsOne(someInfo(),
                                                                                                                     (byte) 0))
                                                   .withMessage(shouldBeEqualMessage("0", "1"));
  }

  @Test
  void should_fail_since_actual_is_not_one_whatever_custom_comparison_strategy_is_in_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsOne(someHexInfo(),
                                                                                                                     (byte) 0x0))
                                                   .withMessage(shouldBeEqualMessage("0x00", "0x01"));
  }

}
