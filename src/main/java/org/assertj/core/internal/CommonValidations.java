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
import static java.util.Objects.requireNonNull;
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
import static org.assertj.core.util.IterableUtil.sizeOf;

import java.lang.reflect.Array;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public final class CommonValidations {

  private static Failures failures = Failures.instance();

  private CommonValidations() {}

  static void checkIndexValueIsValid(Index index, int maximum) {
    requireNonNull(index, "Index should not be null");
    if (index.value <= maximum) return;
    String errorMessage = "Index should be between <0> and <%d> (inclusive) but was:%n <%d>";
    throw new IndexOutOfBoundsException(format(errorMessage, maximum, index.value));
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

  public static void failIfEmptySinceActualIsNotEmpty(Object[] values) {
    if (values.length == 0) throw new AssertionError("actual is not empty");
  }

  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Object other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Array");
    checkSameSizes(info, actual, other, sizeOfActual, Array.getLength(other));
  }

  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Iterable<?> other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Iterable");
    checkSameSizes(info, actual, other, sizeOfActual, sizeOf(other));
  }

  public static void hasSameSizeAsCheck(AssertionInfo info, Object actual, Map<?, ?> other, int sizeOfActual) {
    checkOtherIsNotNull(other, "Map");
    checkSameSizes(info, actual, other, sizeOfActual, other.size());
  }

  static void checkOtherIsNotNull(Object other, String otherType) {
    requireNonNull(other, "The "+ otherType +" to compare actual size with should not be null");
  }

  static void checkSameSizes(AssertionInfo info, Object actual, Object other, int sizeOfActual, int sizeOfOther) {
    if (sizeOfActual != sizeOfOther) throw failures.failure(info, shouldHaveSameSizeAs(actual, other, sizeOfActual, sizeOfOther));
  }

  public static void checkSizes(Object actual, int sizeOfActual, int sizeOfOther, AssertionInfo info) {
    if (sizeOfActual != sizeOfOther) throw failures.failure(info, shouldHaveSize(actual, sizeOfActual, sizeOfOther));
  }

  public static void checkSizeGreaterThan(Object actual, int boundary, int sizeOfActual,
                                          AssertionInfo info) {
    if (!(sizeOfActual > boundary))
      throw failures.failure(info, shouldHaveSizeGreaterThan(actual, sizeOfActual, boundary));
  }

  public static void checkSizeGreaterThanOrEqualTo(Object actual, int boundary, int sizeOfActual,
                                                   AssertionInfo info) {
    if (!(sizeOfActual >= boundary))
      throw failures.failure(info, shouldHaveSizeGreaterThanOrEqualTo(actual, sizeOfActual, boundary));
  }

  public static void checkSizeLessThan(Object actual, int boundary, int sizeOfActual,
                                       AssertionInfo info) {
    if (!(sizeOfActual < boundary))
      throw failures.failure(info, shouldHaveSizeLessThan(actual, sizeOfActual, boundary));
  }

  public static void checkSizeLessThanOrEqualTo(Object actual, int boundary, int sizeOfActual,
                                                AssertionInfo info) {
    if (!(sizeOfActual <= boundary))
      throw failures.failure(info, shouldHaveSizeLessThanOrEqualTo(actual, sizeOfActual, boundary));
  }

  public static void checkSizeBetween(Object actual, int lowerBoundary, int higherBoundary,
                                      int sizeOfActual, AssertionInfo info) {
    if (!(higherBoundary >= lowerBoundary))
      throw new IllegalArgumentException(format("The higher boundary <%s> must be greater than the lower boundary <%s>.",
                                                higherBoundary, lowerBoundary));

    if (!(lowerBoundary <= sizeOfActual && sizeOfActual <= higherBoundary))
      throw failures.failure(info, shouldHaveSizeBetween(actual, sizeOfActual, lowerBoundary, higherBoundary));
  }

  public static void checkLineCounts(Object actual, int lineCountOfActual, int lineCountOfOther, AssertionInfo info) {
    if (lineCountOfActual != lineCountOfOther)
      throw failures.failure(info, shouldHaveLinesCount(actual, lineCountOfActual, lineCountOfOther));
  }

  public static void checkTypeIsNotNull(Class<?> expectedType) {
    requireNonNull(expectedType, "The given type should not be null");
  }

  public static void checkIterableIsNotNull(Iterable<?> set) {
    requireNonNull(set, "The iterable to look for should not be null");
  }

  public static void checkSequenceIsNotNull(Object sequence) {
    requireNonNull(sequence, nullSequence());
  }

  public static void checkSubsequenceIsNotNull(Object subsequence) {
    requireNonNull(subsequence, nullSubsequence());
  }
}
