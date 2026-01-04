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
package org.assertj.tests.core.api.optionalint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.OptionalInt;

import org.junit.jupiter.api.Test;

@SuppressWarnings("DataFlowIssue")
class OptionalIntAssert_isNotEmpty_Test {

  @Test
  void should_pass_when_OptionalInt_is_present() {
    assertThat(OptionalInt.of(10)).isNotEmpty();
  }

  @Test
  void should_fail_when_OptionalInt_is_empty() {
    // GIVEN
    OptionalInt actual = OptionalInt.empty();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(shouldBePresent(actual).create());
  }

  @SuppressWarnings("OptionalAssignedToNull")
  @Test
  void should_fail_when_OptionalInt_is_null() {
    // GIVEN
    OptionalInt nullActual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).isNotEmpty());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }
}
