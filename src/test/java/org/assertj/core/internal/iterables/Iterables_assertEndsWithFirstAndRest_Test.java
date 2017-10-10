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

import static org.assertj.core.error.ShouldEndWith.shouldEndWith;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.prepend;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for <code>{@link Iterables#assertEndsWith(AssertionInfo, Object[], Object, Object[])}</code>.
 */
public class Iterables_assertEndsWithFirstAndRest_Test extends IterablesBaseTest {

  @Override
  @Before
  public void setUp() {
    super.setUp();
    actual = newArrayList("Yoda", "Luke", "Leia", "Obi-Wan");
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException();
    iterables.assertEndsWith(someInfo(), actual, "Luke", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    iterables.assertEndsWith(someInfo(), null, "Luke", array("Yoda"));
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Yoda", "Luke", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      iterables.assertEndsWith(info, actual, "Luke", sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, "Luke", sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_end_with_sequence() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      iterables.assertEndsWith(info, actual, "Obi-Wan", sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, "Obi-Wan", sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_ends_with_first_elements_of_sequence_only() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    try {
      iterables.assertEndsWith(info, actual, "Luke", sequence);
    } catch (AssertionError e) {
      verifyFailureThrownWhenSequenceNotFound(info, "Luke", sequence);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenSequenceNotFound(AssertionInfo info, Object first, Object[] sequence) {
    verify(failures).failure(info, shouldEndWith(actual, prepend(first, sequence)));
  }

  @Test
  public void should_pass_if_actual_ends_with_first_then_sequence() {
    iterables.assertEndsWith(someInfo(), actual, "Luke", array("Leia", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_and_first_then_sequence_are_equal() {
    iterables.assertEndsWith(someInfo(), actual, "Yoda", array("Luke", "Leia", "Obi-Wan"));
  }

  @Test
  public void should_fail_if_sequence_is_bigger_than_actual_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "LUKE", "Leia", "Obi-Wan", "Han", "C-3PO", "R2-D2", "Anakin" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertEndsWith(info, actual, "Yoda", sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, prepend("Yoda", sequence),
                                                   comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_end_with_sequence_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Han", "C-3PO" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertEndsWith(info, actual, "Yoda", sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, prepend("Yoda", sequence),
                                                   comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_ends_with_first_elements_of_sequence_only_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    Object[] sequence = { "Leia", "Obi-Wan", "Han" };
    try {
      iterablesWithCaseInsensitiveComparisonStrategy.assertEndsWith(info, actual, "Luke", sequence);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldEndWith(actual, prepend("Luke", sequence),
                                                   comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_actual_ends_with_sequence_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertEndsWith(someInfo(), actual, "LUKE", array("Leia", "Obi-Wan"));
  }

  @Test
  public void should_pass_if_actual_and_sequence_are_equal_according_to_custom_comparison_strategy() {
    iterablesWithCaseInsensitiveComparisonStrategy.assertEndsWith(someInfo(), actual, "Yoda",
                                                                  array("LUKE", "Leia", "Obi-Wan"));
  }
}
