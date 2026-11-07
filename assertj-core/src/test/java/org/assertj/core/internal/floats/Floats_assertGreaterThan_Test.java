/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.floats;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeGreater.shouldBeGreater;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Floats;
import org.assertj.core.internal.FloatsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Floats#assertGreaterThan(AssertionInfo, Float, float)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Floats_assertGreaterThan_Test extends FloatsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> floats.assertGreaterThan(someInfo(), null, 8f))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other() {
    floats.assertGreaterThan(someInfo(), 8f, 6f);
  }

  @Test
  void should_fail_if_actual_is_equal_to_other() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> floats.assertGreaterThan(info, 6f, 6f));
    // THEN
    then(error).hasMessage(shouldBeGreater(6f, 6f).create());
  }

  @Test
  void should_fail_if_actual_is_less_than_other() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> floats.assertGreaterThan(info, 6f, 8f));
    // THEN
    then(error).hasMessage(shouldBeGreater(6f, 8f).create());
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    floatsWithAbsValueComparisonStrategy.assertGreaterThan(someInfo(), -8f, 6f);
  }

  @Test
  void should_fail_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> floatsWithAbsValueComparisonStrategy.assertGreaterThan(info, -6f, 6f));
    // THEN
    then(error).hasMessage(shouldBeGreater(-6f, 6f, absValueComparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> floatsWithAbsValueComparisonStrategy.assertGreaterThan(info, -6f, 8f));
    // THEN
    then(error).hasMessage(shouldBeGreater(-6f, 8f, absValueComparisonStrategy).create());
  }

}
