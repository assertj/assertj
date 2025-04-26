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
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

class Throwables_assertHasMessageEnding_Test extends ThrowablesBaseTest {

  @Test
  void should_pass_if_actual_has_message_ending_with_expected_description() {
    throwables.assertHasMessageEndingWith(INFO, actual, "sage");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> throwables.assertHasMessageEndingWith(INFO, null, "Boom"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_message_not_ending_with_expected_description() {
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageEndingWith(INFO, actual, "expected end"));
    // THEN
    verify(failures).failure(INFO, shouldEndWith(actual.getMessage(), "expected end"));
  }
}
