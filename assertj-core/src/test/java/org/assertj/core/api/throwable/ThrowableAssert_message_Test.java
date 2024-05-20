/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AbstractThrowableAssert;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractThrowableAssert#message()}</code>.
 *
 * @author Trang Nguyen
 */
class ThrowableAssert_message_Test {

  @Test
  void should_return_string_assertions_for_actual_message() {
    // GIVEN
    Throwable throwable = new Throwable("boom!");
    // WHEN/THEN
    assertThat(throwable).message()
                         .isSameAs(throwable.getMessage());
  }

  @Test
  void should_return_string_assertions_for_actual_message_even_if_null() {
    // GIVEN
    Throwable throwable = new Throwable();
    // WHEN/THEN
    assertThat(throwable).message().isNull();
  }

  @Test
  void should_fail_when_throwable_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).message());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
