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

import java.util.function.Function;
import java.util.function.Predicate;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Condition;

/**
 * {@link Condition} that shows the value under test when the condition fails thanks to the specified {@code objectUnderTestDescriptor} function.
 *
 * <p>
 * When defining the {@code objectUnderTestDescriptor} function, you should take in consideration whether the condition is going to be used 
 * with {@link AbstractAssert#is(Condition) is(Condition)} or {@link AbstractAssert#has(Condition) has(Condition)} since the start of the error message is different between the two.  
 * <p>
 * Let's see how it works with an example that works well with {@link AbstractAssert#is(Condition) is(Condition)}:
 * <pre><code class='java'> Condition&lt;String&gt; shorterThan4 = VerboseCondition.verboseCondition(actual -&gt; actual.length() &lt; 4,
                                                                   // predicate description  
                                                                   "shorter than 4",
                                                                   // value under test description transformation function
                                                                   s -&gt; String.format(" but length was %s", s.length(), s));</code></pre>
 * 
 * If we execute:
 * <pre><code class='java'> assertThat("foooo").is(shorterThan4);</code></pre>
 * it fails with the following assertion error:
 * <pre><code class='text'> Expecting actual:
 *   "foooo"
 * to be shorter than 4 but length was 5</code></pre>
 * <p>
 * Note that the beginning of the error message looks nice with {@link AbstractAssert#is(Condition) is(Condition)}, but not so much with {@link AbstractAssert#has(Condition) has(Condition)}:
 * <pre><code class='text'> Expecting actual:
 *   "foooo"
 * to have shorter than 4 but length was 5</code></pre>
 * <p>
 * The {@code objectUnderTestDescriptor} must not be null, if you don't need one this probably means you can simply use {@link Condition#Condition(Predicate, String, Object...)} instead of a {@code VerboseCondition}.
 *
 * @author Stefan Bischof
 *
 * @param <T> the type of object the given condition accept.
 */
@Beta
public final class VerboseCondition<T> extends Condition<T> {

  private Function<T, String> objectUnderTestDescriptor;

  // needed to avoid an incorrect description when matches is run multiple times.
  private String description;

  /**
   * Creates a new <code>{@link VerboseCondition}</code> to have better control over the condition description when the condition fails thanks 
   * to the {@code objectUnderTestDescriptor} function parameter.
   * <p>
   * When defining the {@code objectUnderTestDescriptor} function, you should take in consideration whether the condition is going to be used 
   * with {@link AbstractAssert#is(Condition) is(Condition)} or {@link AbstractAssert#has(Condition) has(Condition)} since the start of the error message is different between the two.  
   * <p>
   * Let's see how it works with an example that works well with {@link AbstractAssert#is(Condition) is(Condition)}:
   * <pre><code class='java'> Condition&lt;String&gt; shorterThan4 = VerboseCondition.verboseCondition(actual -&gt; actual.length() &lt; 4,
                                                                    // predicate description  
                                                                    "shorter than 4",
                                                                    // value under test description transformation function
                                                                    s -&gt; String.format(" but length was %s", s.length(), s));</code></pre>
   * 
   * If we execute:
   * <pre><code class='java'> assertThat("foooo").is(shorterThan4);</code></pre>
   * it fails with the following assertion error:
   * <pre><code class='text'> Expecting actual:
   *   "foooo"
   * to be shorter than 4 but length was 5</code></pre>
   * <p>
   * Note that the beginning of the error message looks nice with {@link AbstractAssert#is(Condition) is(Condition)}, but not so much with {@link AbstractAssert#has(Condition) has(Condition)}:
   * <pre><code class='text'> Expecting actual:
   *   "foooo"
   * to have shorter than 4 but length was 5</code></pre>
   * <p>
   * The {@code objectUnderTestDescriptor} must not be null, if you don't need one this probably means you can simply use {@link Condition#Condition(Predicate, String, Object...)} instead of a {@code VerboseCondition}.
   *
   * @param <T>                  the type of object the given condition accept.
   * @param predicate            the Predicate that tests the value to test.
   * @param description          describes the Condition verbal.
   * @param objectUnderTestDescriptor Function used to describe the value to test when the actual value does not match the predicate. must not be null.
   * @return the created {@code VerboseCondition}.
   * @throws NullPointerException if the predicate is {@code null}.
   * @throws NullPointerException if the objectUnderTestDescriptor is {@code null}.
   */
  public static <T> VerboseCondition<T> verboseCondition(Predicate<T> predicate, String description,
                                                         Function<T, String> objectUnderTestDescriptor) {
    return new VerboseCondition<>(predicate, description, objectUnderTestDescriptor);
  }

  private VerboseCondition(Predicate<T> predicate, String description, Function<T, String> objectUnderTestDescriptor) {
    super(predicate, description);
    this.description = description;
    this.objectUnderTestDescriptor = requireNonNull(objectUnderTestDescriptor,
                                                    "The objectUnderTest descriptor function must not be null, if you don't need one, consider using the basic Condition(Predicate<T> predicate, String description, Object... args) constructor");
  }

  @Override
  public boolean matches(T objectUnderTest) {
    boolean matches = super.matches(objectUnderTest);
    describedAs(buildVerboseDescription(objectUnderTest, matches));
    return matches;
  }

  /**
   * Build the verbose condition description when applied with the actual values and the match result.
   *
   * @param objectUnderTest the object to test
   * @param matches the result of the match operation
   * @return the verbose condition description.
   */
  protected String buildVerboseDescription(T objectUnderTest, boolean matches) {
    StringBuilder sb = new StringBuilder(format("%s", description));
    if (!matches) sb.append(objectUnderTestDescriptor.apply(objectUnderTest));
    return sb.toString();
  }

}
