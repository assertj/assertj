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
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldStartWithIgnoringCase.shouldStartWithIgnoringCase;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.assertj.core.util.StringHashCodeTestComparator;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;

class Strings_assertStartsWithIgnoringCase_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_starts_with_prefix() {
    strings.assertStartsWithIgnoringCase(INFO, "Yoda", "YODA");
    strings.assertStartsWithIgnoringCase(INFO, "Yoda", "YO");
    strings.assertStartsWithIgnoringCase(INFO, "Yoda", "Yo");
    strings.assertStartsWithIgnoringCase(INFO, "Yoda", "yo");
    strings.assertStartsWithIgnoringCase(INFO, "Yoda", "Y");
    strings.assertStartsWithIgnoringCase(INFO, "Yoda", "y");
  }

  @Test
  void should_fail_if_actual_does_not_start_with_prefix() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertStartsWithIgnoringCase(INFO, "Yoda", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldStartWithIgnoringCase("Yoda", "Luke", StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_throw_error_if_prefix_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertStartsWithIgnoringCase(INFO, "Yoda", null))
                                    .withMessage("The given prefix should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertStartsWithIgnoringCase(INFO, null, "Yoda"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_starts_with_prefix_according_to_custom_comparison_strategy() {
    // GIVEN
    ComparisonStrategy hashCodeComparisonStrategy = new ComparatorBasedComparisonStrategy(new StringHashCodeTestComparator());
    Strings stringsWithHashCodeComparisonStrategy = new Strings(hashCodeComparisonStrategy);
    // WHEN/THEN
    stringsWithHashCodeComparisonStrategy.assertStartsWithIgnoringCase(INFO, "Yoda", "yod");
  }

  @Test
  void should_fail_if_actual_does_not_start_with_prefix_according_to_custom_comparison_strategy() {
    // GIVEN
    ComparisonStrategy hashCodeComparisonStrategy = new ComparatorBasedComparisonStrategy(new StringHashCodeTestComparator());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> new Strings(hashCodeComparisonStrategy).assertStartsWithIgnoringCase(INFO,
                                                                                                                                    "Yoda",
                                                                                                                                    "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldStartWithIgnoringCase("Yoda", "Luke", hashCodeComparisonStrategy).create());
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_pass_with_Turkish_default_locale() {
    // WHEN/THEN
    strings.assertStartsWithIgnoringCase(INFO, "Leia", "LEI");
  }

}
