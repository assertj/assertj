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
package org.assertj.tests.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSizeBetween.shouldHaveSizeBetween;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

class IterableAssert_hasSizeBetween_Test {

  private static final List<String> ACTUAL = List.of("Luke", "Yoda", "Leia");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSizeBetween(0, 6));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_illegal_argument_exception_if_lower_boundary_is_greater_than_higher_boundary() {
    // WHEN
    var iae = catchIllegalArgumentException(() -> assertThat(ACTUAL).hasSizeBetween(4, 2));
    // THEN
    then(iae).hasMessage("The higher boundary <2> must be greater than the lower boundary <4>.");
  }

  @Test
  void should_fail_if_size_of_actual_is_not_greater_than_or_equal_to_lower_boundary() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).hasSizeBetween(4, 6));
    // THEN
    then(assertionError).hasMessage(shouldHaveSizeBetween(ACTUAL, ACTUAL.size(), 4, 6).create());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_less_than_or_equal_to_higher_boundary() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).hasSizeBetween(1, 2));
    // THEN
    then(assertionError).hasMessage(shouldHaveSizeBetween(ACTUAL, ACTUAL.size(), 1, 2).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_between_boundaries() {
    assertThat(ACTUAL).hasSizeBetween(1, 6);
    assertThat(ACTUAL).hasSizeBetween(ACTUAL.size(), ACTUAL.size());
  }

}
