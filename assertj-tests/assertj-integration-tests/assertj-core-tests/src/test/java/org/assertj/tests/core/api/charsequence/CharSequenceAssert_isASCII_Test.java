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
import static org.assertj.core.error.ShouldBeASCII.shouldBeASCII;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CharSequenceAssert_isASCII_Test {

  @ParameterizedTest
  @ValueSource(strings = { "@", "abc", "foo", "Foo", "Foo123", "z", "Z", "1", "5 > 3" })
  void should_pass_when_actual_is_ASCII(CharSequence actual) {
    assertThat(actual).isASCII();
  }

  @ParameterizedTest
  @ValueSource(strings = { "½", "§", "©", "«abc»", "" })
  void should_fail_if_actual_is_not_ASCII(CharSequence actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isASCII());
    // THEN
    then(assertionError).hasMessage(shouldBeASCII(actual).create());
  }

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    CharSequence actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isASCII());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
