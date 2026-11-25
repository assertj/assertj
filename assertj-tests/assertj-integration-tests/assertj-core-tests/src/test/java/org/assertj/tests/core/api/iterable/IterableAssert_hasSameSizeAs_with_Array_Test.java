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
package org.assertj.tests.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAnArray.shouldBeAnArray;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Nicolas François
class IterableAssert_hasSameSizeAs_with_Array_Test {

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    assertThat(List.of("Yoda", "Luke")).hasSameSizeAs(new String[] { "Solo", "Leia" });
    assertThat(List.of("Yoda", "Luke")).hasSameSizeAs(new int[] { 1, 2 });
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    String[] other = { "Solo", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_other_is_null() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Luke");
    String[] other = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAnArray(other).create());
  }

  @Test
  void should_fail_if_other_is_not_an_array() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Luke");
    Object other = "Solo";
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAnArray(other).create());
  }

  @Test
  void should_fail_if_actual_size_is_not_equal_to_other_size() {
    // GIVEN
    List<String> actual = List.of("Yoda");
    String[] other = { "Solo", "Luke", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, other, actual.size(), other.length).create());
  }

  @Test
  void should_pass_if_actual_has_same_size_as_other_whatever_custom_comparison_strategy_is() {
    assertThat(List.of("Luke", "Yoda")).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                       .hasSameSizeAs(new String[] { "Solo", "Leia" });
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = null;
    String[] other = { "Solo", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_other_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = List.of("Yoda", "Luke");
    String[] other = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAnArray(other).create());
  }

  @Test
  void should_fail_if_actual_size_is_not_equal_to_other_size_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = List.of("Yoda");
    String[] other = { "Solo", "Luke", "Leia" };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .hasSameSizeAs(other));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, other, actual.size(), other.length).create());
  }

}
