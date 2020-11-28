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
package org.assertj.core.api.double2darray;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.SubarraysShouldHaveSameSize.subarraysShouldHaveSameSize;
import static org.assertj.core.error.array2d.Array2dElementShouldBeDeepEqual.elementShouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.Double2DArrayAssert;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Double2DArrayAssert#isDeepEqualTo(double[][])}</code>.
 *
 * @author Maciej Wajcht
 */
@DisplayName("Double2DArrayAssert isDeepEqualTo")
class Double2DArrayAssert_isDeepEqualTo_Test {

  @Test
  void should_pass_if_both_actual_and_expected_are_null() {
    // GIVEN
    double[][] actual = null;
    double[][] expected = null;
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_same_arrays() {
    // GIVEN
    double[][] actual = new double[][] { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
    double[][] expected = actual;
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_equal() {
    // GIVEN
    double[][] actual = new double[][] { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
    double[][] expected = new double[][] { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    double[][] actual = null;
    double[][] expected = new double[][] { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_in_second_dimension_is_null() {
    // GIVEN
    double[][] actual = new double[][] { { 1.0, 2.0 }, null, { 4.0, 5.0, 6.0 } };
    double[][] expected = new double[][] { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull("actual[1]").create());
  }

  @Test
  void should_fail_if_first_dimension_size_is_different() {
    // GIVEN
    double[][] actual = new double[][] { { 1.0, 2.0 }, { 3.0 } };
    double[][] expected = new double[][] { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, expected, actual.length, expected.length).create());
  }

  @Test
  void should_fail_if_second_dimension_size_is_different() {
    // GIVEN
    double[][] actual = new double[][] { { 1.0, 2.0 }, { 3.0, 999.0 }, { 4.0, 5.0, 6.0 } };
    double[][] expected = new double[][] { { 1.0, 2.0 }, { 3.0 }, { 4.0, 5.0, 6.0 } };
    double[] actualSubArrayWithDifference = new double[] { 3.0, 999.0 };
    double[] expectedSubArrayWithDifference = new double[] { 3.0 };
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
    double actualValue = 999.0;
    double expectedValue = 3.0;
    double[][] actual = new double[][] { { 1.0, 2.0 }, { actualValue }, { 4.0, 5.0, 6.0 } };
    double[][] expected = new double[][] { { 1.0, 2.0 }, { expectedValue }, { 4.0, 5.0, 6.0 } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    ErrorMessageFactory elementShouldBeEqual = elementShouldBeEqual(actualValue, expectedValue, 1, 0);
    then(assertionError).hasMessage(elementShouldBeEqual.create(emptyDescription(), STANDARD_REPRESENTATION));

  }
}
