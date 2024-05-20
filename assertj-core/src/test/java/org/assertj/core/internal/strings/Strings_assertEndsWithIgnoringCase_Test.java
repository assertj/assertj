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
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldEndWithIgnoringCase.shouldEndWithIgnoringCase;
import static org.assertj.core.testkit.TestData.someInfo;
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

class Strings_assertEndsWithIgnoringCase_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_ends_with_suffix() {
    strings.assertEndsWithIgnoringCase(INFO, "Yoda", "YODA");
    strings.assertEndsWithIgnoringCase(INFO, "Yoda", "ODA");
    strings.assertEndsWithIgnoringCase(INFO, "Yoda", "oda");
    strings.assertEndsWithIgnoringCase(INFO, "Yoda", "Oda");
    strings.assertEndsWithIgnoringCase(INFO, "Yoda", "DA");
    strings.assertEndsWithIgnoringCase(INFO, "Yoda", "A");
    strings.assertEndsWithIgnoringCase(INFO, "Yoda", "a");
  }

  @Test
  void should_fail_if_actual_does_not_end_with_suffix() {
    // GIVEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertEndsWithIgnoringCase(INFO, "Yoda", "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldEndWithIgnoringCase("Yoda", "Luke", StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_throw_error_if_suffix_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertEndsWithIgnoringCase(INFO, "Yoda", null))
                                    .withMessage("The given suffix should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertEndsWithIgnoringCase(INFO, null, "Yoda"));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_ends_with_suffix_according_to_custom_comparison_strategy() {
    // GIVEN
    Strings stringsWithHashCodeComparisonStrategy = new Strings(new ComparatorBasedComparisonStrategy(new StringHashCodeTestComparator()));
    // WHEN/THEN
    stringsWithHashCodeComparisonStrategy.assertEndsWithIgnoringCase(INFO, "Yoda", "ODA");
  }

  @Test
  void should_fail_if_actual_does_not_end_with_suffix_according_to_custom_comparison_strategy() {
    // GIVEN
    ComparisonStrategy hashCodeComparisonStrategy = new ComparatorBasedComparisonStrategy(new StringHashCodeTestComparator());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> new Strings(hashCodeComparisonStrategy).assertEndsWithIgnoringCase(INFO,
                                                                                                                                  "Yoda",
                                                                                                                                  "Luke"));
    // THEN
    then(assertionError).hasMessage(shouldEndWithIgnoringCase("Yoda", "Luke", hashCodeComparisonStrategy).create());
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_pass_with_Turkish_default_locale() {
    // WHEN/THEN
    strings.assertEndsWithIgnoringCase(someInfo(), "Leia", "IA");
  }

}
