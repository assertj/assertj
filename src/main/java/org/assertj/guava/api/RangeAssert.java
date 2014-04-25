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
 * Copyright 2012-2013 the original author or authors.
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
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Objects;
import org.assertj.core.util.VisibleForTesting;

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;

/**
 * Assertions for guava {@link com.google.common.collect.Range}.
 * <p>
 * To create an instance of this class, invoke <code>{@link
 * org.assertj.guava.api.Assertions#assertThat(com.google.common.collect.Range)}</code>
 * <p>
 *
 * @param <T> the type of elements of the tested Range value
 * @author Marcin Kwaczyński
 */
public class RangeAssert<T extends Comparable<T>> extends AbstractAssert<RangeAssert<T>, Range<T>> {

  @VisibleForTesting
  Failures failures = Failures.instance();

  protected RangeAssert(final Range<T> actual) {
    super(actual, RangeAssert.class);
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} contains the given values.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).contains(10, 11, 12);
   * </pre>
   *
   * @param values the values to look for in actual {@link com.google.common.collect.Range}.
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} does not contain the given values.
   */
  public RangeAssert<T> contains(final T... values) {
    Objects.instance().assertNotNull(info, actual);
    throwIllegalArgumentExceptionIfTrue(values == null, "The values to look for should not be null");

    // if both actual and values are empty, then assertion passes.
    if (values.length == 0 && actual.isEmpty()) return this;
    throwIllegalArgumentExceptionIfTrue(values.length == 0, "The values to look for should not be empty");

    final List<T> valuesNotFound = newArrayList();
    for (final T value : values) {
      if (!actual.contains(value)) {
        valuesNotFound.add(value);
      }
    }
    if (!valuesNotFound.isEmpty()) {
      throw failures.failure(info, shouldContain(actual, values, valuesNotFound));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} does not contain the given values.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).doesNotContain(13);
   * </pre>
   *
   * @param values the values to look for in actual {@link com.google.common.collect.Range}.
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} contains the given values.
   */
  public RangeAssert<T> doesNotContain(final T... values) {
    Objects.instance().assertNotNull(info, actual);

    final List<T> valuesFound = newArrayList();
    for (final T value : values) {
      if (actual.contains(value)) {
        valuesFound.add(value);
      }
    }
    if (!valuesFound.isEmpty()) {
      throw failures.failure(info, shouldNotContain(actual, values, valuesFound));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} lower bound is closed.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).hasClosedLowerBound();
   * </pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} lower bound is opened.
   */
  public RangeAssert<T> hasClosedLowerBound() throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (actual.lowerBoundType() != BoundType.CLOSED) {
      throw failures.failure(info, shouldHaveClosedLowerBound(actual));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} upper bound is closed.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).hasClosedUpperBound();
   * </pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} upper bound is opened.
   */
  public RangeAssert<T> hasClosedUpperBound() throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (actual.upperBoundType() != BoundType.CLOSED) {
      throw failures.failure(info, shouldHaveClosedUpperBound(actual));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} lower endpoint is equal to given value.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).hasLowerEndpoint(10);
   * </pre>
   *
   * @param value {@link com.google.common.collect.Range} expected lower bound value.
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} does not have lower endpoint equal to
   *           the given values.
   */
  public RangeAssert<T> hasLowerEndpointEqualTo(final T value) throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (!actual.lowerEndpoint().equals(value)) {
      throw failures.failure(info, shouldHaveEqualLowerEndpoint(actual, value, actual.lowerEndpoint()));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} lower bound is opened.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.openedClosed(10, 12);
   *
   * assertThat(range).hasOpenedLowerBound();
   * </pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} lower bound is closed.
   */
  public RangeAssert<T> hasOpenedLowerBound() throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (actual.lowerBoundType() != BoundType.OPEN) {
      throw failures.failure(info, shouldHaveOpenedLowerBound(actual));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} upper bound is opened.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closedOpen(10, 12);
   *
   * assertThat(range).hasOpenedUpperBound();
   * </pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} upper bound is closed.
   */
  public RangeAssert<T> hasOpenedUpperBound() throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (actual.upperBoundType() != BoundType.OPEN) {
      throw failures.failure(info, shouldHaveOpenedUpperBound(actual));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} upper endpoint is equal to given value.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).hasUpperEndpoint(12);
   * </pre>
   *
   * @param value {@link com.google.common.collect.Range} expected upper bound value.
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} does not have upper endpoint equal to
   *           the given values.
   */
  public RangeAssert<T> hasUpperEndpointEqualTo(final T value) throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (!actual.upperEndpoint().equals(value)) {
      throw failures.failure(info, shouldHaveEqualUpperEndpoint(actual, value, actual.lowerEndpoint()));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} is empty.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closedOpen(10, 12);
   *
   * assertThat(range).isEmpty();
   * </pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is not empty.
   */
  public RangeAssert<T> isEmpty() throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (!actual.isEmpty()) {
      throw failures.failure(info, shouldBeEmpty(actual));
    }

    return this;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} is not empty.<br>
   * <p>
   * Example :
   *
   * <pre>
   * Range&lt;Integer&gt; range = Range.closedOpen(10, 12);
   *
   * assertThat(range).isNotEmpty();
   * </pre>
   *
   * @return this {@link OptionalAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is empty.
   */
  public RangeAssert<T> isNotEmpty() throws AssertionError {
    Objects.instance().assertNotNull(info, actual);

    if (actual.isEmpty()) {
      throw failures.failure(info, shouldNotBeEmpty());
    }

    return this;
  }
}
