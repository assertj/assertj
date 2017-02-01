/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.bytes;

import static org.assertj.core.test.TestData.someHexInfo;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Bytes#assertIsNegative(AssertionInfo, Comparable)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Bytes_assertIsZero_Test extends BytesBaseTest {

  @Override
  public void setUp() {
    super.setUp();
    resetFailures();
  }

  @Test
  public void should_succeed_since_actual_is_zero() {
    bytes.assertIsZero(someInfo(), (byte) 0x00);
  }

  @Test
  public void should_fail_since_actual_is_not_zero() {
    thrown.expectAssertionError("expected:<[0]> but was:<[2]>");
    bytes.assertIsZero(someInfo(), (byte) 2);
  }

  @Test
  public void should_fail_since_actual_is_not_zero_in_hex_representation() {
    thrown.expectAssertionError("expected:<0x0[0]> but was:<0x0[2]>");
    bytes.assertIsZero(someHexInfo(), (byte) 0x02);
  }

  @Test
  public void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is() {
    bytesWithAbsValueComparisonStrategy.assertIsZero(someInfo(), (byte) 0);
  }

  @Test
  public void should_succeed_since_actual_is_zero_whatever_custom_comparison_strategy_is_in_hex_representation() {
    bytesWithAbsValueComparisonStrategy.assertIsZero(someHexInfo(), (byte) 0x00);
  }

  @Test
  public void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is() {
    thrown.expectAssertionError("expected:<[0]> but was:<[1]>");
    bytesWithAbsValueComparisonStrategy.assertIsZero(someInfo(), (byte) 1);
  }

  @Test
  public void should_fail_since_actual_is_not_zero_whatever_custom_comparison_strategy_is_in_hex_representation() {
    thrown.expectAssertionError("expected:<0x0[0]> but was:<0x0[1]>");
    bytesWithAbsValueComparisonStrategy.assertIsZero(someHexInfo(), (byte) 0x01);
  }

}
