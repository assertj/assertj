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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLess.shouldBeLess;
import static org.assertj.core.testkit.TestData.someHexInfo;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Bytes#assertIsNegative(AssertionInfo, Comparable)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Bytes_assertIsNegative_Test extends BytesBaseTest {

  private AssertionInfo hexInfo = someHexInfo();

  @Test
  void should_succeed_since_actual_is_negative() {
    bytes.assertIsNegative(someInfo(), (byte) -6);
  }

  @Test
  void should_fail_since_actual_is_not_negative() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytes.assertIsNegative(someInfo(), (byte) 6));
    // THEN
    then(assertionError).hasMessage(shouldBeLess((byte) 6, (byte) 0).create());
  }

  @Test
  void should_fail_since_actual_is_zero() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytes.assertIsNegative(someInfo(), (byte) 0));
    // THEN
    then(assertionError).hasMessage(shouldBeLess((byte) 0, (byte) 0).create());
  }

  @Test
  void should_fail_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError error = expectAssertionError(() -> bytesWithAbsValueComparisonStrategy.assertIsNegative(someInfo(),
                                                                                                           (byte) -1));
    // THEN
    then(error).hasMessage(shouldBeLess((byte) -1, (byte) 0, absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_since_actual_is_not_negative_with_hex_representation() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytes.assertIsNegative(hexInfo, (byte) 6));
    // THEN
    then(assertionError).hasMessage(shouldBeLess((byte) 0x06, (byte) 0x00).create(hexInfo.description(),
                                                                                  hexInfo.representation()));
  }

  @Test
  void should_fail_since_actual_is_not_negative_according_to_absolute_value_comparison_strategy_in_hex_representation() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytesWithAbsValueComparisonStrategy.assertIsNegative(someHexInfo(),
                                                                                                                    (byte) 0xFA));
    // THEN
    then(assertionError).hasMessage(shouldBeLess((byte) 0xFA, (byte) 0x00,
                                                 absValueComparisonStrategy).create(hexInfo.description(),
                                                                                    hexInfo.representation()));
  }
}
