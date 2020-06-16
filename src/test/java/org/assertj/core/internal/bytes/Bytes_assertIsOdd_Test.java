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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeOdd.shouldBeOdd;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Bytes#assertIsOdd(AssertionInfo, Number)}</code>.
 *
 * @author Cal027
 */
@DisplayName("Bytes assertIsOdd")
class Bytes_assertIsOdd_Test extends BytesBaseTest {

  @ParameterizedTest
  @ValueSource(bytes = { 1, 3, -1, 0x05, -0x05 })
  void should_pass_since_actual_is_odd(byte actual) {
    // WHEN/THEN
    bytes.assertIsOdd(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(bytes = { 0, 2, -2, 0x04, -0x04 })
  void should_fail_since_actual_is_not_odd(byte actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytes.assertIsOdd(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeOdd(actual).create());
  }

  @ParameterizedTest
  @ValueSource(bytes = { 1, 3, -1, 0x05, -0x05 })
  void should_pass_since_actual_is_odd_whatever_custom_comparison_strategy_is(byte actual) {
    // WHEN/THEN
    bytesWithAbsValueComparisonStrategy.assertIsOdd(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(bytes = { 0, 2, -2, 0x04, -0x04 })
  void should_fail_since_actual_is_not_odd_whatever_custom_comparison_strategy_is(byte actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytesWithAbsValueComparisonStrategy.assertIsOdd(someInfo(),
            actual));
    // THEN
    then(assertionError).hasMessage(shouldBeOdd(actual).create());
  }
}
