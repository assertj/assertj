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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.error.MatcherShouldMatch.shouldMatch;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

@DisplayName("MatcherAssert matches")
public class MatcherAssert_matches_Test {

  @Test
  void should_fail_when_if_is_null() {
    // GIVEN
    Matcher nullActual = null;
    // THEN
    assertThatAssertionErrorIsThrownBy(() -> assertThat(nullActual).matches()).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_Matcher_does_not_match() {
    // GIVEN
    Pattern pattern = Pattern.compile("a*");
    String expectedValue = "abc";
    Matcher actual = pattern.matcher(expectedValue);
    // WHEN
    AssertionError error = catchThrowableOfType(() -> assertThat(actual).matches(), AssertionError.class);
    // THEN
    assertThat(error).hasMessage(shouldMatch(actual).create());
  }

  @Test
  void should_pass_if_Matcher_matches() {
    // GIVEN
    Pattern pattern = Pattern.compile("a*");
    String expectedValue = "aaa";
    Matcher actual = pattern.matcher(expectedValue);
    // WHEN/THEN
    assertThat(actual).matches();
  }
}
