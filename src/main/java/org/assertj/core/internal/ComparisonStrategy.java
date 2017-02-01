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
package org.assertj.core.internal;

/**
 * Describes the contract to implement a <b>consistent</b> comparison strategy that covers :<br>
 * - comparing two objects for equality and order<br>
 * - knowing if an object belongs to a group of objects (collection/array)<br>
 * - determining duplicates in a group of objects (collection/array)<br>
 * - string specific comparison<br>
 * 
 * @author Joel Costigliola
 */
public interface ComparisonStrategy {

  /**
   * Returns true if actual and other are equal according to the implemented comparison strategy.
   * 
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual and other are equal according to the underlying comparison strategy.
   */
  boolean areEqual(Object actual, Object other);

  /**
   * Returns true if actual is greater than other, false otherwise.
   * 
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual is greater than other, false otherwise.
   * @throws UnsupportedOperationException if operation is not supported by a concrete implementation.
   */
  boolean isGreaterThan(Object actual, Object other);

  /**
   * Returns true if actual is greater than or equal to other, false otherwise.
   * 
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual is greater than or equal to other, false otherwise.
   * @throws UnsupportedOperationException if operation is not supported by a concrete implementation.
   */
  boolean isGreaterThanOrEqualTo(Object actual, Object other);

  /**
   * Returns true if actual is less than other, false otherwise.
   * 
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual is less than other, false otherwise.
   * @throws UnsupportedOperationException if operation is not supported by a concrete implementation.
   */
  boolean isLessThan(Object actual, Object other);

  /**
   * Returns true if actual is less than or equal to other, false otherwise.
   * 
   * @param actual the object to compare to other
   * @param other the object to compare to actual
   * @return true if actual is less than or equal to other, false otherwise.
   * @throws UnsupportedOperationException if operation is not supported by a concrete implementation.
   */
  boolean isLessThanOrEqualTo(Object actual, Object other);

  /**
   * Returns true if given {@link Iterable} contains given value according to the implemented comparison strategy, false
   * otherwise.<br>
   * If given {@link Iterable} is null, return false.
   * 
   * @param collection the {@link Iterable} to search value in
   * @param value the object to look for in given {@link Iterable}
   * @return true if given {@link Iterable} contains given value according to the implemented comparison strategy, false
   *         otherwise.
   */
  boolean iterableContains(Iterable<?> collection, Object value);

  /**
   * Look for given value in given {@link Iterable} according to the implemented comparison strategy, if value is found it is
   * removed from it.<br>
   * If given {@link Iterable} is null, does nothing.
   * 
   * @param iterable the {@link Iterable} we want remove value from
   * @param value object to remove from given {@link Iterable}
   */
  void iterableRemoves(Iterable<?> iterable, Object value);

  /**
   * Removes the first value in {@code iterable} that matches the {@code value} according to the implemented comparison
   * strategy. If given {@link Iterable} is null, does nothing.
   *
   * @param iterable the {@link Iterable} we want remove value from
   * @param value object to remove from given {@link Iterable}
   */
  void iterablesRemoveFirst(Iterable<?> iterable, Object value);

  /**
   * Returns any duplicate elements from the given {@link Iterable} according to the implemented comparison strategy.
   * 
   * @param iterable the given {@link Iterable} we want to extract duplicate elements.
   * @return an {@link Iterable} containing the duplicate elements of the given one. If no duplicates are found, an empty
   *         {@link Iterable} is returned.
   */
  Iterable<?> duplicatesFrom(Iterable<?> iterable);

  /**
   * Returns true if given array contains given value according to the implemented comparison strategy, false otherwise.
   * 
   * @param array the array to search value in (must not be null)
   * @param value the object to look for in given array
   * @return true if given array contains given value according to the implemented comparison strategy, false otherwise.
   */
  boolean arrayContains(Object array, Object value);

  /**
   * Returns true if given string contains given sequence according to the implemented comparison strategy, false otherwise.
   * 
   * @param string the string to search sequence in (must not be null)
   * @param sequence the String to look for in given string
   * @return true if given string contains given sequence according to the implemented comparison strategy, false otherwise.
   */
  boolean stringContains(String string, String sequence);

  /**
   * Returns true if string starts with prefix according to the implemented comparison strategy, false otherwise.
   * 
   * @param string the String we want to look starting prefix
   * @param prefix the prefix String to look for at string's start
   * @return true if string starts with prefix according to the implemented comparison strategy, false otherwise.
   */
  boolean stringStartsWith(String string, String prefix);

  /**
   * Returns true if string ends with suffix according to the implemented comparison strategy, false otherwise.
   * 
   * @param string the String we want to look starting suffix
   * @param suffix the suffix String to look for at string's end
   * @return true if string ends with suffix according to the implemented comparison strategy, false otherwise.
   */
  boolean stringEndsWith(String string, String suffix);
  
  /**
   * Return true if comparison strategy is default/standard, false otherwise
   * @return true if comparison strategy is default/standard, false otherwise
   */
  boolean isStandard();

}
