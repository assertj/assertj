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
package org.assertj.tests.core.api.list;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.Index.atIndex;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;

import org.assertj.tests.core.testkit.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.Test;

/// @author Alex Ruiz
/// @author Joel Costigliola
class ListAssert_contains_at_Index_Test {

  @Test
  void should_pass_if_actual_contains_value_at_index() {
    assertThat(List.of("Luke")).contains("Luke", atIndex(0));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).contains("Luke", atIndex(0)));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    List<String> actual = List.of();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).contains("Luke", atIndex(0)));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

  @Test
  void should_pass_if_actual_contains_value_at_index_whatever_custom_comparison_strategy_is() {
    assertThat(List.of("Luke")).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                               .contains("luke", atIndex(0));
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .contains("Luke", atIndex(0)));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_empty_whatever_custom_comparison_strategy_is() {
    // GIVEN
    List<String> actual = List.of();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CaseInsensitiveStringComparator.INSTANCE)
                                                                      .contains("Luke", atIndex(0)));
    // THEN
    then(assertionError).hasMessage(shouldNotBeEmpty().create());
  }

}
