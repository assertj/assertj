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
package org.assertj.guava.internal;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.internal.CommonValidations.checkSizes;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.iterableValuesToLookForIsNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.IterableUtil.sizeOf;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.guava.error.RangeSetShouldEnclose.shouldEnclose;
import static org.assertj.guava.error.RangeSetShouldEncloseAnyOf.shouldEncloseAnyOf;
import static org.assertj.guava.error.RangeSetShouldIntersect.shouldIntersect;
import static org.assertj.guava.error.RangeSetShouldIntersectAnyOf.shouldIntersectAnyOf;
import static org.assertj.guava.error.RangeSetShouldNotEnclose.shouldNotEnclose;
import static org.assertj.guava.error.RangeSetShouldNotIntersect.shouldNotIntersects;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsEmpty;
import static org.assertj.guava.internal.ErrorMessages.rangeSetValuesToLookForIsNull;
import static org.assertj.guava.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

/**
 * Reusable assertions for <code>{@link RangeSet}</code>s.
 *
 * @author Ilya_Koshaleu
 */
public class RangeSets {

  private final static RangeSets INSTANCE = new RangeSets();

  @VisibleForTesting
  Failures failures = Failures.instance();

  /**
   * Returns singleton instance of this class.
   *
   * @return singleton instance of this class.
   */
  public static RangeSets instance() {
    return INSTANCE;
  }

  /**
   * Asserts that the actual {@code RangeSet} has specific {@code size}.
   *
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param size expected size
   */
  public void assertHasSize(AssertionInfo info, RangeSet<?> actual, int size) {
    assertNotNull(info, actual);
    checkSizes(actual, sizeOf(actual.asRanges()), size, info);
  }

  /**
   * Asserts that the given {@code RangeSet} contains the given values.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param values the values that are expected to be in the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain the given {@code elements}.
   * @throws IllegalArgumentException if elements are null or elements are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertContains(AssertionInfo info, RangeSet<T> actual, T[] values) {
    assertNotNull(info, actual);
    failIfNull(values);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && values.length == 0) return;
    failIfEmpty(values);
    assertRangeSetContainsGivenValues(info, actual, values);
  }

  /**
   * Asserts that the given {@code RangeSet} contains all the given values.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param values the values that are expected to be in the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain all the given {@code elements}.
   * @throws IllegalArgumentException if elements are null or elements are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertContainsAll(AssertionInfo info, RangeSet<T> actual, Iterable<T> values) {
    assertNotNull(info, actual);
    failIfNull(values);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && !values.iterator().hasNext()) return;
    failIfEmpty(values);
    assertRangeSetContainsGivenValues(info, actual, toArray(values, Comparable.class));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void assertRangeSetContainsGivenValues(AssertionInfo info, RangeSet actual, Comparable[] values) {
    final List<?> elementsNotFound = stream(values).filter(value -> !actual.contains(value)).collect(toList());
    if (!elementsNotFound.isEmpty()) {
      throw failures.failure(info, shouldContain(actual, values, elementsNotFound));
    }
  }

  /**
   * Asserts that the given {@code RangeSet} contains at least one of the given values.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param values the values that are expected to be in the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain at least one of the given {@code elements}.
   * @throws IllegalArgumentException if elements are null or elements are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertContainsAnyOf(AssertionInfo info, RangeSet<T> actual, T[] values) {
    assertNotNull(info, actual);
    failIfNull(values);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && values.length == 0) return;
    failIfEmpty(values);
    assertRangeSetContainsAnyGivenValues(info, actual, values);
  }

  /**
   * Asserts that the given {@code RangeSet} contains at least one element of the given values.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param values the values that are expected to be in the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain at least one element of the given
   *                        {@code values}.
   * @throws IllegalArgumentException if elements are null or elements are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertContainsAnyRangesOf(AssertionInfo info, RangeSet<T> actual,
                                                                  Iterable<? extends T> values) {
    assertNotNull(info, actual);
    failIfNull(values);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && !values.iterator().hasNext()) return;
    failIfEmpty(values);
    assertRangeSetContainsAnyGivenValues(info, actual, toArray(values, Comparable.class));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void assertRangeSetContainsAnyGivenValues(AssertionInfo info, RangeSet actual, Comparable[] values) {
    final boolean match = stream(values).anyMatch(actual::contains);
    if (!match) throw failures.failure(info, shouldContainAnyOf(actual, values));
  }

  /**
   * Asserts that the given {@code RangeSet} does not contain the given values.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param values the values that are expected not to be in the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} contains at least one element of the given {@code values}.
   * @throws IllegalArgumentException if elements are null or elements are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertDoesNotContain(AssertionInfo info, RangeSet<T> actual, T[] values) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(values);
    assertRangeSetDoesNotContainGivenValues(info, actual, values);
  }

  /**
   * Asserts that the given {@code RangeSet} does not contain any elements of the given values.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param values the values that are expected not to be in the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} contains at least one element of the given {@code values}.
   * @throws IllegalArgumentException if values are null or values are empty.
   */
  public <T extends Comparable<T>> void assertDoesNotContainAll(AssertionInfo info, RangeSet<T> actual,
                                                                Iterable<? extends T> values) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(values);
    assertRangeSetDoesNotContainGivenValues(info, actual, toArray(values, Comparable.class));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void assertRangeSetDoesNotContainGivenValues(AssertionInfo info, RangeSet actual, Comparable[] values) {
    final List<?> elementsFound = stream(values).filter(actual::contains).collect(toList());
    if (!elementsFound.isEmpty()) {
      throw failures.failure(info, shouldNotContain(actual, values, elementsFound));
    }
  }

  /**
   * Asserts that the given {@code RangeSet} intersects the given ranges.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param ranges the ranges that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertIntersects(AssertionInfo info, RangeSet<T> actual, Range<T>[] ranges) {
    assertNotNull(info, actual);
    failIfNull(ranges);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges);
    assertRangeSetIntersectsGivenValues(info, actual, ranges);
  }

  /**
   * Asserts that the given {@code RangeSet} intersects all the given ranges.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param range the ranges that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the given {@code range}.
   * @throws IllegalArgumentException if range is null or range is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertIntersectsAll(AssertionInfo info, RangeSet<T> actual,
                                                            Iterable<? extends Range<T>> range) {
    assertNotNull(info, actual);
    failIfNull(range);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && !range.iterator().hasNext()) return;
    failIfEmpty(range);
    assertRangeSetIntersectsGivenValues(info, actual, toArray(range, Range.class));
  }

  /**
   * Asserts that the given {@code RangeSet} intersects all the given values of range set.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param rangeSet the range set that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the given {@code rangeSet}.
   * @throws IllegalArgumentException if range set is null or range set is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertIntersectsAll(AssertionInfo info, RangeSet<T> actual, RangeSet<T> rangeSet) {
    assertNotNull(info, actual);
    failIfNull(rangeSet);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetIntersectsGivenValues(info, actual, toArray(rangeSet.asRanges(), Range.class));
  }

  private <T extends Comparable<T>> void assertRangeSetIntersectsGivenValues(AssertionInfo info, RangeSet<T> actual,
                                                                             Range<T>[] ranges) {
    final List<?> nonIntersectedRanges = stream(ranges).filter(range -> !actual.intersects(range)).collect(toList());
    if (!nonIntersectedRanges.isEmpty()) {
      throw failures.failure(info, shouldIntersect(actual, ranges, nonIntersectedRanges));
    }
  }

  /**
   * Asserts that the given {@code RangeSet} is empty.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} is not empty.
   */
  public <T extends Comparable<T>> void assertEmpty(AssertionInfo info, RangeSet<T> actual) {
    assertNotNull(info, actual);
    if (!actual.isEmpty()) throw failures.failure(info, shouldBeEmpty(actual));
  }

  /**
   * Asserts that the given {@code RangeSet} is not empty.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} is empty.
   */
  public <T extends Comparable<T>> void assertNotEmpty(AssertionInfo info, RangeSet<T> actual) {
    assertNotNull(info, actual);
    if (actual.isEmpty()) throw failures.failure(info, shouldNotBeEmpty());
  }

  /**
   * Asserts that the given {@code RangeSet} is {@code null} or empty.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @throws AssertionError if the actual {@code RangeSet} is not {@code null} or not empty.
   */
  public <T extends Comparable<T>> void assertNullOrEmpty(AssertionInfo info, RangeSet<T> actual) {
    if (actual != null && !actual.isEmpty()) throw failures.failure(info, shouldBeNullOrEmpty(actual));
  }

  /**
   * Asserts that the given {@code RangeSet} intersects at least one of the given ranges.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param ranges the ranges that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect at least one of the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertIntersectsAnyOf(AssertionInfo info, RangeSet<T> actual, Range<T>[] ranges) {
    assertNotNull(info, actual);
    failIfNull(ranges);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges);
    assertRangeSetIntersectsAnyOfGivenValues(info, actual, ranges);
  }

  /**
   * Asserts that the given {@code RangeSet} intersects at least one element of the given range.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param range the range that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect at least one element of the given
   *                        {@code range}.
   * @throws IllegalArgumentException if range is null or range is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertIntersectsAnyRangesOf(AssertionInfo info, RangeSet<T> actual,
                                                                    Iterable<? extends Range<T>> range) {
    assertNotNull(info, actual);
    failIfNull(range);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && !range.iterator().hasNext()) return;
    failIfEmpty(range);
    assertRangeSetIntersectsAnyOfGivenValues(info, actual, toArray(range, Range.class));
  }

  /**
   * Asserts that the given {@code RangeSet} intersects at least one element of the given range set.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param rangeSet the range set that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect at least one element of the given
   *                        {@code rangeSet}.
   * @throws IllegalArgumentException if range set is null or range set is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertIntersectsAnyRangesOf(AssertionInfo info, RangeSet<T> actual,
                                                                    RangeSet<T> rangeSet) {
    assertNotNull(info, actual);
    failIfNull(rangeSet);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetIntersectsAnyOfGivenValues(info, actual, toArray(rangeSet.asRanges(), Range.class));
  }

  private <T extends Comparable<T>> void assertRangeSetIntersectsAnyOfGivenValues(AssertionInfo info, RangeSet<T> actual,
                                                                                  Range<T>[] ranges) {
    final boolean match = stream(ranges).anyMatch(actual::intersects);
    if (!match) throw failures.failure(info, shouldIntersectAnyOf(actual, ranges));
  }

  /**
   * Asserts that the given {@code RangeSet} does not intersect the given ranges.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param ranges the range that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects at least one element of the given {@code range}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertDoesNotIntersect(AssertionInfo info, RangeSet<T> actual, Range<T>[] ranges) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(ranges);
    assertRangeSetDoesNotIntersectGivenValues(info, actual, ranges);
  }

  /**
   * Asserts that the given {@code RangeSet} does not intersect all elements from the given range.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param range the range that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects at least one element of the given {@code range}.
   * @throws IllegalArgumentException if range is null or range is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertDoesNotIntersectAnyRangeFrom(AssertionInfo info, RangeSet<T> actual,
                                                                           Iterable<? extends Range<T>> range) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(range);
    assertRangeSetDoesNotIntersectGivenValues(info, actual, toArray(range, Range.class));
  }

  /**
   * Asserts that the given {@code RangeSet} does not intersect all elements from the given range set.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param rangeSet the range set that actual {@code RangeSet} has to intersect.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects at least one element of the given {@code range}.
   * @throws IllegalArgumentException if range set is null or range set is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertDoesNotIntersectAnyRangeFrom(AssertionInfo info, RangeSet<T> actual,
                                                                           RangeSet<T> rangeSet) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(rangeSet);
    assertRangeSetDoesNotIntersectGivenValues(info, actual, toArray(rangeSet.asRanges(), Range.class));
  }

  private <T extends Comparable<T>> void assertRangeSetDoesNotIntersectGivenValues(AssertionInfo info, RangeSet<T> actual,
                                                                                   Range<T>[] ranges) {
    final List<?> intersected = stream(ranges).filter(actual::intersects).collect(toList());
    if (!intersected.isEmpty()) {
      throw failures.failure(info, shouldNotIntersects(actual, ranges, intersected));
    }
  }

  /**
   * Asserts that the given {@code RangeSet} encloses the given ranges.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param ranges the ranges that actual {@code RangeSet} has to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertEncloses(AssertionInfo info, RangeSet<T> actual, Range<T>[] ranges) {
    assertNotNull(info, actual);
    failIfNull(ranges);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges);
    assertRangeSetEnclosesGivenValues(info, actual, ranges);
  }

  /**
   * Asserts that the given {@code RangeSet} encloses all elements of the given range.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param range the range that actual {@code RangeSet} has to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose elements of the given {@code range}.
   * @throws IllegalArgumentException if range is null or range is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertEnclosesAll(AssertionInfo info, RangeSet<T> actual,
                                                          Iterable<? extends Range<T>> range) {
    assertNotNull(info, actual);
    failIfNull(range);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && !range.iterator().hasNext()) return;
    failIfEmpty(range);
    assertRangeSetEnclosesGivenValues(info, actual, toArray(range, Range.class));
  }

  /**
   * Asserts that the given {@code RangeSet} encloses all elements of the given range set.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param rangeSet the range set that actual {@code RangeSet} has to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose elements of the given {@code range}.
   * @throws IllegalArgumentException if range set is null or range set is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertEnclosesAll(AssertionInfo info, RangeSet<T> actual, RangeSet<T> rangeSet) {
    assertNotNull(info, actual);
    failIfNull(rangeSet);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetEnclosesGivenValues(info, actual, toArray(rangeSet.asRanges(), Range.class));
  }

  private <T extends Comparable<T>> void assertRangeSetEnclosesGivenValues(AssertionInfo info, RangeSet<T> actual,
                                                                           Range<T>[] ranges) {
    final List<?> notEnclosed = stream(ranges).filter(range -> !actual.encloses(range)).collect(toList());
    if (!notEnclosed.isEmpty()) {
      throw failures.failure(info, shouldEnclose(actual, ranges, notEnclosed));
    }
  }

  /**
   * Asserts that the given {@code RangeSet} encloses the given ranges.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param ranges the ranges that actual {@code RangeSet} has to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertEnclosesAnyOf(AssertionInfo info, RangeSet<T> actual, Range<T>[] ranges) {
    assertNotNull(info, actual);
    failIfNull(ranges);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges);
    assertRangeSetEnclosesAnyOfGivenValues(info, actual, ranges);
  }

  /**
   * Asserts that the given {@code RangeSet} encloses all elements of the given range.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param range the range that actual {@code RangeSet} has to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose all elements of the given {@code range}.
   * @throws IllegalArgumentException if range is null or range is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertEnclosesAnyRangesOf(AssertionInfo info, RangeSet<T> actual,
                                                                  Iterable<? extends Range<T>> range) {
    assertNotNull(info, actual);
    failIfNull(range);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && !range.iterator().hasNext()) return;
    failIfEmpty(range);
    assertRangeSetEnclosesAnyOfGivenValues(info, actual, toArray(range, Range.class));
  }

  /**
   * Asserts that the given {@code RangeSet} encloses all elements of the given range set.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param rangeSet the range set that actual {@code RangeSet} has to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose all elements of the given {@code rangeSet}.
   * @throws IllegalArgumentException if range set is null or range set is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void assertEnclosesAnyRangesOf(AssertionInfo info, RangeSet<T> actual, RangeSet<T> rangeSet) {
    assertNotNull(info, actual);
    failIfNull(rangeSet);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetEnclosesAnyOfGivenValues(info, actual, toArray(rangeSet.asRanges(), Range.class));
  }

  private <T extends Comparable<T>> void assertRangeSetEnclosesAnyOfGivenValues(AssertionInfo info, RangeSet<T> actual,
                                                                                Range<T>[] ranges) {
    final boolean match = stream(ranges).anyMatch(actual::encloses);
    if (!match) throw failures.failure(info, shouldEncloseAnyOf(actual, ranges));
  }

  /**
   * Asserts that the given {@code RangeSet} does not enclose the given ranges.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param ranges the ranges that actual {@code RangeSet} has not to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses the given {@code ranges}.
   * @throws IllegalArgumentException if ranges are null or ranges are empty while actual is not empty.
   */
  public <T extends Comparable<T>> void assertDoesNotEnclose(AssertionInfo info, RangeSet<T> actual, Range<T>[] ranges) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(ranges);
    assertRangeSetDoesNotEncloseGivenValues(info, actual, ranges);
  }

  /**
   * Asserts that the given {@code RangeSet} does not enclose all elements of the given range.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param range the ranges that actual {@code RangeSet} has not to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses all elements of the given {@code range}.
   * @throws IllegalArgumentException if range is null or range is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void doesNotEncloseAnyRangesOf(AssertionInfo info, RangeSet<T> actual,
                                                                  Iterable<? extends Range<T>> range) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(range);
    assertRangeSetDoesNotEncloseGivenValues(info, actual, toArray(range, Range.class));
  }

  /**
   * Asserts that the given {@code RangeSet} does not enclose all elements of the given range set.
   *
   * @param <T> the type of rangeset elements
   * @param info contains information about the assertion.
   * @param actual the given {@code RangeSet}.
   * @param rangeSet the range set that actual {@code RangeSet} has not to enclose.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses all elements of the given {@code range}.
   * @throws IllegalArgumentException if range set is null or range set is empty while actual is not empty.
   */
  @SuppressWarnings("unchecked")
  public <T extends Comparable<T>> void doesNotEncloseAnyRangesOf(AssertionInfo info, RangeSet<T> actual, RangeSet<T> rangeSet) {
    assertNotNull(info, actual);
    failIfNullOrEmpty(rangeSet);
    assertRangeSetDoesNotEncloseGivenValues(info, actual, toArray(rangeSet.asRanges(), Range.class));
  }

  private <T extends Comparable<T>> void assertRangeSetDoesNotEncloseGivenValues(AssertionInfo info, RangeSet<T> actual,
                                                                                 Range<T>[] ranges) {
    final List<?> enclosedRanges = stream(ranges).filter(actual::encloses).collect(toList());
    if (!enclosedRanges.isEmpty()) {
      throw failures.failure(info, shouldNotEnclose(actual, ranges, enclosedRanges));
    }
  }

  private void assertNotNull(AssertionInfo info, RangeSet<?> actual) {
    Objects.instance().assertNotNull(info, actual);
  }

  private void failIfNull(Object[] array) {
    throwIllegalArgumentExceptionIfTrue(array == null, valuesToLookForIsNull());
  }

  private void failIfNull(Iterable<?> iterable) {
    throwIllegalArgumentExceptionIfTrue(iterable == null, iterableValuesToLookForIsNull());
  }

  private void failIfNull(RangeSet<?> rangeSet) {
    throwIllegalArgumentExceptionIfTrue(rangeSet == null, rangeSetValuesToLookForIsNull());
  }

  private void failIfEmpty(Object[] values) {
    throwIllegalArgumentExceptionIfTrue(values.length == 0, valuesToLookForIsEmpty());
  }

  private void failIfEmpty(Iterable<?> values) {
    throwIllegalArgumentExceptionIfTrue(!values.iterator().hasNext(), iterableValuesToLookForIsEmpty());
  }

  private void failIfEmpty(RangeSet<?> rangeSet) {
    throwIllegalArgumentExceptionIfTrue(rangeSet.isEmpty(), rangeSetValuesToLookForIsEmpty());
  }

  private void failIfNullOrEmpty(Object[] values) {
    failIfNull(values);
    failIfEmpty(values);
  }

  private void failIfNullOrEmpty(Iterable<?> values) {
    failIfNull(values);
    failIfEmpty(values);
  }

  private void failIfNullOrEmpty(RangeSet<?> rangeSet) {
    failIfNull(rangeSet);
    failIfEmpty(rangeSet);
  }
}
