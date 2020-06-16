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
package org.assertj.core.api.boolean2darray;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.SubarraysShouldHaveSameSize.subarraysShouldHaveSameSize;
import static org.assertj.core.error.array2d.Array2dElementShouldBeDeepEqual.elementShouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.Boolean2DArrayAssert;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Boolean2DArrayAssert#isDeepEqualTo(boolean[][])}</code>.
 *
 * @author Maciej Wajcht
 */
@DisplayName("Boolean2DArrayAssert isDeepEqualTo")
class Boolean2DArrayAssert_isDeepEqualTo_Test {

  @Test
  void should_pass_if_both_actual_and_expected_are_null() {
    // GIVEN
    boolean[][] actual = null;
    boolean[][] expected = null;
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_same_arrays() {
    // GIVEN
    boolean[][] actual = new boolean[][] { { true, false }, { true }, { true, false, true } };
    boolean[][] expected = actual;
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_equal() {
    // GIVEN
    boolean[][] actual = new boolean[][] { { true, false }, { true } };
    boolean[][] expected = new boolean[][] { { true, false }, { true } };
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    boolean[][] actual = null;
    boolean[][] expected = new boolean[][] { { true, false }, { true } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_in_second_dimension_is_null() {
    // GIVEN
    boolean[][] actual = new boolean[][] { { true, false }, null, { false, true } };
    boolean[][] expected = new boolean[][] { { true, false }, { true }, { false, true } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull("actual[1]").create());
  }

  @Test
  void should_fail_if_first_dimension_size_is_different() {
    // GIVEN
    boolean[][] actual = new boolean[][] { { true, false }, { true } };
    boolean[][] expected = new boolean[][] { { true, false }, { true }, { false, true } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, expected, actual.length, expected.length).create());
  }

  @Test
  void should_fail_if_second_dimension_size_is_different() {
    // GIVEN
    boolean[][] actual = new boolean[][] { { true, false }, { false, true }, { true, false, true } };
    boolean[][] expected = new boolean[][] { { true, false }, { false }, { true, false, true } };
    boolean[] actualSubArrayWithDifference = new boolean[] { false, true };
    boolean[] expectedSubArrayWithDifference = new boolean[] { false };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    ErrorMessageFactory subarraysShouldHaveSameSize = subarraysShouldHaveSameSize(actual, expected,
                                                                                  actualSubArrayWithDifference,
                                                                                  actualSubArrayWithDifference.length,
                                                                                  expectedSubArrayWithDifference,
                                                                                  expectedSubArrayWithDifference.length,
                                                                                  1);
    then(assertionError).hasMessage(subarraysShouldHaveSameSize.create());

  }

  @Test
  void should_fail_if_one_value_in_second_dimension_is_different() {
    // GIVEN
    boolean actualValue = true;
    boolean expectedValue = false;
    boolean[][] actual = new boolean[][] { { true, false }, { actualValue }, { false, true, false } };
    boolean[][] expected = new boolean[][] { { true, false }, { expectedValue }, { false, true, false } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    ErrorMessageFactory elementShouldBeEqual = elementShouldBeEqual(actualValue, expectedValue, 1, 0);
    then(assertionError).hasMessage(elementShouldBeEqual.create(emptyDescription(), STANDARD_REPRESENTATION));
  }
}
