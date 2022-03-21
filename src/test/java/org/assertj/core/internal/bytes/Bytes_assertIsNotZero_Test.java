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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.bytes;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someHexInfo;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Bytes#assertIsNotZero(AssertionInfo, Byte)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Bytes_assertIsNotZero_Test extends BytesBaseTest {

  @Test
  void should_succeed_since_actual_is_not_zero() {
    bytes.assertIsNotZero(someInfo(), (byte) 2);
  }

  @Test
  void should_fail_since_actual_is_zero() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsNotZero(someInfo(), (byte) 0))
                                                   .withMessage(format("%nExpecting actual:%n  0%nnot to be equal to:%n  0%n"));
  }

  @Test
  void should_fail_since_actual_is_zero_in_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsNotZero(someHexInfo(), (byte) 0x00))
                                                   .withMessage(format("%nExpecting actual:%n  0x00%nnot to be equal to:%n  0x00%n"));
  }

  @Test
  void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    bytesWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(), (byte) 1);
  }

  @Test
  void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is_in_hex_representation() {
    bytesWithAbsValueComparisonStrategy.assertIsNotZero(someHexInfo(), (byte) 0x01);
  }

  @Test
  void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsNotZero(someInfo(),
                                                                                                                         (byte) 0))
                                                   .withMessage(format("%nExpecting actual:%n  0%nnot to be equal to:%n  0%n"));
  }

  @Test
  void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is_in_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsNotZero(someHexInfo(),
                                                                                                                         (byte) 0x00))
                                                   .withMessage(format("%nExpecting actual:%n  0x00%nnot to be equal to:%n  0x00%n"));
  }

}
