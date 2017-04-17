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

import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertContainsSubsequence(AssertionInfo, Collection, Object[])}</code>.
 * 
 * @author Marcin Mikosik
 */
public class Iterables_assertContainsSubsequence_Test extends IterablesBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = newArrayList("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_throw_error_if_subsequence_is_null() {
    thrown.expectNullPointerException(valuesToLookForIsNull());
    Object[] nullArray = null;
    iterables.assertContainsSubsequence(someInfo(), actual, nullArray);
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    actual.clear();
    iterables.assertContainsSubsequence(someInfo(), actual, array());
  }

  @Test
  public void should_fail_if_array_of_values_to_look_for_is_empty_and_actual_is_not() {
    thrown.expectAssertionError();
    iterables.assertContainsSubsequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertContainsSubsequence(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_fail_if_subsequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      iterables.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSubsequenceNotFound(info, subsequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_whole_subsequence() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Han", "C-3PO" };
    try {
      iterables.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSubsequenceNotFound(info, subsequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Luke", "Leia", "Han" };
    try {
      iterables.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSubsequenceNotFound(info, subsequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenSubsequenceNotFound(AssertionInfo info, Object[] subsequence) {
    verify(failures).failure(info, shouldContainSubsequence(actual, subsequence));
  }

  @Test
  public void should_pass_if_actual_contains_subsequence_without_elements_between() {
    iterables.assertContainsSubsequence(someInfo(), actual, array("Luke", "Leia"));
  }

  @Test
  public void should_pass_if_actual_contains_subsequence_with_elements_between() {
    iterables.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Leia"));
  }

  @Test
  public void should_pass_if_actual_and_subsequence_are_equal() {
    iterables.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Luke", "Leia", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_contains_both_partial_and_complete_subsequence() {
    actual = newArrayList("Yoda", "Luke", "Yoda", "Obi-Wan");
    iterables.assertContainsSubsequence(someInfo(), actual, array("Yoda", "Obi-Wan"));
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_fail_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Han", "C-3PO" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSubsequence(actual, subsequence, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] subsequence = { "Luke", "Leia", "Han" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(info, actual, subsequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainSubsequence(actual, subsequence, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), actual, array("yODa", "leia"));
  }

  @Test
  public void should_pass_if_actual_and_subsequence_are_equal_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertContainsSubsequence(someInfo(), actual,
        array("YODA", "luke", "lEIA", "Obi-wan"));
  }

}
