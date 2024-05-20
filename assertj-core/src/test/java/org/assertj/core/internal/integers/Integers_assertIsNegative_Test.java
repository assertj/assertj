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
package org.assertj.core.internal.integers;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLess.shouldBeLess;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Integers;
import org.assertj.core.internal.IntegersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Integers#assertIsNegative(AssertionInfo, Integer)}</code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Integers_assertIsNegative_Test extends IntegersBaseTest {

  @Test
  void should_succeed_since_actual_is_negative() {
    integers.assertIsNegative(someInfo(), -6);
  }

  @Test
  void should_fail_since_actual_is_not_negative() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> integers.assertIsNegative(someInfo(), 6));
    // THEN
    then(assertionError).hasMessage(shouldBeLess(6, 0).create());
  }

  @Test
  void should_fail_since_actual_is_zero() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> integers.assertIsNegative(someInfo(), 0));
    // THEN
    then(assertionError).hasMessage(shouldBeLess(0, 0).create());
  }

  @Test
  void should_fail_since_actual_is_not_negative_according_to_custom_comparison_strategy() {
    // WHEN
    AssertionError error = expectAssertionError(() -> integersWithAbsValueComparisonStrategy.assertIsNegative(someInfo(), -1));
    // THEN
    then(error).hasMessage(shouldBeLess(-1, 0, absValueComparisonStrategy).create());
  }

}
