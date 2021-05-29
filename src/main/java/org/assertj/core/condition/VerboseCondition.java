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

import java.util.function.Function;
import java.util.function.Predicate;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.Condition;

/**
 * Condition that shows the expected and the value to test in the verbose
 * description. The description output of the value to test could also be
 * transformed by functions.
 *
 * <pre>
 * <code class=
 * 'java'> Condition&lt;String&gt; verboseCondition = VerboseCondition.verboseCondition(
        actual -&gt; actual.length() &lt; 4, // predicate
        "word must be shorter than &gt;4&lt;", // verbal description of the predicate
        (s) -&gt; String.format("%s (original word: %s)", s.length(), s)); // actual value transformation
 *</code>
 * </pre>
 * 
 * Tested against the (not matching) word `foooo` the description of the
 * condition would be `word must be shorter than &lt;4 (maximum word length)&gt;
 * but was &lt;5 (original word: foooo)&gt;`
 * <p>
 * 
 * @param <T>        the type of object the given condition accept.
 *
 * @author Stefan Bischof
 */
@Beta
public final class VerboseCondition<T> extends Condition<T> {

  /**
   * Creates a new <code>{@link VerboseCondition}</code>
   *
   * @param <T>                  the type of object the given condition accept.
   * @param predicate            the Predicate that tests the value to test.
   * @param description          describes the Condition verbal.
   * @param butWasTransformation Function that is called to transforms the value
   *                             to test into a text, when the actual value does
   *                             not match the predicate.
   * @return the created {@code VerboseCondition}.
   * @throws NullPointerException if the predicate is {@code null}.
   */
  public static <T> VerboseCondition<T> verboseCondition(Predicate<T> predicate, String description,
      Function<T, String> butWasTransformation) {

    return new VerboseCondition<>(predicate, description, butWasTransformation);
  }

  /**
   * Creates a new <code>{@link VerboseCondition}</code>
   *
   * @param <T>                  the type of object the given condition accept.
   * @param predicate            the Predicate that tests the value to test.
   * @param description          describes the Condition verbal.
   * @return the created {@code VerboseCondition}.
   * @throws NullPointerException if the predicate is {@code null}.
   */  public static <T> VerboseCondition<T> verboseCondition(Predicate<T> predicate,
      String description) {

    return verboseCondition(predicate, description, null);
  }

  private Function<T, String> butWasTransformation;

  private String description;

  private VerboseCondition(Predicate<T> predicate, String description,
      Function<T, String> butWasTransformation) {

    super(predicate, description);
    this.description = description;
    this.butWasTransformation = butWasTransformation;
  }

  @Override
  public boolean matches(T value) {

    boolean matches = super.matches(value);
    String desc = buildMappingDescription(value, matches);
    describedAs(desc);
    return matches;
  }

  /**
   * Build the verbose condition description when applied with the actual values
   * and the match result.
   *
   * @param actual  the actual value
   * @param matches the result of the match operation
   * @return the verbose condition description.
   */
  protected String buildMappingDescription(T actual, boolean matches) {

    StringBuilder sb = new StringBuilder();

    sb.append(String.format("%s", description));
    if (!matches) {
      sb.append(String.format(" but was <%s>", transformIfNotNull(butWasTransformation, actual)));
    }
    return sb.toString();
  }

  private static <E> Object transformIfNotNull(Function<E, ?> transform, E object) {

    return transform == null ? object : transform.apply(object);
  }
}
