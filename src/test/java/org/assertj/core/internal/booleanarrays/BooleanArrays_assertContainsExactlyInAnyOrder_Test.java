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
package org.assertj.core.internal.booleanarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainExactlyInAnyOrder.shouldContainExactlyInAnyOrder;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.assertj.core.test.BooleanArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.emptyList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BooleanArrays#assertContainsExactlyInAnyOrder(AssertionInfo, boolean[], boolean[])}</code>.
 */
public class BooleanArrays_assertContainsExactlyInAnyOrder_Test extends BooleanArraysBaseTest {

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_in_any_order() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(true, false));
  }

  @Test
  public void should_pass_if_actual_and_given_values_are_empty() {
    arrays.assertContainsExactlyInAnyOrder(someInfo(), emptyArray(), emptyArray());
  }

  @Test
  public void should_pass_if_actual_contains_given_values_exactly_but_in_different_order() {
    AssertionInfo info = someInfo();
    arrays.assertContainsExactlyInAnyOrder(info, actual, arrayOf(false, true));
  }

  @Test
  public void should_fail_if_arrays_have_different_sizes() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, arrayOf(true)));
  }

  @Test
  public void should_fail_if_expected_is_empty_and_actual_is_not() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), actual, emptyArray()));
  }

  @Test
  public void should_throw_error_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(),
                                                                                             actual,
                                                                                             null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsExactlyInAnyOrder(someInfo(), null, arrayOf(true)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_values_exactly() {
    AssertionInfo info = someInfo();
    boolean[] expected = {true, true};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainExactlyInAnyOrder(actual, expected,
          newArrayList(true), newArrayList(false), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_all_given_values_but_size_differ() {
    AssertionInfo info = someInfo();
    boolean[] expected = {true};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(false),
              StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_duplicates_and_expected_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(true, false, true);
    boolean[] expected = {true, false};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, emptyList(), newArrayList(true), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_expected_contains_duplicates_and_actual_does_not() {
    AssertionInfo info = someInfo();
    actual = arrayOf(true, false);
    boolean[] expected = {true, false, true};
    try {
      arrays.assertContainsExactlyInAnyOrder(info, actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info,
          shouldContainExactlyInAnyOrder(actual, expected, newArrayList(true), emptyList(), StandardComparisonStrategy.instance()));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
