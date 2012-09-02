/*
 * Created on Oct 3, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.iterables;

import static java.util.Collections.emptyList;

import static org.fest.assertions.error.ShouldContainExactly.shouldContainExactly;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.ErrorMessages.valuesToLookForIsEmpty;
import static org.fest.test.ErrorMessages.valuesToLookForIsNull;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.Arrays.array;
import static org.fest.util.FailureMessages.actualIsNull;
import static org.fest.util.ObjectArrayFactory.emptyArray;
import static org.fest.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Iterables;
import org.fest.assertions.internal.IterablesBaseTest;

/**
 * Tests for <code>{@link Iterables#assertContainsExactly(AssertionInfo, Iterable, Object[])}</code>.
 * 
 * @author Joel Costigliola
 */
public class Iterables_assertContainsExactly_Test extends IterablesBaseTest {

  @Test
  public void should_pass_if_actual_contains_exactly_given_values() {
    iterables.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_with_null_elements() {
    iterables.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia"));
    actual.add(null);
    iterables.assertContainsExactly(someInfo(), actual, array("Luke", "Yoda", "Leia", null));
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_empty() {
    thrown.expectIllegalArgumentException(valuesToLookForIsEmpty());
    iterables.assertContainsExactly(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_throw_error_if_array_of_values_to_look_for_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    iterables.assertContainsExactly(someInfo(), emptyList(), null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsExactly(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };
    try {
      iterables.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactly(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet("Leia")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_in_different_order() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Leia", "Yoda" };
    try {
      iterables.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly("Yoda", "Leia", 1));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(someInfo(), actual,
        array("LUKE", "YODA", "Leia"));
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Yoda", "Han" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures)
          .failure(
              info,
              shouldContainExactly(actual, expected, newLinkedHashSet("Han"), newLinkedHashSet("Leia"),
                  comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_in_different_order_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] expected = { "Luke", "Leia", "Yoda" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsExactly(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactly("Yoda", "Leia", 1, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
