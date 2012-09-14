/*
 * Created on Oct 26, 2010
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
package org.fest.assertions.api;

import java.util.Comparator;
import java.util.List;

import org.fest.assertions.core.Condition;
import org.fest.assertions.core.IndexedObjectEnumerableAssert;
import org.fest.assertions.data.Index;
import org.fest.assertions.internal.*;
import org.fest.util.VisibleForTesting;

/**
 * Assertion methods for <code>{@link List}</code>s.
 * <p>
 * To create an instance of this class, invoke <code>{@link Assertions#assertThat(List)}</code>.
 * </p>
 * @param <T> the type of elements of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
// TODO inherits from IterableAssert and remove AbstractIterableAssert ?
public class ListAssert<T> extends AbstractIterableAssert<ListAssert<T>, List<T>, T> implements
    IndexedObjectEnumerableAssert<ListAssert<T>, T> {

  @VisibleForTesting
  Lists lists = Lists.instance();

  protected ListAssert(List<T> actual) {
    super(actual, ListAssert.class);
  }

  /** {@inheritDoc} */
  public ListAssert<T> contains(T value, Index index) {
    lists.assertContains(info, actual, value, index);
    return this;
  }

  /** {@inheritDoc} */
  public ListAssert<T> doesNotContain(T value, Index index) {
    lists.assertDoesNotContain(info, actual, value, index);
    return this;
  }

  /**
   * Verifies that the actual object at the given index in the actual group satisfies the given condition.
   * @param condition the given condition.
   * @param index the index where the object should be stored in the actual group.
   * @return this assertion object.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the given
   *           {@code List}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the value in the given {@code List} at the given index does not satisfy the given {@code Condition}
   *           .
   */
  public ListAssert<T> has(Condition<? super T> condition, Index index) {
    lists.assertHas(info, actual, condition, index);
    return this;
  }

  /**
   * Verifies that the actual object at the given index in the actual group satisfies the given condition.
   * @param condition the given condition.
   * @param index the index where the object should be stored in the actual group.
   * @return this assertion object.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the given
   *           {@code List}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the value in the given {@code List} at the given index does not satisfy the given {@code Condition}
   *           .
   */
  public ListAssert<T> is(Condition<? super T> condition, Index index) {
    lists.assertIs(info, actual, condition, index);
    return this;
  }

  /**
   * Verifies that the actual list is sorted into ascending order according to the natural ordering of its elements.
   * <p>
   * All list elements must implement the {@link Comparable} interface and must be mutually comparable (that is, e1.compareTo(e2)
   * must not throw a ClassCastException for any elements e1 and e2 in the list), examples :
   * <ul>
   * <li>a list composed of {"a1", "a2", "a3"} is ok because the element type (String) is Comparable</li>
   * <li>a list composed of Rectangle {r1, r2, r3} is <b>NOT ok</b> because Rectangle is not Comparable</li>
   * <li>a list composed of {True, "abc", False} is <b>NOT ok</b> because elements are not mutually comparable</li>
   * </ul>
   * Empty lists are considered sorted.</br> Unique element lists are considered sorted unless the element type is not Comparable.
   * 
   * @return {@code this} assertion object.
   * 
   * @throws AssertionError if the actual list is not sorted into ascending order according to the natural ordering of its
   *           elements.
   * @throws AssertionError if the actual list is <code>null</code>.
   * @throws AssertionError if the actual list element type does not implement {@link Comparable}.
   * @throws AssertionError if the actual list elements are not mutually {@link Comparable}.
   */
  public ListAssert<T> isSorted() {
    lists.assertIsSorted(info, actual);
    return this;
  }

  /**
   * Verifies that the actual list is sorted according to the given comparator.</br> Empty lists are considered sorted whatever
   * the comparator is.</br> One element lists are considered sorted if element is compatible with comparator.
   * 
   * @param comparator the {@link Comparator} used to compare list elements
   * 
   * @return {@code this} assertion object.
   * 
   * @throws AssertionError if the actual list is not sorted according to the given comparator.
   * @throws AssertionError if the actual list is <code>null</code>.
   * @throws NullPointerException if the given comparator is <code>null</code>.
   * @throws AssertionError if the actual list elements are not mutually comparable according to given Comparator.
   */
  public ListAssert<T> isSortedAccordingTo(Comparator<? super T> comparator) {
    lists.assertIsSortedAccordingToComparator(info, actual, comparator);
    return this;
  }

  @Override
  public ListAssert<T> usingElementComparator(Comparator<? super T> customComparator) {
    super.usingElementComparator(customComparator);
    this.lists = new Lists(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  public ListAssert<T> usingDefaultElementComparator() {
    super.usingDefaultElementComparator();
    this.lists = Lists.instance();
    return myself;
  }
}
