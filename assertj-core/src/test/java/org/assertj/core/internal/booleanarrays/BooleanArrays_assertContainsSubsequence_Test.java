/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.booleanarrays;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainSubsequence.shouldContainSubsequence;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.testkit.BooleanArrays.arrayOf;
import static org.assertj.core.testkit.BooleanArrays.emptyArray;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BooleanArrays#assertContainsSubsequence(AssertionInfo, boolean[], boolean[])}</code>.
 *
 * @author Natália Struharová
 */
class BooleanArrays_assertContainsSubsequence_Test extends BooleanArraysBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = arrayOf(true, false, false, true);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, null, arrayOf(true)));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsSubsequence(INFO, actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    // GIVEN
    actual = emptyArray();
    // WHEN/THEN
    arrays.assertContainsSubsequence(INFO, actual, emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_given_subsequence() {
    // GIVEN
    boolean[] subsequence = arrayOf(true, false);
    // WHEN/THEN
    arrays.assertContainsSubsequence(INFO, actual, subsequence);
  }

  @Test
  void should_fail_if_actual_contains_first_elements_of_subsequence_but_not_whole_subsequence() {
    // GIVEN
    actual = arrayOf(false, false);
    boolean[] subsequence = { false, true };
    // WHEN
    expectAssertionError(() -> arrays.assertContainsSubsequence(INFO, actual, subsequence));
    // THEN
    verify(failures).failure(INFO, shouldContainSubsequence(actual, subsequence, 1, StandardComparisonStrategy.instance()));
  }
}
