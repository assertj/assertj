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
import static org.assertj.core.api.charsequence.CharSequenceAssert_doesNotStartWithWhitespaces_Test.WHITESPACES;
import static org.assertj.core.error.ShouldNotEndWithWhitespaces.shouldNotEndWithWhitespaces;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author Lim Wonjae
 */
class CharSequenceAssert_doesNotEndWithWhitespaces_Test {

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
  protected void should_pass_if_actual_does_not_end_with_whitespaces(String actual) {
    assertThat(actual).doesNotEndWithWhitespaces();
  }

  @ParameterizedTest
  @MethodSource
  protected void should_fail_if_actual_ends_with_whitespaces(String actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).doesNotEndWithWhitespaces());
    // THEN
    then(assertionError).hasMessage(shouldNotEndWithWhitespaces(actual).create());
  }

  static Stream<String> should_fail_if_actual_ends_with_whitespaces() {
    return Stream.of(WHITESPACES).map(whitespace -> unescapeJava("abc" + whitespace));
  }

  static Stream<String> should_pass_if_actual_does_not_end_with_whitespaces() {
    return Stream.concat(Stream.of("<abc>", "  ?", "\t\t\"", ""),
                         Stream.of(WHITESPACES).map(whitespace -> unescapeJava(whitespace + "abc")));
  }
}
