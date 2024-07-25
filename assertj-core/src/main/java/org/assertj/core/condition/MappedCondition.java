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
package org.assertj.core.condition;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;
import static org.assertj.core.util.Lists.list;

import java.util.function.Function;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;
import org.assertj.core.description.JoinDescription;

/**
 * Container {@link Condition} that maps the object under test and then check the resulting mapped value against its nested {@link Condition}.
 * <p>
 * Example:
 * <pre><code class='java'> Condition&lt;String&gt; hasLineSeparator = new Condition&lt;&gt;(t -&gt; t.contains(System.lineSeparator()), "has lineSeparator");
 *
 * Condition&lt;Optional&lt;String&gt;&gt; optionalWithLineSeparator = MappedCondition.mappedCondition(Optional::get, hasLineSeparator, "optional value has lineSeparator");
 *
 * // assertion succeeds
 * assertThat(Optional.of("a" + System.lineSeparator())).is(optionalWithLineSeparator);
 * // returns true
 * optionalWithLineSeparator.matches(Optional.of("a" + System.lineSeparator()));
 *
 * // assertion fails
 * assertThat(Optional.of("a")).is(optionalWithLineSeparator);
 * // returns false
 * optionalWithLineSeparator.matches(Optional.of("a"));</code></pre>
 *
 * @author Stefan Bischof
 *
 * @param <FROM> the type of object this condition accepts.
 * @param <TO> the type of object the nested condition accepts.
 */
@Beta
public class MappedCondition<FROM, TO> extends Condition<FROM> {

  private Condition<TO> condition;
  private Function<FROM, TO> mapping;
  private String mappingDescription;

  /**
   * Creates a new <code>{@link MappedCondition}</code>.
   * <p>
   * Example:
   * <pre><code class='java'> Condition&lt;String&gt; hasLineSeparator = new Condition&lt;&gt;(t -&gt; t.contains(System.lineSeparator()), "has lineSeparator");
   *
   * Condition&lt;Optional&lt;String&gt;&gt; optionalWithLineSeparator = MappedCondition.mappedCondition(Optional::get, hasLineSeparator, "optional value has lineSeparator");
   *
   * // assertion succeeds
   * assertThat(Optional.of("a" + System.lineSeparator())).is(optionalWithLineSeparator);
   * // returns true
   * optionalWithLineSeparator.matches(Optional.of("a" + System.lineSeparator()));
   *
   * // assertion fails
   * assertThat(Optional.of("a")).is(optionalWithLineSeparator);
   * // returns false
   * optionalWithLineSeparator.matches(Optional.of("a"));</code></pre>
     * <p>
   * Note that the mappingDescription argument follows {@link String#format(String, Object...)} syntax.
   *
   * @param <FROM> the type of object the given condition accept.
   * @param <TO> the type of object the nested condition accept.
   * @param mapping the Function that maps the value to test to the a value for the nested condition.
   * @param condition the nested condition to evaluate.
   * @param mappingDescription describes the mapping, follows {@link String#format(String, Object...)} syntax.
   * @param args for describing the mapping as in {@link String#format(String, Object...)} syntax.
   * @return the created {@code MappedCondition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws NullPointerException if the given mapping is {@code null}.
   */
  public static <FROM, TO> MappedCondition<FROM, TO> mappedCondition(Function<FROM, TO> mapping, Condition<TO> condition,
                                                                     String mappingDescription, Object... args) {
    requireNonNull(mappingDescription, "The given mappingDescription should not be null");
    return new MappedCondition<>(mapping, condition, format(mappingDescription, args));
  }

  /**
   * Creates a new <code>{@link MappedCondition}</code>
   *
   * @param <FROM> the type of object the given condition accept.
   * @param <TO> the type of object the nested condition accept.
   * @param mapping the Function that maps the value to test to the a value for the nested condition.
   * @param condition the nested condition to evaluate.
   * @return the created {@code MappedCondition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws NullPointerException if the given mapping is {@code null}.
   */
  public static <FROM, TO> MappedCondition<FROM, TO> mappedCondition(Function<FROM, TO> mapping, Condition<TO> condition) {
    return mappedCondition(mapping, condition, "");
  }

  private MappedCondition(Function<FROM, TO> mapping, Condition<TO> condition, String mappingDescription) {
    requireNonNull(condition, "The given condition should not be null");
    requireNonNull(mapping, "The given mapping function should not be null");
    this.mapping = mapping;
    this.mappingDescription = mappingDescription;
    this.condition = condition;
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
    String desc = buildMappingDescription(value, mappedObject);
    describedAs(desc);
    return condition.matches(mappedObject);
  }

  /**
   * Build the mapped condition description when applied with the FROM and TO values.
   *
   * @param from the value to map
   * @param to the mapped value
   * @return the mapped condition description .
   */
  protected String buildMappingDescription(FROM from, TO to) {
    return buildMappingDescription(from, to, true);
  }

  private String buildMappingDescription(FROM from, TO to, boolean withNested) {

    StringBuilder sb = new StringBuilder("mapped");
    if (!mappingDescription.isEmpty()) sb.append(format("%n   using: %s", mappingDescription));

    if (from == null) sb.append(format("%n   from: %s%n", from));
    else sb.append(format("%n   from: <%s> %s%n", className(from), from));

    if (to == null) sb.append(format("   to:   %s%n", to));
    else sb.append(format("   to:   <%s> %s%n", className(to), to));

    sb.append("   then checked:");
    if (withNested) sb.append(format("%n      %-10s", condition));
    return sb.toString();
  }

  @Override
  public Description conditionDescriptionWithStatus(FROM actual) {
    TO mappedObject = mapping.apply(actual);
    Description descriptionsWithStatus = condition.conditionDescriptionWithStatus(mappedObject);
    Status status = status(actual);
    String descriptionStart = format("%s %s", status, buildMappingDescription(actual, mappedObject, false));
    return new JoinDescription(descriptionStart, "", list(descriptionsWithStatus));
  }

  private static String className(Object object) {
    return object.getClass().getSimpleName();
  }

}
