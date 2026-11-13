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
package org.assertj.core.internal.iterables;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveAllElementsEqual.shouldHaveAllElementsEqual;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.comparisonstrategy.StandardComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertAllElementsAreEqual(AssertionInfo, Iterable)}</code>.
 */
class Iterables_assertAllElementsAreEqual_Test extends IterablesBaseTest {

  @Test
  void should_pass_if_all_elements_are_equal() {
    // GIVEN
    List<String> actual = newArrayList("Yoda", "Yoda", "Yoda");
    // WHEN/THEN
    iterables.assertAllElementsAreEqual(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_only_one_element() {
    // GIVEN
    List<String> actual = newArrayList("Yoda");
    // WHEN/THEN
    iterables.assertAllElementsAreEqual(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_is_empty() {
    // WHEN/THEN
    iterables.assertAllElementsAreEqual(someInfo(), emptyList());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertAllElementsAreEqual(someInfo(), null));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_and_report_all_elements_that_are_not_equal_to_the_first() {
    // GIVEN
    AssertionInfo info = someInfo();
    List<String> actual = newArrayList("Yoda", "Yoda", "Luke", "Yoda", "Leia");
    // WHEN
    expectAssertionError(() -> iterables.assertAllElementsAreEqual(info, actual));
    // THEN
    verify(failures).failure(info, shouldHaveAllElementsEqual(actual, "Yoda", newArrayList("Luke", "Leia"),
                                                              StandardComparisonStrategy.instance()));
  }

  @Test
  void should_produce_a_readable_error_message() {
    // GIVEN
    List<String> actual = newArrayList("a", "b");
    // WHEN
    AssertionError error = expectAssertionError(() -> iterables.assertAllElementsAreEqual(someInfo(), actual));
    // THEN
    then(error).hasMessageContaining("Expecting all elements to be equal to:")
               .hasMessageContaining("\"a\"")
               .hasMessageContaining("but the following element(s) were not:")
               .hasMessageContaining("[\"b\"]");
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_all_elements_are_equal_according_to_custom_comparison_strategy() {
    // GIVEN
    List<String> actual = newArrayList("Yoda", "YODA", "yoda");
    // WHEN/THEN
    iterablesWithCaseInsensitiveComparisonStrategy.assertAllElementsAreEqual(someInfo(), actual);
  }

  @Test
  void should_fail_if_elements_are_not_equal_according_to_custom_comparison_strategy() {
    // GIVEN
    AssertionInfo info = someInfo();
    List<String> actual = newArrayList("Yoda", "YODA", "Luke");
    // WHEN
    expectAssertionError(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertAllElementsAreEqual(info, actual));
    // THEN
    verify(failures).failure(info, shouldHaveAllElementsEqual(actual, "Yoda", newArrayList("Luke"), comparisonStrategy));
  }

}
