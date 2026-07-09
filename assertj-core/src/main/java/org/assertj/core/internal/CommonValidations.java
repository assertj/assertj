/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldHaveLineCount.shouldHaveLinesCount;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldHaveSizeBetween.shouldHaveSizeBetween;
import static org.assertj.core.error.ShouldHaveSizeGreaterThan.shouldHaveSizeGreaterThan;
import static org.assertj.core.error.ShouldHaveSizeGreaterThanOrEqualTo.shouldHaveSizeGreaterThanOrEqualTo;
import static org.assertj.core.error.ShouldHaveSizeLessThan.shouldHaveSizeLessThan;
import static org.assertj.core.error.ShouldHaveSizeLessThanOrEqualTo.shouldHaveSizeLessThanOrEqualTo;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.arrayOfValuesToLookForIsNull;
import static org.assertj.core.internal.CommonErrors.iterableOfValuesToLookForIsEmpty;
import static org.assertj.core.internal.CommonErrors.iterableOfValuesToLookForIsNull;
import static org.assertj.core.internal.ErrorMessages.nullSequence;
import static org.assertj.core.internal.ErrorMessages.nullSubsequence;
import static org.assertj.core.util.Arrays.isArrayEmpty;
import static org.assertj.core.util.IterableUtil.sizeOf;

import java.lang.reflect.Array;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;

/**
 * Provides validation helpers shared by internal assertion implementations.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public final class CommonValidations {

  private static final Failures FAILURES = Failures.instance();

  private CommonValidations() {}

  static void checkIndexValueIsValid(Index index, int maximum) {
    requireNonNull(index, "Index should not be null");
    if (index.value <= maximum) return;
    String errorMessage = "Index should be between <0> and <%d> (inclusive) but was:%n <%d>";
    throw new IndexOutOfBoundsException(errorMessage.formatted(maximum, index.value));
  }

  static void checkOffsetIsNotNull(Offset<?> offset) {
    requireNonNull(offset, "The given offset should not be null");
  }

  static void checkPercentageIsNotNull(Percentage percentage) {
    requireNonNull(percentage, "The given percentage should not be null");
  }

  static void checkNumberIsNotNull(Number number) {
    requireNonNull(number, "The given number should not be null");
  }

  static void checkIsNotEmpty(Object[] values) {
    if (values.length == 0) throw arrayOfValuesToLookForIsEmpty();
  }

  static void checkIsNotEmpty(Iterable<?> iterable) {
    if (!iterable.iterator().hasNext()) throw iterableOfValuesToLookForIsEmpty();
  }

  /**
   * Checks that the values array is not {@code null}.
   *
   * @param values the values to check
   */
  public static void checkIsNotNull(Object[] values) {
    if (values == null) throw arrayOfValuesToLookForIsNull();
  }

  static void checkIsNotNull(Iterable<?> iterable) {
    if (iterable == null) throw iterableOfValuesToLookForIsNull();
  }

  static void checkIsNotNullAndNotEmpty(Object[] values) {
    checkIsNotNull(values);
    checkIsNotEmpty(values);
  }

  static void checkIsNotNullAndNotEmpty(Iterable<?> iterable) {
    checkIsNotNull(iterable);
    checkIsNotEmpty(iterable);
  }

  /**
   * Fails when values are empty while the actual value is not.
   *
   * @param info assertion information
   * @param failures failure handler
   * @param actual the actual value
   * @param values the values to check
   */
  public static void failIfEmptySinceActualIsNotEmpty(AssertionInfo info, Failures failures, Object actual,
                                                      Object values) {
    if (isArrayEmpty(values)) throw failures.failure(info, actualIsNotEmpty(actual));
  }

  /**
   * Verifies that two arrays have the same size.
   *
   * @param info assertion information
   * @param actual the actual value
   * @param other the value to compare with
   * @param sizeOfActual the actual size
   */
  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Object other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Array");
    checkSameSizes(info, actual, other, sizeOfActual, Array.getLength(other));
  }

  /**
   * Verifies that an actual value and an iterable have the same size.
   *
   * @param info assertion information
   * @param actual the actual value
   * @param other the iterable to compare with
   * @param sizeOfActual the actual size
   */
  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Iterable<?> other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Iterable");
    checkSameSizes(info, actual, other, sizeOfActual, sizeOf(other));
  }

  /**
   * Verifies that an actual value and a map have the same size.
   *
   * @param info assertion information
   * @param actual the actual value
   * @param other the map to compare with
   * @param sizeOfActual the actual size
   */
  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Map<?, ?> other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Map");
    checkSameSizes(info, actual, other, sizeOfActual, other.size());
  }

  static void checkOtherIsNotNull(Object other, String otherType) {
    requireNonNull(other, "The " + otherType + " to compare actual size with should not be null");
  }

  static void checkSameSizes(AssertionInfo info, Object actual, Object other, int sizeOfActual, int sizeOfOther) {
    if (sizeOfActual != sizeOfOther)
      throw FAILURES.failure(info, shouldHaveSameSizeAs(actual, other, sizeOfActual, sizeOfOther));
  }

  /**
   * Verifies that two sizes are equal.
   *
   * @param actual the actual value
   * @param sizeOfActual the actual size
   * @param sizeOfOther the expected size
   * @param info assertion information
   */
  public static void checkSizes(Object actual, int sizeOfActual, int sizeOfOther, AssertionInfo info) {
    if (sizeOfActual != sizeOfOther) throw FAILURES.failure(info, shouldHaveSize(actual, sizeOfActual, sizeOfOther));
  }

  /**
   * Verifies that the actual size is greater than the boundary.
   *
   * @param actual the actual value
   * @param boundary the size boundary
   * @param sizeOfActual the actual size
   * @param info assertion information
   */
  public static void checkSizeGreaterThan(Object actual, int boundary, int sizeOfActual,
                                          AssertionInfo info) {
    if (!(sizeOfActual > boundary))
      throw FAILURES.failure(info, shouldHaveSizeGreaterThan(actual, sizeOfActual, boundary));
  }

  /**
   * Verifies that the actual size is at least the boundary.
   *
   * @param actual the actual value
   * @param boundary the size boundary
   * @param sizeOfActual the actual size
   * @param info assertion information
   */
  public static void checkSizeGreaterThanOrEqualTo(Object actual, int boundary, int sizeOfActual,
                                                   AssertionInfo info) {
    if (!(sizeOfActual >= boundary))
      throw FAILURES.failure(info, shouldHaveSizeGreaterThanOrEqualTo(actual, sizeOfActual, boundary));
  }

  /**
   * Verifies that the actual size is less than the boundary.
   *
   * @param actual the actual value
   * @param boundary the size boundary
   * @param sizeOfActual the actual size
   * @param info assertion information
   */
  public static void checkSizeLessThan(Object actual, int boundary, int sizeOfActual,
                                       AssertionInfo info) {
    if (!(sizeOfActual < boundary))
      throw FAILURES.failure(info, shouldHaveSizeLessThan(actual, sizeOfActual, boundary));
  }

  /**
   * Verifies that the actual size is at most the boundary.
   *
   * @param actual the actual value
   * @param boundary the size boundary
   * @param sizeOfActual the actual size
   * @param info assertion information
   */
  public static void checkSizeLessThanOrEqualTo(Object actual, int boundary, int sizeOfActual,
                                                AssertionInfo info) {
    if (!(sizeOfActual <= boundary))
      throw FAILURES.failure(info, shouldHaveSizeLessThanOrEqualTo(actual, sizeOfActual, boundary));
  }

  /**
   * Verifies that the actual size is between the given boundaries.
   *
   * @param actual the actual value
   * @param lowerBoundary the lower size boundary
   * @param higherBoundary the higher size boundary
   * @param sizeOfActual the actual size
   * @param info assertion information
   */
  public static void checkSizeBetween(Object actual, int lowerBoundary, int higherBoundary,
                                      int sizeOfActual, AssertionInfo info) {
    if (!(higherBoundary >= lowerBoundary))
      throw new IllegalArgumentException("The higher boundary <%s> must be greater than the lower boundary <%s>.".formatted(
                                                                                                                            higherBoundary,
                                                                                                                            lowerBoundary));

    if (!(lowerBoundary <= sizeOfActual && sizeOfActual <= higherBoundary))
      throw FAILURES.failure(info, shouldHaveSizeBetween(actual, sizeOfActual, lowerBoundary, higherBoundary));
  }

  /**
   * Verifies that two line counts are equal.
   *
   * @param actual the actual value
   * @param lineCountOfActual the actual line count
   * @param lineCountOfOther the expected line count
   * @param info assertion information
   */
  public static void checkLineCounts(Object actual, int lineCountOfActual, int lineCountOfOther, AssertionInfo info) {
    if (lineCountOfActual != lineCountOfOther)
      throw FAILURES.failure(info, shouldHaveLinesCount(actual, lineCountOfActual, lineCountOfOther));
  }

  /**
   * Checks that the expected type is not {@code null}.
   *
   * @param expectedType the type to check
   */
  public static void checkTypeIsNotNull(Class<?> expectedType) {
    requireNonNull(expectedType, "The given type should not be null");
  }

  /**
   * Checks that the iterable is not {@code null}.
   *
   * @param set the iterable to check
   */
  public static void checkIterableIsNotNull(Iterable<?> set) {
    requireNonNull(set, "The iterable to look for should not be null");
  }

  /**
   * Checks that the sequence is not {@code null}.
   *
   * @param sequence the sequence to check
   */
  public static void checkSequenceIsNotNull(Object sequence) {
    requireNonNull(sequence, nullSequence());
  }

  /**
   * Checks that the subsequence is not {@code null}.
   *
   * @param subsequence the subsequence to check
   */
  public static void checkSubsequenceIsNotNull(Object subsequence) {
    requireNonNull(subsequence, nullSubsequence());
  }
}
