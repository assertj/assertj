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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal.throwables;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Throwables;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.error.ShouldHaveMessage.shouldHaveMessage;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link Throwables#assertHasMessageNormalizingNewlines(AssertionInfo, Throwable, String)}.
 */
class Throwables_assertHasMessageNormalizingNewlines_Test extends ThrowablesBaseTest {

  @BeforeAll
  public static void setUpOnce() {
    actual = new NullPointerException("Throwable\r\nmessage");
  }

  @Test
  void should_pass_if_actual_has_expected_message_normalizing_new_lines() {
    throwables.assertHasMessageNormalizingNewlines(someInfo(), actual, "Throwable\nmessage");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    AssertionInfo info = someInfo();
    Throwable actual = null;
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> throwables.assertHasMessageNormalizingNewlines(info, actual, "message"))
                                                                                                                     .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_not_expected_message_normalizing_new_lines() {
    // GIVEN
    AssertionInfo info = someInfo();
    String expectedMessage = "expected message";
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageNormalizingNewlines(info, actual, expectedMessage));
    // THEN
    verify(failures).failure(info, shouldHaveMessage(actual, expectedMessage), "Throwable\r\nmessage", expectedMessage);
  }
}
