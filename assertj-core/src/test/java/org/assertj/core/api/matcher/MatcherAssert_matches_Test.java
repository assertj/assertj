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
package org.assertj.core.api.matcher;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.MatcherShouldMatch.shouldMatch;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class MatcherAssert_matches_Test {

  @Test
  void should_fail_if_Matcher_is_null() {
    // GIVEN
    Matcher nullActual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).matches());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_Matcher_does_not_match() {
    // GIVEN
    Matcher actual = Pattern.compile("a*").matcher("abc");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).matches());
    // THEN
    then(assertionError).hasMessage(shouldMatch(actual).create());
  }

  @Test
  void should_pass_if_Matcher_matches() {
    // GIVEN
    Matcher actual = Pattern.compile("a*").matcher("aaa");
    // WHEN/THEN
    then(actual).matches();
  }
}
