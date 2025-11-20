/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.tests.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMessageMatchingRegex.shouldHaveMessageMatchingRegex;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

/**
 * @author Libor Ondrusek
 */
class Throwables_assertHasMessageMatching_Test extends ThrowablesBaseTest {

  public static final String REGEX = "Given id='\\d{2,4}' not exists";

  @Test
  void should_pass_if_throwable_message_matches_given_regex() {
    actual = new RuntimeException("Given id='259' not exists");
    throwables.assertHasMessageMatching(INFO, actual, REGEX);
  }

  @Test
  void should_pass_if_throwable_message_is_empty_and_regex_is_too() {
    actual = new RuntimeException("");
    throwables.assertHasMessageMatching(INFO, actual, "");
  }

  @Test
  void should_fail_if_throwable_message_does_not_match_given_regex() {
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageMatching(INFO, actual, REGEX));
    // THEN
    verify(failures).failure(INFO, shouldHaveMessageMatchingRegex(actual, REGEX));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> throwables.assertHasMessageMatching(INFO, null, REGEX));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_regex_is_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasMessageMatching(INFO, actual, (String) null))
                                    .withMessage("regex must not be null");
  }

  @Test
  void should_fail_if_given_pattern_is_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasMessageMatching(INFO, actual, (Pattern) null))
                                    .withMessage("regex must not be null");
  }

  @Test
  void should_fail_if_throwable_does_not_have_a_message() {
    // GIVEN
    actual = new RuntimeException();
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageMatching(INFO, actual, REGEX));
    // THEN
    verify(failures).failure(INFO, shouldHaveMessageMatchingRegex(actual, REGEX));
  }

}
