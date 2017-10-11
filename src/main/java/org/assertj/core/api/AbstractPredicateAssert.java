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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.error.ShouldAccept.shouldAccept;
import static org.assertj.core.error.ShouldNotAccept.shouldNotAccept;
import static org.assertj.core.presentation.PredicateDescription.GIVEN;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.function.Predicate;

import org.assertj.core.internal.Iterables;
import org.assertj.core.presentation.PredicateDescription;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for {@link Predicate}.
 *
 * @param <T> type of the value contained in the {@link Predicate}.
 *
 * @author Filip Hrisafov
 */
public abstract class AbstractPredicateAssert<SELF extends AbstractPredicateAssert<SELF, T>, T> extends
    AbstractAssert<SELF, Predicate<T>> {

  @VisibleForTesting
  Iterables iterables = Iterables.instance();

  protected AbstractPredicateAssert(Predicate<T> actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that the {@link Predicate} evaluates all given values to {@code true}.
   * <p>
   * Example :
   * <pre><code class='java'> Predicate&lt;String&gt; ballSportPredicate = sport -&gt; sport.contains("ball");
   *
   * // assertion succeeds:
   * assertThat(ballSportPredicate).accepts("football")
   *                               .accepts("football", "basketball", "handball");
   *
   * // assertions fail because of curling :p
   * assertThat(ballSportPredicate).accepts("curling")
   * assertThat(ballSportPredicate).accepts("football", "basketball", "curling");</code></pre>
   *
   * @param values values the actual {@code Predicate} should accept.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} does not accept all the given {@code Iterable}'s elements.
   */
  public SELF accepts(@SuppressWarnings("unchecked") T... values) {
    isNotNull();
    if (values.length == 1 && !actual.test(values[0])) throwAssertionError(shouldAccept(actual, values[0], GIVEN));
    else iterables.assertAllMatch(info, newArrayList(values), actual, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * Verifies that the {@link Predicate} evaluates all given values to {@code false}.
   * <p>
   * Example :
   * <pre><code class='java'> Predicate&lt;String&gt; ballSportPredicate = sport -&gt; sport.contains("ball");
   *
   * // assertion succeeds:
   * assertThat(ballSportPredicate).rejects("curling")
   *                               .rejects("curling", "judo", "marathon");
   *
   * // assertion fails because of football:
   * assertThat(ballSportPredicate).rejects("football");
   * assertThat(ballSportPredicate).rejects("curling", "judo", "football");</code></pre>
   *
   * @param values values the actual {@code Predicate} should reject.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} accepts one of the given {@code Iterable}'s elements.
   */
  public SELF rejects(@SuppressWarnings("unchecked") T... values) {
    isNotNull();
    if (values.length == 1 && actual.test(values[0])) throwAssertionError(shouldNotAccept(actual, values[0], GIVEN));
    else iterables.assertNoneMatch(info, newArrayList(values), actual, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * Verifies that the {@link Predicate} evaluates all given {@link Iterable}'s elements to {@code true}.
   * <p>
   * Example :
   * <pre><code class='java'> Predicate&lt;String&gt; ballSportPredicate = sport -&gt; sport.contains("ball");
   *
   * // assertion succeeds:
   * assertThat(ballSportPredicate).acceptsAll(list("football", "basketball", "handball"));
   *
   * // assertion fails because of curling :p
   * assertThat(ballSportPredicate).acceptsAll(list("football", "basketball", "curling"));</code></pre>
   *
   * @param iterable {@code Iterable} whose elements the actual {@code Predicate} should accept.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} does not accept all the given {@code Iterable}'s elements.
   */
  public SELF acceptsAll(Iterable<? extends T> iterable) {
    isNotNull();
    iterables.assertAllMatch(info, iterable, actual, PredicateDescription.GIVEN);
    return myself;
  }

  /**
   * Verifies that the {@link Predicate} evaluates all given {@link Iterable}'s elements to {@code false}.
   * <p>
   * Example :
   * <pre><code class='java'> Predicate&lt;String&gt; ballSportPredicate = sport -&gt; sport.contains("ball");
   *
   * // assertion succeeds:
   * assertThat(ballSportPredicate).rejectsAll(list("curling", "judo", "marathon"));
   *
   * // assertion fails because of football:
   * assertThat(ballSportPredicate).rejectsAll(list("curling", "judo", "football"));</code></pre>
   *
   * @param iterable {@code Iterable} whose elements the actual {@code Predicate} should reject.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} accepts one of the given {@code Iterable}'s elements.
   */
  public SELF rejectsAll(Iterable<? extends T> iterable) {
    isNotNull();
    iterables.assertNoneMatch(info, iterable, actual, PredicateDescription.GIVEN);
    return myself;
  }
}
