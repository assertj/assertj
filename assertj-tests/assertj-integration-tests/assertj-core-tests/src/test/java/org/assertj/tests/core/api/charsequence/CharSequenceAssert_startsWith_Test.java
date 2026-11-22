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
import static org.assertj.core.error.ShouldStartWith.shouldStartWith;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
class CharSequenceAssert_startsWith_Test {

  @Test
  void should_pass_if_actual_starts_with_prefix() {
    assertThat("Yoda").startsWith("Yo");
  }

  @Test
  void should_fail_if_actual_does_not_start_with_prefix() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).startsWith("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldStartWith(actual, "Luke").create());
  }

  @Test
  void should_throw_error_if_prefix_is_null() {
    // GIVEN
    String prefix = null;
    // WHEN
    var npe = catchNullPointerException(() -> assertThat("Yoda").startsWith(prefix));
    // THEN
    then(npe).hasMessage("The given prefix should not be null");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).startsWith("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_starts_with_prefix_according_to_custom_comparison_strategy() {
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).startsWith("Y");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).startsWith("Yo");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).startsWith("Yod");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).startsWith("Yoda");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).startsWith("yoda");
    assertThat("Yoda").usingComparator(CaseInsensitiveStringComparator.INSTANCE).startsWith("YODA");
  }

  @Test
  void should_fail_if_actual_does_not_start_with_prefix_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .startsWith("Luke"));
    // THEN
    then(assertionError).hasMessage(shouldStartWith(actual, "Luke",
                                                    new ComparatorBasedComparisonStrategy(CaseInsensitiveStringComparator.INSTANCE)).create());
  }

}
