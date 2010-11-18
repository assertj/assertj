/*
 * Created on Oct 26, 2010
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
package org.fest.assertions.api;

import java.util.List;

import org.fest.assertions.data.Index;

/**
 * Assertion methods for <code>{@link List}</code>s. To create an instance of this class, use the factory method
 * <code>{@link Assertions#assertThat(List)}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ListAssert extends GenericCollectionAssert<ListAssert, List<?>> {

  protected ListAssert(List<?> actual) {
    super(actual);
  }

  /**
   * Verifies that the list value contains the given object at the given index.
   * @param o the object to look for.
   * @param index the index where the object should be stored in the actual {@code List}.
   * @return this assertion object.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   * the actual {@code List}.
   * @throws AssertionError if the actual list is {@code null}.
   * @throws AssertionError if the actual list does not contain the given object at the given index.
   */
  public ListAssert contains(Object o, Index index) {
    // TODO implement
    return null;
  }

  /**
   * Verifies that the actual list contains the given sequence of objects, without any other objects between them.
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the actual list is {@code null}.
   * @throws AssertionError if the actual list does not contain the given sequence of objects.
   */
  public ListAssert containsSequence(Object... sequence) {
    // TODO implement
    return null;
  }

  /**
   * Verifies that the actual list starts with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence}</code>, but verifies also that the first given object is also the first
   * element of the actual list.
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the actual list is {@code null}.
   * @throws AssertionError if the actual list does not start with the given sequence of objects.
   */
  public ListAssert startsWith(Object... sequence) {
    // TODO implement
    return null;
  }

  /**
   * Verifies that the actual list ends with the given sequence of objects, without any other objects between them.
   * Similar to <code>{@link #containsSequence}</code>, but verifies also that the last given object is also the last
   * element of actual list.
   * @param sequence the sequence of objects to look for.
   * @return this assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the actual list is {@code null}.
   * @throws AssertionError if the actual list does not end with the given sequence of objects.
   */
  public ListAssert endsWith(Object... sequence) {
    // TODO implement
    return null;
  }
}
