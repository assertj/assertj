/*
 * Created on Jul 15, 2010
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

import java.util.Comparator;

/**
 * Base contract of all assertion objects: the minimum functionality that any assertion object should provide.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public interface Assert<S, A> extends Descriptable<S>, ExtensionPoints<S, A> {

  /**
   * Verifies that the actual value is equal to the given one.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  S isEqualTo(A expected);

  /**
   * Verifies that the actual value is not equal to the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  S isNotEqualTo(A other);

  /**
   * Verifies that the actual value is {@code null}.
   * @throws AssertionError if the actual value is not {@code null}.
   */
  void isNull();

  /**
   * Verifies that the actual value is not {@code null}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   */
  S isNotNull();

  /**
   * Verifies that the actual value is the same as the given one.
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not the same as the given one.
   */
  S isSameAs(A expected);

  /**
   * Verifies that the actual value is not the same as the given one.
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is the same as the given one.
   */
  S isNotSameAs(A other);

  /**
   * Verifies that the actual value is present in the given array of values.
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the actual value is not present in the given array.
   */
  S isIn(A... values);

  /**
   * Verifies that the actual value is not present in the given array of values.
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the actual value is present in the given array.
   */
  S isNotIn(A... values);

  /**
   * Verifies that the actual value is present in the given values.
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the actual value is not present in the given collection.
   */
  S isIn(Iterable<? extends A> values);

  /**
   * Verifies that the actual value is not present in the given values.
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the actual value is present in the given collection.
   */
  S isNotIn(Iterable<? extends A> values);

  /**
   * Use given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * Custom comparator is bound to assertion instance, meaning that if a new assertion is created, it will use default comparison
   * strategy.
   * <p>
   * Examples :
   * 
   * <pre>
   * // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt; 
   * assertThat(frodo).usingComparator(raceComparator).isEqualTo(sam); 
   * </pre>
   * 
   * @param customComparator the comparator to use for incoming assertion checks.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  S usingComparator(Comparator<? super A> customComparator);

  /**
   * Revert to standard comparison for incoming assertion checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling {@link #usingComparator(Comparator)}.
   * @return {@code this} assertion object.
   */
  S usingDefaultComparator();

  /**
   * Verifies that the actual value is an instance of the given type.
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of the given type.
   */
  S isInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is an instance of any of the given types.
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of any of the given types.
   * @throws NullPointerException if the given array of types is {@code null}.
   * @throws NullPointerException if the given array of types contains {@code null}s.
   */
  S isInstanceOfAny(Class<?>... types);

  /**
   * Verifies that the actual value is not an instance of the given type.
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is an instance of the given type.
   */
  S isNotInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is not an instance of any of the given types.
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is an instance of any of the given types.
   * @throws NullPointerException if the given array of types is {@code null}.
   * @throws NullPointerException if the given array of types contains {@code null}s.
   */
  S isNotInstanceOfAny(Class<?>... types);

  /**
   * Verifies that the actual value has the same class as the given object.
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if the actual has not the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S hasSameClassAs(Object other);

  /**
   * Verifies that the actual value does not have the same class as the given object.
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if the actual has the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S doesNotHaveSameClassAs(Object other);

  /**
   * Verifies that the actual value is <b>exactly</b> an instance of the given type.
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual is not <b>exactly</b> an instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S isExactlyInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is not <b>exactly</b> an instance of given type.
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual is exactly a instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S isNotExactlyInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value type is in given types.
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is not in given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  S isOfAnyClassIn(Class<?>... types);

  /**
   * Verifies that the actual value type is not in given types.
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is in given types.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  S isNotOfAnyClassIn(Class<?>... types);

  /**
   * Throws <code>{@link UnsupportedOperationException}</code> if called. It is easy to accidentally call
   * <code>{@link #equals(Object)}</code> instead of <code>isEqualTo</code>.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  boolean equals(Object obj);
}
