/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static java.util.Collections.emptyList;
import static org.assertj.core.error.ShouldContainsOnlyOnce.shouldContainsOnlyOnce;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.mockito.Mockito.verify;

import java.awt.Rectangle;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link Iterables#assertContainsOnlyOnce(org.assertj.core.api.AssertionInfo, Iterable, Object[])}</code>.
 * 
 * @author William Delanoue
 */
public class Iterables_assertContainsOnlyOnce_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_only_once() {
    iterables.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_even_if_actual_type_is_not_comparable() {
    // Rectangle class does not implement Comparable
    Rectangle r1 = new Rectangle(1, 1);
    Rectangle r2 = new Rectangle(2, 2);
    iterables.assertContainsOnlyOnce(someInfo(), newArrayList(r1, r2, r2), array(r1));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_with_null_element() {
    actual.add(null);
    iterables.assertContainsOnlyOnce(someInfo(), actual, array("Luke", null, "Yoda", "Leia", null));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_in_different_order() {
    iterables.assertContainsOnlyOnce(someInfo(), actual, array("Leia", "Yoda", "Luke"));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_more_than_once() {
    AssertionInfo info = someInfo();
    actual.addAll(newArrayList("Luke", "Luke", null, null));
    Object[] expected = { "Luke", "Luke", "Yoda", "Han", null };
    try {
      iterables.assertContainsOnlyOnce(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet("Luke", null)));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contains_null_value() {
    AssertionInfo info = someInfo();
    actual.addAll(newArrayList("Luke", "Luke"));
    Object[] expected = { null };
    try {
      iterables.assertContainsOnlyOnce(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet(array((String) null)), newLinkedHashSet()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_even_if_duplicated() {
    iterables.assertContainsOnlyOnce(someInfo(), actual, array("Luke", "Luke", "Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContainsOnlyOnce(someInfo(), actual, array());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    iterables.assertContainsOnlyOnce(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    iterables.assertContainsOnlyOnce(someInfo(), emptyList(), null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsOnlyOnce(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only_once() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };
    try {
      iterables.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual,
        array("LUKE", "YODA", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_only_once_in_different_order_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual,
        array("LEIA", "yoda", "LukE"));
  }

  @Test
  public void should_fail_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual.addAll(newArrayList("Luke", "Luke"));
    Object[] expected = array("luke", "YOda", "LeIA");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet(), newLinkedHashSet("luke"), comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();

  }

  @Test
  public void should_fail_if_actual_contains_given_values_more_than_once_even_if_duplicated_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    actual.addAll(newArrayList("LUKE"));
    Object[] expected = array("LUke", "LUke", "lukE", "YOda", "Leia", "Han");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet("LUke", "lukE"),
              comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainsOnlyOnce(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet(), comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
