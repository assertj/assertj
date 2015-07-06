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

import org.assertj.core.description.Description;
import org.assertj.core.description.EmptyTextDescription;

/**
 * An object that has a description.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 */
public interface Descriptable<S extends Descriptable<S>> {

  /**
   * Sets the description of this object supporting {@link String#format(String, Object...)} syntax.
   * <p>
   * Example :
   * </p>
   * 
   * <pre><code class='java'>
   * try {
   *   // set a bad age to Mr Frodo which is really 33 years old.
   *   frodo.setAge(50);
   *   // you can specify a test description with as() method or describedAs(), it supports String format args
   *   assertThat(frodo.getAge()).as(&quot;check %s's age&quot;, frodo.getName()).isEqualTo(33);
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage(&quot;[check Frodo's age] expected:&lt;[33]&gt; but was:&lt;[50]&gt;&quot;);
   * }
   * 
   * </code></pre>
   * 
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(String, Object...)
   */
  S as(String description, Object... args);

  /**
   * Sets the description of this object. To remove or clear the description, pass a
   * <code>{@link EmptyTextDescription}</code> as argument.
   * <p>
   * This overloaded version of "describedAs" offers more flexibility than the one taking a {@code String} by allowing
   * users to pass their own implementation of a description. For example, a description that creates its value lazily,
   * only when an assertion failure occurs.
   * </p>
   * 
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(Description)
   */
  S as(Description description);

  /**
   * Alias for <code>{@link #as(String, Object...)}</code> since "as" is a keyword in <a
   * href="http://groovy.codehaus.org/" target="_blank">Groovy</a>.
   * 
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  S describedAs(String description, Object... args);

  /**
   * Alias for <code>{@link #as(String, Object...)}</code> since "as" is a keyword in <a
   * href="http://groovy.codehaus.org/" target="_blank">Groovy</a>. To remove or clear the description, pass a
   * <code>{@link EmptyTextDescription}</code> as argument.
   * <p>
   * This overloaded version of "describedAs" offers more flexibility than the one taking a {@code String} by allowing
   * users to pass their own implementation of a description. For example, a description that creates its value lazily,
   * only when an assertion failure occurs.
   * </p>
   * 
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  S describedAs(Description description);
}
