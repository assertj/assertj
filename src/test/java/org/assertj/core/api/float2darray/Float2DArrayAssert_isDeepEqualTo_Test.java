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
package org.assertj.core.api.float2darray;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.SubarraysShouldHaveSameSize.subarraysShouldHaveSameSize;
import static org.assertj.core.error.array2d.Array2dElementShouldBeDeepEqual.elementShouldBeEqual;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.Float2DArrayAssert;
import org.assertj.core.error.ErrorMessageFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Float2DArrayAssert#isDeepEqualTo(float[][])}</code>.
 *
 * @author Maciej Wajcht
 */
@DisplayName("Float2DArrayAssert isDeepEqualTo")
class Float2DArrayAssert_isDeepEqualTo_Test {

  @Test
  void should_pass_if_both_actual_and_expected_are_null() {
    // GIVEN
    float[][] actual = null;
    float[][] expected = null;
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_same_arrays() {
    // GIVEN
    float[][] actual = new float[][] { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
    float[][] expected = actual;
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_pass_if_both_actual_and_expected_are_equal() {
    // GIVEN
    float[][] actual = new float[][] { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
    float[][] expected = new float[][] { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
    // WHEN/THEN
    then(actual).isDeepEqualTo(expected);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    float[][] actual = null;
    float[][] expected = new float[][] { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_in_second_dimension_is_null() {
    // GIVEN
    float[][] actual = new float[][] { { 1.0f, 2.0f }, null, { 4.0f, 5.0f, 6.0f } };
    float[][] expected = new float[][] { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull("actual[1]").create());
  }

  @Test
  void should_fail_if_first_dimension_size_is_different() {
    // GIVEN
    float[][] actual = new float[][] { { 1.0f, 2.0f }, { 3.0f } };
    float[][] expected = new float[][] { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveSameSizeAs(actual, expected, actual.length, expected.length).create());
  }

  @Test
  void should_fail_if_second_dimension_size_is_different() {
    // GIVEN
    float[][] actual = new float[][] { { 1.0f, 2.0f }, { 3.0f, 999.0f }, { 4.0f, 5.0f, 6.0f } };
    float[][] expected = new float[][] { { 1.0f, 2.0f }, { 3.0f }, { 4.0f, 5.0f, 6.0f } };
    float[] actualSubArrayWithDifference = new float[] { 3.0f, 999.0f };
    float[] expectedSubArrayWithDifference = new float[] { 3.0f };
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
    float actualValue = 999.0f;
    float expectedValue = 3.0f;
    float[][] actual = new float[][] { { 1.0f, 2.0f }, { actualValue }, { 4.0f, 5.0f, 6.0f } };
    float[][] expected = new float[][] { { 1.0f, 2.0f }, { expectedValue }, { 4.0f, 5.0f, 6.0f } };
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isDeepEqualTo(expected));
    // THEN
    ErrorMessageFactory elementShouldBeEqual = elementShouldBeEqual(actualValue, expectedValue, 1, 0);
    then(assertionError).hasMessage(elementShouldBeEqual.create(emptyDescription(), STANDARD_REPRESENTATION));
  }
}
