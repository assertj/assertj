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

import org.assertj.core.description.Description;

/**
 * An object that has a description.
 * 
 * @param <SELF> the "self" type of this assertion class.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 */
public interface Descriptable<SELF> {

  /**
   * Sets the description of the assertion that is going to be called after. 
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks 
   * the chained call by throwing an AssertionError.
   * <p>
   * The description follows {@link String#format(String, Object...)} syntax.
   * <p>
   * Example :
   * <pre><code class='java'> try {
   *   // set a bad age to Mr Frodo which is really 33 years old.
   *   frodo.setAge(50);
   *   // specify a test description (call as() before the assertion !), it supports String format syntax.
   *   assertThat(frodo.getAge()).as(&quot;check %s's age&quot;, frodo.getName()).isEqualTo(33);
   * } catch (AssertionError e) {
   *   assertThat(e).hasMessage(&quot;[check Frodo's age] expected:&lt;[33]&gt; but was:&lt;[50]&gt;&quot;);
   * }</code></pre>
   * 
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(String, Object...)
   */
  SELF as(String description, Object... args);

  /**
   * Sets the description of the assertion that is going to be called after. 
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks 
   * the chained call by throwing an AssertionError.
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
  SELF as(Description description);

  /**
   * Sets the description of the assertion that is going to be called after. 
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks 
   * the chained call by throwing an AssertionError.
   * <p>
   * Alias for <code>{@link #as(String, Object...)}</code> since "as" is a keyword in <a
   * href="http://groovy-lang.org/" target="_blank">Groovy</a>.
   * 
   * @param description the new description to set.
   * @param args optional parameter if description is a format String.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  SELF describedAs(String description, Object... args);

  /**
   * Sets the description of the assertion that is going to be called after. 
   * <p>
   * You must set it <b>before</b> calling the assertion otherwise it is ignored as the failing assertion breaks 
   * the chained call by throwing an AssertionError.
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
  SELF describedAs(Description description);
}
