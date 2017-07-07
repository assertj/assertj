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
package org.assertj.core.api;

import java.util.Comparator;
import java.util.List;

import org.assertj.core.data.Index;
import org.assertj.core.description.Description;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ComparisonStrategy;
import org.assertj.core.internal.Lists;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link List}s.
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @param <ELEMENT> the type of elements of the "actual" value.
 * @param <ELEMENT_ASSERT> used for navigational assertions to return the right assert type.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 */
//@format:off
public abstract class AbstractListAssert<SELF extends AbstractListAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>, 
                                         ACTUAL extends List<? extends ELEMENT>, 
                                         ELEMENT, 
                                         ELEMENT_ASSERT extends AbstractAssert<ELEMENT_ASSERT, ELEMENT>>
       extends AbstractIterableAssert<SELF, ACTUAL, ELEMENT, ELEMENT_ASSERT>
       implements IndexedObjectEnumerableAssert<SELF, ELEMENT> {
// @format:on

  @VisibleForTesting
  Lists lists = Lists.instance();

  public AbstractListAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public SELF contains(ELEMENT value, Index index) {
    lists.assertContains(info, actual, value, index);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public SELF doesNotContain(ELEMENT value, Index index) {
    lists.assertDoesNotContain(info, actual, value, index);
    return myself;
  }

  /**
   * Verifies that the actual object at the given index in the actual group satisfies the given condition.
   * 
   * @param condition the given condition.
   * @param index the index where the object should be stored in the actual group.
   * @return this assertion object.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the given {@code List}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the value in the given {@code List} at the given index does not satisfy the given
   *           {@code Condition} .
   */
  public SELF has(Condition<? super ELEMENT> condition, Index index) {
    lists.assertHas(info, actual, condition, index);
    return myself;
  }

  /**
   * Verifies that the actual object at the given index in the actual group satisfies the given condition.
   * 
   * @param condition the given condition.
   * @param index the index where the object should be stored in the actual group.
   * @return this assertion object.
   * @throws AssertionError if the given {@code List} is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of
   *           the given {@code List}.
   * @throws NullPointerException if the given {@code Condition} is {@code null}.
   * @throws AssertionError if the value in the given {@code List} at the given index does not satisfy the given
   *           {@code Condition} .
   */
  public SELF is(Condition<? super ELEMENT> condition, Index index) {
    lists.assertIs(info, actual, condition, index);
    return myself;
  }

  /**
   * Verifies that the actual list is sorted in ascending order according to the natural ordering of its elements.
   * <p>
   * All list elements must implement the {@link Comparable} interface and must be mutually comparable (that is,
   * e1.compareTo(e2) must not throw a ClassCastException for any elements e1 and e2 in the list), examples :
   * <ul>
   * <li>a list composed of {"a1", "a2", "a3"} is ok because the element type (String) is Comparable</li>
   * <li>a list composed of Rectangle {r1, r2, r3} is <b>NOT ok</b> because Rectangle is not Comparable</li>
   * <li>a list composed of {True, "abc", False} is <b>NOT ok</b> because elements are not mutually comparable</li>
   * </ul>
   * Empty lists are considered sorted.<br> Unique element lists are considered sorted unless the element type is not Comparable.
   *
   * @return {@code this} assertion object.
   *
   * @throws AssertionError if the actual list is not sorted in ascending order according to the natural ordering of its
   *           elements.
   * @throws AssertionError if the actual list is <code>null</code>.
   * @throws AssertionError if the actual list element type does not implement {@link Comparable}.
   * @throws AssertionError if the actual list elements are not mutually {@link Comparable}.
   */
  public SELF isSorted() {
    lists.assertIsSorted(info, actual);
    return myself;
  }

  /**
   * Verifies that the actual list is sorted according to the given comparator.<br> Empty lists are considered sorted whatever
   * the comparator is.<br> One element lists are considered sorted if the element is compatible with comparator.
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
  public SELF isSortedAccordingTo(Comparator<? super ELEMENT> comparator) {
    lists.assertIsSortedAccordingToComparator(info, actual, comparator);
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingElementComparator(Comparator<? super ELEMENT> customComparator) {
    super.usingElementComparator(customComparator);
    lists = new Lists(new ComparatorBasedComparisonStrategy(customComparator));
    return myself;
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultElementComparator() {
    super.usingDefaultElementComparator();
    lists = Lists.instance();
    return myself;
  }

  // can't really honor basic assertion consistently with this comparisonStrategy
  @Override
  protected SELF usingComparisonStrategy(ComparisonStrategy comparisonStrategy) {
    super.usingComparisonStrategy(comparisonStrategy);
    lists = new Lists(comparisonStrategy);
    return myself;
  }

  //

  // override methods to avoid compilation error when chaining an AbstractAssert method with a AbstractListAssert one on
  // raw types :(

  @Override
  @CheckReturnValue
  public SELF as(String description, Object... args) {
    return super.as(description, args);
  }

  @Override
  @CheckReturnValue
  public SELF as(Description description) {
    return super.as(description);
  }

  @Override
  @CheckReturnValue
  public SELF describedAs(Description description) {
    return super.describedAs(description);
  }

  @Override
  @CheckReturnValue
  public SELF describedAs(String description, Object... args) {
    return super.describedAs(description, args);
  }

  @Override
  public SELF doesNotHave(Condition<? super ACTUAL> condition) {
    return super.doesNotHave(condition);
  }

  @Override
  public SELF doesNotHaveSameClassAs(Object other) {
    return super.doesNotHaveSameClassAs(other);
  }

  @Override
  public SELF has(Condition<? super ACTUAL> condition) {
    return super.has(condition);
  }

  @Override
  public SELF hasSameClassAs(Object other) {
    return super.hasSameClassAs(other);
  }

  @Override
  public SELF hasToString(String expectedToString) {
    return super.hasToString(expectedToString);
  }

  @Override
  public SELF is(Condition<? super ACTUAL> condition) {
    return super.is(condition);
  }

  @Override
  public SELF isEqualTo(Object expected) {
    return super.isEqualTo(expected);
  }

  @Override
  public SELF isExactlyInstanceOf(Class<?> type) {
    return super.isExactlyInstanceOf(type);
  }

  @Override
  public SELF isIn(Iterable<?> values) {
    return super.isIn(values);
  }

  @Override
  public SELF isIn(Object... values) {
    return super.isIn(values);
  }

  @Override
  public SELF isInstanceOf(Class<?> type) {
    return super.isInstanceOf(type);
  }

  @Override
  public SELF isInstanceOfAny(Class<?>... types) {
    return super.isInstanceOfAny(types);
  }

  @Override
  public SELF isNot(Condition<? super ACTUAL> condition) {
    return super.isNot(condition);
  }

  @Override
  public SELF isNotEqualTo(Object other) {
    return super.isNotEqualTo(other);
  }

  @Override
  public SELF isNotExactlyInstanceOf(Class<?> type) {
    return super.isNotExactlyInstanceOf(type);
  }

  @Override
  public SELF isNotIn(Iterable<?> values) {
    return super.isNotIn(values);
  }

  @Override
  public SELF isNotIn(Object... values) {
    return super.isNotIn(values);
  }

  @Override
  public SELF isNotInstanceOf(Class<?> type) {
    return super.isNotInstanceOf(type);
  }

  @Override
  public SELF isNotInstanceOfAny(Class<?>... types) {
    return super.isNotInstanceOfAny(types);
  }

  @Override
  public SELF isNotOfAnyClassIn(Class<?>... types) {
    return super.isNotOfAnyClassIn(types);
  }

  @Override
  public SELF isNotNull() {
    return super.isNotNull();
  }

  @Override
  public SELF isNotSameAs(Object other) {
    return super.isNotSameAs(other);
  }

  @Override
  public SELF isOfAnyClassIn(Class<?>... types) {
    return super.isOfAnyClassIn(types);
  }

  @Override
  public SELF isSameAs(Object expected) {
    return super.isSameAs(expected);
  }

  @Override
  @CheckReturnValue
  public SELF overridingErrorMessage(String newErrorMessage, Object... args) {
    return super.overridingErrorMessage(newErrorMessage, args);
  }

  @Override
  @CheckReturnValue
  public SELF usingDefaultComparator() {
    return super.usingDefaultComparator();
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator) {
    return super.usingComparator(customComparator);
  }

  @Override
  @CheckReturnValue
  public SELF withFailMessage(String newErrorMessage, Object... args) {
    return super.withFailMessage(newErrorMessage, args);
  }

  @Override
  @CheckReturnValue
  public SELF withThreadDumpOnError() {
    return super.withThreadDumpOnError();
  }

}
