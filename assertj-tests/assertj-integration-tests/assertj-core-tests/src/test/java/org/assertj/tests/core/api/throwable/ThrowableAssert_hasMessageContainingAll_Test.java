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

import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/// @author Phillip Webb
class ThrowableAssert_hasMessageContainingAll_Test {

  @Test
  void should_pass_if_actual_has_message_containing_the_expected_string() {
    assertThat(new NullPointerException("Throwable message")).hasMessageContainingAll("able");
  }

  @Test
  void should_pass_if_actual_has_message_containing_all_the_expected_strings() {
    assertThat(new NullPointerException("Throwable message")).hasMessageContainingAll("able", "message");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Throwable actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageContainingAll("Boom"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_has_message_not_containing_all_the_expected_strings() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    String content = "expected description part";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageContainingAll(content));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, content).create());
  }

  @Test
  void should_fail_if_actual_has_message_not_containing_some_of_the_expected_strings() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    String[] content = { "catchable", "message" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMessageContainingAll(content));
    // THEN
    then(assertionError).hasMessage(shouldContain(actual, content, singleton("catchable")).create());
  }

  @Test
  void should_throw_error_if_expected_strings_are_null() {
    // GIVEN
    Throwable actual = new NullPointerException("Throwable message");
    // WHEN
    var npe = catchNullPointerException(() -> assertThat(actual).hasMessageContainingAll((String) null));
    // THEN
    then(npe).hasMessage(charSequenceToLookForIsNull());
  }

}
