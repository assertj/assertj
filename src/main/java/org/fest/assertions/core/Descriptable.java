/*
 * Created on Jul 19, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.core;

import org.fest.assertions.description.*;

/**
 * An object that has a description.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * 
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public interface Descriptable<S> {

  /**
   * Sets the description of this object.
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(String)
   */
  S as(String description);

  /**
   * Sets the description of this object. To remove or clear the description, pass a <code>{@link EmptyTextDescription}</code> as
   * argument.
   * <p>
   * This overloaded version of "describedAs" offers more flexibility than the one taking a {@code String} by allowing users to
   * pass their own implementation of a description. For example, a description that creates its value lazily, only when an
   * assertion failure occurs.
   * </p>
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   * @see #describedAs(Description)
   */
  S as(Description description);

  /**
   * Alias for <code>{@link #as(String)}</code> since "as" is a keyword in <a href="http://groovy.codehaus.org/"
   * target="_blank">Groovy</a>.
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  S describedAs(String description);

  /**
   * Alias for <code>{@link #as(String)}</code> since "as" is a keyword in <a href="http://groovy.codehaus.org/"
   * target="_blank">Groovy</a>. To remove or clear the description, pass a <code>{@link EmptyTextDescription}</code> as argument.
   * <p>
   * This overloaded version of "describedAs" offers more flexibility than the one taking a {@code String} by allowing users to
   * pass their own implementation of a description. For example, a description that creates its value lazily, only when an
   * assertion failure occurs.
   * </p>
   * @param description the new description to set.
   * @return {@code this} object.
   * @throws NullPointerException if the description is {@code null}.
   */
  S describedAs(Description description);
}
