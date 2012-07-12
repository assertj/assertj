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
package org.fest.assertions.core;

/**
 * Assertion methods applicable to <code>{@link Number}</code>s.
 * @param <T> the type of the "actual" value.
 * 
 * @author Alex Ruiz
 * @author Nicolas François
 */
public interface NumberAssert<T extends Number> {

  /**
   * Verifies that the actual value is equal to zero.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to zero.
   */
  NumberAssert<T> isZero();

  /**
   * Verifies that the actual value is not equal to zero.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to zero.
   */
  NumberAssert<T> isNotZero();

  /**
   * Verifies that the actual value is positive.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not positive.
   */
  NumberAssert<T> isPositive();

  /**
   * Verifies that the actual value is negative.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not negative.
   */
  NumberAssert<T> isNegative();

  /**
   * Verifies that the actual value is non negative (positive or equal zero).
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not non negative.
   */
  NumberAssert<T> isNotNegative();

  /**
   * Verifies that the actual value is non positive (negative or equal zero).
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not non positive.
   */
  NumberAssert<T> isNotPositive();

}
