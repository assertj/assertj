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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeSubsetOf.shouldBeSubsetOf;
import static org.assertj.core.internal.ErrorMessages.iterableToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertIsSubsetOf(AssertionInfo, Collection, Object[])}</code>.
 * 
 * @author Maciej Jaskowski
 */
class Iterables_assertIsSubsetOf_Test extends IterablesBaseTest {

  @Test
  void should_pass_if_actual_is_subset_of_set() {
    actual = newArrayList("Yoda", "Luke");
    iterables.assertIsSubsetOf(someInfo(), actual, newArrayList("Luke", "Yoda", "Obi-Wan"));
  }

  @Test
  void should_pass_if_actual_has_the_same_elements_as_set() {
    actual = newArrayList("Yoda", "Luke");
    iterables.assertIsSubsetOf(someInfo(), actual, newArrayList("Luke", "Yoda"));
  }

  @Test
  void should_pass_if_actual_is_empty() {
    actual = newArrayList();
    iterables.assertIsSubsetOf(someInfo(), actual, newArrayList("Luke", "Yoda"));
  }

  @Test
  void should_pass_if_actual_and_set_are_both_empty() {
    actual = newArrayList();
    iterables.assertIsSubsetOf(someInfo(), actual, newArrayList());
  }

  @Test
  void should_pass_if_actual_has_duplicates_but_all_elements_are_in_values() {
    actual = newArrayList("Yoda", "Yoda");
    iterables.assertIsSubsetOf(someInfo(), actual, newArrayList("Yoda"));
  }

  @Test
  void should_pass_if_values_has_duplicates_but_all_elements_are_in_values() {
    actual = newArrayList("Yoda", "C-3PO");
    iterables.assertIsSubsetOf(someInfo(), actual, newArrayList("Yoda", "Yoda", "C-3PO"));
  }

  @Test
  void should_pass_if_both_actual_and_values_have_duplicates_but_all_elements_are_in_values() {
    actual = newArrayList("Yoda", "Yoda", "Yoda", "C-3PO", "Obi-Wan");
    iterables.assertIsSubsetOf(someInfo(), actual, newArrayList("Yoda", "Yoda", "C-3PO", "C-3PO", "Obi-Wan"));
  }

  @Test
  void should_throw_error_if_set_is_null() {
    actual = newArrayList("Yoda", "Luke");
    assertThatNullPointerException().isThrownBy(() -> iterables.assertIsSubsetOf(someInfo(), actual, null))
                                    .withMessage(iterableToLookForIsNull());
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    actual = null;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertIsSubsetOf(someInfo(), actual,
                                                                                                newArrayList()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_subset_of_values() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Yoda");
    List<String> values = newArrayList("C-3PO", "Leila");
    List<String> extra = newArrayList("Yoda");

    Throwable error = catchThrowable(() -> iterables.assertIsSubsetOf(info, actual, values));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeSubsetOf(actual, values, extra));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  void should_pass_if_actual_is_subset_of_values_according_to_custom_comparison_strategy() {
    actual = newArrayList("Yoda", "Luke");
    iterablesWithCaseInsensitiveComparisonStrategy.assertIsSubsetOf(someInfo(), actual, newArrayList("yoda", "lUKE"));
  }

  @Test
  void should_pass_if_actual_contains_duplicates_according_to_custom_comparison_strategy() {
    actual = newArrayList("Luke", "Luke");
    iterablesWithCaseInsensitiveComparisonStrategy.assertIsSubsetOf(someInfo(), actual, newArrayList("LUke", "yoda"));
  }

  @Test
  void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    actual = newArrayList("Yoda", "Luke");
    iterablesWithCaseInsensitiveComparisonStrategy.assertIsSubsetOf(someInfo(), actual, newArrayList("LUke", "LuKe", "yoda"));
  }

  @Test
  void should_fail_if_actual_is_not_subset_of_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual = newArrayList("Yoda", "Luke");
    List<String> values = newArrayList("yoda", "C-3PO");
    List<String> extra = newArrayList("Luke");

    Throwable error = catchThrowable(() -> iterablesWithCaseInsensitiveComparisonStrategy.assertIsSubsetOf(info, actual, values));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeSubsetOf(actual, values, extra, comparisonStrategy));
  }

}
