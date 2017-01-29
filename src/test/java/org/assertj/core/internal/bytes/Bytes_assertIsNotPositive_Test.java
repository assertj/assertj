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

import org.assertj.core.internal.BytesBaseTest;
import org.junit.Test;


/**
 * Tests for <code>{@link Bytes#assertIsNotPositive(AssertionInfo, Bytes))}</code>.
 * 
 * @author Nicolas François
 */
public class Bytes_assertIsNotPositive_Test extends BytesBaseTest {

  @Test
  public void should_succeed_since_actual_is_not_positive() {
    bytes.assertIsNotPositive(someInfo(), (byte) -6);
  }

  @Test
  public void should_succeed_since_actual_is_zero() {
    bytes.assertIsNotPositive(someInfo(), (byte) 0);
  }

  @Test
  public void should_fail_since_actual_is_positive() {
    thrown.expectAssertionError("%nExpecting:%n <6>%nto be less than or equal to:%n <0> ");
    bytes.assertIsNotPositive(someInfo(), (byte) 6);
  }

  @Test
  public void should_fail_since_actual_is_positive_in_hex_representation() {
    thrown.expectAssertionError("%nExpecting:%n <0x06>%nto be less than or equal to:%n <0x00> ");
    bytes.assertIsNotPositive(someHexInfo(), (byte) 0x06);
  }

  @Test
  public void should_fail_since_actual_can_be_positive_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError("%nExpecting:%n <-1>%nto be less than or equal to:%n <0> when comparing values using 'AbsValueComparator'");
    bytesWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), (byte) -1);
  }

  @Test
  public void should_fail_since_actual_can_be_positive_according_to_custom_comparison_strategy_in_hex_representation() {
    thrown.expectAssertionError("%nExpecting:%n <0xFF>%nto be less than or equal to:%n <0x00> when comparing values using 'AbsValueComparator'");
    bytesWithAbsValueComparisonStrategy.assertIsNotPositive(someHexInfo(), (byte) 0xFF);
  }

  @Test
  public void should_fail_since_actual_is_positive_according_to_custom_comparison_strategy() {
    thrown.expectAssertionError("%nExpecting:%n <1>%nto be less than or equal to:%n <0> when comparing values using 'AbsValueComparator'");
    bytesWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(), (byte) 1);
  }

  @Test
  public void should_fail_since_actual_is_positive_according_to_custom_comparison_strategy_in_hex_representation() {
    thrown.expectAssertionError("%nExpecting:%n <0x01>%nto be less than or equal to:%n <0x00> when comparing values using 'AbsValueComparator'");
    bytesWithAbsValueComparisonStrategy.assertIsNotPositive(someHexInfo(), (byte) 0x01);
  }

}
