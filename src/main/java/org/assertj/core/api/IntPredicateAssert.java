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

import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Assertions for {@link Predicate}.
 *
 * @author Filip Hrisafov
 * @since 3.5.0
 */
public class IntPredicateAssert extends AbstractPredicateLikeAssert<IntPredicateAssert, IntPredicate, Integer> {

  public IntPredicateAssert(IntPredicate actual) {
    super(actual, toPredicate(actual), IntPredicateAssert.class);
  }

  private static Predicate<Integer> toPredicate(IntPredicate actual) {
    return actual != null ? actual::test : null;
  }

  /**
   * <p>
   * Verifies that the {@link IntPredicate} evaluates {@code true} for the given value.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .accepts(1);</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .accepts(2);</code></pre>
   *
   * @return this assertion object.
   */
  public IntPredicateAssert accepts(int value) {
    return acceptsInternal(value);
  }

  /**
   * Verifies that the {@link IntPredicate} evaluates {@code true} for the given value.
   * </p>
   * Assertion will pass :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .doesNotAccept(3);</code></pre>
   *
   * Assertion will fail :
   * <pre><code class='java'> assertThat(predicate -> predicate < 2)
   *            .doesNotAccept(1);</code></pre>
   *
   * @return this assertion object.
   */
  public IntPredicateAssert doesNotAccept(int value) {
    return doesNotAcceptInternal(value);
  }

  /**
   * <p>
   * Verifies that {@link IntPredicate} evaluates to {@code true} for all the elements from the {@code values}.
   * </p>
   * Assertion will pass:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).acceptsAll(0, 1);</code></pre>
   *
   * Assertion will fail:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).acceptsAll(1, 2);</code></pre>
   *
   * @return this assertion object
   */
  public IntPredicateAssert acceptsAll(int... values) {
    return acceptsAllInternal(IntStream.of(values).boxed().collect(Collectors.toList()));
  }

  /**
   * <p>
   * Verifies that {@link IntPredicate} evaluates to {@code false} for all the elements from the {@code values}.
   * </p>
   * Assertion will pass:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).noneAccepted(0, 1);</code></pre>
   *
   * Assertion will fail:
   * <pre><code class='java'>
   *     assertThat(value -> value < 2).noneAccepted(1, 2);</code></pre>
   *
   * @return this assertion object
   */
  public IntPredicateAssert noneAccepted(int... values) {
    return noneAcceptedInternal(IntStream.of(values).boxed().collect(Collectors.toList()));
  }
}
