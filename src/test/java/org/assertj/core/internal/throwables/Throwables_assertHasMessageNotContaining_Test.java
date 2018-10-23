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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.throwables;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Throwables;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.assertj.core.util.AssertionsUtil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link Throwables#assertHasMessageNotContaining(AssertionInfo, Throwable, String)}</code>.
 *
 * @author Sandra Parsick
 * @author Georg Berky
 */
class Throwables_assertHasMessageNotContaining_Test extends ThrowablesBaseTest {

  @Test
  void should_pass_if_actual_has_message_not_containing_expected_description() {
    throwables.assertHasMessageNotContaining(someInfo(), actual, "catchable");
  }

  @Test
  void should_pass_if_actual_has_NULL_message() {
    throwables.assertHasMessageNotContaining(someInfo(), new NullPointerException(), "some description");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(
      () -> throwables.assertHasMessageNotContaining(someInfo(), null, "Throwable"))
      .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_message_containing_description() {
    AssertionInfo info = someInfo();

    expectAssertionError(() -> throwables.assertHasMessageNotContaining(info, actual, "message"));
    verify(failures).failure(info, shouldNotContain(actual.getMessage(), "message", StandardComparisonStrategy.instance()));
  }
}
