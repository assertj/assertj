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
import static org.assertj.core.error.ShouldHaveMessageFindingMatchRegex.shouldHaveMessageFindingMatchRegex;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/// @author David Haccoun
class ThrowableAssert_hasMessageFindingMatch_Test {

  private static final String REGEX = "waiting for Foo";

  @Test
  void should_pass_if_throwable_message_matches_given_regex() {
    // GIVEN
    Throwable actual = new RuntimeException("""
        Blablabla...
        waiting for Foo\
        ...blablabla...
        """);
    // WHEN/THEN
    then(actual).hasMessageFindingMatch(REGEX);
  }

  @Test
  void should_pass_if_throwable_message_is_empty_and_regex_is_too() {
    assertThat(new RuntimeException("")).hasMessageFindingMatch("");
  }

  @Test
  void should_fail_if_throwable_message_does_not_match_given_regex() {
    // GIVEN
    Throwable actual = new RuntimeException("""
        Blablabla...
        waiting for Bar\
        ...blablabla...
        """);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageFindingMatch(REGEX));
    // THEN
    then(assertionError).hasMessage(shouldHaveMessageFindingMatchRegex(actual, REGEX).create());
  }

  @Test
  void should_fail_if_given_regex_is_null() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasMessageFindingMatch(null));
    // THEN
    then(npe).hasMessage("regex must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageFindingMatch(REGEX));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_throwable_does_not_have_a_message() {
    // GIVEN
    Throwable actual = new RuntimeException();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageFindingMatch(REGEX));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull("exception message of actual").create());
  }

}
