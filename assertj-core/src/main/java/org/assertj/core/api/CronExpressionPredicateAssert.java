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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import org.assertj.core.internal.CronExpression;

import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Assertions for {@link CronExpressionAssert}.
 * @author Neil Wang
 */
public class CronExpressionPredicateAssert extends AbstractPredicateLikeAssert<CronExpressionPredicateAssert, CronExpressionPredicate, CronExpression> {

  public static CronExpressionPredicateAssert assertThatCronExpressionPredicate(CronExpressionPredicate actual) {
    return new CronExpressionPredicateAssert(actual);
  }

  public CronExpressionPredicateAssert(CronExpressionPredicate actual) {
    super(actual, toPredicate(actual), CronExpressionPredicateAssert.class);
  }

  private static Predicate<CronExpression> toPredicate(CronExpressionPredicate actual) {
    return actual != null ? actual::test : null;
  }

  /**
   * Verifies that {@link CronExpressionPredicate} evaluates all the given values to {@code true}.
   * <p>
   * Example :
   * <pre><code class='java'> CronExpressionPredicate cronExpressions = n -&gt; n % 2 == 0;
   *
   * // assertion succeeds:
   * assertThat(new CronExp).accepts(2, 4, 6);
   *
   * // assertion fails because of 3:
   * assertThat(evenNumber).accepts(2, 3, 4);</code></pre>
   * @param values values that the actual {@code Predicate} should accept.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} does not accept all given values.
   */
  public CronExpressionPredicateAssert accepts(CronExpression... values) {
    if (values.length == 1) return acceptsInternal(values[0]);
    return acceptsAllInternal(Stream.of(values).collect(Collectors.toList()));
  }

  /**
   * Verifies that {@link LongPredicate} evaluates all the given values to {@code false}.
   * <p>
   * Example :
   * <pre><code class='java'> LongPredicate evenNumber = n -&gt; n % 2 == 0;
   *
   * // assertion succeeds:
   * assertThat(evenNumber).rejects(1, 3, 5);
   *
   * // assertion fails because of 2:
   * assertThat(evenNumber).rejects(1, 2, 3);</code></pre>
   * @param values values that the actual {@code Predicate} should reject.
   * @return this assertion object.
   * @throws AssertionError if the actual {@code Predicate} accepts one of the given values.
   */
  public CronExpressionPredicateAssert rejects(CronExpression... values) {
    if (values.length == 1) return rejectsInternal(values[0]);
    return rejectsAllInternal(Stream.of(values).collect(Collectors.toList()));
  }
}
