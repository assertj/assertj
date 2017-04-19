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

import static org.assertj.core.error.ShouldNotContainSubsequence.shouldNotContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.emptySubsequence;
import static org.assertj.core.internal.ErrorMessages.nullSubsequence;
import static org.assertj.core.test.ObjectArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertDoesNotContainSubsequence(AssertionInfo, Iterable, Object[])} </code>.
 * 
 * @author Marcin Mikosik
 */
public class Iterables_assertDoesNotContainSubsequence_Test extends IterablesBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = newArrayList("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_throw_error_if_subsequence_is_null() {
    thrown.expectNullPointerException(nullSubsequence());
    iterables.assertDoesNotContainSubsequence(someInfo(), actual, null);
  }

  @Test
  public void should_throw_error_if_subsequence_is_empty() {
    thrown.expectIllegalArgumentException(emptySubsequence());
    iterables.assertDoesNotContainSubsequence(someInfo(), actual, emptyArray());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertDoesNotContainSubsequence(someInfo(), null, array("Yoda"));
  }

  @Test
  public void should_pass_if_subsequence_is_bigger_than_actual() {
    Object[] subsequence = { "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    iterables.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  public void should_pass_if_actual_does_not_contain_whole_subsequence() {
    Object[] subsequence = { "Han", "C-3PO" };
    iterables.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  public void should_pass_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence() {
    Object[] subsequence = { "Luke", "Leia", "Han" };
    iterables.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  public void should_fail_if_actual_contains_subsequence_without_elements_between() {
    Object[] subsequence = array("Luke", "Leia");
    expectFailure(iterables, actual, subsequence, 1);
  }

  @Test
  public void should_fail_if_actual_contains_subsequence_with_elements_between() {
    Object[] subsequence = array("Yoda", "Leia");
    expectFailure(iterables, actual, subsequence, 0);
  }

  @Test
  public void should_fail_if_actual_and_subsequence_are_equal() {
    Object[] subsequence = array("Yoda", "Luke", "Leia", "Obi-Wan");
    expectFailure(iterables, actual, subsequence, 0);
  }

  @Test
  public void should_fail_if_actual_contains_both_partial_and_complete_subsequence() {
    actual = newArrayList("Yoda", "Luke", "Yoda", "Obi-Wan");
    Object[] subsequence = array("Yoda", "Obi-Wan");
    expectFailure(iterables, actual, subsequence, 0);
  }

  // ------------------------------------------------------------------------------------------------------------------
  // tests using a custom comparison strategy
  // ------------------------------------------------------------------------------------------------------------------

  @Test
  public void should_pass_if_actual_does_not_contain_whole_subsequence_according_to_custom_comparison_strategy() {
    Object[] subsequence = { "Han", "C-3PO" };
    iterables.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  public void should_pass_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence_according_to_custom_comparison_strategy() {
    Object[] subsequence = { "Luke", "LEIA", "Han" };
    iterablesWithCaseInsensitiveComparisonStrategy.assertDoesNotContainSubsequence(someInfo(), actual, subsequence);
  }

  @Test
  public void should_fail_if_actual_contains_subsequence_according_to_custom_comparison_strategy() {
    Object[] subsequence = array("yODa", "leia");
    expectFailure(iterablesWithCaseInsensitiveComparisonStrategy, actual, subsequence, 0);
  }

  @Test
  public void should_fail_if_actual_and_subsequence_are_equal_according_to_custom_comparison_strategy() {
    Object[] subsequence = array("YODA", "luke", "lEIA", "Obi-wan");
    expectFailure(iterablesWithCaseInsensitiveComparisonStrategy, actual, subsequence, 0);
  }

  private void expectFailure(Iterables iterables, Iterable<String> sequence, Object[] subsequence, int index) {
    AssertionInfo info = someInfo();
    try {
      iterables.assertDoesNotContainSubsequence(info, sequence, subsequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldNotContainSubsequence(actual, subsequence, iterables.getComparisonStrategy(),
                                                                 index));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
