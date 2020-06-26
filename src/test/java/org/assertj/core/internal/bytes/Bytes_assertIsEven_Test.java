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
import static org.assertj.core.error.ShouldBeEven.shouldBeEven;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Bytes;
import org.assertj.core.internal.BytesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Bytes#assertIsEven(AssertionInfo, Number)}</code>.
 *
 * @author Cal027
 */
@DisplayName("Bytes assertIsEven")
class Bytes_assertIsEven_Test extends BytesBaseTest {

  @ParameterizedTest
  @ValueSource(bytes = { 0, 2, -2, 0x04, -0x04 })
  void should_pass_since_actual_is_even(byte actual) {
    // WHEN/THEN
    bytes.assertIsEven(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(bytes = { 1, 3, -1, 0x05, -0x05 })
  void should_fail_since_actual_is_not_even(byte actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytes.assertIsEven(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeEven(actual).create());
  }

  @ParameterizedTest
  @ValueSource(bytes = { 0, 2, -2, 0x04, -0x04 })
  void should_pass_since_actual_is_even_whatever_custom_comparison_strategy_is(byte actual) {
    // WHEN/THEN
    bytesWithAbsValueComparisonStrategy.assertIsEven(someInfo(), actual);
  }

  @ParameterizedTest
  @ValueSource(bytes = { 1, 3, -1, 0x05, -0x05 })
  void should_fail_since_actual_is_not_even_whatever_custom_comparison_strategy_is(byte actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> bytesWithAbsValueComparisonStrategy.assertIsEven(someInfo(),
                                                                                                                actual));
    // THEN
    then(assertionError).hasMessage(shouldBeEven(actual).create());
  }
}
