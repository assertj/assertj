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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.Objects.requireNonNull;
import static org.assertj.core.api.DescriptionValidations.checkIsNotNull;

import java.util.function.Predicate;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.util.VisibleForTesting;

/**
 * A condition to be met by an object.
 * 
 * @param <T> the type of object this condition accepts.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Condition<T> implements Descriptable<Condition<T>> {

  @VisibleForTesting
  Description description;

  // might not be used
  private Predicate<T> predicate;

  /**
   * Creates a new </code>{@link Condition}</code>. The default description of this condition will the simple name of
   * the
   * condition's class.
   */
  public Condition() {
	as(getClass().getSimpleName());
  }

  /**
   * Creates a new </code>{@link Condition}</code>.
   * 
   * @param description the description of this condition.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public Condition(String description) {
	as(description);
  }

  /**
   * Creates a new </code>{@link Condition}</code> with the given {@link Predicate}, the built Condition will be met if
   * the Predicate is.
   * 
   * <p>
   * You must give a description, it will be used to build a nice error message when the condition fails, you can pass
   * args to build the description as in {@link String#format(String, Object...)}.
   * <p>
   * Example:
   * <pre><code class='java'> // build condition with Predicate&lt;String&gt; and set description using String#format pattern.
   * Condition&lt;String&gt; fairyTale = new Condition&lt;String&gt;(s -> s.startsWith("Once upon a time"), "a %s tale", "fairy");
   * 
   * String littleRedCap = "Once upon a time there was a dear little girl ...";
   * assertThat(littleRedCap).is(fairyTale);</code></pre>
   * 
   * Error message example:
   * <pre><code class='java'> // unfortunately this assertion fails ... but contact me if you can make it pass :)
   * assertThat("life").is(fairyTale);
   * // error message
   * Expecting:
   *  &lt;"life"&gt;
   * to be &lt;a fairy tale&gt;</code></pre>
   * 
   * @param predicate the {@link Predicate} used to build the condition.
   * @param description the description of this condition.
   * @param args optional parameter if description is a format String.
   * @throws NullPointerException if the given {@link Predicate} is {@code null}.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public Condition(Predicate<T> predicate, String description, Object... args) {
	checkPredicate(predicate);
	this.predicate = predicate;
	this.description = new TextDescription(description, args);
  }

  /**
   * Creates a new </code>{@link Condition}</code>.
   * 
   * @param description the description of this condition.
   * @throws NullPointerException if the given description is {@code null}.
   */
  public Condition(Description description) {
	as(description);
  }

  /** {@inheritDoc} */
  @Override
  public Condition<T> describedAs(String newDescription, Object... args) {
	return as(newDescription, args);
  }

  /** {@inheritDoc} */
  @Override
  public Condition<T> as(String newDescription, Object... args) {
	description = checkIsNotNull(newDescription, args);
	return this;
  }

  /** {@inheritDoc} */
  @Override
  public Condition<T> describedAs(Description newDescription) {
	return as(newDescription);
  }

  /** {@inheritDoc} */
  @Override
  public Condition<T> as(Description newDescription) {
	description = checkIsNotNull(newDescription);
	return this;
  }

  /**
   * Returns the description of this condition.
   * 
   * @return the description of this condition.
   */
  public Description description() {
	return description;
  }

  /**
   * Verifies that the given value satisfies this condition.
   * 
   * @param value the value to verify.
   * @return {@code true} if the given value satisfies this condition; {@code false} otherwise.
   */
  public boolean matches(T value) {
	checkPredicate(predicate);
	return predicate.test(value);
  }

  private void checkPredicate(Predicate<T> predicate) {
	requireNonNull(predicate,
	               "Unless you subclass Condition and override matches, you need to pass a non null Predicate to build a Condition.");
  }

  @Override
  public String toString() {
	return description.value();
  }
}
