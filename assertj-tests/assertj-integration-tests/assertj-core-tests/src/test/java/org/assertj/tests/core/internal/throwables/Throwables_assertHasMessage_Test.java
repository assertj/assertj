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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMessage.shouldHaveMessage;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class Throwables_assertHasMessage_Test extends ThrowablesBaseTest {

  @Test
  void should_pass_if_actual_has_expected_message() {
    throwables.assertHasMessage(INFO, actual, "Throwable message");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> throwables.assertHasMessage(INFO, actual, "message"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_not_expected_message() {
    // GIVEN
    String expectedMessage = "expected message";
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessage(INFO, actual, expectedMessage));
    // THEN
    verify(failures).failure(INFO, shouldHaveMessage(actual, expectedMessage), "Throwable message", expectedMessage);
  }
}
