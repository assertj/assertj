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

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotStartWithWhitespaces.shouldNotStartWithWhitespaces;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Lim Wonjae
 */
class CharSequenceAssert_doesNotStartWithWhitespaces_Test {

  // java whitespace according to Character.html#isWhitespace(int)
  static final String[] WHITESPACES = {
      "\\u0020",
      "    ",
      "\t ",
      "\\u0009",
      "\n",
      "\\u000A",
      "\f",
      "\\u000C",
      "\r",
      "\\u000D",
      "\r \t",
      "\\u001C",
      "\\u001C",
      "\\u001D",
      "\\u001E",
      "\\u001F",
      "\\u000B", // VERTICAL TABULATION.
      "\\u1680",
      "\\u2000",
      "\\u2001",
      "\\u2002",
      "\\u2003",
      "\\u2004",
      "\\u2005",
      "\\u2006",
      "\\u2008",
      "\\u2009",
      "\\u200A",
      "\\u205F",
      "\\u2028", // LINE SEPARATOR
      "\\u2029", // PARAGRAPH SEPARATOR
      "\\u3000"
  };

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).doesNotStartWithWhitespaces());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @MethodSource
  protected void should_pass_if_actual_does_not_start_with_whitespaces(String actual) {
    assertThat(actual).doesNotStartWithWhitespaces();
  }

  @ParameterizedTest
  @MethodSource
  protected void should_fail_if_actual_starts_with_whitespaces(String actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).doesNotStartWithWhitespaces());
    // THEN
    then(assertionError).hasMessage(shouldNotStartWithWhitespaces(actual).create());
  }

  static Stream<String> should_fail_if_actual_starts_with_whitespaces() {
    return Stream.of(WHITESPACES).map(whitespace -> unescapeJava(whitespace + "abc"));
  }

  static Stream<String> should_pass_if_actual_does_not_start_with_whitespaces() {
    return Stream.concat(Stream.of("<abc>", "? ", ""),
                         Stream.of(WHITESPACES).map(whitespace -> unescapeJava("abc" + whitespace)));
  }

}
