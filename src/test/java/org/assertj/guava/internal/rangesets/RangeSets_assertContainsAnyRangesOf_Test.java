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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.guava.internal.rangesets;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.IterableUtil.iterable;

import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

class RangeSets_assertContainsAnyRangesOf_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_actual_contains_all_values() {
    rangeSets.assertContainsAnyRangesOf(someInfo(), actual, iterable(1, 15, 32));
    // Order does not matter
    rangeSets.assertContainsAnyRangesOf(someInfo(), actual, iterable(15, 1, 32));
  }

  @Test
  void should_pass_if_actual_contains_any_value() {
    rangeSets.assertContainsAnyRangesOf(someInfo(), actual, iterable(-100, 15, 37, 45));
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_null() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertContainsAnyRangesOf(someInfo(), actual, null))
                                        .withMessage(iterableValuesToLookForIsNull());
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertContainsAnyRangesOf(someInfo(),
                                                                                                         null,
                                                                                                         iterable()))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_iterable_of_values_to_look_for_is_empty() {
    assertThatIllegalArgumentException().isThrownBy(() -> rangeSets.assertContainsAnyRangesOf(someInfo(), actual, iterable()))
                                        .withMessage(iterableValuesToLookForIsEmpty());
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_empty() {
    actual.clear();
    rangeSets.assertContainsAnyRangesOf(someInfo(), actual, iterable());
  }

  @Test
  void should_fail_if_actual_does_not_contain_value() {
    Iterable<Integer> expected = iterable(100, 200);
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertContainsAnyRangesOf(someInfo(),
                                                                                                         actual,
                                                                                                         expected))
                                                   .withMessage(shouldContainAnyOf(actual, expected).create());
  }
}
