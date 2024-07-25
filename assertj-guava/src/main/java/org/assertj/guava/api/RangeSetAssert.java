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

import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldContainAnyOf.shouldContainAnyOf;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldNotContain.shouldNotContain;
import static org.assertj.core.util.IterableUtil.toArray;
import static org.assertj.guava.error.RangeSetShouldEnclose.shouldEnclose;
import static org.assertj.guava.error.RangeSetShouldEncloseAnyOf.shouldEncloseAnyOf;
import static org.assertj.guava.error.RangeSetShouldIntersect.shouldIntersect;
import static org.assertj.guava.error.RangeSetShouldIntersectAnyOf.shouldIntersectAnyOf;
import static org.assertj.guava.error.RangeSetShouldNotEnclose.shouldNotEnclose;
import static org.assertj.guava.error.RangeSetShouldNotIntersect.shouldNotIntersect;

import java.util.List;

import org.assertj.core.api.AbstractAssert;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;

/**
 * Assertion for guava {@link RangeSet}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(RangeSet)}</code>.
 * <p>
 *
 * @author Ilya Koshaleu
 *
 * @param <T> the type of the tested RangeSet elements
 */
public class RangeSetAssert<T extends Comparable<T>> extends AbstractAssert<RangeSetAssert<T>, RangeSet<T>> {

  protected RangeSetAssert(RangeSet<T> actual) {
    super(actual, RangeSetAssert.class);
  }

  /**
   * Verifies that the given {@code RangeSet} has specific {@code size} of disconnected {@code Range} elements.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).hasSize(3);</code></pre>
   *
   * @param size expected amount of disconnected {@code Range} elements.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual size of {@code RangeSet} is different from the expected {@code size}.
   */
  public RangeSetAssert<T> hasSize(int size) {
    isNotNull();
    assertHasSize(size);
    return myself;
  }

  private void assertHasSize(int expectedSize) {
    int actualSize = actual.asRanges().size();
    if (actualSize != expectedSize) throwAssertionError(shouldHaveSize(actual, actualSize, expectedSize));
  }

  /**
   * Verifies that the given {@code RangeSet} contains the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).contains(50, 270, 550);</code></pre>
   *
   * @param values the values to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain the given {@code values}.
   * @throws NullPointerException if values are null.
   * @throws IllegalArgumentException if values are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> contains(T... values) {
    isNotNull();
    assertContains(values);
    return myself;
  }

  private void assertContains(T[] values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    if (actual.isEmpty() && values.length == 0) return;
    failIfEmpty(values, "values");
    assertRangeSetContainsGivenValues(actual, values);
  }

  /**
   * Verifies that the given {@code RangeSet} contains all the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).containsAll(Arrays.asList(50, 270, 550));</code></pre>
   *
   * @param values the values to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain all the given {@code values}.
   * @throws NullPointerException if values are null.
   * @throws IllegalArgumentException if values are empty while actual is not empty.
   */
  public RangeSetAssert<T> containsAll(Iterable<T> values) {
    isNotNull();
    assertContainsAll(values);
    return myself;
  }

  private void assertContainsAll(Iterable<T> values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    if (actual.isEmpty() && !values.iterator().hasNext()) return;
    failIfEmpty(values, "values");
    assertRangeSetContainsGivenValues(actual, toArray(values, Comparable.class));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void assertRangeSetContainsGivenValues(RangeSet actual, Comparable[] values) {
    List<?> elementsNotFound = stream(values).filter(value -> !actual.contains(value)).collect(toList());
    if (!elementsNotFound.isEmpty()) throwAssertionError(shouldContain(actual, values, elementsNotFound));
  }

  /**
   * Verifies that the given {@code RangeSet} contains at least one of the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).containsAnyOf(150, 250, 700);</code></pre>
   *
   * @param values the values to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain at least one of the given {@code values}.
   * @throws NullPointerException if values are null.
   * @throws IllegalArgumentException if values are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> containsAnyOf(T... values) {
    isNotNull();
    assertContainsAnyOf(values);
    return myself;
  }

  private void assertContainsAnyOf(T[] values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && values.length == 0) return;
    failIfEmpty(values, "values");
    assertRangeSetContainsAnyGivenValues(actual, values);
  }

  /**
   * Verifies that the given {@code RangeSet} contains at least one of the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).containsAnyRangesOf(Arrays.asList(150, 250, 700));</code></pre>
   *
   * @param values the values to look for in actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not contain at least one of the given {@code values}.
   * @throws NullPointerException if values are null.
   * @throws IllegalArgumentException if values are empty while actual is not empty.
   */
  public RangeSetAssert<T> containsAnyRangesOf(Iterable<T> values) {
    isNotNull();
    assertContainsAnyRangesOf(values);
    return myself;
  }

  private void assertContainsAnyRangesOf(Iterable<T> values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    if (actual.isEmpty() && !values.iterator().hasNext()) return;
    failIfEmpty(values, "values");
    assertRangeSetContainsAnyGivenValues(actual, toArray(values, Comparable.class));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void assertRangeSetContainsAnyGivenValues(RangeSet actual, Comparable[] values) {
    boolean match = stream(values).anyMatch(actual::contains);
    if (!match) throwAssertionError(shouldContainAnyOf(actual, values));
  }

  /**
   * Verifies that the given {@code RangeSet} does not contain any of the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotContain(150, 320, 650);</code></pre>
   *
   * @param values the values that should not be present in actual {@code RangeSet}
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} contains any of the given {@code values}.
   * @throws NullPointerException if values are null.
   * @throws IllegalArgumentException if values are empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> doesNotContain(T... values) {
    isNotNull();
    assertDoesNotContain(values);
    return myself;
  }

  private void assertDoesNotContain(T[] values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    failIfEmpty(values, "values");
    assertRangeSetDoesNotContainGivenValues(actual, values);
  }

  /**
   * Verifies that the given {@code RangeSet} does not contain any of the given values.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotContain(Arrays.asList(150, 320, 650));</code></pre>
   *
   * @param values the values that should not be present in actual {@code RangeSet}
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} contains any of the given {@code values}.
   * @throws NullPointerException if values are null.
   * @throws IllegalArgumentException if values are empty.
   */
  public RangeSetAssert<T> doesNotContainAll(Iterable<T> values) {
    isNotNull();
    assertDoesNotContainAll(values);
    return myself;
  }

  private void assertDoesNotContainAll(Iterable<T> values) {
    requireNonNull(values, shouldNotBeNull("values")::create);
    failIfEmpty(values, "values");
    assertRangeSetDoesNotContainGivenValues(actual, toArray(values, Comparable.class));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private void assertRangeSetDoesNotContainGivenValues(RangeSet actual, Comparable[] values) {
    List<?> elementsFound = stream(values).filter(actual::contains).collect(toList());
    if (!elementsFound.isEmpty()) throwAssertionError(shouldNotContain(actual, values, elementsFound));
  }

  /**
   * Verifies that the actual {@code RangeSet} is empty.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * assertThat(rangeSet).isEmpty();</code></pre>
   *
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} is not empty.
   */
  public RangeSetAssert<T> isEmpty() {
    isNotNull();
    assertEmpty();
    return myself;
  }

  private void assertEmpty() {
    if (!actual.isEmpty()) throwAssertionError(shouldBeEmpty(actual));
  }

  /**
   * Verifies that the actual {@code RangeSet} is not empty.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).isNotEmpty();</code></pre>
   *
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} is empty.
   */
  public RangeSetAssert<T> isNotEmpty() {
    isNotNull();
    assertNotEmpty();
    return myself;
  }

  private void assertNotEmpty() {
    if (actual.isEmpty()) throwAssertionError(shouldNotBeEmpty());
  }

  /**
   * Verifies that the actual {@code RangeSet} is {@code null} or empty.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * assertThat(rangeSet).isNullOrEmpty();</code></pre>
   *
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is not {@code null} or not empty.
   */
  public RangeSetAssert<T> isNullOrEmpty() {
    assertNullOrEmpty();
    return myself;
  }

  private void assertNullOrEmpty() {
    if (actual != null && !actual.isEmpty()) throwAssertionError(shouldBeNullOrEmpty(actual));
  }

  /**
   * Verifies that the given {@code RangeSet} intersects all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersects(Range.closed(50, 150),
   *                                 Range.openClosed(170, 220),
   *                                 Range.open(520, 570));</code></pre>
   *
   * @param ranges the ranges to check whether they intersect the given {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> intersects(Range<T>... ranges) {
    isNotNull();
    assertIntersects(ranges);
    return myself;
  }

  private void assertIntersects(Range<T>[] ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetIntersectsGivenValues(ranges);
  }

  /**
   * Verifies that the given {@code RangeSet} intersects all ranges from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAll(ImmutableRangeSet.of(Range.closed(50, 250)));</code></pre>
   *
   * @param rangeSet the range set to check whether it intersects the actual {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the ranges from the given range set.
   * @throws NullPointerException if range set is null.
   * @throws IllegalArgumentException if range set is empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAll(RangeSet<T> rangeSet) {
    isNotNull();
    assertIntersectsAll(rangeSet);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertIntersectsAll(RangeSet<T> rangeSet) {
    requireNonNull(rangeSet, shouldNotBeNull("rangeSet")::create);
    // Should pass if both actual and expected are empty
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetIntersectsGivenValues(toArray(rangeSet.asRanges(), Range.class));
  }

  /**
   * Verifies that the given {@code RangeSet} intersects all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAll(Arrays.asList(Range.closed(50, 150),
   *                                                  Range.openClosed(170, 220),
   *                                                  Range.open(520, 570)));</code></pre>
   *
   * @param ranges the ranges to check whether they all intersect the given {@code RangeSet}.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect all the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAll(Iterable<Range<T>> ranges) {
    isNotNull();
    assertIntersectsAll(ranges);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertIntersectsAll(Iterable<Range<T>> ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && !ranges.iterator().hasNext()) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetIntersectsGivenValues(toArray(ranges, Range.class));
  }

  private void assertRangeSetIntersectsGivenValues(Range<T>[] ranges) {
    List<?> notIntersected = stream(ranges).filter(range -> !actual.intersects(range)).collect(toList());
    if (!notIntersected.isEmpty()) throwAssertionError(shouldIntersect(actual, ranges, notIntersected));
  }

  /**
   * Verifies that the given {@link RangeSet} intersects at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAnyOf(Range.closed(50, 150),
   *                                      Range.open(170, 190),
   *                                      Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} intersects at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect any of the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> intersectsAnyOf(Range<T>... ranges) {
    isNotNull();
    assertIntersectsAnyOf(ranges);
    return myself;
  }

  private void assertIntersectsAnyOf(Range<T>[] ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetIntersectsAnyOfGivenValues(ranges);
  }

  /**
   * Verifies that the given {@code RangeSet} intersects at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAnyRangesOf(Arrays.asList(Range.closed(50, 150),
   *                                                          Range.open(170, 190),
   *                                                          Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} intersects at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect any of the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAnyRangesOf(Iterable<Range<T>> ranges) {
    isNotNull();
    assertIntersectsAnyRangesOf(ranges);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertIntersectsAnyRangesOf(Iterable<Range<T>> ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && !ranges.iterator().hasNext()) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetIntersectsAnyOfGivenValues(toArray(ranges, Range.class));
  }

  /**
   * Verifies that the given {@code RangeSet} intersects at least one range of the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).intersectsAnyRangesOf(ImmutableRangeSet.of(Range.close(50, 150)));</code></pre>
   *
   * @param rangeSet the range set with ranges to check whether the actual {@code RangeSet} intersects at least one of
   *                 them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not intersect any of the ranges from the given ranges
   *         set.
   * @throws NullPointerException if range set is null.
   * @throws IllegalArgumentException if range set is empty while actual is not empty.
   */
  public RangeSetAssert<T> intersectsAnyRangesOf(RangeSet<T> rangeSet) {
    isNotNull();
    assertIntersectsAnyRangesOf(rangeSet);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertIntersectsAnyRangesOf(RangeSet<T> rangeSet) {
    requireNonNull(rangeSet, shouldNotBeNull("rangeSet")::create);
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetIntersectsAnyOfGivenValues(toArray(rangeSet.asRanges(), Range.class));
  }

  private void assertRangeSetIntersectsAnyOfGivenValues(Range<T>[] ranges) {
    boolean intersects = stream(ranges).anyMatch(actual::intersects);
    if (!intersects) throwAssertionError(shouldIntersectAnyOf(actual, ranges));
  }

  /**
   * Verifies that the given {@code RangeSet} does not intersect the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotIntersect(Range.closed(120, 150),
   *                                       Range.open(302, 490),
   *                                       Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not intersect them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> doesNotIntersect(Range<T>... ranges) {
    isNotNull();
    assertDoesNotIntersect(ranges);
    return myself;
  }

  private void assertDoesNotIntersect(Range<T>[] ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    failIfEmpty(ranges, "ranges");
    assertRangeSetDoesNotIntersectGivenValues(ranges);
  }

  /**
   * Verifies that the given {@code RangeSet} does not intersect ranges from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotIntersectAnyRangeFrom(ImmutableRangeSet.of(Range.close(120, 170)));</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} does not intersect ranges from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects the ranges from the given range set.
   * @throws NullPointerException if range set is null.
   * @throws IllegalArgumentException if range set is empty.
   */
  public RangeSetAssert<T> doesNotIntersectAnyRangeFrom(RangeSet<T> rangeSet) {
    isNotNull();
    assertDoesNotIntersectAnyRangeFrom(rangeSet);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertDoesNotIntersectAnyRangeFrom(RangeSet<T> rangeSet) {
    requireNonNull(rangeSet, shouldNotBeNull("rangeSet")::create);
    failIfEmpty(rangeSet);
    assertRangeSetDoesNotIntersectGivenValues(toArray(rangeSet.asRanges(), Range.class));
  }

  /**
   * Verifies that the given {@code RangeSet} does not intersect all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotIntersectAnyRangeFrom(Arrays.asList(Range.closed(120, 150),
   *                                                                 Range.open(302, 490),
   *                                                                 Range.open(600, 670));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not intersect them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} intersects all the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty.
   */
  public RangeSetAssert<T> doesNotIntersectAnyRangeFrom(Iterable<Range<T>> ranges) {
    isNotNull();
    assertDoesNotIntersectAnyRangeFrom(ranges);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertDoesNotIntersectAnyRangeFrom(Iterable<Range<T>> ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    failIfEmpty(ranges, "ranges");
    assertRangeSetDoesNotIntersectGivenValues(toArray(ranges, Range.class));
  }

  private void assertRangeSetDoesNotIntersectGivenValues(Range<T>[] ranges) {
    List<?> intersected = stream(ranges).filter(actual::intersects).collect(toList());
    if (!intersected.isEmpty()) throwAssertionError(shouldNotIntersect(actual, ranges, intersected));
  }

  /**
   * Verifies that the given {@code RangeSet} encloses the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).encloses(Range.closed(0, 10),
   *                               Range.open(50, 60),
   *                               Range.open(90, 100));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> encloses(Range<T>... ranges) {
    isNotNull();
    assertEncloses(ranges);
    return myself;
  }

  private void assertEncloses(Range<T>[] ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetEnclosesGivenValues(ranges);
  }

  /**
   * Verifies that the given {@code RangeSet} encloses all the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAll(Arrays.asList(Range.closed(0, 10),
   *                                                Range.open(50, 60),
   *                                                Range.open(90, 100)));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses all of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose all the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAll(Iterable<Range<T>> ranges) {
    isNotNull();
    assertEnclosesAll(ranges);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertEnclosesAll(Iterable<Range<T>> ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && !ranges.iterator().hasNext()) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetEnclosesGivenValues(toArray(ranges, Range.class));
  }

  /**
   * Verifies that the given {@code RangeSet} encloses all ranges from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAll(ImmutableRangeSet.of(Range.closed(0, 50));</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} encloses all range from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose all ranges from the given range set.
   * @throws NullPointerException if range set is null.
   * @throws IllegalArgumentException if range set is empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAll(RangeSet<T> rangeSet) {
    isNotNull();
    assertEnclosesAll(rangeSet);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertEnclosesAll(RangeSet<T> rangeSet) {
    requireNonNull(rangeSet, shouldNotBeNull("rangeSet")::create);
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetEnclosesGivenValues(toArray(rangeSet.asRanges(), Range.class));
  }

  private void assertRangeSetEnclosesGivenValues(Range<T>[] ranges) {
    List<?> notEnclosed = stream(ranges).filter(range -> !actual.encloses(range)).collect(toList());
    if (!notEnclosed.isEmpty()) throwAssertionError(shouldEnclose(actual, ranges, notEnclosed));
  }

  /**
   * Verifies that the given {@code RangeSet} encloses at least one of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAnyOf(Range.closed(-10, 10),
   *                                    Range.open(150, 260),
   *                                    Range.open(290, 296));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose at least one of the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> enclosesAnyOf(Range<T>... ranges) {
    isNotNull();
    assertEnclosesAnyOf(ranges);
    return myself;
  }

  private void assertEnclosesAnyOf(Range<T>[] ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && ranges.length == 0) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetEnclosesAnyOfGivenValues(ranges);
  }

  /**
   * Verifies that the given {@code RangeSet} encloses at least one range of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).enclosesAnyRangesOf(Arrays.asList(Range.closed(-10, 10),
   *                                                        Range.open(150, 260),
   *                                                        Range.open(290, 296)));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} encloses at least one of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose at least one of the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAnyRangesOf(Iterable<Range<T>> ranges) {
    isNotNull();
    assertEnclosesAnyRangesOf(ranges);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertEnclosesAnyRangesOf(Iterable<Range<T>> ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    if (actual.isEmpty() && !ranges.iterator().hasNext()) return;
    failIfEmpty(ranges, "ranges");
    assertRangeSetEnclosesAnyOfGivenValues(toArray(ranges, Range.class));
  }

  /**
   * Verifies that the given {@code RangeSet} encloses at least one range from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * RangeSet&lt;Integer&gt; enclosedSet = TreeRangeSet.create();
   *
   * enclosedSet.add(Range.closed(-10, 10));
   * enclosedSet.add(Range.open(150, 260));
   * enclosedSet.add(Range.open(290, 296));
   *
   * assertThat(rangeSet).enclosesAll(enclosedSet);</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} encloses at least one range from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} does not enclose at least one range from the given range set.
   * @throws NullPointerException if range set is null.
   * @throws IllegalArgumentException if range set is empty while actual is not empty.
   */
  public RangeSetAssert<T> enclosesAnyRangesOf(RangeSet<T> rangeSet) {
    isNotNull();
    assertEnclosesAnyRangesOf(rangeSet);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertEnclosesAnyRangesOf(RangeSet<T> rangeSet) {
    requireNonNull(rangeSet, shouldNotBeNull("rangeSet")::create);
    if (actual.isEmpty() && rangeSet.isEmpty()) return;
    failIfEmpty(rangeSet);
    assertRangeSetEnclosesAnyOfGivenValues(toArray(rangeSet.asRanges(), Range.class));
  }

  private void assertRangeSetEnclosesAnyOfGivenValues(Range<T>[] ranges) {
    boolean match = stream(ranges).anyMatch(actual::encloses);
    if (!match) throwAssertionError(shouldEncloseAnyOf(actual, ranges));
  }

  /**
   * Verifies that the given {@code RangeSet} does not enclose the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotEnclose(Range.closed(-10, 10),
   *                                     Range.open(150, 160),
   *                                     Range.open(590, 700));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not enclose them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses any of the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty.
   */
  @SafeVarargs
  public final RangeSetAssert<T> doesNotEnclose(Range<T>... ranges) {
    isNotNull();
    assertDoesNotEnclose(ranges);
    return myself;
  }

  private void assertDoesNotEnclose(Range<T>[] ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    failIfEmpty(ranges, "ranges");
    assertRangeSetDoesNotEncloseGivenValues(ranges);
  }

  /**
   * Verifies that the given {@code RangeSet} does not enclose any of the given ranges.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * assertThat(rangeSet).doesNotEncloseAnyRangesOf(Arrays.asList(Range.closed(-10, 10),
   *                                                              Range.open(150, 160),
   *                                                              Range.open(590, 700));</code></pre>
   *
   * @param ranges the ranges to check whether the actual {@code RangeSet} does not enclose any of them.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses any of the given ranges.
   * @throws NullPointerException if ranges are null.
   * @throws IllegalArgumentException if ranges are empty.
   */
  public RangeSetAssert<T> doesNotEncloseAnyRangesOf(Iterable<Range<T>> ranges) {
    isNotNull();
    assertDoesNotEncloseAnyRangesOf(ranges);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertDoesNotEncloseAnyRangesOf(Iterable<Range<T>> ranges) {
    requireNonNull(ranges, shouldNotBeNull("ranges")::create);
    failIfEmpty(ranges, "ranges");
    assertRangeSetDoesNotEncloseGivenValues(toArray(ranges, Range.class));
  }

  /**
   * Verifies that the given {@code RangeSet} does not enclose any range from the given range set.
   * <p>
   * Example:
   *
   * <pre><code class='java'> RangeSet&lt;Integer&gt; rangeSet = TreeRangeSet.create();
   *
   * rangeSet.add(Range.closed(0, 100));
   * rangeSet.add(Range.closed(200, 300));
   * rangeSet.add(Range.closed(500, 600));
   *
   * RangeSet&lt;Integer&gt; enclosedSet = TreeRangeSet.create();
   *
   * enclosedSet.add(Range.closed(-10, 10));
   * enclosedSet.add(Range.open(150, 360));
   * enclosedSet.add(Range.open(590, 690));
   *
   * assertThat(rangeSet).doesNotEncloseAnyRangesOf(enclosedSet);</code></pre>
   *
   * @param rangeSet the range set to check whether the actual {@code RangeSet} does not enclose any ranges from it.
   * @return this {@link RangeSetAssert} for assertions chaining.
   * @throws AssertionError if the actual {@code RangeSet} is {@code null}.
   * @throws AssertionError if the actual {@code RangeSet} encloses any range from the given range set.
   * @throws NullPointerException if range set is null.
   * @throws IllegalArgumentException if range set is empty.
   */
  public RangeSetAssert<T> doesNotEncloseAnyRangesOf(RangeSet<T> rangeSet) {
    isNotNull();
    assertDoesNotEncloseAnyRangesOf(rangeSet);
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertDoesNotEncloseAnyRangesOf(RangeSet<T> rangeSet) {
    requireNonNull(rangeSet, shouldNotBeNull("rangeSet")::create);
    failIfEmpty(rangeSet);
    assertRangeSetDoesNotEncloseGivenValues(toArray(rangeSet.asRanges(), Range.class));
  }

  private void assertRangeSetDoesNotEncloseGivenValues(Range<T>[] ranges) {
    List<?> enclosed = stream(ranges).filter(actual::encloses).collect(toList());
    if (!enclosed.isEmpty()) throwAssertionError(shouldNotEnclose(actual, ranges, enclosed));
  }

  private static <T> void failIfEmpty(T[] array, String label) {
    if (array.length == 0) throw new IllegalArgumentException("Expecting " + label + " not to be empty");
  }

  private static <T> void failIfEmpty(Iterable<T> iterable, String label) {
    if (!iterable.iterator().hasNext()) throw new IllegalArgumentException("Expecting " + label + " not to be empty");
  }

  private static <T> void failIfEmpty(RangeSet<?> rangeSet) {
    if (rangeSet.isEmpty()) throw new IllegalArgumentException("Expecting rangeSet not to be empty");
  }

}
