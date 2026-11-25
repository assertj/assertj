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
package org.assertj.tests.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMessageMatchingRegex.shouldHaveMessageMatchingRegex;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/// @author Andrei Solntsev
/// @author Libor Ondrusek
class ThrowableAssert_hasMessageMatchingPattern_Test {

  private static final Pattern REGEX = Pattern.compile("Given id='\\d{2,4}' not exists");

  @Test
  void should_pass_if_throwable_message_matches_given_pattern() {
    assertThat(new RuntimeException("Given id='259' not exists")).hasMessageMatching(REGEX);
  }

  @Test
  void should_pass_if_throwable_message_is_empty_and_pattern_is_too() {
    assertThat(new RuntimeException("")).hasMessageMatching(Pattern.compile(""));
  }

  @Test
  void should_fail_if_throwable_message_does_not_match_given_pattern() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageMatching(REGEX));
    // THEN
    then(assertionError).hasMessage(shouldHaveMessageMatchingRegex(actual, REGEX.pattern()).create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageMatching(REGEX));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_given_pattern_is_null() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    Pattern regex = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasMessageMatching(regex));
    // THEN
    then(npe).hasMessage("regex must not be null");
  }

  @Test
  void should_fail_if_throwable_does_not_have_a_message() {
    // GIVEN
    Throwable actual = new RuntimeException();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageMatching(REGEX));
    // THEN
    then(assertionError).hasMessage(shouldHaveMessageMatchingRegex(actual, REGEX.pattern()).create());
  }

}
