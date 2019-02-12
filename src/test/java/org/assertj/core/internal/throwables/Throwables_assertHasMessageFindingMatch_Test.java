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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldHaveMessageFindingMatchRegex.shouldHaveMessageFindingMatchRegex;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 *
 * @author David Haccoun
 */
class Throwables_assertHasMessageFindingMatch_Test extends ThrowablesBaseTest {

  private static final String REGEX = "waiting for Foo";

  @Test
  void should_pass_if_throwable_message_matches_given_regex() {
    // GIVEN
    Throwable actual = new RuntimeException("Blablabla...\n" +
                                            "waiting for Foo" +
                                            "...blablabla...\n");
    // THEN
    throwables.assertHasMessageFindingMatch(someInfo(), actual, REGEX);
  }

  @Test
  void should_pass_if_throwable_message_is_empty_and_regex_is_too() {
    // GIVEN
    Throwable actual = new RuntimeException("");
    // THEN
    throwables.assertHasMessageFindingMatch(someInfo(), actual, "");
  }

  @Test
  void should_fail_if_throwable_message_does_not_match_given_regex() {
    // GIVEN
    Throwable actual = new RuntimeException("Blablabla...\n" +
                                            "waiting for Bar" +
                                            "...blablabla...\n");
    AssertionInfo info = someInfo();
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> throwables.assertHasMessageFindingMatch(someInfo(), actual, REGEX));
    verify(failures).failure(info, shouldHaveMessageFindingMatchRegex(actual, REGEX));
  }

  @Test
  void should_fail_if_given_regex_is_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasMessageFindingMatch(someInfo(), actual, null))
                                    .withMessage("regex must not be null");
  }

  @Test
  void should_fail_if_throwable_is_null() {
    // GIVEN
    AssertionInfo info = someInfo();
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> throwables.assertHasMessageFindingMatch(someInfo(), null, REGEX));
    verify(failures).failure(info, shouldNotBeNull());
  }

  @Test
  void should_fail_if_throwable_does_not_have_a_message() {
    // GIVEN
    actual = new RuntimeException();
    AssertionInfo info = someInfo();
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> throwables.assertHasMessageFindingMatch(someInfo(), actual, REGEX));
    verify(failures).failure(info, shouldNotBeNull("exception message of actual"));
  }

}
