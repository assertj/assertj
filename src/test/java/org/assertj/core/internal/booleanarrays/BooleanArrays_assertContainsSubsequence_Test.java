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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.booleanarrays;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.BooleanArrays.arrayOf;
import static org.assertj.core.test.BooleanArrays.emptyArray;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

/**
 * Tests for <code>{@link BooleanArrays#assertContainsSubsequence(AssertionInfo, boolean[], boolean[])}</code>.
 *
 * @author Natália Struharová
 */
public class BooleanArrays_assertContainsSubsequence_Test extends BooleanArraysBaseTest {

  @Override
  @BeforeEach
  public void setUp() {
    super.setUp();
    actual = arrayOf(true, false, false, true);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertContainsSubsequence(someInfo(), null,
                                                                                                      arrayOf(true)))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_throw_error_if_subsequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertContainsSubsequence(someInfo(),
                                                                                       actual, null))
                                    .withMessage(valuesToLookForIsNull());
  }

  @Test
  void should_pass_if_actual_and_given_values_are_empty() {
    // GIVEN
    actual = emptyArray();

    // WHEN/THEN
    arrays.assertContainsSequence(someInfo(), actual, emptyArray());
  }

  @Test
  void should_pass_if_actual_contains_given_subsequence() {
    // GIVEN
    boolean[] subsequence = arrayOf(true, false);

    // WHEN/THEN
    arrays.assertContainsSequence(someInfo(), actual, subsequence);
  }

}
