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
package org.assertj.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldHaveRootCause.shouldHaveRootCause;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ThrowableAssertAlternative havingRootCause")
class ThrowableAssertAlternative_havingRootCause_Test {

  @Test
  void should_return_root_cause_if_throwable_has_cause() {
    // GIVEN
    Throwable rootCause = new Throwable("root message");
    Throwable throwable = new Throwable("top level message", new Throwable("cause message", rootCause));
    // WHEN
    ThrowableAssertAlternative<?> rootCauseAssertions = new ThrowableAssertAlternative<>(throwable).havingRootCause();
    // THEN
    assertThat(rootCauseAssertions.actual).isSameAs(rootCause);
  }

  @Test
  void should_fail_if_throwable_has_no_root_cause() {
    // GIVEN
    Throwable throwable = new Throwable("top level message");
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(throwable);
    // WHEN
    AssertionError error = expectAssertionError(taa::havingRootCause);
    // THEN
    assertThat(error).hasMessage(shouldHaveRootCause(throwable).create());
  }

  @Test
  void should_fail_if_throwable_is_null() {
    // GIVEN
    ThrowableAssertAlternative<Throwable> taa = new ThrowableAssertAlternative<>(null);
    // WHEN
    AssertionError error = expectAssertionError(taa::havingRootCause);
    // THEN
    assertThat(error).hasMessage(shouldNotBeNull().create());
  }

}
