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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.throwables;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Throwables;
import org.assertj.core.internal.ThrowablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Throwables#assertHasMessageEndingWith(AssertionInfo, Throwable, String)}</code>.
 * 
 * @author Joel Costigliola
 */
class Throwables_assertHasMessageEnding_Test extends ThrowablesBaseTest {

  @Test
  void should_pass_if_actual_has_message_ending_with_expected_description() {
    throwables.assertHasMessageEndingWith(someInfo(), actual, "sage");
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> throwables.assertHasMessageEndingWith(someInfo(), null,
                                                                                                           "Throwable"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_message_not_ending_with_expected_description() {
    AssertionInfo info = someInfo();
    try {
      throwables.assertHasMessageEndingWith(info, actual, "expected end");
      fail("AssertionError expected");
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldEndWith(actual.getMessage(), "expected end"));
    }
  }
}
