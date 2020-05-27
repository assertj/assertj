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

import static java.lang.reflect.Array.getLength;
import static org.assertj.core.error.ShouldBeAnArray.shouldBeAnArray;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContainAtIndex.shouldContainAtIndex;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContainAtIndex.shouldNotContainAtIndex;
import static org.assertj.core.internal.CommonValidations.checkIndexValueIsValid;
import static org.assertj.core.internal.CommonValidations.hasSameSizeAsCheck;
import static org.assertj.core.util.Arrays.isArray;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.Index;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.util.VisibleForTesting;

import java.lang.reflect.Array;
import java.util.Comparator;

/**
 * Assertions for object and primitive two-dimensional arrays. It trades off performance for DRY.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Florent Biville
 */
public class Arrays2D {

  private static final Arrays2D INSTANCE = new Arrays2D();
  private final ComparisonStrategy comparisonStrategy;

  /**
   * Returns the singleton instance of this class based on {@link StandardComparisonStrategy}.
   *
   * @return the singleton instance of this class based on {@link StandardComparisonStrategy}.
   */
  public static Arrays2D instance() {
    return INSTANCE;
  }

  public Arrays2D() {
    this.comparisonStrategy = StandardComparisonStrategy.instance();
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    if (!(comparisonStrategy instanceof ComparatorBasedComparisonStrategy)) return null;
    return ((ComparatorBasedComparisonStrategy) comparisonStrategy).getComparator();
  }

  @VisibleForTesting
  public ComparisonStrategy getComparisonStrategy() {
    return comparisonStrategy;
  }

  public static void assertIsArray(AssertionInfo info, Object array) {
    if (!isArray(array)) throw Failures.instance().failure(info, shouldBeAnArray(array));
  }

  void assertNullOrEmpty(AssertionInfo info, Failures failures, Object array) {
    if (array != null && !isArrayEmpty(array)) throw failures.failure(info, shouldBeNullOrEmpty(array));
  }

  void assertEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (!isArrayEmpty(array)) throw failures.failure(info, shouldBeEmpty(array));
  }

  void assertHasSize(AssertionInfo info, Object array, int expectedSize) {
    assertNotNull(info, array);
    int sizeOfActual = sizeOf(array);
    CommonValidations.checkSizes(array, sizeOfActual, expectedSize, info);
  }

  void assertHasSize(AssertionInfo info, Failures failures, Object array, int expectedSize, int firstDimensionIndex) {
    assertNotNull(info, array);
    int sizeOfActual = sizeOf(array);
    checkSizes(array, failures, sizeOfActual, expectedSize, info, firstDimensionIndex);
  }

  private void checkSizes(Object actual, Failures failures, int sizeOfActual, int sizeOfOther, AssertionInfo info,
    int firstDimensionIndex) {
    if (sizeOfActual != sizeOfOther) throw failures.failure(info, shouldHaveSize(actual, sizeOfActual, sizeOfOther,
      firstDimensionIndex));
  }

  public void assertHasSameSizeAs(AssertionInfo info, Object array, Object other) {
    assertNotNull(info, array);
    assertIsArray(info, array);
    assertIsArray(info, other);
    hasSameSizeAsCheck(info, array, other, sizeOf(array));
  }

  void assertContains(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    assertNotEmpty(info, failures, array);
    checkIndexValueIsValid(index, sizeOf(array) - 1);
    Object actualElement = Array.get(array, index.value);
    if (!areEqual(actualElement, value))
      throw failures.failure(info, shouldContainAtIndex(array, value, index, Array.get(array, index.value),
                                                        comparisonStrategy));
  }

  void assertNotEmpty(AssertionInfo info, Failures failures, Object array) {
    assertNotNull(info, array);
    if (isArrayEmpty(array)) throw failures.failure(info, shouldNotBeEmpty());
  }

  void assertNotEmpty(AssertionInfo info, Failures failures, Object array, ErrorMessageFactory failureMessage) {
    assertNotNull(info, array);
    if (isArrayEmpty(array)) throw failures.failure(info, failureMessage);
  }

  void assertDoesNotContain(AssertionInfo info, Failures failures, Object array, Object value, Index index) {
    assertNotNull(info, array);
    checkIndexValueIsValid(index, Integer.MAX_VALUE);
    if (index.value >= sizeOf(array)) return;
    if (areEqual(Array.get(array, index.value), value))
      throw failures.failure(info, shouldNotContainAtIndex(array, value, index, comparisonStrategy));
  }

  private boolean areEqual(Object actual, Object other) {
    return comparisonStrategy.areEqual(actual, other);
  }

  private static boolean isArrayEmpty(Object array) {
    return sizeOf(array) == 0;
  }

  private static void assertNotNull(AssertionInfo info, Object array) {
    Objects.instance().assertNotNull(info, array);
  }

  private static int sizeOf(Object array) {
    if (array instanceof Object[]) return ((Object[]) array).length;
    return getLength(array);
  }

}
