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
   * Verifies that {@link DoublePredicate} evaluates all the given values to {@code true}.
   * <p>
   * Example :
   * <pre><code class='java'> DoublePredicate tallSize = size -&gt; size &gt; 1.90;
   *
   * // assertion succeeds:
   * assertThat(tallSize).accepts(1.95, 2.00, 2.05);
   *
   * // assertion fails:
   * assertThat(tallSize).accepts(1.85, 1.95, 2.05);</code></pre>
   *
   * @param values values that the actual {@code Predicate} should accept.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} does not accept all given values.
   */
  public DoublePredicateAssert accepts(double... values) {
    if (values.length == 1) return acceptsInternal(values[0]);
    return acceptsAllInternal(DoubleStream.of(values).boxed().collect(Collectors.toList()));
  }

  /**
   * Verifies that {@link DoublePredicate} evaluates all the given values to {@code false}.
   * <p>
   * Example :
   * <pre><code class='java'> DoublePredicate tallSize = size -&gt; size &gt; 1.90;
   *
   * // assertion succeeds:
   * assertThat(tallSize).rejects(1.75, 1.80, 1.85);
   *
   * // assertion fails because of 1.90 size:
   * assertThat(tallSize).rejects(1.80, 1.85, 1.90);</code></pre>
   *
   * @param values values that the actual {@code Predicate} should reject.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} accepts one of the given values.
   */
  public DoublePredicateAssert rejects(double... values) {
    if (values.length == 1) return rejectsInternal(values[0]);
    return rejectsAllInternal(DoubleStream.of(values).boxed().collect(Collectors.toList()));
  }
}
