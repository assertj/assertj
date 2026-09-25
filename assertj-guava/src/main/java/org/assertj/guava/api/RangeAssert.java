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
package org.assertj.guava.api;

import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.guava.error.RangeShouldBeClosedInTheLowerBound.shouldHaveClosedLowerBound;
import static org.assertj.guava.error.RangeShouldBeClosedInTheUpperBound.shouldHaveClosedUpperBound;
import static org.assertj.guava.error.RangeShouldBeOpenedInTheLowerBound.shouldHaveOpenedLowerBound;
import static org.assertj.guava.error.RangeShouldBeOpenedInTheUpperBound.shouldHaveOpenedUpperBound;
import static org.assertj.guava.error.RangeShouldHaveLowerEndpointEqual.shouldHaveEqualLowerEndpoint;
import static org.assertj.guava.error.RangeShouldHaveUpperEndpointEqual.shouldHaveEqualUpperEndpoint;
import static org.assertj.guava.util.ExceptionUtils.throwIllegalArgumentExceptionIfTrue;

import java.util.List;

import org.assertj.core.api.AbstractAssert;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

/// Assertions for guava [Range].
///
/// To create an instance of this class, invoke [Assertions#assertThat(Range)]
///
/// @author Marcin Kwaczyński
/// @param <T> the type of elements of the tested Range value
public class RangeAssert<T extends Comparable<T>> extends AbstractAssert<RangeAssert<T>, Range<T>> {

  protected RangeAssert(Range<T> actual) {
    super(actual, RangeAssert.class);
  }

  /// Verifies that the actual [Range] contains the given values.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.closed(10, 12);
  ///
  /// assertThat(range).contains(10, 11, 12);
  /// ```
  ///
  /// @param values the values to look for in actual [Range].
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] does not contain the given values.
  public RangeAssert<T> contains(@SuppressWarnings("unchecked") T... values) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(values == null, "The values to look for should not be null");

    // if both actual and values are empty, then assertion passes.
    if (values.length == 0 && actual.isEmpty()) return myself;
    throwIllegalArgumentExceptionIfTrue(values.length == 0, "The values to look for should not be empty");

    List<T> valuesNotFound = newArrayList();
    for (T value : values) {
      if (!actual.contains(value)) {
        valuesNotFound.add(value);
      }
    }
    if (!valuesNotFound.isEmpty()) {
      throw assertionError(shouldContain(actual, values, valuesNotFound));
    }

    return myself;
  }

  /// Verifies that the actual [Range] does not contain the given values.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.closed(10, 12);
  ///
  /// assertThat(range).doesNotContain(13);
  /// ```
  ///
  /// @param values the values that should not be present in actual [Range].
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] contains the given values.
  public RangeAssert<T> doesNotContain(@SuppressWarnings("unchecked") T... values) {
    isNotNull();

    List<T> valuesFound = newArrayList();
    for (T value : values) {
      if (actual.contains(value)) {
        valuesFound.add(value);
      }
    }
    if (!valuesFound.isEmpty()) {
      throw assertionError(shouldNotContain(actual, values, valuesFound));
    }

    return myself;
  }

  /// Verifies that the actual [Range] lower bound is closed.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.closed(10, 12);
  ///
  /// assertThat(range).hasClosedLowerBound();
  /// ```
  ///
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] lower bound is opened.
  public RangeAssert<T> hasClosedLowerBound() throws AssertionError {
    isNotNull();

    if (actual.lowerBoundType() != BoundType.CLOSED) {
      throw assertionError(shouldHaveClosedLowerBound(actual));
    }

    return myself;
  }

  /// Verifies that the actual [Range] upper bound is closed.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.closed(10, 12);
  ///
  /// assertThat(range).hasClosedUpperBound();
  /// ```
  ///
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] upper bound is opened.
  public RangeAssert<T> hasClosedUpperBound() throws AssertionError {
    isNotNull();

    if (actual.upperBoundType() != BoundType.CLOSED) {
      throw assertionError(shouldHaveClosedUpperBound(actual));
    }

    return myself;
  }

  /// Verifies that the actual [Range] lower endpoint is equal to the given value.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.closed(10, 12);
  ///
  /// assertThat(range).hasLowerEndpointEqualTo(10);
  /// ```
  ///
  /// @param value expected lower bound value.
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] does not have lower endpoint equal to
  ///           the given values.
  public RangeAssert<T> hasLowerEndpointEqualTo(T value) throws AssertionError {
    isNotNull();

    if (!actual.lowerEndpoint().equals(value)) {
      throw assertionError(shouldHaveEqualLowerEndpoint(actual, value));
    }

    return myself;
  }

  /// Verifies that the actual [Range] lower bound is opened.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.open(1, 2);
  ///
  /// assertThat(range).hasOpenedLowerBound();
  /// ```
  ///
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] lower bound is closed.
  public RangeAssert<T> hasOpenedLowerBound() throws AssertionError {
    isNotNull();

    if (actual.lowerBoundType() != BoundType.OPEN) {
      throw assertionError(shouldHaveOpenedLowerBound(actual));
    }

    return myself;
  }

  /// Verifies that the actual [Range] upper bound is opened.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.open(10, 12);
  ///
  /// assertThat(range).hasOpenedUpperBound();
  /// ```
  ///
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] upper bound is closed.
  public RangeAssert<T> hasOpenedUpperBound() throws AssertionError {
    isNotNull();

    if (actual.upperBoundType() != BoundType.OPEN) {
      throw assertionError(shouldHaveOpenedUpperBound(actual));
    }

    return myself;
  }

  /// Verifies that the actual [Range] upper endpoint is equal to the given value.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.open(10, 12);
  ///
  /// assertThat(range).hasUpperEndpointEqualTo(12);
  /// ```
  ///
  /// @param value [Range] expected upper bound value.
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] does not have upper endpoint equal to
  ///           the given values.
  public RangeAssert<T> hasUpperEndpointEqualTo(T value) throws AssertionError {
    isNotNull();

    if (!actual.upperEndpoint().equals(value)) {
      throw assertionError(shouldHaveEqualUpperEndpoint(actual, value));
    }

    return myself;
  }

  /// Verifies that the actual [Range] is empty.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.closedOpen(0, 0);
  ///
  /// assertThat(range).isEmpty();
  /// ```
  ///
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] is not empty.
  public RangeAssert<T> isEmpty() throws AssertionError {
    isNotNull();

    if (!actual.isEmpty()) {
      throw assertionError(shouldBeEmpty(actual));
    }

    return myself;
  }

  /// Verifies that the actual [Range] is not empty.
  ///
  /// Example:
  ///
  /// ```java
  /// Range<Integer> range = Range.closed(0, 0);
  ///
  /// assertThat(range).isNotEmpty();
  /// ```
  ///
  /// @return this [RangeAssert] for assertions chaining.
  /// @throws AssertionError if the actual [Range] is `null`.
  /// @throws AssertionError if the actual [Range] is empty.
  public RangeAssert<T> isNotEmpty() throws AssertionError {
    isNotNull();

    if (actual.isEmpty()) {
      throw assertionError(shouldNotBeEmpty());
    }

    return myself;
  }
}
