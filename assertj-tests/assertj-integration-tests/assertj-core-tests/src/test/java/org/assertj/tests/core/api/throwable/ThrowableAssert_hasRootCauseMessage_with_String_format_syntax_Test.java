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
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCauseWithMessage;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class ThrowableAssert_hasRootCauseMessage_with_String_format_syntax_Test {

  @Test
  void should_pass_if_actual_has_root_cause_with_message_equal_to_formatted_expected_message() {
    // GIVEN
    Throwable actual = new RuntimeException(new RuntimeException("expected message"));
    // WHEN/THEN
    then(actual).hasRootCauseMessage("%s %s", "expected", "message");
  }

  @Test
  void should_fail_if_root_cause_message_is_different_from_formatted_expected_message() {
    // GIVEN
    Throwable root = new RuntimeException("fail");
    Throwable actual = new RuntimeException(root);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCauseMessage("%s %s", "expected", "message"));
    // THEN
    then(assertionError).hasMessage(shouldHaveRootCauseWithMessage(actual, root, "expected message").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasRootCauseMessage("%s %s", "expected", "message"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_IllegalArgumentException_if_String_format_syntax_is_not_met() {
    // GIVEN
    Throwable actual = new RuntimeException(new RuntimeException("expected message"));
    // WHEN
    var illegalArgumentException = catchIllegalArgumentException(() -> assertThat(actual).hasRootCauseMessage("%s %s",
                                                                                                              "message"));
    // THEN
    then(illegalArgumentException).hasMessage("Format specifier '%s'");
  }

}
