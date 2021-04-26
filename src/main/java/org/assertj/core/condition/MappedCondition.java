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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;
import java.util.function.Function;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.Condition;

/**
 * Container-<code>{@link Condition}</code> that does a mapping and then uses nested
 * <code>{@link Condition}</code> to test the mapped actual value.
 *
 * <pre><code class='java'> Condition&lt;String&gt; hasLineSeparator = new Condition&lt;&gt;(t -&gt; t.contains(System.lineSeparator()), "has lineSeparator");
 *
 * Condition&lt;Optional&lt;String&gt;&gt; optionalHasLineSeparator = MappedCondition.mappedCondition(Optional::get, hasLineSeparator);
 *
 * // returns true
 * optionalHasLineSeparator.matches(Optional.of("a" + System.lineSeparator()));
 *
 * // returns false
 * optionalHasLineSeparator.matches(Optional.of("a"));</code></pre>
 *
 * @param <FROM> the type of object this condition accepts.
 * @param <TO> the type of object the nested condition accepts.
 *
 * @author Stefan Bischof
 */
@Beta
public class MappedCondition<FROM, TO> extends Condition<FROM> {

  private Condition<TO> condition;
  private Function<FROM, TO> mapping;
  private String mappingDescription;

  /**
   * Creates a new <code>{@link MappedCondition}</code>
   *
   * @param <FROM> the type of object the given condition accept.
   * @param <TO> the type of object the nested condition accept.
   * @param mapping the Function that maps the value to test to the a value for the nested condition.
   * @param mappingDescription describes the mapping verbal.
   * @param condition the nested condition to evaluate.
   * @return the created {@code Mapped}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws NullPointerException if the given mapping is {@code null}.
   */
  public static <FROM, TO> MappedCondition<FROM, TO> mappedCondition(Function<FROM, TO> mapping, String mappingDescription,
                                                                     Condition<TO> condition) {
    return new MappedCondition<>(mapping, mappingDescription, condition);
  }

  /**
   * Creates a new <code>{@link MappedCondition}</code>
   *
   * @param <FROM> the type of object the given condition accept.
   * @param <TO> the type of object the nested condition accept.
   * @param mapping the Function that maps the value to test to the a value for the nested condition.
   * @param condition the nested condition to evaluate.
   * @return the created {@code Mapped}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws NullPointerException if the given mapping is {@code null}.
   */
  public static <FROM, TO> MappedCondition<FROM, TO> mappedCondition(Function<FROM, TO> mapping, Condition<TO> condition) {
    return mappedCondition(mapping, null, condition);
  }

  private MappedCondition(Function<FROM, TO> mapping, String mappingDescription, Condition<TO> condition) {
    requireNonNull(condition, "The given condition should not be null");
    requireNonNull(mapping, "The given mapping function should not be null");
    this.mapping = mapping;
    this.mappingDescription = mappingDescription;
    this.condition = condition;
    describeMappedCondition("mapping");
  }

  /**
   * Maps the value with the given function and verifies that it satisfies the nested <code>{@link Condition}</code>.
   *
   * @param value the value to map
   * @return {@code true} if the given mapped value satisfies the nested condition; {@code false} otherwise.
   */
  @Override
  public boolean matches(FROM value) {
    TO mappedObject = mapping.apply(value);
    String desc = mappingDescription(mappingDescription).apply(value, mappedObject);
    describeMappedCondition(desc);
    return condition.matches(mappedObject);
  }

  private static <FROM, TO> BiFunction<FROM, TO, String> mappingDescription(String using) {
    return (from, to) -> {
      StringBuilder sb = new StringBuilder();
      sb.append("mapped");
      if (using != null) sb.append(format("%n   using: %s", using));
      sb.append(format("%n   from: <%s> %s%n", from.getClass().getSimpleName(), from));
      sb.append(format("   to:   <%s> %s%n", to.getClass().getSimpleName(), from, to));
      sb.append("   then checked:");
      return sb.toString();
    };
  }

  private void describeMappedCondition(String mappingDescription) {
    describedAs(mappingDescription + " [%n      %-10s%n]", condition);
  }

}
