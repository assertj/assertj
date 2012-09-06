/*
 * Created on Sep 30, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.iterables;

import static org.fest.assertions.error.ShouldContain.shouldContain;
import static org.fest.assertions.test.ErrorMessages.*;
import static org.fest.assertions.test.ObjectArrays.emptyArray;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.Sets.newLinkedHashSet;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertContains(AssertionInfo, Collection, Object[])}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Iterables_assertContains_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values() {
    iterables.assertContains(someInfo(), actual, array("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order() {
    iterables.assertContains(someInfo(), actual, array("Leia", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values() {
    iterables.assertContains(someInfo(), actual, array("Luke", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterables.assertContains(someInfo(), actual, array("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated() {
    iterables.assertContains(someInfo(), actual, array("Luke", "Luke"));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    iterables.assertContains(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    iterables.assertContains(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContains(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "Luke" };
    try {
      iterables.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_in_different_order_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LEIA", "yODa"));
  }

  @Test
  public void should_pass_if_actual_contains_all_given_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("luke", "YODA"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_more_than_once_according_to_custom_comparison_strategy() {
    actual.addAll(newArrayList("Luke", "Luke"));
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LUke"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContains(someInfo(), actual, array("LUke", "LuKe"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Han", "Luke" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContains(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected, newLinkedHashSet("Han"), comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
