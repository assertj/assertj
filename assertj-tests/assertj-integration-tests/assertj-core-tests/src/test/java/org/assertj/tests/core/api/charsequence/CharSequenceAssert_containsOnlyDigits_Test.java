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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainOnlyDigits.shouldContainOnlyDigits;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class CharSequenceAssert_containsOnlyDigits_Test {

  @Test
  void should_pass_if_actual_contains_only_digits() {
    assertThat("10").containsOnlyDigits();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyDigits());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_contains_any_non_digit_character() {
    // GIVEN
    String actual = "10$";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyDigits());
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyDigits(actual, '$', 2).create());
  }

  // See https://github.com/assertj/assertj/pull/342 - discussion on failing the assertion for empty CharSequence
  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    String actual = "";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsOnlyDigits());
    // THEN
    then(assertionError).hasMessage(shouldContainOnlyDigits(actual).create());
  }

}
