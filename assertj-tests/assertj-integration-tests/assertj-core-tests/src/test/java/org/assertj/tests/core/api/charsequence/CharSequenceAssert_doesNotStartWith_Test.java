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
import static org.assertj.core.error.ShouldNotStartWith.shouldNotStartWith;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Michal Kordas
class CharSequenceAssert_doesNotStartWith_Test {

  @Test
  void should_pass_if_actual_does_not_start_with_prefix() {
    assertThat("Yoda").doesNotStartWith("Luke");
    assertThat("Yoda").doesNotStartWith("YO");
  }

  @Test
  void should_fail_if_actual_starts_with_prefix() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotStartWith("Yo"));
    // THEN
    then(assertionError).hasMessage(shouldNotStartWith(actual, "Yo").create());
  }

  @Test
  void should_throw_error_if_prefix_is_null() {
    // GIVEN
    String prefix = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat("Yoda").doesNotStartWith(prefix));
    // THEN
    then(npe).hasMessage("The given prefix should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotStartWith("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_does_not_start_with_prefix_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).doesNotStartWith("Luke");
  }

  @Test
  void should_fail_if_actual_starts_with_prefix_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotStartWith("yODA"));
    // THEN
    then(assertionError).hasMessage(shouldNotStartWith(actual, "yODA",
                                                       new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE)).create());
  }

}
