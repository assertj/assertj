/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal;

import java.util.Comparator;

import org.assertj.core.api.ArraySortedAssert;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.util.VisibleForTesting;

/**
 * Reusable assertions for arrays of {@code long}s.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class LongArrays {

  private static final LongArrays INSTANCE = new LongArrays();

  /**
   * Returns the singleton instance of this class.
   * 
   * @return the singleton instance of this class.
   */
  public static LongArrays instance() {
    return INSTANCE;
  }

  private Arrays arrays = Arrays.instance();

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  LongArrays() {
    this(StandardComparisonStrategy.instance());
  }

  public LongArrays(ComparisonStrategy comparisonStrategy) {
    setArrays(new Arrays(comparisonStrategy));
  }

  @VisibleForTesting
  public void setArrays(Arrays arrays) {
    this.arrays = arrays;
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    return arrays.getComparator();
  }

  /**
   * Asserts that the given array is {@code null} or empty.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, long[] actual) {
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
  public void assertEmpty(AssertionInfo info, long[] actual) {
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
  public void assertNotEmpty(AssertionInfo info, long[] actual) {
    arrays.assertNotEmpty(info, failures, actual);
  }

  /**
   * Asserts that the number of elements in the given array is equal to the expected one.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param expectedSize the expected size of {@code actual}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of elements in the given array is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, long[] actual, int expectedSize) {
    arrays.assertHasSize(info, failures, actual, expectedSize);
  }

  /**
   * Assert that the actual array has the same size as the other {@code Iterable}.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param other the group to compare
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other group is {@code null}.
   * @throws AssertionError if the actual group does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, long[] actual, Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, actual, other);
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
  public void assertHasSameSizeAs(AssertionInfo info, long[] actual, Object[] other) {
    arrays.assertHasSameSizeAs(info, actual, other);
  }

  /**
   * Asserts that the given array contains the given values, in any order.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain the given values.
   */
  public void assertContains(AssertionInfo info, long[] actual, long[] values) {
    arrays.assertContains(info, failures, actual, values);
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
  public void assertContains(AssertionInfo info, long[] actual, long value, Index index) {
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
  public void assertDoesNotContain(AssertionInfo info, long[] actual, long value, Index index) {
    arrays.assertDoesNotContain(info, failures, actual, value, index);
  }

  /**
   * Asserts that the given array contains only the given values and nothing else, in any order.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain the given values or if the given array contains values
   *           that are not in the given array.
   */
  public void assertContainsOnly(AssertionInfo info, long[] actual, long[] values) {
    arrays.assertContainsOnly(info, failures, actual, values);
  }

  public void assertContainsExactly(AssertionInfo info, long[] actual, long[] values) {
    arrays.assertContainsExactly(info, failures, actual, values);
  }

  public void assertContainsExactlyInAnyOrder(AssertionInfo info, long[] actual, long[] values) {
    arrays.assertContainsExactlyInAnyOrder(info, failures, actual, values);
  }

  /**
   * Asserts that the given array contains only once the given values.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain the given values or if the given array contains more
   *           than once values.
   */
  public void assertContainsOnlyOnce(AssertionInfo info, long[] actual, long[] values) {
    arrays.assertContainsOnlyOnce(info, failures, actual, values);
  }

  /**
   * Verifies that the given array contains the given sequence of values, without any other values between them.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param sequence the sequence of values to look for.
   * @throws AssertionError if the given array is {@code null}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence is empty.
   * @throws AssertionError if the given array does not contain the given sequence of values.
   */
  public void assertContainsSequence(AssertionInfo info, long[] actual, long[] sequence) {
    arrays.assertContainsSequence(info, failures, actual, sequence);
  }

  /**
   * Verifies that the given array contains the given subsequence of values (possibly with other values between them).
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param subsequence the subsequence of values to look for.
   * @throws AssertionError if the given array is {@code null}.
   * @throws NullPointerException if the given subsequence is {@code null}.
   * @throws IllegalArgumentException if the given subsequence is empty.
   * @throws AssertionError if the given array does not contain the given subsequence of values.
   */
  public void assertContainsSubsequence(AssertionInfo info, long[] actual, long[] subsequence) {
    arrays.assertContainsSubsequence(info, failures, actual, subsequence);
  }

  /**
   * Asserts that the given array does not contain the given values.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected not to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array contains any of given values.
   */
  public void assertDoesNotContain(AssertionInfo info, long[] actual, long[] values) {
    arrays.assertDoesNotContain(info, failures, actual, values);
  }

  /**
   * Asserts that the given array does not have duplicate values.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array contains duplicate values.
   */
  public void assertDoesNotHaveDuplicates(AssertionInfo info, long[] actual) {
    arrays.assertDoesNotHaveDuplicates(info, failures, actual);
  }

  /**
   * Verifies that the given array starts with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #assertContainsSequence(AssertionInfo, long[], long[])}</code>, but it also verifies that
   * the first element in the sequence is also the first element of the given array.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param sequence the sequence of values to look for.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not start with the given sequence of values.
   */
  public void assertStartsWith(AssertionInfo info, long[] actual, long[] sequence) {
    arrays.assertStartsWith(info, failures, actual, sequence);
  }

  /**
   * Verifies that the given array ends with the given sequence of values, without any other values between them.
   * Similar to <code>{@link #assertContainsSequence(AssertionInfo, long[], long[])}</code>, but it also verifies that
   * the last element in the sequence is also the last element of the given array.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param sequence the sequence of values to look for.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not end with the given sequence of values.
   */
  public void assertEndsWith(AssertionInfo info, long[] actual, long[] sequence) {
    arrays.assertEndsWith(info, failures, actual, sequence);
  }

  /**
   * Concrete implementation of {@link ArraySortedAssert#isSorted()}.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   */
  public void assertIsSorted(AssertionInfo info, long[] actual) {
    arrays.assertIsSorted(info, failures, actual);
  }

  /**
   * Concrete implementation of {@link ArraySortedAssert#isSortedAccordingTo(Comparator)}.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param comparator the {@link Comparator} used to compare array elements
   */
  public void assertIsSortedAccordingToComparator(AssertionInfo info, long[] actual,
                                                  Comparator<? super Long> comparator) {
    Arrays.assertIsSortedAccordingToComparator(info, failures, actual, comparator);
  }

  public <E> void assertContainsAnyOf(AssertionInfo info, long[] actual, long[] values) {
    arrays.assertContainsAnyOf(info, failures, actual, values);
  }
}
