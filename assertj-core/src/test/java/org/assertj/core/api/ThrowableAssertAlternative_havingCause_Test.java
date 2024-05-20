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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveCause.shouldHaveCause;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class ThrowableAssertAlternative_havingCause_Test {

  @Test
  void should_return_cause_if_throwable_has_cause() {
    // GIVEN
    Throwable cause = new Throwable("cause message");
    Throwable throwable = new Throwable("top level message", cause);
    // WHEN
    ThrowableAssertAlternative<?> causeAssertions = new ThrowableAssertAlternative<>(throwable).havingCause();
    // THEN
    assertThat(causeAssertions.actual).isSameAs(cause);
  }

  @Test
  void should_fail_if_throwable_has_no_cause() {
    // GIVEN
    Throwable throwable = new Throwable("top level message");
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(throwable);
    // WHEN
    AssertionError error = expectAssertionError(taa::havingCause);
    // THEN
    assertThat(error).hasMessage(shouldHaveCause(throwable).create());
  }

  @Test
  void should_fail_if_throwable_is_null() {
    // GIVEN
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(null);
    // WHEN
    AssertionError error = expectAssertionError(taa::havingCause);
    // THEN
    assertThat(error).hasMessage(shouldNotBeNull().create());
  }
}
