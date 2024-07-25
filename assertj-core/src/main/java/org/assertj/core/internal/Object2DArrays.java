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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.internal;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for two-dimensional arrays of objects.
 *
 * @author Maciej Wajcht
 *
 * @param <ELEMENT> the type of elements of the 2D array.
 */
public class Object2DArrays<ELEMENT> {

  /**
   * Returns the singleton instance of this class.
   *
   * @param <ELEMENT> the type of elements of the 2D array.
   *
   * @return the singleton instance of this class.
   */
  public static <ELEMENT> Object2DArrays<ELEMENT> instance() {
    return new Object2DArrays<>();
  }

  private Arrays2D arrays = Arrays2D.instance();

  @VisibleForTesting
  void setArrays(Arrays2D arrays) {
    this.arrays = arrays;
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  /**
   * Asserts that the given array is {@code null} or empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, ELEMENT[][] actual) {
    arrays.assertNullOrEmpty(info, failures, actual);
  }

  /**
   * Asserts that the given array is empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array is not empty.
   */
  public void assertEmpty(AssertionInfo info, ELEMENT[][] actual) {
    arrays.assertEmpty(info, failures, actual);
  }

  /**
   * Asserts that the given array is not empty.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array is empty.
   */
  public void assertNotEmpty(AssertionInfo info, ELEMENT[][] actual) {
    arrays.assertNotEmpty(info, failures, actual);
  }

  /**
   * Asserts that the number of elements in the given array is equal to the expected one.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param expectedFirstDimension the expected first dimension size of {@code actual}.
   * @param expectedSecondDimension the expected second dimension size of {@code actual}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual array's dimensions are not equal to the given ones.
   */
  public void assertHasDimensions(AssertionInfo info, ELEMENT[][] actual, int expectedFirstDimension,
                                  int expectedSecondDimension) {
    arrays.assertHasDimensions(info, failures, actual, expectedFirstDimension, expectedSecondDimension);
  }

  /**
   * Assert that the actual array has the same dimensions as the other array.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param other the group to compare
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other group is {@code null}.
   * @throws AssertionError if the actual group does not have the same dimension.
   */
  public void assertHasSameDimensionsAs(AssertionInfo info, ELEMENT[][] actual, Object other) {
    arrays.assertHasSameDimensionsAs(info, actual, other);
  }

  /**
   * Asserts that the number of rows in the given array is equal to the expected one.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param expectedNumberOfRows the expected first dimension size of {@code actual}.
   */
  public void assertNumberOfRows(AssertionInfo info, ELEMENT[][] actual, int expectedNumberOfRows) {
    arrays.assertNumberOfRows(info, failures, actual, expectedNumberOfRows);
  }

  /**
   * Verifies that the given array contains the given object at the given index.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param value the object to look for.
   * @param index the index where the object should be stored in the given array.
   * @throws AssertionError if the given array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the given array.
   * @throws AssertionError if the given array does not contain the given object at the given index.
   */
  public void assertContains(AssertionInfo info, ELEMENT[][] actual, ELEMENT[] value, Index index) {
    arrays.assertContains(info, failures, actual, value, index);
  }

  /**
   * Verifies that the given array does not contain the given object at the given index.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param value the object to look for.
   * @param index the index where the object should be stored in the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the given array contains the given object at the given index.
   */
  public void assertDoesNotContain(AssertionInfo info, ELEMENT[][] actual, ELEMENT[] value, Index index) {
    arrays.assertDoesNotContain(info, failures, actual, value, index);
  }
}
