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
package org.assertj.tests.core.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.ErrorMessagesForTest.shouldBeEqualMessage;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.presentation.UnicodeRepresentation#toStringOf(Object)}.
 *
 * @author Mariusz Smykula
 */
class Assertions_assertThat_inUnicode_Test {

  @Test
  void should_assert_String_in_unicode() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("a6c").inUnicode().isEqualTo("abó"));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("a6c", "ab\\u00f3"));
  }

  @Test
  void should_assert_CharSequence_implementation_in_unicode() {
    // GIVEN
    CharSequence charSequence = new StringBuilder("abó");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("a6c").inUnicode().isEqualTo(charSequence));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("a6c", "ab\\u00f3"));
  }

  @Test
  void should_assert_Character_in_unicode() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat('o').inUnicode().isEqualTo('ó'));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("o", "\\u00f3"));
  }

  @Test
  void should_assert_char_array_in_unicode_representation() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("a6c".toCharArray()).inUnicode()
                                                                                              .isEqualTo("abó".toCharArray()));
    // THEN
    then(assertionError).hasMessage(shouldBeEqualMessage("[a, 6, c]", "[a, b, \\u00f3]"));
  }

  @Test
  public void should_keep_existing_description_set_before_calling_inUnicode() {
    // GIVEN
    String description = "My description";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("ab").as(description).inUnicode().isNull());
    // THEN
    then(assertionError).hasMessageContaining(description);
  }

}
