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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.test.TestData.someHexInfo;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Bytes#assertIsPositive(AssertionInfo, Byte)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Bytes_assertIsPositive_Test extends BytesBaseTest {

  @Test
  public void should_succeed_since_actual_is_positive() {
    bytes.assertIsPositive(someInfo(), (byte) 6);
  }

  @Test
  public void should_fail_since_actual_is_not_positive() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsPositive(someInfo(), (byte) -1))
                                                   .withMessage(format("%nExpecting:%n <-1>%nto be greater than:%n <0> "));
  }

  @Test
  public void should_fail_since_actual_is_not_positive_in_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytes.assertIsPositive(someHexInfo(), (byte) 0xFA))
                                                   .withMessage(format("%nExpecting:%n <0xFA>%nto be greater than:%n <0x00> "));
  }

  @Test
  public void should_succeed_since_actual_is_positive_according_to_custom_comparison_strategy() {
    bytesWithAbsValueComparisonStrategy.assertIsPositive(someInfo(), (byte) -1);
  }

  @Test
  public void should_fail_since_actual_is_not_positive_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsPositive(someInfo(),
                                                                                                                          (byte) 0))
                                                   .withMessage(format("%nExpecting:%n <0>%nto be greater than:%n <0> when comparing values using AbsValueComparator"));
  }

  @Test
  public void should_fail_since_actual_is_not_positive_according_to_custom_comparison_strategy_in_hex_representation() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> bytesWithAbsValueComparisonStrategy.assertIsPositive(someHexInfo(),
                                                                                                                          (byte) 0x00))
                                                   .withMessage(format("%nExpecting:%n <0x00>%nto be greater than:%n <0x00> when comparing values using AbsValueComparator"));
  }
}
