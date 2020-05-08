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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.bytes;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someHexInfo;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Bytes#assertIsOdd(AssertionInfo, Byte)}</code>.
 *
 * @author Cal027
 */
public class Bytes_assertIsOdd_Test extends BytesBaseTest {
  @BeforeEach
  @Override
  public void setUp() {
    super.setUp();
    resetFailures();
  }

  @Test
  public void should_succeed_since_actual_is_odd() {
    bytes.assertIsOdd(someInfo(), (byte) 3);
    bytes.assertIsOdd(someInfo(), (byte) -3);
  }

  @Test
  public void should_fail_since_actual_is_not_odd() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsOdd(someInfo(), (byte) 2))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsOdd(someInfo(), (byte) -2))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
  }

  @Test
  public void should_fail_since_actual_is_not_odd_with_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsOdd(someHexInfo(), (byte) 0x08))
                                                   .withMessage(format("%nExpecting:%n <0x00>%nnot to be equal to:%n <0x00>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsOdd(someHexInfo(), (byte) -0x08))
                                                   .withMessage(format("%nExpecting:%n <0x00>%nnot to be equal to:%n <0x00>%n"));
  }

  @Test
  public void should_succeed_since_actual_is_odd_whatever_custom_comparison_strategy_is() {
    bytesWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), (byte) 5);
    bytesWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), (byte) -5);
  }

  @Test
  public void should_succeed_since_actual_is_odd_whatever_custom_comparison_strategy_is_in_hex_representation() {
    bytesWithAbsValueComparisonStrategy.assertIsOdd(someHexInfo(), (byte) 0x05);
    bytesWithAbsValueComparisonStrategy.assertIsOdd(someHexInfo(), (byte) -0x05);
  }

  @Test
  public void should_fail_since_actual_is_not_odd_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
                                                                                                                     (byte) 6))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
                                                                                                                     (byte) 6))
                                                   .withMessage(format("%nExpecting:%n <0>%nnot to be equal to:%n <0>%n"));
  }

  @Test
  public void should_fail_since_actual_is_not_odd_whatever_custom_comparison_strategy_is_in_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsOdd(someHexInfo(),
                                                                                                                     (byte) 0x04))
                                                   .withMessage(format("%nExpecting:%n <0x00>%nnot to be equal to:%n <0x00>%n"));
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsOdd(someHexInfo(),
                                                                                                                     (byte) -0x04))
                                                   .withMessage(format("%nExpecting:%n <0x00>%nnot to be equal to:%n <0x00>%n"));

  }
}
