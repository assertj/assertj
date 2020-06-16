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

import static org.assertj.core.error.ShouldNotBeEmpty.subarrayShouldNotBeEmpty;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.util.VisibleForTesting;

import java.util.Comparator;

/**
 * Reusable assertions for two-dimensional arrays of {@code char}s.
 * 
 * @author Maciej Wajcht
 */
public class Char2DArrays {

  private static final Char2DArrays INSTANCE = new Char2DArrays();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static Char2DArrays instance() {
    return INSTANCE;
  }

  private Arrays2D arrays = Arrays2D.instance();

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  public Comparator<?> getComparator() {
    return arrays.getComparator();
  }

  @VisibleForTesting
  public void setArrays(Arrays2D arrays) {
    this.arrays = arrays;
  }

  /**
   * Asserts that the given array is {@code null} or empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, char[][] actual) {
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
  public void assertEmpty(AssertionInfo info, char[][] actual) {
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
  public void assertNotEmpty(AssertionInfo info, char[][] actual) {
    arrays.assertNotEmpty(info, failures, actual);
    for (int i = 0; i < actual.length; i++) {
      arrays.assertNotEmpty(info, failures, actual[i], subarrayShouldNotBeEmpty(i));
    }
  }

  /**
   * Asserts that the number of elements in the given array is equal to the expected one.
   *
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param expectedFirstDimensionSize the expected first dimension size of {@code actual}.
   * @param expectedSecondDimensionSize the expected second dimension size of {@code actual}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of elements in the given array is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, char[][] actual, int expectedFirstDimensionSize,
    int expectedSecondDimensionSize) {
    arrays.assertHasSize(info, actual, expectedFirstDimensionSize);
    for (int i = 0; i < actual.length; i++) {
      arrays.assertHasSize(info, failures, actual[i], expectedSecondDimensionSize, i);
    }
  }

  /**
   * Assert that the actual array has the same size as the other array.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param other the group to compare
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other group is {@code null}.
   * @throws AssertionError if the actual group does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, char[][] actual, Object other) {
    arrays.assertHasSameSizeAs(info, actual, other);
  }

  /**
   * Verifies that the given array contains the given value at the given index.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param value the value to look for.
   * @param index the index where the value should be stored in the given array.
   * @throws AssertionError if the given array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the given array.
   * @throws AssertionError if the given array does not contain the given value at the given index.
   */
  public void assertContains(AssertionInfo info, char[][] actual, char[] value, Index index) {
    arrays.assertContains(info, failures, actual, value, index);
  }

  /**
   * Verifies that the given array does not contain the given value at the given index.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param value the value to look for.
   * @param index the index where the value should be stored in the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the given array contains the given value at the given index.
   */
  public void assertDoesNotContain(AssertionInfo info, char[][] actual, char[] value, Index index) {
    arrays.assertDoesNotContain(info, failures, actual, value, index);
  }
}
