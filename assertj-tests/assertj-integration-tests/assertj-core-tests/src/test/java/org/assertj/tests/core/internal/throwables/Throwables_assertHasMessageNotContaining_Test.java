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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Throwables;
import org.assertj.tests.core.testkit.TestData;
import org.junit.jupiter.api.Test;

/**
 * @author Sandra Parsick
 * @author Georg Berky
 */
class Throwables_assertHasMessageNotContaining_Test {

  private static final AssertionInfo INFO = TestData.someInfo();
  private final Throwable actual = new NullPointerException("Throwable message");

  @Test
  void should_pass_if_actual_has_a_message_not_containing_the_given_content() {
    Throwables.instance().assertHasMessageNotContaining(INFO, actual, "catchable");
  }

  @Test
  void should_pass_if_actual_has_no_message() {
    Throwables.instance().assertHasMessageNotContaining(INFO, new NullPointerException(), "some description");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> Throwables.instance().assertHasMessageNotContaining(INFO, null, "foo"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_a_message_containing_the_given_content() {
    // GIVEN
    String content = "message";
    // WHEN
    var error = expectAssertionError(() -> Throwables.instance().assertHasMessageNotContaining(INFO, actual, content));
    // THEN
    then(error).hasMessage(shouldNotContain(actual, content).create());
  }
}
