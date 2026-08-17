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
package org.assertj.tests.core.api.integer_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class IntegerAssert_isEqualTo_long_Test {

  @Test
  void should_pass_when_expected_long_equals_actual_int() {
    // WHEN/THEN
    assertThat(123).isEqualTo(123L);
  }

  @Test
  void should_pass_if_expected_long_is_Integer_MAX_and_actual_is_too() {
    // GIVEN
    int actual = Integer.MAX_VALUE;
    long expected = Integer.MAX_VALUE;
    // WHEN/THEN
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_pass_if_expected_long_is_Integer_MIN_and_actual_is_too() {
    // GIVEN
    int actual = Integer.MIN_VALUE;
    long expected = Integer.MIN_VALUE;
    // WHEN/THEN
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_fail_if_expected_long_is_Integer_MAX_plus_one() {
    // GIVEN
    int actual = Integer.MAX_VALUE;
    long expected = Integer.MAX_VALUE + 1L;
    // WHEN
    expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
  }

  @Test
  void should_fail_if_expected_long_is_Integer_MIN_minus_one() {
    // GIVEN
    int actual = Integer.MIN_VALUE;
    long expected = Integer.MIN_VALUE - 1L;
    // WHEN
    expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
  }

  @Test
  void should_fail_if_expected_long_is_different_from_actual_int() {
    // WHEN
    expectAssertionError(() -> assertThat(123).isEqualTo(456L));
  }

}
