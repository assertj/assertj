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

import java.util.function.DoublePredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Assertions for {@link Predicate}.
 *
 * @author Filip Hrisafov
 * @since 3.5.0
 */
public class DoublePredicateAssert extends AbstractPredicateLikeAssert<DoublePredicateAssert, DoublePredicate, Double> {

  public DoublePredicateAssert(DoublePredicate actual) {
    super(actual, toPredicate(actual), DoublePredicateAssert.class);
  }

  private static Predicate<Double> toPredicate(DoublePredicate actual) {
    return actual != null ? actual::test : null;
  }

  /**
   * <p>
   * Verifies that the {@link DoublePredicate} evaluates {@code true} for the given value.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .accepts(1);</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .accepts(2);</code></pre>
   *
   * @return this assertion object.
   */
  public DoublePredicateAssert accepts(double value) {
    return acceptsInternal(value);
  }

  /**
   * Verifies that the {@link DoublePredicate} evaluates {@code false} for the given value.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .rejects(3);</code></pre>
   * <p>
   * Assertion will fail :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .rejects(1);</code></pre>
   *
   * @return this assertion object.
   */
  public DoublePredicateAssert rejects(double value) {
    return rejectsInternal(value);
  }

  /**
   * <p>
   * Verifies that {@link DoublePredicate} evaluates to {@code true} for all the elements from the {@code values}.
   * </p>
   * Assertion will pass:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).acceptsAll(0, 1);</code></pre>
   * <p>
   * Assertion will fail:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).acceptsAll(1, 2);</code></pre>
   *
   * @return this assertion object
   */
  public DoublePredicateAssert acceptsAll(double... values) {
    return acceptsAllInternal(DoubleStream.of(values).boxed().collect(Collectors.toList()));
  }

  /**
   * <p>
   * Verifies that {@link DoublePredicate} evaluates to {@code false} for all the elements from the {@code values}.
   * </p>
   * Assertion will pass:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).rejectsAll(0, 1);</code></pre>
   * <p>
   * Assertion will fail:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).rejectsAll(1, 2);</code></pre>
   *
   * @return this assertion object
   */
  public DoublePredicateAssert rejectsAll(double... values) {
    return rejectsAllInternal(DoubleStream.of(values).boxed().collect(Collectors.toList()));
  }
}
