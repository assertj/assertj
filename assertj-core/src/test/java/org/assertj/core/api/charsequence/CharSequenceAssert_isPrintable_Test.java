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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBePrintable.shouldBePrintable;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CharSequenceAssert_isPrintable_Test {

  @ParameterizedTest
  @ValueSource(strings = { "abc", "foo", "foo123 ", " ", "!", "\"", "#", "$", "%", "&", "'", "(", ")", "*", "+", ",", "-", ".",
      "/", ":", ";", "<", "=", ">", "?", "@", "[", "\\", "]", "^", "_", "`", "{", "|", "}", "~" })
  void should_pass_when_actual_is_printable(CharSequence actual) {
    assertThat(actual).isPrintable();
  }

  @ParameterizedTest
  @ValueSource(strings = { "\t", "\n", "½", "§", "©", "«abc»", "" })
  void should_fail_if_actual_is_not_printable(CharSequence actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPrintable());
    // THEN
    then(assertionError).hasMessage(shouldBePrintable(actual).create());
  }

  @Test
  void should_fail_when_actual_is_null() {
    // GIVEN
    CharSequence actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPrintable());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
