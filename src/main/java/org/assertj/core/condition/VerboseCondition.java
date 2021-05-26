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

import static java.util.Objects.requireNonNull;

import java.util.function.BiPredicate;
import java.util.function.Function;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.Condition;

/**
 * Condition that shows the expected and the value to test in the verbose description.
 * Part of the resulting description is also a verbal description of BiPredicate. 
 * The description output of the expected and the value to test could also be transformed by functions.
 *
 * <pre><code class='java'> Condition&lt;String&gt; verboseCondition = VerboseCondition.verboseCondition(
        4, // the expected value
        (String actual, Integer expected) -&gt; actual.length() &lt; expected, // matchesBiPredicate
        "word must be shorter than", // verbal description of the matchesBiPredicate
        (i) -&gt; i + " (maximum word length)", // expected value transformation
        (s) -&gt; String.format("%s (original word: %s)", s.length(), s)); // actual value transformation
 *</code></pre>
 * Tested against the (not matching) word `foooo` the description of the condition would be 
 * `word must be shorter than &lt;4 (maximum word length)&gt; but was &lt;5 (original word: foooo)&gt;`
 *<p>
 * @param <T> the type of object the given condition accept.
 * @param <EXPECTED> the type of object the expected value has.
 *
 * @author Stefan Bischof
 */
@Beta
public final class VerboseCondition<T, EXPECTED> extends Condition<T> {

  private EXPECTED expectedValue;

  private BiPredicate<T, EXPECTED> matchBiPredicate;

  private String matchDescription;

  private Function<T, ?> actualValueTransformation;

  private Function<EXPECTED, ?> expectedValueTransformation;


  /**
   * Creates a new <code>{@link VerboseCondition}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param <EXPECTED> the type of object the expected value has.
   * @param expectedValue the object the given value would be tested against.
   * @param matchesBiPredicate the BiPredicate that tests the value to test against the expected value.
   * @return the created {@code VerboseCondition}.
   * @throws NullPointerException if the matchesBiPredicate is {@code null}.
   */
  public static <T,EXPECTED> VerboseCondition<T,EXPECTED> verboseCondition(EXPECTED expectedValue, BiPredicate<T, EXPECTED> matchesBiPredicate) {
      return verboseCondition(expectedValue,matchesBiPredicate,"",null,null);
  }
  
  /**
   * Creates a new <code>{@link VerboseCondition}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param <EXPECTED> the type of object the expected value has.
   * @param expectedValue the object the given value would be tested against.
   * @param matchesBiPredicate the BiPredicate that tests the value to test against the expected value.
   * @param matchDescription describes the action of the matchesBiPredicate verbal.
   * @return the created {@code VerboseCondition}.
   * @throws NullPointerException if the matchesBiPredicate is {@code null}.
   */
  public static <T,EXPECTED> VerboseCondition<T,EXPECTED> verboseCondition(EXPECTED expectedValue, BiPredicate<T, EXPECTED> matchesBiPredicate,
          String matchDescription) {
      return verboseCondition(expectedValue,matchesBiPredicate,matchDescription,null,null);
  }
  
  /**
   * Creates a new <code>{@link VerboseCondition}</code>
   *
   * @param <T> the type of object the given condition accept.
   * @param <EXPECTED> the type of object the expected value has.
   * @param expectedValue the object the given value would be tested against.
   * @param matchesBiPredicate the BiPredicate that tests the value to test against the expected value.
   * @param matchDescription describes the action of the matchesBiPredicate verbal.
   * @param expectedValueTransformation Function that transforms the expected value into a text.
   * @param actualValueTransformation  Function that transforms the value to test into a text.
   * @return the created {@code VerboseCondition}.
   * @throws NullPointerException if the matchesBiPredicate is {@code null}.
   */
  public static <T,EXPECTED> VerboseCondition<T,EXPECTED> verboseCondition(EXPECTED expectedValue, BiPredicate<T, EXPECTED> matchesBiPredicate,
          String matchDescription, Function<EXPECTED, ?> expectedValueTransformation,
          Function<T, ?> actualValueTransformation) {
      return new VerboseCondition<T, EXPECTED>(expectedValue,matchesBiPredicate,matchDescription,expectedValueTransformation,actualValueTransformation);
  }
  
  private VerboseCondition(EXPECTED expectedValue, BiPredicate<T, EXPECTED> matchesBiPredicate,
      String matchDescription, Function<EXPECTED, ?> expectedValueTransformation,
      Function<T, ?> actualValueTransformation) {
    requireNonNull(matchesBiPredicate, "The matchesBiPredicate should not be null");
    this.expectedValue = expectedValue;
    this.matchBiPredicate = matchesBiPredicate;
    this.matchDescription = matchDescription;
    this.expectedValueTransformation = expectedValueTransformation;
    this.actualValueTransformation = actualValueTransformation;
    describedAs("%s <%s>",matchDescription,
        transformIfNotNull(expectedValueTransformation, expectedValue));
  }

  /**
   * Maps the value with the given function and verifies that it satisfies the nested <code>{@link Condition}</code>.
   *
   * @param value the value to map
   * @return {@code true} if the given value satisfies the BiPredicate; {@code false} otherwise.
   */
  @Override
  public boolean matches(T value) {

    boolean matches = matchBiPredicate.test(value, expectedValue);
    
    String desc = buildMappingDescription(value,matches);
    describedAs(desc);

    return matches;
  }
  
  /**
   * Build the verbose condition description when applied with the actual values and the match result.
   *
   * @param actual the actual value
   * @param matches the result of the match operation
   * @return the verbose condition description.
   */
  protected String buildMappingDescription(T actual,boolean matches) {
    StringBuilder sb = new StringBuilder();

     sb.append(String.format("%s <%s>", matchDescription,
          transformIfNotNull(expectedValueTransformation, expectedValue)));
     if (!matches) {
    	sb.append( String.format(" but was <%s>",transformIfNotNull(actualValueTransformation, actual)));
    }
    return sb.toString();
  }

  private static <E> Object transformIfNotNull(Function<E, ?> transform, E object) {

    return transform == null ? object : transform.apply(object);
  }
}
