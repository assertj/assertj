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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo.shouldHaveSizeGreaterThanOrEqualTo;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.junit.jupiter.api.Test;

class IterableAssert_hasSizeGreaterThanOrEqualTo_Test {

  private static final List<String> ACTUAL = List.of("Luke", "Yoda", "Leia");

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSizeGreaterThanOrEqualTo(6));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_greater_than_or_equal_to_boundary() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(ACTUAL).hasSizeGreaterThanOrEqualTo(6));
    // THEN
    then(assertionError).hasMessage(shouldHaveSizeGreaterThanOrEqualTo(ACTUAL, ACTUAL.size(), 6).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_greater_than_boundary() {
    assertThat(ACTUAL).hasSizeGreaterThanOrEqualTo(1);
  }

  @Test
  void should_pass_if_size_of_actual_is_equal_to_boundary() {
    assertThat(ACTUAL).hasSizeGreaterThanOrEqualTo(ACTUAL.size());
  }

}
