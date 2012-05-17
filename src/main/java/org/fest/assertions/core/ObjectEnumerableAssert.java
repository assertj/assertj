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
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <T> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 * @author Joel Costigliola
 */
public interface ObjectEnumerableAssert<S, T> extends EnumerableAssert<S, T> {

  /**
   * Verifies that the actual group contains the given values, in any order.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values.
   */
  S contains(T... values);

  /**
   * Verifies that the actual group contains only the given values and nothing else, in any order.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain the given values, i.e. the actual group contains some
   *           or none of the given values, or the actual group contains more values than the given ones.
   */
  S containsOnly(T... values);

  /**
   * Verifies that the actual group contains the given sequence, without any other values between them.
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the actual group does not contain the given sequence.
   */
  S containsSequence(T... sequence);

  /**
   * Verifies that the actual group does not contain the given values.
   * @param values the given values.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group contains any of the given values.
   */
  S doesNotContain(T... values);

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
  S startsWith(T... sequence);

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
  S endsWith(T... sequence);

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
   * Verifies that the actual group has the same size as given array.
   * @param other the array to compare size with actual group.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other array is {@code null}.
   * @throws AssertionError if actual group and given array don't have the same size.
   */
  S hasSameSizeAs(Object[] other);

  /**
   * Verifies that the actual group has the same size as given {@link Iterable}.
   * @param other the {@code Iterable} to compare size with actual group.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other {@code Iterable} is {@code null}.
   * @throws AssertionError if actual group and given {@code Iterable} don't have the same size.
   */
  S hasSameSizeAs(Iterable<?> other);

  /**
   * Verifies that each element value satisfies the given condition
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  <E> S are(Condition<E> condition);

  /**
   * Verifies that each element value not satisfies the given condition
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  <E> S areNot(Condition<E> condition);

  /**
   * Verifies that each element value satisfies the given condition
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  <E> S have(Condition<E> condition);

  /**
   * Verifies that each element value not satisfies the given condition
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  <E> S doNotHave(Condition<E> condition);

  /**
   * Verifies that there is <b>at least</b> <i>n</i> elements in the actual group satisfying the given condition.
   * @param n the minimum number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if an element can not be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  <E> S areAtLeast(int n, Condition<E> condition);

  /**
   * Verifies that there is <b>at least</b> <i>n</i> elements in the actual group <b>not</b> satisfying the given
   * condition.
   * @param n the number of times the condition should not be verified at least.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements not satisfying the given condition is &lt; n.
   */
  <E> S areNotAtLeast(int n, Condition<E> condition);

  /**
   * Verifies that there is <b>at most</b> <i>n</i> elements in the actual group satisfying the given condition.
   * @param n the number of times the condition should be at most verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  <E> S areAtMost(int n, Condition<E> condition);

  /**
   * Verifies that there is <b>at most</b> <i>n</i> elements in the actual group <b>not</b> satisfying the given
   * condition.
   * @param n the number of times the condition should not be verified at most.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements not satisfying the given condition is &gt; n.
   */
  <E> S areNotAtMost(int n, Condition<E> condition);

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> elements in the actual group satisfying the given condition.
   * @param n the exact number of times the condition should be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  <E> S areExactly(int n, Condition<E> condition);

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> elements in the actual group <b>not</b> satisfying the given
   * condition.
   * @param n the exact number of times the condition should not be verified.
   * @param condition the given condition.
   * @return {@code this} object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if a element cannot be cast to E.
   * @throws AssertionError if the number of elements not satisfying the given condition is &ne; n.
   */
  <E> S areNotExactly(int n, Condition<E> condition);

  /**
   * This method is an alias for {@link #areAtLeast(int, Condition)}.
   */
  <E> S haveAtLeast(int n, Condition<E> condition);

  /**
   * This method is an alias {@link #areNotAtLeast(int, Condition)}.
   */
  <E> S doNotHaveAtLeast(int n, Condition<E> condition);

  /**
   * This method is an alias {@link #areAtMost(int, Condition)}.
   */
  <E> S haveAtMost(int n, Condition<E> condition);

  /**
   * This method is an alias {@link #areNotAtMost(int, Condition)}.
   */
  <E> S doNotHaveAtMost(int n, Condition<E> condition);

  /**
   * This method is an alias {@link #areExactly(int, Condition)}.
   */
  <E> S haveExactly(int n, Condition<E> condition);

  /**
   * This method is an alias {@link #areNotExactly(int, Condition)}.
   */
  <E> S doNotHaveExactly(int n, Condition<E> condition);

  /**
   * Verifies that the actual group contains all the elements of given {@code Iterable}, in any order.
   * @param iterable the given {@code Iterable} we will get elements from.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the actual group does not contain all the elements of given {@code Iterable}.
   */
  S containsAll(Iterable<? extends T> iterable);

}
