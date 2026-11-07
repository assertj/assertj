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
package org.assertj.tests.core.internal.throwables;

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Throwables;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.Test;

/**
 * @author Phillip Webb
 */
class Throwables_assertHasMessageContainingAll_Test {

  private static final AssertionInfo INFO = TestData.someInfo();
  private final Throwable actual = new NullPointerException("Throwable message");

  @Test
  void should_pass_if_actual_has_message_containing_the_expected_string() {
    Throwables.instance().assertHasMessageContainingAll(INFO, actual, "able");
  }

  @Test
  void should_pass_if_actual_has_message_containing_all_the_expected_strings() {
    Throwables.instance().assertHasMessageContainingAll(INFO, actual, "able", "message");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> Throwables.instance().assertHasMessageContainingAll(INFO, null, "Boom"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_message_not_containing_all_the_expected_strings() {
    // GIVEN
    String content = "expected description part";
    // WHEN
    var error = expectAssertionError(() -> Throwables.instance().assertHasMessageContainingAll(INFO, actual, content));
    // THEN
    then(error).hasMessage(shouldContain(actual, content).create());
  }

  @Test
  void should_fail_if_actual_has_message_not_containing_some_of_the_expected_strings() {
    // GIVEN
    String[] content = { "catchable", "message" };
    // WHEN
    var error = expectAssertionError(() -> Throwables.instance().assertHasMessageContainingAll(INFO, actual, content));
    // THEN
    then(error).hasMessage(shouldContain(actual, content, singleton("catchable")).create());
  }

  @Test
  void should_throw_error_if_expected_strings_are_null() {
    assertThatNullPointerException().isThrownBy(() -> Throwables.instance().assertHasMessageContainingAll(INFO, actual,
                                                                                                          (String) null))
                                    .withMessage(charSequenceToLookForIsNull());
  }
}
