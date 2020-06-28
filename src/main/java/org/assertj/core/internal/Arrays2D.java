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
package org.assertj.core.internal;

import static java.lang.String.format;
import static java.util.Objects.deepEquals;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.error.ShouldHaveDimensions.shouldHaveSize;
import static org.assertj.core.error.ShouldHaveSameDimensionsAs.shouldHaveSameDimensionsAs;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.internal.Arrays.assertNotNull;
import static org.assertj.core.internal.Arrays.sizeOf;
import static org.assertj.core.internal.CommonValidations.checkIndexValueIsValid;

import java.lang.reflect.Array;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.error.ShouldHaveDimensions;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for object and primitive two-dimensional arrays. It trades off performance for DRY.
 *
 * @author Maciej Wajcht
 * @since 3.17.0
 */
public class Arrays2D {

  private static final Arrays2D INSTANCE = new Arrays2D();

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   *
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Arrays2D instance() {
    return INSTANCE;
  }

  @VisibleForTesting
  public void assertNullOrEmpty(AssertionInfo info, Failures failures, Object array) {
    if (array == null) return;
    if (countArrayElements(array) > 0) throw failures.failure(info, shouldBeNullOrEmpty(array));
  }

  @VisibleForTesting
  public void assertEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    // need to check that all rows are empty
    int numberOfRows = sizeOf(array);
    for (int i = 0; i < numberOfRows; i++) {
      Object actualArrayRow = Array.get(array, i);
      if (sizeOf(actualArrayRow) > 0) throw failures.failure(info, shouldBeEmpty(array));
    }
  }

  @VisibleForTesting
  public void assertHasDimensions(AssertionInfo info, Failures failures, Object array2d, int expectedNumberOfRows,
                                  int expectedRowSize) {
    assertNumberOfRows(info, failures, array2d, expectedNumberOfRows);
    for (int i = 0; i < expectedNumberOfRows; i++) {
      Object actualRow = Array.get(array2d, i);
      assertSecondDimension(info, failures, actualRow, expectedRowSize, i);
    }
  }

  private void assertNumberOfRows(AssertionInfo info, Failures failures, Object array, int expectedSize) {
    assertNotNull(info, array);
    int sizeOfActual = sizeOf(array);
    if (sizeOfActual != expectedSize)
      throw failures.failure(info, ShouldHaveDimensions.shouldHaveFirstDimension(array, sizeOfActual, expectedSize));
  }

  private void assertSecondDimension(AssertionInfo info, Failures failures, Object actual, int expectedSize, int rowIndex) {
    assertNotNull(info, actual);
    checkArraySizes(actual, failures, sizeOf(actual), expectedSize, info, rowIndex);
  }

  private static void checkArraySizes(Object actual, Failures failures, int sizeOfActual, int sizeOfOther, AssertionInfo info,
                                      int rowIndex) {
    if (sizeOfActual != sizeOfOther) {
      throw failures.failure(info, shouldHaveSize(actual, sizeOfActual, sizeOfOther, rowIndex));
    }
  }

  @VisibleForTesting
  public void assertHasSameDimensionsAs(AssertionInfo info, Object actual, Object other) {
    assertNotNull(info, actual);
    assertIsArray(info, actual);
    assertIsArray(info, other);
    // check first dimension
    int actualFirstDimension = sizeOf(actual);
    int otherFirstDimension = sizeOf(other);
    if (actualFirstDimension != otherFirstDimension) {
      throw Failures.instance().failure(info,
                                        shouldHaveSameDimensionsAs(actual, other, actualFirstDimension, otherFirstDimension));
    }
    // check second dimensions
    for (int i = 0; i < actualFirstDimension; i++) {
      Object actualRow = Array.get(actual, i);
      assertIsArray(info, actualRow);
      Object otherRow = Array.get(other, i);
      assertIsArray(info, otherRow);
      hasSameRowSizeAsCheck(info, i, actual, other, actualRow, otherRow, sizeOf(actualRow));
    }
  }

  static void hasSameRowSizeAsCheck(AssertionInfo info, int rowIndex, Object actual, Object other, Object actualRow,
                                    Object otherRow, int actualRowSize) {
    requireNonNull(other, format("The array to compare %s size with should not be null", actual));
    int expectedRowSize = Array.getLength(otherRow);
    if (actualRowSize != expectedRowSize)
      throw Failures.instance().failure(info, shouldHaveSameDimensionsAs(rowIndex, actualRowSize, expectedRowSize, actualRow,
                                                                         otherRow, actual, other));
  }

  @VisibleForTesting
  public void assertContains(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    assertNotEmpty(info, failures, array);
    checkIndexValueIsValid(index, sizeOf(array) - 1);
    Object actualElement = Array.get(array, index.value);
    if (!deepEquals(actualElement, value)) {
      throw failures.failure(info, shouldContainAtIndex(array, value, index, Array.get(array, index.value)));
    }
  }

  @VisibleForTesting
  public void assertNotEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (countArrayElements(array) == 0) throw failures.failure(info, shouldNotBeEmpty());
  }

  private static int countArrayElements(Object array) {
    // even if array has many rows, they could all be empty
    int numberOfRows = sizeOf(array);
    // if any rows is not empty, the assertion succeeds.
    int allRowsElementsCount = 0;
    for (int i = 0; i < numberOfRows; i++) {
      Object actualRow = Array.get(array, i);
      allRowsElementsCount += sizeOf(actualRow);
    }
    return allRowsElementsCount;
  }

  @VisibleForTesting
  public void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    checkIndexValueIsValid(index, Integer.MAX_VALUE);
    if (index.value >= sizeOf(array)) return;
    if (deepEquals(Array.get(array, index.value), value)) {
      throw failures.failure(info, shouldNotContainAtIndex(array, value, index));
    }
  }

}
