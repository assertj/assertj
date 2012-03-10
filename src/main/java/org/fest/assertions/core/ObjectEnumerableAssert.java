/*
 * Created on Jul 26, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.core;

/**
 * Assertions methods applicable to groups of objects (e.g. arrays or collections.)
 * @param <S> the "self" type of this assertion class. Please read
 * &quot;<a href="http://bit.ly/anMa4g" target="_blank">Emulating 'self types' using Java Generics to simplify fluent
 * API implementation</a>&quot; for more details.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François 
 */
public interface ObjectEnumerableAssert<S> extends EnumerableAssert<S> {

  /**
   * Verifies that the actual group contains the given values, in any order.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values.
   */
  S contains(Object... values);

  /**
   * Verifies that the actual group contains only the given values and nothing else, in any order.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   * or none of the given values, or the actual group contains more values than the given ones.
   */
  S containsOnly(Object... values);

  /**
   * Verifies that the actual group contains the given sequence, without any other values between them.
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given sequence.
   */
  S containsSequence(Object... sequence);

  /**
   * Verifies that the actual group does not contain the given values.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains any of the given values.
   */
  S doesNotContain(Object... values);

  /**
   * Verifies that the actual group does not contain duplicates.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains duplicates.
   */
  S doesNotHaveDuplicates();

  /**
   * Verifies that the actual group starts with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the first element in the
   * sequence is also first element of the actual group.
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not start with the given sequence of objects.
   */
  S startsWith(Object... sequence);

  /**
   * Verifies that the actual group ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence(Object...)}</code>, but it also verifies that the last element in the
   * sequence is also last element of the actual group.
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not end with the given sequence of objects.
   */
  S endsWith(Object... sequence);

  /**
   * Verifies that the actual group contains at least a null element.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain a null element.
   */
  S containsNull();

  /**
   * Verifies that the actual group does not contain null elements.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains a null element.
   */
  S doesNotContainNull();
  
  /**
   * Verifies that each element value satisfies the given condition
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  <E> S eachElementIs(Condition<E> condition);
  
 
  /**
   * Verifies that each element value not satisfies the given condition
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  <E> S eachElementIsNot(Condition<E> condition);
  
  /**
   * Verifies that each element value satisfies the given condition
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  <E> S eachElementHas(Condition<E> condition);
  
 
  /**
   * Verifies that each element value not satisfies the given condition
   * @param condition the given condition.
   * @return {@code this ExtensionPoints} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  <E> S eachElementHasNot(Condition<E> condition); 

}
