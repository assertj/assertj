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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.util.function.Predicate;

import org.assertj.core.internal.Iterables;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.VisibleForTesting;

import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.error.ShouldNotAccept.shouldNotAccept;
import static org.assertj.core.presentation.PredicateDescription.fromDescription;

/**
 * Assertions for {@link Predicate}.
 *
 * @param <T> the actual type
 * @param <W> the type of the wrapped variable in one of the special predicates
 *
 * @author Filip Hrisafov
 */
public abstract class AbstractPredicateLikeAssert<S extends AbstractPredicateLikeAssert<S, T, W>, T, W> extends
  AbstractAssert<S, T> {

  @VisibleForTesting
  Iterables iterables = Iterables.instance();

  @VisibleForTesting
  Predicate<W> wrappedPredicate;

  protected AbstractPredicateLikeAssert(T actual, Predicate<W> wrappedPredicate, Class<?> selfType) {
    super(actual, selfType);
    this.wrappedPredicate = wrappedPredicate;
  }

  /**
   * Verifies that the {@link Predicate} evaluates {@code true} for the given value.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(predicate -> predicate.equals("something"))
   *            .accepts("something");</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(predicate -> predicate.equals("something"))
   *            .accepts("something else");</code></pre>
   *
   * @return this assertion object.
   */
  protected S acceptsInternal(W value) {
    isNotNull();
    if (!wrappedPredicate.test(value)) { throwAssertionError(shouldAccept(wrappedPredicate, value, fromDescription(info.description()))); }
    return myself;
  }

  /**
   * Verifies that the {@link Predicate} evaluates {@code true} for the given value.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(predicate -> predicate.equals("something"))
   *            .rejects("something else");</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(predicate -> predicate.equals("something"))
   *            .rejects("something");</code></pre>
   *
   * @return this assertion object.
   */
  protected S rejectsInternal(W value) {
    isNotNull();
    if (wrappedPredicate.test(value)) { throwAssertionError(shouldNotAccept(wrappedPredicate, value, fromDescription(info.description()))); }
    return myself;
  }

  /**
   * <p>
   * Verifies that {@link Predicate} evaluates to {@code true} for all the elements from the {@code values}.
   * </p>
   * Assertion will pass:
   * <pre><code class='java'>
   *     List<String> elements = Arrays.asList("first", "second");
   *     assertThat(value -> elements.contains(value)).acceptsAll(elements);</code></pre>
   *
   * Assertion will fail:
   * <pre><code class='java'>
   *     List<String> elements = Arrays.asList("first", "second");
   *     assertThat(value -> elements.contains(value)).acceptsAll(Arrays.asList("first", "third"));</code></pre>
   *
   * @return this assertion object
   */
  protected S acceptsAllInternal(Iterable<? extends W> values) {
    isNotNull();
    iterables.assertAllMatch(info, values, wrappedPredicate);
    return myself;
  }

  /**
   * <p>
   * Verifies that {@link Predicate} evaluates to {@code false} for all the elements from the {@code values}.
   * </p>
   * Assertion will pass:
   * <pre><code class='java'>
   *     List<String> elements = Arrays.asList("first", "second");
   *     assertThat(value -> elements.contains(value)).rejectsAll(Arrays.asList("third", "fourth"));</code></pre>
   *
   * Assertion will fail:
   * <pre><code class='java'>
   *     List<String> elements = Arrays.asList("first", "second");
   *     assertThat(value -> elements.contains(value)).rejectsAll(Arrays.asList("first", "third"));</code></pre>
   *
   * @return this assertion object
   */
  protected S rejectsAllInternal(Iterable<? extends W> values) {
    isNotNull();
    iterables.assertNoneMatch(info, values, wrappedPredicate);
    return myself;
  }
}
