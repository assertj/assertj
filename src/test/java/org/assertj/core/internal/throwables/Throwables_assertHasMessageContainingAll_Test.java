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
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.Throwables;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Throwables#assertHasMessageContainingAll(AssertionInfo, Throwable, CharSequence...)}</code>.
 *
 * @author Phillip Webb
 */
public class Throwables_assertHasMessageContainingAll_Test extends ThrowablesBaseTest {

  private static final AssertionInfo INFO = someInfo();

  @Test
  public void should_pass_if_actual_has_message_containing_the_expected_string() {
    throwables.assertHasMessageContainingAll(someInfo(), actual, "able");
  }

  @Test
  public void should_pass_if_actual_has_message_containing_all_the_expected_strings() {
    throwables.assertHasMessageContainingAll(someInfo(), actual, "able", "message");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    ThrowingCallable code = () -> throwables.assertHasMessageContainingAll(INFO, null, "Throwable");
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_has_message_not_containing_all_the_expected_strings() {
    // GIVEN
    String content = "expected description part";
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageContainingAll(INFO, actual, content));
    // THEN
    verify(failures).failure(INFO, shouldContain(actual.getMessage(), content), actual.getMessage(), content);
  }

  @Test
  public void should_fail_if_actual_has_message_not_containing_some_of_the_expected_strings() {
    // GIVEN
    String[] content = { "catchable", "message" };
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageContainingAll(INFO, actual, content));
    // THEN
    verify(failures).failure(INFO, shouldContain(actual.getMessage(), content, Collections.singleton("catchable")),
                             actual.getMessage(), content);
  }

  @Test
  public void should_throw_error_if_expected_strings_are_null() {
    assertThatNullPointerException().isThrownBy(() -> throwables.assertHasMessageContainingAll(INFO, actual, (String) null))
                                    .withMessage(charSequenceToLookForIsNull());
  }
}
