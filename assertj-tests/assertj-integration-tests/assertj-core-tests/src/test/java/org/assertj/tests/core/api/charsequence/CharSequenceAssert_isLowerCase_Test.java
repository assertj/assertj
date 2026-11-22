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
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/// @author Marcel Overdijk
class CharSequenceAssert_isLowerCase_Test {

  @Test
  void should_pass_if_actual_is_lowercase() {
    assertThat("lego").isLowerCase();
  }

  @Test
  void should_pass_if_actual_is_empty() {
    assertThat("").isLowerCase();
  }

  @Test
  void should_fail_if_actual_is_not_fully_lowercase() {
    // GIVEN
    String actual = "Lego";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isLowerCase());
    // THEN
    then(assertionError).hasMessage(shouldBeLowerCase(actual).create());
  }

  @Test
  void should_fail_if_actual_is_uppercase() {
    // GIVEN
    String actual = "LEGO";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isLowerCase());
    // THEN
    then(assertionError).hasMessage(shouldBeLowerCase(actual).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isLowerCase());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

}
