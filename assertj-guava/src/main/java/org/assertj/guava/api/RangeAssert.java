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

/**
 * Assertions for guava {@link com.google.common.collect.Range}.
 * <p>
 * To create an instance of this class, invoke <code>{@link
 * org.assertj.guava.api.Assertions#assertThat(com.google.common.collect.Range)}</code>
 * <p>
 *
 * @author Marcin Kwaczyński
 *
 * @param <T> the type of elements of the tested Range value
 */
public class RangeAssert<T extends Comparable<T>> extends AbstractAssert<RangeAssert<T>, Range<T>> {

  protected RangeAssert(final Range<T> actual) {
    super(actual, RangeAssert.class);
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} contains the given values.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).contains(10, 11, 12);</code></pre>
   *
   * @param values the values to look for in actual {@link com.google.common.collect.Range}.
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} does not contain the given values.
   */
  public RangeAssert<T> contains(@SuppressWarnings("unchecked") final T... values) {
    isNotNull();
    throwIllegalArgumentExceptionIfTrue(values == null, "The values to look for should not be null");

    // if both actual and values are empty, then assertion passes.
    if (values.length == 0 && actual.isEmpty()) return myself;
    throwIllegalArgumentExceptionIfTrue(values.length == 0, "The values to look for should not be empty");

    final List<T> valuesNotFound = newArrayList();
    for (final T value : values) {
      if (!actual.contains(value)) {
        valuesNotFound.add(value);
      }
    }
    if (!valuesNotFound.isEmpty()) {
      throw assertionError(shouldContain(actual, values, valuesNotFound));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} does not contain the given values.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).doesNotContain(13);</code></pre>
   *
   * @param values the values that should not be present in actual {@link com.google.common.collect.Range}.
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} contains the given values.
   */
  public RangeAssert<T> doesNotContain(@SuppressWarnings("unchecked") final T... values) {
    isNotNull();

    final List<T> valuesFound = newArrayList();
    for (final T value : values) {
      if (actual.contains(value)) {
        valuesFound.add(value);
      }
    }
    if (!valuesFound.isEmpty()) {
      throw assertionError(shouldNotContain(actual, values, valuesFound));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} lower bound is closed.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).hasClosedLowerBound();</code></pre>
   *
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} lower bound is opened.
   */
  public RangeAssert<T> hasClosedLowerBound() throws AssertionError {
    isNotNull();

    if (actual.lowerBoundType() != BoundType.CLOSED) {
      throw assertionError(shouldHaveClosedLowerBound(actual));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} upper bound is closed.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).hasClosedUpperBound();</code></pre>
   *
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} upper bound is opened.
   */
  public RangeAssert<T> hasClosedUpperBound() throws AssertionError {
    isNotNull();

    if (actual.upperBoundType() != BoundType.CLOSED) {
      throw assertionError(shouldHaveClosedUpperBound(actual));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} lower endpoint is equal to the given value.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.closed(10, 12);
   *
   * assertThat(range).hasLowerEndpointEqualTo(10);</code></pre>
   *
   * @param value {@link com.google.common.collect.Range} expected lower bound value.
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} does not have lower endpoint equal to
   *           the given values.
   */
  public RangeAssert<T> hasLowerEndpointEqualTo(final T value) throws AssertionError {
    isNotNull();

    if (!actual.lowerEndpoint().equals(value)) {
      throw assertionError(shouldHaveEqualLowerEndpoint(actual, value));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} lower bound is opened.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.open(1, 2);
   *
   * assertThat(range).hasOpenedLowerBound();</code></pre>
   *
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} lower bound is closed.
   */
  public RangeAssert<T> hasOpenedLowerBound() throws AssertionError {
    isNotNull();

    if (actual.lowerBoundType() != BoundType.OPEN) {
      throw assertionError(shouldHaveOpenedLowerBound(actual));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} upper bound is opened.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.open(10, 12);
   *
   * assertThat(range).hasOpenedUpperBound();</code></pre>
   *
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} upper bound is closed.
   */
  public RangeAssert<T> hasOpenedUpperBound() throws AssertionError {
    isNotNull();

    if (actual.upperBoundType() != BoundType.OPEN) {
      throw assertionError(shouldHaveOpenedUpperBound(actual));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} upper endpoint is equal to the given value.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.open(10, 12);
   *
   * assertThat(range).hasUpperEndpointEqualTo(12);</code></pre>
   *
   * @param value {@link com.google.common.collect.Range} expected upper bound value.
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} does not have upper endpoint equal to
   *           the given values.
   */
  public RangeAssert<T> hasUpperEndpointEqualTo(final T value) throws AssertionError {
    isNotNull();

    if (!actual.upperEndpoint().equals(value)) {
      throw assertionError(shouldHaveEqualUpperEndpoint(actual, value));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} is empty.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.closedOpen(0, 0);
   *
   * assertThat(range).isEmpty();</code></pre>
   *
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is not empty.
   */
  public RangeAssert<T> isEmpty() throws AssertionError {
    isNotNull();

    if (!actual.isEmpty()) {
      throw assertionError(shouldBeEmpty(actual));
    }

    return myself;
  }

  /**
   * Verifies that the actual {@link com.google.common.collect.Range} is not empty.<br>
   * <p>
   * Example :
   *
   * <pre><code class='java'> Range&lt;Integer&gt; range = Range.closed(0, 0);
   *
   * assertThat(range).isNotEmpty();</code></pre>
   *
   * @return this {@link RangeAssert} for assertions chaining.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is {@code null}.
   * @throws AssertionError if the actual {@link com.google.common.collect.Range} is empty.
   */
  public RangeAssert<T> isNotEmpty() throws AssertionError {
    isNotNull();

    if (actual.isEmpty()) {
      throw assertionError(shouldNotBeEmpty());
    }

    return myself;
  }
}
