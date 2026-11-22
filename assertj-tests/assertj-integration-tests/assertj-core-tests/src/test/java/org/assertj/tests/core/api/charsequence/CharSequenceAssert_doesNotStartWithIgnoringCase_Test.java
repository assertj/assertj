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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotStartWithIgnoringCase.shouldNotStartWithIgnoringCase;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.StringHashCodeTestComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;

class CharSequenceAssert_doesNotStartWithIgnoringCase_Test {

  @Test
  void should_pass_if_actual_does_not_start_with_prefix() {
    assertThat("Yoda").doesNotStartWithIgnoringCase("Luke");
    assertThat("Yoda").doesNotStartWithIgnoringCase("da");
  }

  @Test
  void should_fail_if_actual_starts_with_prefix() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotStartWithIgnoringCase("yo"));
    // THEN
    then(assertionError).hasMessage(shouldNotStartWithIgnoringCase(actual, "yo", StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_throw_error_if_prefix_is_null() {
    // GIVEN
    String prefix = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat("Yoda").doesNotStartWithIgnoringCase(prefix));
    // THEN
    then(npe).hasMessage("The given prefix should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotStartWithIgnoringCase("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_does_not_start_with_prefix_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(new StringHashCodeTestComparator()).doesNotStartWithIgnoringCase("Luke");
  }

  @Test
  void should_fail_if_actual_starts_with_prefix_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparator = new StringHashCodeTestComparator();
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(comparator)
                                                                      .doesNotStartWithIgnoringCase("yODA"));
    // THEN
    then(assertionError).hasMessage(shouldNotStartWithIgnoringCase(actual, "yODA",
                                                                   new ComparatorBasedComparisonStrategy(comparator)).create());
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_fail_with_Turkish_default_locale() {
    // GIVEN
    String actual = "Leia";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotStartWithIgnoringCase("LEI"));
    // THEN
    then(assertionError).hasMessage(shouldNotStartWithIgnoringCase(actual, "LEI",
                                                                   StandardComparisonStrategy.instance()).create());
  }

}
