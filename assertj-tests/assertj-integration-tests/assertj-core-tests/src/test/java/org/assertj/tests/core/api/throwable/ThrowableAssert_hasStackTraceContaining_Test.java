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
package org.assertj.tests.core.api.throwable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Throwables.getStackTrace;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/// @author Daniel Zlotin
class ThrowableAssert_hasStackTraceContaining_Test {

  @Test
  void should_pass_if_actual_has_stacktrace_containing_the_expected_description() {
    assertThat(new NullPointerException("Throwable message")).hasStackTraceContaining("able");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasStackTraceContaining("boom"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_stacktrace_does_not_contain_the_expected_description() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasStackTraceContaining("expected description part"));
    // THEN
    then(assertionError).hasMessage(shouldContain(getStackTrace(actual), "expected description part").create());
  }

}
