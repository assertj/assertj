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

import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Assertions for {@link LongPredicate}.
 *
 * @author Filip Hrisafov
 * @since 3.5.0
 */
public class LongPredicateAssert extends AbstractPredicateLikeAssert<LongPredicateAssert, LongPredicate, Long> {

  public LongPredicateAssert(LongPredicate actual) {
    super(actual, toPredicate(actual), LongPredicateAssert.class);
  }

  private static Predicate<Long> toPredicate(LongPredicate actual) {
    return actual != null ? actual::test : null;
  }

  /**
   * <p>
   * Verifies that the {@link LongPredicate} evaluates {@code true} for the given value.
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
  public LongPredicateAssert accepts(long value) {
    return acceptsInternal(value);
  }

  /**
   * Verifies that the {@link LongPredicate} evaluates {@code true} for the given value.
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
  public LongPredicateAssert rejects(long value) {
    return rejectsInternal(value);
  }

  /**
   * <p>
   * Verifies that {@link LongPredicate} evaluates to {@code true} for all the elements from the {@code values}.
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
  public LongPredicateAssert acceptsAll(long... values) {
    return acceptsAllInternal(LongStream.of(values).boxed().collect(Collectors.toList()));
  }

  /**
   * <p>
   * Verifies that {@link LongPredicate} evaluates to {@code false} for all the elements from the {@code values}.
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
  public LongPredicateAssert rejectsAll(long... values) {
    return rejectsAllInternal(LongStream.of(values).boxed().collect(Collectors.toList()));
  }
}
