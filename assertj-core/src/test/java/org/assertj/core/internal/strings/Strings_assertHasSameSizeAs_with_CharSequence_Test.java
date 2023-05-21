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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertHasSameSizeAs(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)}</code>.
 *
 * @author Joel Costigliola
 */
class Strings_assertHasSameSizeAs_with_CharSequence_Test extends StringsBaseTest {

  private String actual = "Luke";

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    ThrowingCallable code = () -> strings.assertHasSameSizeAs(someInfo(), actual, "Solo");
    // THEN
    assertThatAssertionErrorIsThrownBy(code).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    // GIVEN
    AssertionInfo info = someInfo();
    String other = "Han";
    // WHEN
    ThrowingCallable code = () -> strings.assertHasSameSizeAs(info, actual, other);
    // THEN
    String error = shouldHaveSameSizeAs(actual, other, actual.length(), other.length()).create(null, info.representation());
    assertThatAssertionErrorIsThrownBy(code).withMessage(error);
  }

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    strings.assertHasSameSizeAs(someInfo(), actual, "Solo");
  }
}
