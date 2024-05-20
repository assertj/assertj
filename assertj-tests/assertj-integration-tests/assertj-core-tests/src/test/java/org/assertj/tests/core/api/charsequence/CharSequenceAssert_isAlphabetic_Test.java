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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAlphabetic.shouldBeAlphabetic;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CharSequenceAssert_isAlphabetic_Test {

  @ParameterizedTest
  @ValueSource(strings = { "abc", "foo", "Foo", "z", "Z" })
  void should_pass_when_actual_is_alphabetic(CharSequence actual) {
    assertThat(actual).isAlphabetic();
  }

  @ParameterizedTest
  @ValueSource(strings = { "@", "abc#", "123", "abc ", " ", "" })
  void should_fail_if_actual_is_not_alphabetic(CharSequence actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAlphabetic());
    // THEN
    then(assertionError).hasMessage(shouldBeAlphabetic(actual).create());
  }

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    CharSequence actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAlphabetic());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
