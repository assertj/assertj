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
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.error.ShouldHaveValue.shouldHaveValue;
import static org.assertj.core.error.ShouldMatch.shouldMatch;
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.assertj.core.presentation.PredicateDescription;

public class AtomicReferenceAssert<V> extends AbstractAssert<AtomicReferenceAssert<V>, AtomicReference<V>> {

  public AtomicReferenceAssert(AtomicReference<V> actual) {
    super(actual, AtomicReferenceAssert.class);
  }

  /**
   * Verifies that the atomic under test has the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(new AtomicReference("foo")).hasValue("foo");
   *
   * // assertion fails
   * assertThat(new AtomicReference("foo")).hasValue("bar");</code></pre>
   *
   * @param expectedValue the expected value.
   * @return {@code this} assertion object.
   * @throws AssertionError if the atomic under test is {@code null}.
   * @throws AssertionError if the atomic under test does not have the given value.
   * @since 2.7.0 / 3.7.0
   */
  public AtomicReferenceAssert<V> hasValue(V expectedValue) {
    isNotNull();
    V actualValue = actual.get();
    if (!objects.getComparisonStrategy().areEqual(actualValue, expectedValue)) {
      throw assertionError(shouldHaveValue(actual, expectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the atomic under test does not have the given value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(new AtomicReference("foo")).doesNotHaveValue("bar");
   *
   * // assertion fails
   * assertThat(new AtomicReference("foo")).doesNotHaveValue("foo");</code></pre>
   *
   * @param nonExpectedValue the value not expected.
   * @return {@code this} assertion object.
   * @throws AssertionError if the atomic under test is {@code null}.
   * @throws AssertionError if the atomic under test has the given value.
   *
   * @since 2.7.0 / 3.7.0
   */
  public AtomicReferenceAssert<V> doesNotHaveValue(V nonExpectedValue) {
    isNotNull();
    V actualValue = actual.get();
    if (objects.getComparisonStrategy().areEqual(actualValue, nonExpectedValue)) {
      throw assertionError(shouldNotContainValue(actual, nonExpectedValue));
    }
    return myself;
  }

  /**
   * Verifies that the atomic under test has a value satisfying the given predicate.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(new AtomicReference("foo")).hasValueMatching(result -&gt; result != null);
   *
   * // assertion fails
   * assertThat(new AtomicReference("foo")).hasValueMatching(result -&gt; result == null); </code></pre>
   *
   * @param predicate the {@link Predicate} to apply on the resulting value.
   * @return {@code this} assertion object.
   * @throws AssertionError if the atomic under test is {@code null}.
   * @throws AssertionError if the atomic under test value does not matches with the given predicate.
   * @throws NullPointerException if the given {@link Predicate} is null
   *
   * @since 3.18.0
   */
  public AtomicReferenceAssert<V> hasValueMatching(Predicate<? super V> predicate) {
    return hasValueMatching(predicate, PredicateDescription.GIVEN);
  }

  /**
   * Verifies that the atomic under test has a value satisfying the given predicate, the string parameter is used in the error message
   * to describe the predicate.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(new AtomicReference("foo")).hasValueMatching(result -&gt; result != null, "expected not null");
   *
   * // assertion fails
   * assertThat(new AtomicReference("foo")).hasValueMatching(result -&gt; result == null, "expected null");
   * </code></pre>
   *
   * @param predicate   the {@link Predicate} to apply on the resulting value.
   * @param description the {@link Predicate} description.
   * @return {@code this} assertion object.
   * @throws AssertionError if the atomic under test is {@code null}.
   * @throws AssertionError if the atomic under test value does not matches with the given predicate.
   * @throws NullPointerException if the given {@link Predicate} is null
   *
   * @since 3.18.0
   */
  public AtomicReferenceAssert<V> hasValueMatching(Predicate<? super V> predicate, String description) {
    return hasValueMatching(predicate, new PredicateDescription(description));
  }

  private AtomicReferenceAssert<V> hasValueMatching(Predicate<? super V> predicate, PredicateDescription description) {
    requireNonNull(predicate, "The predicate must not be null");
    isNotNull();
    V actualValue = actual.get();
    if (!predicate.test(actualValue)) {
      throw assertionError(shouldMatch(actualValue, predicate, description));
    }
    return myself;
  }

  /**
   * Verifies that the atomic under test has a value satisfying the given requirements.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(new AtomicReference("foo")).hasValueSatisfying(result -&gt; assertThat(result).isNotBlank());
   *
   * // assertion fails
   * assertThat(new AtomicReference("foo")).hasValueSatisfying(result -&gt; assertThat(result).isBlank()); </code></pre>
   *
   * @param requirements to assert on the actual object - must not be null.
   * @return this assertion object.
   * @throws AssertionError if the atomic under test is {@code null}.
   * @throws AssertionError if the atomic under test value does not satisfies with the given requirements.
   * @throws NullPointerException if the given {@link Consumer} is null
   *
   * @since 3.18.0
   */
  public AtomicReferenceAssert<V> hasValueSatisfying(Consumer<? super V> requirements) {
    requireNonNull(requirements, "The Consumer<? super V> expressing the assertions requirements must not be null");
    isNotNull();
    requirements.accept(actual.get());
    return myself;
  }

  /**
   * Verifies that the atomic under test has the {@code null} value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(new AtomicReference(null)).hasNullValue();
   *
   * // assertion fails
   * assertThat(new AtomicReference("foo")).hasNullValue();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the atomic under test does not have the null value.
   * @since 3.25.0
   */
  public AtomicReferenceAssert<V> hasNullValue() {
    return hasValue(null);
  }

  /**
   * Verifies that the atomic under test does not have the {@code null} value.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion succeeds
   * assertThat(new AtomicReference("foo")).doesNotHaveNullValue();
   *
   * // assertion fails
   * assertThat(new AtomicReference(null)).doesNotHaveNullValue();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the atomic under test has the null value.
   * @since 3.25.0
   */
  public AtomicReferenceAssert<V> doesNotHaveNullValue() {
    return doesNotHaveValue(null);
  }
}
