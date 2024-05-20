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
package org.assertj.core.internal.longs;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLess.shouldBeLess;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Longs;
import org.assertj.core.internal.LongsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Longs#assertIsNegative(AssertionInfo, Long)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Longs_assertIsNegative_Test extends LongsBaseTest {

  @Test
  void should_succeed_since_actual_is_negative() {
    longs.assertIsNegative(INFO, -6L);
  }

  @Test
  void should_fail_since_actual_is_not_negative() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> longs.assertIsNegative(INFO, 6L));
    // THEN
    then(assertionError).hasMessage(shouldBeLess(6L, 0L).create());
  }

  @Test
  void should_fail_since_actual_is_zero() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> longs.assertIsNegative(INFO, 0L));
    // THEN
    then(assertionError).hasMessage(shouldBeLess(0L, 0L).create());
  }

  @Test
  void should_fail_since_actual_can_not_be_negative_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> longsWithAbsValueComparisonStrategy.assertIsNegative(INFO, 6L));
    // THEN
    then(assertionError).hasMessage(shouldBeLess(6L, 0L, absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> longsWithAbsValueComparisonStrategy.assertIsNegative(INFO, -1L));
    // THEN
    then(assertionError).hasMessage(shouldBeLess(-1L, 0L, absValueComparisonStrategy).create());
  }

}
