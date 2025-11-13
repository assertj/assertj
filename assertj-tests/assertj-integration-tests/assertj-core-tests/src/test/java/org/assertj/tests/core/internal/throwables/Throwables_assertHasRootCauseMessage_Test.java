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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCauseWithMessage;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class Throwables_assertHasRootCauseMessage_Test extends ThrowablesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, null, "message"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_root_cause_is_null() {
    // GIVEN
    Throwable throwable = new RuntimeException();
    // WHEN
    var assertionError = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, throwable, "message"));
    // THEN
    then(assertionError).hasMessage(shouldHaveRootCauseWithMessage(throwable, null, "message").create());
  }

  @Test
  void should_fail_if_root_cause_has_no_message() {
    // GIVEN
    Throwable root = new RuntimeException();
    Throwable throwable = new RuntimeException(root);
    // WHEN
    var assertionError = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, throwable, "message"));
    // THEN
    then(assertionError).hasMessage(shouldHaveRootCauseWithMessage(throwable, root, "message").create());
  }

  @Test
  void should_fail_if_root_cause_message_is_different() {
    // GIVEN
    Throwable root = new RuntimeException("fail");
    Throwable throwable = new RuntimeException(root);
    // WHEN
    var assertionError = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, throwable, "message"));
    // THEN
    then(assertionError).hasMessage(shouldHaveRootCauseWithMessage(throwable, root, "message").create());
  }

  @Test
  void should_pass_if_throwable_has_root_cause_with_message_equal_to_expected() {
    // GIVEN
    Throwable throwable = new RuntimeException(new RuntimeException("expected message"));
    // THEN
    throwables.assertHasRootCauseMessage(INFO, throwable, "expected message");
  }

  @Test
  void should_pass_if_actual_root_cause_has_no_message_and_expected_message_is_null() {
    // GIVEN
    Throwable throwable = new RuntimeException(new RuntimeException());
    // THEN
    throwables.assertHasRootCauseMessage(INFO, throwable, null);
  }
}
