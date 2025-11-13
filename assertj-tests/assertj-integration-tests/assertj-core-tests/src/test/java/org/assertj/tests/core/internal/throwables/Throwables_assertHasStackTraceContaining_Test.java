/*
 * Copyright 2012-2025 the original author or authors.
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
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Throwables.getStackTrace;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author Daniel Zlotin
 */
class Throwables_assertHasStackTraceContaining_Test extends ThrowablesBaseTest {

  @Test
  void should_fail_if_actual_stacktrace_does_not_contain_the_expected_description() {
    // WHEN
    var assertionError = expectAssertionError(() -> throwables.assertHasStackTraceContaining(INFO, actual,
                                                                                             "expected description part"));
    // THEN
    then(assertionError).hasMessage(shouldContain(getStackTrace(actual), "expected description part").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> throwables.assertHasStackTraceContaining(INFO, null, "boom"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_has_stacktrace_containing_the_expected_description() {
    throwables.assertHasStackTraceContaining(INFO, actual, "able");
  }

}
