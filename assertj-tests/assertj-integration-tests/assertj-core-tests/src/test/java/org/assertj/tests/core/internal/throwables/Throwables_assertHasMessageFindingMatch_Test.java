/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMessageFindingMatchRegex.shouldHaveMessageFindingMatchRegex;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author David Haccoun
 */
class Throwables_assertHasMessageFindingMatch_Test extends ThrowablesBaseTest {

  private static final String REGEX = "waiting for Foo";

  @Test
  void should_pass_if_throwable_message_matches_given_regex() {
    // GIVEN
    Throwable actual = new RuntimeException("""
        Blablabla...
        waiting for Foo\
        ...blablabla...
        """);
    // THEN
    throwables.assertHasMessageFindingMatch(INFO, actual, REGEX);
  }

  @Test
  void should_pass_if_throwable_message_is_empty_and_regex_is_too() {
    // GIVEN
    Throwable actual = new RuntimeException("");
    // THEN
    throwables.assertHasMessageFindingMatch(INFO, actual, "");
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
    AssertionError error = expectAssertionError(() -> throwables.assertHasMessageFindingMatch(INFO, actual, REGEX));
    // THEN
    then(error).hasMessage(shouldHaveMessageFindingMatchRegex(actual, REGEX).create());
  }

  @Test
  void should_fail_if_given_regex_is_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasMessageFindingMatch(INFO, actual, null))
                                    .withMessage("regex must not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> throwables.assertHasMessageFindingMatch(INFO, null, REGEX));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_throwable_does_not_have_a_message() {
    // WHEN
    AssertionError error = expectAssertionError(() -> throwables.assertHasMessageFindingMatch(INFO, new RuntimeException(),
                                                                                              REGEX));
    // THEN
    then(error).hasMessage(shouldNotBeNull("exception message of actual").create());
  }
}
