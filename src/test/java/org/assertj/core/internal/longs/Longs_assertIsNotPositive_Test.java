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
package org.assertj.core.internal.longs;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLessOrEqual.shouldBeLessOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertIsNotPositive(AssertionInfo, Long))}</code>.
 * 
 * @author Nicolas François
 */
class Longs_assertIsNotPositive_Test extends LongsBaseTest {

  @Test
  void should_succeed_since_actual_is_not_positive() {
    longs.assertIsNotPositive(someInfo(), -6L);
  }

  @Test
  void should_succeed_since_actual_is_zero() {
    longs.assertIsNotPositive(someInfo(), 0L);
  }

  @Test
  void should_fail_since_actual_is_positive() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> longs.assertIsNotPositive(someInfo(), 6L));
    // THEN
    then(assertionError).hasMessage(shouldBeLessOrEqual(6L, 0L).create());
  }

  @Test
  void should_fail_since_actual_can_be_positive_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> longsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(),
                                                                                                                       -1L));
    // THEN
    then(assertionError).hasMessage(shouldBeLessOrEqual(-1L, 0L, absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_since_actual_is_positive_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> longsWithAbsValueComparisonStrategy.assertIsNotPositive(someInfo(),
                                                                                                                       1L));
    // THEN
    then(assertionError).hasMessage(shouldBeLessOrEqual(1L, 0L, absValueComparisonStrategy).create());
  }

}
