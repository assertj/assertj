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

import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import java.util.Collections;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Throwables;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Throwables#assertHasMessageNotContaining(AssertionInfo, Throwable, String)}</code>.
 *
 * @author Phillip Webb
 */
class Throwables_assertHasMessageNotContainingAny_Test extends ThrowablesBaseTest {

  private static final AssertionInfo INFO = someInfo();

  @Test
  void should_pass_if_actual_has_a_message_not_containing_the_given_string() {
    throwables.assertHasMessageNotContainingAny(INFO, actual, "catchable");
  }

  @Test
  void should_pass_if_actual_has_a_message_not_containing_any_the_given_strings() {
    throwables.assertHasMessageNotContainingAny(INFO, actual, "catchable", "foo");
  }

  @Test
  void should_pass_if_actual_has_no_message() {
    throwables.assertHasMessageNotContainingAny(INFO, new NullPointerException(), "some description");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    ThrowingCallable code = () -> throwables.assertHasMessageNotContainingAny(INFO, null, "foo");
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_a_message_containing_the_given_string() {
    // GIVEN
    String content = "message";
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageNotContainingAny(INFO, actual, content));
    // THEN
    verify(failures).failure(INFO, shouldNotContain(actual.getMessage(), content), actual.getMessage(), content);
  }

  @Test
  void should_fail_if_actual_has_a_message_containing_some_of_the_given_strings() {
    // GIVEN
    String[] content = { "catchable", "message" };
    // WHEN
    expectAssertionError(() -> throwables.assertHasMessageNotContainingAny(INFO, actual, content));
    // THEN
    verify(failures).failure(INFO, shouldNotContain(actual.getMessage(), content, Collections.singleton("message"), StandardComparisonStrategy.instance()), actual.getMessage(), content);
  }
}
