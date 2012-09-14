/*
 * Created on Nov 3, 2010
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
package org.fest.assertions.internal;

import java.util.Comparator;

import org.fest.assertions.core.ArraySortedAssert;
import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.Condition;
import org.fest.assertions.data.Index;
import org.fest.util.VisibleForTesting;

/**
 * Reusable assertions for arrays of objects.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public class ObjectArrays {

  private static final ObjectArrays INSTANCE = new ObjectArrays();

  /**
   * Returns the singleton instance of this class.
   * @return the singleton instance of this class.
   */
  public static ObjectArrays instance() {
    return INSTANCE;
  }

  private Arrays arrays = Arrays.instance();

  @VisibleForTesting
  ObjectArrays() {
    this(StandardComparisonStrategy.instance());
  }

  public ObjectArrays(ComparisonStrategy comparisonStrategy) {
    this.arrays = new Arrays(comparisonStrategy);
  }

  @VisibleForTesting
  public Comparator<?> getComparator() {
    return arrays.getComparator();
  }

  @VisibleForTesting
  Failures failures = Failures.instance();

  @VisibleForTesting
  Conditions conditions = Conditions.instance();

  /**
   * Asserts that the given array is {@code null} or empty.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is not {@code null} *and* contains one or more elements.
   */
  public void assertNullOrEmpty(AssertionInfo info, Object[] actual) {
    arrays.assertNullOrEmpty(info, failures, actual);
  }

  /**
   * Asserts that the given array is empty.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array is not empty.
   */
  public void assertEmpty(AssertionInfo info, Object[] actual) {
    arrays.assertEmpty(info, failures, actual);
  }

  /**
   * Asserts that the given array is not empty.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array is empty.
   */
  public void assertNotEmpty(AssertionInfo info, Object[] actual) {
    arrays.assertNotEmpty(info, failures, actual);
  }

  /**
   * Asserts that the number of elements in the given array is equal to the expected one.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param expectedSize the expected size of {@code actual}.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the number of elements in the given array is different than the expected one.
   */
  public void assertHasSize(AssertionInfo info, Object[] actual, int expectedSize) {
    arrays.assertHasSize(info, failures, actual, expectedSize);
  }

  /**
   * Assert that the actual array has the same size as the other {@code Iterable}.
   * @param info contains information about the assertion.
   * @param actual the given iterable.
   * @param other the group to compare
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other group is {@code null}.
   * @throws AssertionError if the actual group does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Object[] actual, Iterable<?> other) {
    arrays.assertHasSameSizeAs(info, failures, actual, other);
  }

  /**
   * Assert that the actual array has the same size as the other array.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param other the group to compare
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the other group is {@code null}.
   * @throws AssertionError if the actual group does not have the same size.
   */
  public void assertHasSameSizeAs(AssertionInfo info, Object[] actual, Object[] other) {
    arrays.assertHasSameSizeAs(info, failures, actual, other);
  }

  /**
   * Asserts that the given array contains the given values, in any order.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain the given values.
   */
  public void assertContains(AssertionInfo info, Object[] actual, Object[] values) {
    arrays.assertContains(info, failures, actual, values);
  }

  /**
   * Verifies that the given array contains the given object at the given index.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param value the object to look for.
   * @param index the index where the object should be stored in the given array.
   * @throws AssertionError if the given array is {@code null} or empty.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws IndexOutOfBoundsException if the value of the given {@code Index} is equal to or greater than the size of the given
   *           array.
   * @throws AssertionError if the given array does not contain the given object at the given index.
   */
  public void assertContains(AssertionInfo info, Object[] actual, Object value, Index index) {
    arrays.assertContains(info, failures, actual, value, index);
  }

  /**
   * Verifies that the given array does not contain the given object at the given index.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param value the object to look for.
   * @param index the index where the object should be stored in the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws NullPointerException if the given {@code Index} is {@code null}.
   * @throws AssertionError if the given array contains the given object at the given index.
   */
  public void assertDoesNotContain(AssertionInfo info, Object[] actual, Object value, Index index) {
    arrays.assertDoesNotContain(info, failures, actual, value, index);
  }

  /**
   * Asserts that the given array contains only the given values and nothing else, in any order.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain the given values or if the given array contains values that are
   *           not in the given array.
   */
  public void assertContainsOnly(AssertionInfo info, Object[] actual, Object[] values) {
    arrays.assertContainsOnly(info, failures, actual, values);
  }

  /**
   * Verifies that the given array contains the given sequence of objects, without any other objects between them.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param sequence the sequence of objects to look for.
   * @throws AssertionError if the given array is {@code null}.
   * @throws NullPointerException if the given sequence is {@code null}.
   * @throws IllegalArgumentException if the given sequence is empty.
   * @throws AssertionError if the given array does not contain the given sequence of objects.
   */
  public void assertContainsSequence(AssertionInfo info, Object[] actual, Object[] sequence) {
    arrays.assertContainsSequence(info, failures, actual, sequence);
  }

  /**
   * Asserts that the given array does not contain the given values.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param values the values that are expected not to be in the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array contains any of given values.
   */
  public void assertDoesNotContain(AssertionInfo info, Object[] actual, Object[] values) {
    arrays.assertDoesNotContain(info, failures, actual, values);
  }

  /**
   * Asserts that the given array does not have duplicate values.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws NullPointerException if the array of values is {@code null}.
   * @throws IllegalArgumentException if the array of values is empty.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array contains duplicate values.
   */
  public void assertDoesNotHaveDuplicates(AssertionInfo info, Object[] actual) {
    arrays.assertDoesNotHaveDuplicates(info, failures, actual);
  }

  /**
   * Verifies that the given array starts with the given sequence of objects, without any other objects between them. Similar to
   * <code>{@link #assertContainsSequence(AssertionInfo, Object[], Object[])}</code>, but it also verifies that the first element
   * in the sequence is also the first element of the given array.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param sequence the sequence of objects to look for.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not start with the given sequence of objects.
   */
  public void assertStartsWith(AssertionInfo info, Object[] actual, Object[] sequence) {
    arrays.assertStartsWith(info, failures, actual, sequence);
  }

  /**
   * Verifies that the given array ends with the given sequence of objects, without any other objects between them. Similar to
   * <code>{@link #assertContainsSequence(AssertionInfo, Object[], Object[])}</code>, but it also verifies that the last element
   * in the sequence is also the last element of the given array.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param sequence the sequence of objects to look for.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not end with the given sequence of objects.
   */
  public void assertEndsWith(AssertionInfo info, Object[] actual, Object[] sequence) {
    arrays.assertEndsWith(info, failures, actual, sequence);
  }

  /**
   * Asserts that the given array contains at least a null element.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array does not contain a null element.
   */
  public void assertContainsNull(AssertionInfo info, Object[] actual) {
    arrays.assertContainsNull(info, failures, actual);
  }

  /**
   * Asserts that the given array does not contain null elements.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @throws AssertionError if the given array is {@code null}.
   * @throws AssertionError if the given array contains a null element.
   */
  public void assertDoesNotContainNull(AssertionInfo info, Object[] actual) {
    arrays.assertDoesNotContainNull(info, failures, actual);
  }

  /**
   * Assert that each element of given array satisfies the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  public <E> void assertAre(AssertionInfo info, E[] actual, Condition<? super E> condition) {
    arrays.assertAre(info, failures, conditions, actual, condition);
  }

  /**
   * Assert that each element of given array not satisfies the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  public <E> void assertAreNot(AssertionInfo info, E[] actual, Condition<? super E> condition) {
    arrays.assertAreNot(info, failures, conditions, actual, condition);
  }

  /**
   * Assert that each element of given array satisfies the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if one or more element not satisfy the given condition.
   */
  public <E> void assertHave(AssertionInfo info, E[] actual, Condition<? super E> condition) {
    arrays.assertHave(info, failures, conditions, actual, condition);
  }

  /**
   * Assert that each element of given array not satisfies the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if one or more element satisfy the given condition.
   */
  public <E> void assertDoNotHave(AssertionInfo info, E[] actual, Condition<? super E> condition) {
    arrays.assertHaveNot(info, failures, conditions, actual, condition);
  }

  /**
   * Assert that there is <b>at least</b> <i>n</i> array elements satisfying the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param n the minimum number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the number of elements satisfying the given condition is &lt; n.
   */
  public <E> void assertAreAtLeast(AssertionInfo info, E[] actual, int n, Condition<? super E> condition) {
    arrays.assertAreAtLeast(info, failures, conditions, actual, n, condition);
  }

  /**
   * Assert that there is <b>at least</b> <i>n</i> array elements <b>not</b> satisfying the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param n the number of times the condition should not be verified at least.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the number of elements not satisfying the given condition is &lt; n.
   */
  public <E> void assertAreNotAtLeast(AssertionInfo info, E[] actual, int n, Condition<? super E> condition) {
    arrays.assertAreNotAtLeast(info, failures, conditions, actual, n, condition);
  }

  /**
   * Assert that there is <b>at most</b> <i>n</i> array elements satisfying the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param n the number of times the condition should be at most verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the number of elements satisfying the given condition is &gt; n.
   */
  public <E> void assertAreAtMost(AssertionInfo info, E[] actual, int n, Condition<? super E> condition) {
    arrays.assertAreAtMost(info, failures, conditions, actual, n, condition);
  }

  /**
   * Verifies that there is <b>at most</b> <i>n</i> array elements <b>not</b> satisfying the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param n the number of times the condition should not be verified at most.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the number of elements not satisfying the given condition is &gt; n.
   */
  public <E> void assertAreNotAtMost(AssertionInfo info, E[] actual, int n, Condition<? super E> condition) {
    arrays.assertAreNotAtMost(info, failures, conditions, actual, n, condition);
  }

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> array elements satisfying the given condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param n the exact number of times the condition should be verified.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the number of elements satisfying the given condition is &ne; n.
   */
  public <E> void assertAreExactly(AssertionInfo info, E[] actual, int n, Condition<? super E> condition) {
    arrays.assertAreExactly(info, failures, conditions, actual, n, condition);
  }

  /**
   * Verifies that there is <b>exactly</b> <i>n</i> elements in the actual {@code Iterable} <b>not</b> satisfying the given
   * condition.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param n most times the condition should not be verify.
   * @param condition the given {@code Condition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the number of elements not satisfying the given condition is &ne; n.
   */
  public <E> void assertAreNotExactly(AssertionInfo info, E[] actual, int n, Condition<? super E> condition) {
    arrays.assertAreNotExactly(info, failures, conditions, actual, n, condition);
  }

  /**
   * An alias method of {@link #assertAreAtLeast(AssertionInfo, Object[], int, Condition)} to provide a richer fluent api (same
   * logic, only error message differs).
   */
  public <E> void assertHaveAtLeast(AssertionInfo info, E[] actual, int times, Condition<? super E> condition) {
    arrays.assertHaveAtLeast(info, failures, conditions, actual, times, condition);
  }

  /**
   * An alias method of {@link #assertAreNotAtLeast(AssertionInfo, Object[], int, Condition)} to provide a richer fluent api (same
   * logic, only error message differs).
   */
  public <E> void assertDoNotHaveAtLeast(AssertionInfo info, E[] actual, int times, Condition<? super E> condition) {
    arrays.assertDoNotHaveAtLeast(info, failures, conditions, actual, times, condition);
  }

  /**
   * An alias method of {@link #assertAreAtMost(AssertionInfo, Object[], int, Condition)} to provide a richer fluent api (same
   * logic, only error message differs).
   */
  public <E> void assertHaveAtMost(AssertionInfo info, E[] actual, int times, Condition<? super E> condition) {
    arrays.assertHaveAtMost(info, failures, conditions, actual, times, condition);
  }

  /**
   * An alias method of {@link #assertAreNotAtMost(AssertionInfo, Object[], int, Condition)} to provide a richer fluent api (same
   * logic, only error message differs).
   */
  public <E> void assertDoNotHaveAtMost(AssertionInfo info, E[] actual, int times, Condition<? super E> condition) {
    arrays.assertDoNotHaveAtMost(info, failures, conditions, actual, times, condition);
  }

  /**
   * An alias method of {@link #assertAreExactly(AssertionInfo, Object[], int, Condition)} to provide a richer fluent api (same
   * logic, only error message differs).
   */
  public <E> void assertHaveExactly(AssertionInfo info, E[] actual, int times, Condition<? super E> condition) {
    arrays.assertHaveExactly(info, failures, conditions, actual, times, condition);
  }

  /**
   * An alias method of {@link #assertAreNotExactly(AssertionInfo, Object[], int, Condition)} to provide a richer fluent api (same
   * logic, only error message differs).
   */
  public <E> void assertDoNotHaveExactly(AssertionInfo info, E[] actual, int times, Condition<? super E> condition) {
    arrays.assertDoNotHaveExactly(info, failures, conditions, actual, times, condition);
  }

  /**
   * Concrete implementation of {@link ArraySortedAssert#isSorted()}.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   */
  public void assertIsSorted(AssertionInfo info, Object[] actual) {
    arrays.assertIsSorted(info, failures, actual);
  }

  /**
   * Concrete implementation of {@link ArraySortedAssert#isSortedAccordingTo(Comparator)}.
   * 
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param comparator the {@link Comparator} used to compare array elements
   */
  public <E> void assertIsSortedAccordingToComparator(AssertionInfo info, E[] actual, Comparator<? super E> comparator) {
    Arrays.assertIsSortedAccordingToComparator(info, failures, actual, comparator);
  }

  /**
   * Asserts that the given array contains all the elements of the given {@code Iterable}, in any order.
   * @param info contains information about the assertion.
   * @param actual the given array.
   * @param other the other {@code Iterable}.
   * @throws NullPointerException if {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} is {@code null}.
   * @throws AssertionError if the given {@code Iterable} does not contain all the elements of the other {@code Iterable}, in any
   *           order.
   */
  public <E> void assertContainsAll(AssertionInfo info, E[] actual, Iterable<? extends E> other) {
    arrays.assertcontainsAll(info, failures, actual, other);
  }

}
