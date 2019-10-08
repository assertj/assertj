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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCauseWithMessage;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Throwables assertHasRootCauseMessage")
public class Throwables_assertHasRootCauseMessage_Test extends ThrowablesBaseTest {

  private static final AssertionInfo INFO = someInfo();

  @Test
  public void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, null, "message"));
    // THEN
    assertThat(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  public void should_fail_if_root_cause_is_null() {
    // GIVEN
    Throwable error = new RuntimeException();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, error, "message"));
    // THEN
    assertThat(assertionError).hasMessage(shouldHaveRootCauseWithMessage(null, "message").create());
  }

  @Test
  public void should_fail_if_root_cause_has_no_message() {
    // GIVEN
    Throwable root = new RuntimeException();
    Throwable error = new RuntimeException(root);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, error, "message"));
    // THEN
    assertThat(assertionError).hasMessage(shouldHaveRootCauseWithMessage(root, "message").create());
  }

  @Test
  public void should_fail_if_root_cause_message_is_different() {
    // GIVEN
    Throwable root = new RuntimeException("fail");
    Throwable error = new RuntimeException(root);
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> throwables.assertHasRootCauseMessage(INFO, error, "message"));
    // THEN
    assertThat(assertionError).hasMessage(shouldHaveRootCauseWithMessage(root, "message").create());
  }

  @Test
  public void should_pass_if_throwable_has_root_cause_with_message_equal_to_expected() {
    // GIVEN
    Throwable error = new RuntimeException(new RuntimeException("expected message"));
    // THEN
    throwables.assertHasRootCauseMessage(INFO, error, "expected message");
  }

  @Test
  public void should_pass_if_actual_root_cause_has_no_message_and_expected_message_is_null() {
    // GIVEN
    Throwable error = new RuntimeException(new RuntimeException());
    // THEN
    throwables.assertHasRootCauseMessage(INFO, error, null);
  }
}
