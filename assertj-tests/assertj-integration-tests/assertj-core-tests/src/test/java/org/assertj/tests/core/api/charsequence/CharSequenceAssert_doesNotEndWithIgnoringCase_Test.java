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
import static org.assertj.core.error.ShouldNotEndWithIgnoringCase.shouldNotEndWithIgnoringCase;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.StringHashCodeTestComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;

class CharSequenceAssert_doesNotEndWithIgnoringCase_Test {

  @Test
  void should_pass_if_actual_does_not_end_with_suffix() {
    assertThat("Yoda").doesNotEndWithIgnoringCase("Luke");
    assertThat("Yoda").doesNotEndWithIgnoringCase("Yo");
  }

  @Test
  void should_fail_if_actual_ends_with_suffix() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotEndWithIgnoringCase("oda"));
    // THEN
    then(assertionError).hasMessage(shouldNotEndWithIgnoringCase(actual, "oda", StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_throw_error_if_suffix_is_null() {
    // GIVEN
    String suffix = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat("Yoda").doesNotEndWithIgnoringCase(suffix));
    // THEN
    then(npe).hasMessage("The given suffix should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotEndWithIgnoringCase("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_does_not_end_with_suffix_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(new StringHashCodeTestComparator()).doesNotEndWithIgnoringCase("Luke");
  }

  @Test
  void should_fail_if_actual_ends_with_suffix_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparator = new StringHashCodeTestComparator();
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(comparator)
                                                                      .doesNotEndWithIgnoringCase("A"));
    // THEN
    then(assertionError).hasMessage(shouldNotEndWithIgnoringCase(actual, "A",
                                                                 new ComparatorBasedComparisonStrategy(comparator)).create());
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_fail_with_Turkish_default_locale() {
    // GIVEN
    String actual = "Leia";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotEndWithIgnoringCase("IA"));
    // THEN
    then(assertionError).hasMessage(shouldNotEndWithIgnoringCase(actual, "IA", StandardComparisonStrategy.instance()).create());
  }

}
