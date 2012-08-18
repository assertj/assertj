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
import static org.fest.util.ErrorMessages.iterableToLookForIsNull;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.util.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Collections.*;

import static org.mockito.Mockito.verify;

import java.util.List;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesBaseTest;

/**
 * Tests for <code>{@link Iterables#assertContainsAll(AssertionInfo, Iterable, Iterable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Iterables_assertContainsAll_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_contains_all_iterable_values() {
    iterables.assertContainsAll(someInfo(), actual, list("Luke"));
    // order does not matter
    iterables.assertContainsAll(someInfo(), actual, list("Leia", "Yoda"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once() {
    actual.addAll(list("Luke", "Luke"));
    iterables.assertContainsAll(someInfo(), actual, list("Luke"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated() {
    iterables.assertContainsAll(someInfo(), actual, list("Luke", "Luke"));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(iterableToLookForIsNull());
    iterables.assertContainsAll(someInfo(), actual, null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsAll(someInfo(), null, list("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values() {
    AssertionInfo info = someInfo();
    List<String> expected = list("Han", "Luke");
    try {
      iterables.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected.toArray(), set("Han")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, list("LUKE"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_in_different_order_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, list("LEIA", "yODa"));
  }

  @Test
  public void should_pass_if_actual_contains_all_all_iterable_values_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, list("luke", "YODA"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_more_than_once_according_to_custom_comparison_strategy() {
    actual.addAll(list("Luke", "Luke"));
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, list("LUke"));
  }

  @Test
  public void should_pass_if_actual_contains_all_iterable_values_even_if_duplicated_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(someInfo(), actual, list("LUke", "LuKe"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_values_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    List<String> expected = list("Han", "LUKE");
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsAll(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContain(actual, expected.toArray(), set("Han"), comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
