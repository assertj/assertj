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


/**
 * Mechanism for extending assertion classes.
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 */
public interface ExtensionPoints<SELF extends ExtensionPoints<SELF, ACTUAL>, ACTUAL> {

  /**
   * Verifies that the actual value satisfies the given condition. This method is an alias for
   * <code>{@link #has(Condition)}</code>.
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value does not satisfy the given condition.
   * @see #is(Condition)
   */
  SELF is(Condition<? super ACTUAL> condition);

  /**
   * Verifies that the actual value does not satisfy the given condition. This method is an alias for
   * <code>{@link #doesNotHave(Condition)}</code>.
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value satisfies the given condition.
   * @see #isNot(Condition)
   */
  SELF isNot(Condition<? super ACTUAL> condition);

  /**
   * Verifies that the actual value satisfies the given condition. This method is an alias for <code>{@link #is(Condition)}</code>
   * .
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value does not satisfy the given condition.
   * @see #is(Condition)
   */
  SELF has(Condition<? super ACTUAL> condition);

  /**
   * Verifies that the actual value does not satisfy the given condition. This method is an alias for
   * <code>{@link #isNot(Condition)}</code>.
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual value satisfies the given condition.
   * @see #isNot(Condition)
   */
  SELF doesNotHave(Condition<? super ACTUAL> condition);
}
