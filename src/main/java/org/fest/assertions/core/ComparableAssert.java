/*
 * Created on Oct 17, 2010
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.core;

/**
 * Assertion methods applicable to <code>{@link Comparable}</code>s.
 * @param <T> the type of the "actual" value.
 *
 * @author Alex Ruiz
 * @author Ted M. Young
 */
public interface ComparableAssert<T extends Comparable<T>> extends Assert<T> {

  /**
   * Verifies that the actual value is equal to the given one by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  ComparableAssert<T> isEqualByComparingTo(T expected);

  /**
   * Verifies that the actual value is not equal to the given one by invoking
   * <code>{@link Comparable#compareTo(Object)}</code>.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  ComparableAssert<T> isNotEqualByComparingTo(T other);

  /**
   * Verifies that the actual value is less than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is equal to or greater than the given one.
   */
  ComparableAssert<T> isLessThan(T other);

  /**
   * Verifies that the actual value is less than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is greater than the given one.
   */
  ComparableAssert<T> isLessThanOrEqualTo(T other);

  /**
   * Verifies that the actual value is greater than the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is equal to or less than the given one.
   */
  ComparableAssert<T> isGreaterThan(T other);

  /**
   * Verifies that the actual value is greater than or equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is less than the given one.
   */
  ComparableAssert<T> isGreaterThanOrEqualTo(T other);
}
