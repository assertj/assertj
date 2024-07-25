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

import static com.google.common.base.Preconditions.checkArgument;
import static org.assertj.guava.error.MultisetShouldContainAtLeastTimes.shouldContainAtLeastTimes;
import static org.assertj.guava.error.MultisetShouldContainAtMostTimes.shouldContainAtMostTimes;
import static org.assertj.guava.error.MultisetShouldContainTimes.shouldContainTimes;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.ObjectAssert;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

/**
 *
 * Assertions for guava {@link Multiset}.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(Multiset)}</code>
 * <p>
 *
 * @author Max Daniline
 *
 * @param <T> the type of elements contained in the tested Multiset value
 */
public class MultisetAssert<T> extends AbstractIterableAssert<MultisetAssert<T>, Multiset<? extends T>, T, ObjectAssert<T>> {

  protected MultisetAssert(Multiset<? extends T> actual) {
    super(actual, MultisetAssert.class);
  }

  /**
   * Verifies the actual {@link Multiset} contains the given value <b>exactly</b> the given number of times.
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multiset&lt;String&gt; actual = HashMultiset.create();
   * actual.add("shoes", 2);
   *
   * // assertion succeeds
   * assertThat(actual).contains(2, "shoes");
   *
   * // assertions fail
   * assertThat(actual).contains(1, "shoes");
   * assertThat(actual).contains(3, "shoes");</code></pre>
   *
   * @param expectedCount the exact number of times the given value should appear in the set
   * @param expected the value which to expect
   *
   * @return this {@link MultisetAssert} for fluent chaining
   *
   * @throws AssertionError if the actual {@link Multiset} is null
   * @throws AssertionError if the actual {@link Multiset} contains the given value a number of times different to the given count
   */
  public MultisetAssert<T> contains(int expectedCount, T expected) {
    isNotNull();
    checkArgument(expectedCount >= 0, "The expected count should not be negative.");
    int actualCount = actual.count(expected);
    if (actualCount != expectedCount) {
      throw assertionError(shouldContainTimes(actual, expected, expectedCount, actualCount));
    }
    return myself;
  }

  /**
   * Verifies the actual {@link Multiset} contains the given value <b>at least</b> the given number of times.
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multiset&lt;String&gt; actual = HashMultiset.create();
   * actual.add("shoes", 2);
   *
   * // assertions succeed
   * assertThat(actual).containsAtLeast(1, "shoes");
   * assertThat(actual).containsAtLeast(2, "shoes");
   *
   * // assertion fails
   * assertThat(actual).containsAtLeast(3, "shoes");</code></pre>
   *
   * @param minimumCount the minimum number of times the given value should appear in the set
   * @param expected the value which to expect
   *
   * @return this {@link MultisetAssert} for fluent chaining
   *
   * @throws AssertionError if the actual {@link Multiset} is null
   * @throws AssertionError if the actual {@link Multiset} contains the given value fewer times than the given count
   */
  public MultisetAssert<T> containsAtLeast(int minimumCount, T expected) {
    isNotNull();
    checkArgument(minimumCount >= 0, "The minimum count should not be negative.");
    int actualCount = actual.count(expected);
    if (actualCount < minimumCount) {
      throw assertionError(shouldContainAtLeastTimes(actual, expected, minimumCount, actualCount));
    }
    return myself;
  }

  /**
   * Verifies the actual {@link Multiset} contains the given value <b>at most</b> the given number of times.
   * <p>
   * Example :
   *
   * <pre><code class='java'> Multiset&lt;String&gt; actual = HashMultiset.create();
   * actual.add("shoes", 2);
   *
   * // assertions succeed
   * assertThat(actual).containsAtMost(3, "shoes");
   * assertThat(actual).containsAtMost(2, "shoes");
   *
   * // assertion fails
   * assertThat(actual).containsAtMost(1, "shoes");</code></pre>
   *
   *
   * @param maximumCount the maximum number of times the given value should appear in the set
   *
   * @param expected the value which to expect
   * @return this {@link MultisetAssert} for fluent chaining
   *
   * @throws AssertionError if the actual {@link Multiset} is null
   * @throws AssertionError if the actual {@link Multiset} contains the given value more times than the given count
   */
  public MultisetAssert<T> containsAtMost(int maximumCount, T expected) {
    isNotNull();
    checkArgument(maximumCount >= 0, "The maximum count should not be negative.");
    int actualCount = actual.count(expected);
    if (actualCount > maximumCount) {
      throw assertionError(shouldContainAtMostTimes(actual, expected, maximumCount, actualCount));
    }
    return myself;
  }

  @Override
  protected ObjectAssert<T> toAssert(T value, String description) {
    return null;
  }

  @Override
  protected MultisetAssert<T> newAbstractIterableAssert(Iterable<? extends T> iterable) {
    // actual may not have been a HashMultiset but there is no easy elegant to build the same Multiset subtype.
    Multiset<T> filtered = HashMultiset.create();
    iterable.forEach(filtered::add);
    return new MultisetAssert<>(filtered);
  }
}
