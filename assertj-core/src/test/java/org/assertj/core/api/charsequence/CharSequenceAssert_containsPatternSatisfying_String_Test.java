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
import static org.assertj.core.error.ShouldContainCharSequence.shouldContain;
import static org.assertj.core.error.ShouldContainPattern.shouldContainPattern;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.function.Consumer;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link CharSequenceAssert#containsPatternSatisfying(CharSequence, Consumer)}</code>.
 */
class CharSequenceAssert_containsPatternSatisfying_String_Test {
  @Test
  void should_pass_if_string_contains_given_pattern_and_first_match_satisfies_assertion() {
    // GIVEN
    String pattern = ".o(.o)";
    // WHEN/THEN
    then("Frodo").containsPatternSatisfying(pattern, matcher -> assertThat(matcher.group(1)).isEqualTo("do"));
  }

  @Test
  void should_fail_if_string_does_not_contain_given_pattern() {
    // GIVEN
    String pattern = "(o.a)";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("Frodo").containsPatternSatisfying(pattern,
                                                                                                             matcher -> assertThat(true).isTrue()));
    // THEN
    then(assertionError).hasMessage(shouldContainPattern("Frodo", pattern).create());
  }

  @Test
  void should_pass_if_string_contains_given_pattern_but_match_does_not_satisfy_assertion() {
    // GIVEN
    String pattern = ".(a.)";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat("bar").containsPatternSatisfying(pattern,
                                                                                                           matcher -> assertThat(matcher.group(1)).contains("z")));
    // THEN
    then(assertionError).hasMessage(shouldContain("ar", "z", StandardComparisonStrategy.instance()).create());
  }

}
