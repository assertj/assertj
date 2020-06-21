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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.int2darrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Int2DArrays;
import org.assertj.core.internal.Int2DArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Int2DArrays#assertHasDimensions(AssertionInfo, int[][], int, int)}</code>.
 *
 * @author Maciej Wajcht
 */
public class Int2DArrays_assertHasSize_Test extends Int2DArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    int[][] actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertHasDimensions(someInfo(), actual, 2, 3));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  public void should_fail_if_first_dimension_size_of_actual_is_not_equal_to_expected_size() {
    // GIVEN
    int expectedFirstDimensionSize = 10;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertHasDimensions(someInfo(), actual,
                                                                                    expectedFirstDimensionSize, 3));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(actual, actual.length, expectedFirstDimensionSize).create());
  }

  @Test
  public void should_fail_if_second_dimension_size_of_actual_is_not_equal_to_expected_size() {
    // GIVEN
    int expectedSecondDimensionSize = 10;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> arrays.assertHasDimensions(someInfo(), actual, 2,
                                                                                    expectedSecondDimensionSize));
    // THEN
    then(assertionError).hasMessage(shouldHaveSize(new int[] { 0, 2, 4 }, 3, expectedSecondDimensionSize, 0).create());
  }

  @Test
  public void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasDimensions(someInfo(), actual, 2, 3);
  }
}
