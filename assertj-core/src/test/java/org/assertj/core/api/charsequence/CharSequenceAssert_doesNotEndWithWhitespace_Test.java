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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.charsequence;

import org.assertj.core.api.BDDAssertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldNotEndWithWhitespace.shouldNotEndWithWhitespace;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

/**
 * @author Lim Wonjae
 */
public class CharSequenceAssert_doesNotEndWithWhitespace_Test {

  @ParameterizedTest
  @ValueSource(strings =  {"abc", "  ?", "  ab", "\t\t\""})
  protected void should_pass_if_actual_does_not_end_with_whitespace(String actual) {
    assertThat(actual).doesNotEndWithWhitespace();
  }

  @ParameterizedTest
  @ValueSource(strings =  {"abc ", "abc\t", "abc\n", "abc\r"})
  protected void should_fail_if_actual_ends_with_whitespace(String actual) {
    //When
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).doesNotEndWithWhitespace());
    //Then
    BDDAssertions.then(assertionError).hasMessage(shouldNotEndWithWhitespace(actual).create());
  }

}
