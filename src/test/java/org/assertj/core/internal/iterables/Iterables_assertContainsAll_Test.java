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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.iterableToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsAll(AssertionInfo, Iterable, Iterable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Iterables_assertContainsAll_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_contains_all_iterable_values() {
    iterables.assertContainsAll(someInfo(), actual, newArrayList("Luke"));
    // order does not matter
    iterables.assertContainsAll(someInfo(), actual, newArrayList("Leia", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterables.assertContainsAll(someInfo(), actual, newArrayList("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated() {
    iterables.assertContainsAll(someInfo(), actual, newArrayList("Luke", "Luke"));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    assertThatNullPointerException().isThrownBy(() -> iterables.assertContainsAll(someInfo(), actual, null))
                                    .withMessage(iterableToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> iterables.assertContainsAll(someInfo(), null, newArrayList("Yoda")))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    List<String> expected = newArrayList("Han", "Luke");
    try {
      iterables.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected.toArray(), newLinkedHashSet("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_in_different_order_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LEIA", "yODa"));
  }

  @Test
  public void should_pass_if_actual_contains_all_all_iterable_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("luke", "YODA"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once_according_to_custom_comparison_strategy() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LUke"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, newArrayList("LUke", "LuKe"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    List<String> expected = newArrayList("Han", "LUKE");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected.toArray(), newLinkedHashSet("Han"), comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
