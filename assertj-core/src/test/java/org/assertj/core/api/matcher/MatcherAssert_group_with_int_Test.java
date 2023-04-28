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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.matcher;

import static java.util.regex.Pattern.compile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.MatcherShouldHaveGroup.shouldHaveGroup;
import static org.assertj.core.error.MatcherShouldMatch.shouldMatch;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class MatcherAssert_group_with_int_Test {

  private static final Pattern PATTERN = compile("hi my name is (\\S+) (\\S+)");
  private static final Pattern NAMED_PATTERN = compile("hi my name is (?<first>\\S+) (?<last>\\S+)");

  @Test
  void should_fail_on_numbered_group_if_Matcher_is_null() {
    // GIVEN
    Matcher nullActual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(nullActual).group(1));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_on_numbered_group_if_Matcher_does_not_match() {
    // GIVEN
    Matcher actual = PATTERN.matcher("bonjour je m'appelle slim shady");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).group(1));
    // THEN
    then(assertionError).hasMessage(shouldMatch(actual).create());
  }

  @Test
  void should_fail_on_numbered_group_if_does_not_exist() {
    // GIVEN
    Matcher actual = PATTERN.matcher("hi my name is slim shady");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).group(3));
    // THEN
    then(assertionError).hasMessage(shouldHaveGroup(actual, 3).create());
  }

  @Test
  void should_pass_if_numbered_group_matches() {
    // GIVEN
    Matcher actual = PATTERN.matcher("hi my name is slim shady");
    // WHEN/THEN
    assertThat(actual).group(1).isEqualTo("slim");
    assertThat(actual).group(2).isEqualTo("shady");
  }
}
