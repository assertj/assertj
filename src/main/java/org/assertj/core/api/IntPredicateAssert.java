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
   * Verifies that {@link IntPredicate} evaluates all the given values to {@code true}.
   * <p>
   * Example :
   * <pre><code class='java'> IntPredicate evenNumber = n -&gt; n % 2 == 0;
   *
   * // assertion succeeds:
   * assertThat(evenNumber).accepts(2, 4, 6);
   *
   * // assertion fails because of 3:
   * assertThat(evenNumber).accepts(2, 3, 4);</code></pre>
   *
   * @param values values that the actual {@code Predicate} should accept.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} does not accept all given values.
   */
  public IntPredicateAssert accepts(int... values) {
    if (values.length == 1) return acceptsInternal(values[0]);
    return acceptsAllInternal(IntStream.of(values).boxed().collect(Collectors.toList()));
  }

  /**
   * Verifies that {@link IntPredicate} evaluates all the given values to {@code false}.
   * <p>
   * Example :
   * <pre><code class='java'> IntPredicate evenNumber = n -&gt; n % 2 == 0;
   *
   * // assertion succeeds:
   * assertThat(evenNumber).rejects(1, 3, 5);
   *
   * // assertion fails because of 2:
   * assertThat(evenNumber).rejects(1, 2, 3);</code></pre>
   *
   * @param values values that the actual {@code Predicate} should reject.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} accepts one of the given values.
   */
  public IntPredicateAssert rejects(int... values) {
    if (values.length == 1) return rejectsInternal(values[0]);
    return rejectsAllInternal(IntStream.of(values).boxed().collect(Collectors.toList()));
  }
}
