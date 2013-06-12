/*
 * Created on Oct 17, 2010
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
package org.assertj.core.api;

/**
 * Assertion methods applicable to <code>{@link Number}</code>s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 *
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public interface NumberAssert<S extends NumberAssert<S, A>, A extends Number> {

  /**
   * Verifies that the actual value is equal to zero.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  S isZero();

  /**
   * Verifies that the actual value is not equal to zero.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  S isNotZero();

  /**
   * Verifies that the actual value is positive.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive.
   */
  S isPositive();

  /**
   * Verifies that the actual value is negative.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative.
   */
  S isNegative();

  /**
   * Verifies that the actual value is non negative (positive or equal zero).
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not non negative.
   */
  S isNotNegative();

  /**
   * Verifies that the actual value is non positive (negative or equal zero).
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not non positive.
   */
  S isNotPositive();
  
  /**
   * Verifies that the actual value is in [start, end] range (start included, end included).
   * <pre>
   * // these assertions succeed ... 
   * assertThat(12).isBetween(10, 14);
   * assertThat(12).isBetween(12, 14);
   * assertThat(12).isBetween(10, 12);
   * 
   * // ... but these one fails
   * assertThat(12).isBetween(14, 16);
   * </pre>
   * @param start the start value (inclusive), expected not to be null.
   * @param end the end value (inclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in [start, end] range.
   */
  S isBetween(A start, A end);
  
  /**
   * Verifies that the actual value is in ]start, end[ range (start excluded, end excluded).
   * 
   * <pre>
   * // this assertion succeeds ... 
   * assertThat(12).isBetween(10, 14);
   * 
   * // ... but these one fails
   * assertThat(12).isBetween(12, 14);
   * assertThat(12).isBetween(10, 12);
   * assertThat(12).isBetween(16, 18);
   * </pre>
   * 
   * @param start the start value (exclusive), expected not to be null.
   * @param end the end value (exclusive), expected not to be null.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws NullPointerException if start value is {@code null}.
   * @throws NullPointerException if end value is {@code null}.
   * @throws AssertionError if the actual value is not in ]start, end[ range.
   */
  S isStrictlyBetween(A start, A end);
}
