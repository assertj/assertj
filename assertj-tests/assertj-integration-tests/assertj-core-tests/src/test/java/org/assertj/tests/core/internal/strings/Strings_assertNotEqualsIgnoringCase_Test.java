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
package org.assertj.tests.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeEqualIgnoringCase.shouldNotBeEqualIgnoringCase;
import static org.assertj.tests.core.testkit.CharArrays.arrayOf;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.util.DefaultLocale;

/**
 * @author Alexander Bischof
 */
class Strings_assertNotEqualsIgnoringCase_Test {

  private final ComparatorBasedComparisonStrategy comparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE);
  private final Strings stringsWithCaseInsensitiveComparisonStrategy = new Strings(comparisonStrategy);
  private static final AssertionInfo INFO = someInfo();

  @Test
  void should_pass_if_actual_is_null_and_expected_is_not() {
    Strings.instance().assertNotEqualsIgnoringCase(someInfo(), null, "Luke");
  }

  @Test
  void should_pass_if_actual_is_not_null_and_expected_is() {
    Strings.instance().assertNotEqualsIgnoringCase(someInfo(), "Luke", null);
  }

  @Test
  void should_pass_if_both_Strings_are_not_equal_regardless_of_case() {
    Strings.instance().assertNotEqualsIgnoringCase(someInfo(), "Yoda", "Luke");
  }

  private void verifyFailureThrownWhenStringsAreNotEqual(AssertionError error, String actual, String expected) {
    then(error).hasMessage(shouldNotBeEqualIgnoringCase(actual, expected).create());
  }

  @Test
  void should_fail_if_both_Strings_are_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Strings.instance().assertNotEqualsIgnoringCase(someInfo(),
                                                                                                                    null, null))
                                                   .withMessage(shouldNotBeEqualIgnoringCase(null, null).create());
  }

  @Test
  void should_fail_if_both_Strings_are_the_same() {
    String s = "Yoda";
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Strings.instance().assertNotEqualsIgnoringCase(someInfo(), s,
                                                                                                                    s))
                                                   .withMessage(shouldNotBeEqualIgnoringCase(s, s).create());
  }

  @Test
  void should_fail_if_both_Strings_are_equal_but_not_same() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Strings.instance()
                                                                            .assertNotEqualsIgnoringCase(someInfo(), "Yoda",
                                                                                                         new String(arrayOf('Y',
                                                                                                                            'o',
                                                                                                                            'd',
                                                                                                                            'a'))))
                                                   .withMessage(shouldNotBeEqualIgnoringCase("Yoda", "Yoda").create());
  }

  @Test
  void should_fail_if_both_Strings_are_equal_ignoring_case() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> Strings.instance().assertNotEqualsIgnoringCase(someInfo(),
                                                                                                                    "Yoda",
                                                                                                                    "YODA"))
                                                   .withMessage(shouldNotBeEqualIgnoringCase("Yoda", "YODA").create());
  }

  @Test
  void should_pass_if_actual_is_null_and_expected_is_not_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(someInfo(), null, "Luke");
  }

  @Test
  void should_pass_if_both_Strings_are_not_equal_regardless_of_case_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(someInfo(), "Yoda", "Luke");
  }

  @Test
  void should_fail_if_both_Strings_are_null_whatever_custom_comparison_strategy_is() {
    var error = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(someInfo(),
                                                                                                                    null, null));
    verifyFailureThrownWhenStringsAreNotEqual(error, null, null);
  }

  @Test
  void should_fail_if_both_Strings_are_the_same_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    String s = "Yoda";

    var error = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(info, s, s));
    verifyFailureThrownWhenStringsAreNotEqual(error, s, s);
  }

  @Test
  void should_fail_if_both_Strings_are_equal_but_not_same_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    var error = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(info, "Yoda",
                                                                                                                    new String(arrayOf('Y',
                                                                                                                                       'o',
                                                                                                                                       'd',
                                                                                                                                       'a'))));
    verifyFailureThrownWhenStringsAreNotEqual(error, "Yoda", "Yoda");
  }

  @Test
  void should_fail_if_both_Strings_are_equal_ignoring_case_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();

    var error = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(info, "Yoda",
                                                                                                                    "YODA"));
    verifyFailureThrownWhenStringsAreNotEqual(error, "Yoda", "YODA");
  }

  @Test
  @DefaultLocale("tr-TR")
  void should_fail_with_Turkish_default_locale() {
    // WHEN
    var assertionError = expectAssertionError(() -> Strings.instance().assertNotEqualsIgnoringCase(INFO, "Leia", "LEIA"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEqualIgnoringCase("Leia", "LEIA").create());
  }

}
