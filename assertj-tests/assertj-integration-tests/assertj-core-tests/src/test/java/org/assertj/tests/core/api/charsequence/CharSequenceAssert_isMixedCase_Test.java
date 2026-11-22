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
import static org.assertj.core.error.ShouldBeMixedCase.shouldBeMixedCase;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/// @author Andrey Kuzmin
class CharSequenceAssert_isMixedCase_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isMixedCase());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "I AM GROOT!",
      "P",
  })
  void should_fail_if_actual_is_uppercase(String actual) {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isMixedCase());
    // THEN
    then(assertionError).hasMessage(shouldBeMixedCase(actual).create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "please be quiet",
      "p",
  })
  void should_fail_if_actual_is_lowercase(String actual) {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isMixedCase());
    // THEN
    then(assertionError).hasMessage(shouldBeMixedCase(actual).create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      " ",
      "anExampleOfCamelCaseString",
      "anEx4mpl3OfC4m3lC4s3Str1ng!", // with numbers and special characters
      "@$#24^", // only numbers and special characters
  })
  void should_pass_if_actual_is_mixed_case(String actual) {
    // WHEN/THEN
    then(actual).isMixedCase();
  }

}
