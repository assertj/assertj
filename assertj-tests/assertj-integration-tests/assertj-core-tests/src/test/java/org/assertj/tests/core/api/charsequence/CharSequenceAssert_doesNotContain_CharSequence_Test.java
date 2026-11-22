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
import static org.assertj.core.error.ShouldNotContainCharSequence.shouldNotContain;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
class CharSequenceAssert_doesNotContain_CharSequence_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).doesNotContain("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_given_value_is_null() {
    // GIVEN
    String value = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat("Yoda").doesNotContain(value));
    // THEN
    then(exception).hasMessage("The char sequence to look for should not be null");
  }

  @Test
  void should_pass_if_actual_does_not_contain_sequence() {
    assertThat("Yoda").doesNotContain("Lu");
  }

  @Test
  void should_fail_if_actual_contains_sequence() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").doesNotContain("oda"));
    // THEN
    then(assertionError).hasMessage(shouldNotContain("Yoda", "oda", StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).doesNotContain("Lu");
  }

  @Test
  void should_fail_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .doesNotContain("yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotContain("Yoda", "yoda", comparisonStrategy).create());
  }

}
