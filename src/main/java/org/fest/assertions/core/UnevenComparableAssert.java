/*
 * Created on Oct 19, 2010
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

import java.math.BigDecimal;

/**
 * Assertion methods applicable to <code>{@link Comparable}</code>s whose implementation of {@code compareTo} is not consistent
 * with their implementation of {@code equals} (e.g. <code>{@link BigDecimal}</code>.)
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <T> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 */
public interface UnevenComparableAssert<S, T extends Comparable<? super T>> extends ComparableAssert<S, T> {

  /**
   * Verifies that the actual value is equal to the given one by invoking <code>{@link Comparable#compareTo(Object)}</code>.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  S isEqualByComparingTo(T expected);

  /**
   * Verifies that the actual value is not equal to the given one by invoking <code>{@link Comparable#compareTo(Object)}</code>.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  S isNotEqualByComparingTo(T other);
}
