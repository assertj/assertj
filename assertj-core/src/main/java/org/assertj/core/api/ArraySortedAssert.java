/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

/**
 * Assertions applicable to primitive arrays or arrays of elements either naturally {@link Comparable} or according to a given
 * {@link Comparator}.
 * <p>
 * Note that the contract defined here is can't be totally applied to List (that's why its name is not SortedAssert), the
 * differences being that we can't check that - for empty List - the list parameter is comparable or compatible with given
 * comparator due to type erasure.
 *
 * @author Joel Costigliola
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class that must be a array type (e.g. arrays, collections).<br>
 *          Please read &quot;<a href="http://bit.ly/1IZIRcY" target="_blank">Emulating 'self types' using Java Generics to
 *          simplify fluent API implementation</a>&quot; for more details.
 * @param <ELEMENT> the array element type.
 */
public interface ArraySortedAssert<SELF extends ArraySortedAssert<SELF, ELEMENT>, ELEMENT> {

  /**
   * Verifies that the actual array is sorted in ascending order according to the natural ordering of its elements.
   * <p>
   * All array elements must be primitive or implement the {@link Comparable} interface and must be mutually comparable (that is,
   * e1.compareTo(e2) must not throw a ClassCastException for any elements e1 and e2 in the array), examples :
   * <ul>
   * <li>a array composed of {2, 4, 6} is ok because the element type is a primitive type.</li>
   * <li>a array composed of {"a1", "a2", "a3"} is ok because the element type (String) is Comparable</li>
   * <li>a array composed of Rectangle {r1, r2, r3} is <b>NOT ok</b> because Rectangle is not Comparable</li>
   * <li>a array composed of {True, "abc", False} is <b>NOT ok</b> because elements are not mutually comparable (even though each
   * element type implements Comparable)</li>
   * </ul>
   * Empty or one element arrays are considered sorted (unless the array element type is not Comparable).<br><br>
   * 
   * @return {@code this} assertion object.
   * 
   * @throws AssertionError if the actual array is not sorted in ascending order according to the natural ordering of its
   *           elements.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws AssertionError if the actual array element type does not implement {@link Comparable}.
   * @throws AssertionError if the actual array elements are not mutually {@link Comparable}.
   */
  SELF isSorted();

  /**
   * Verifies that the actual array is sorted according to the given comparator.<br> Empty arrays are considered sorted whatever
   * the comparator is.<br> One element arrays are considered sorted if the element is compatible with comparator, otherwise an
   * AssertionError is thrown.
   * 
   * @param comparator the {@link Comparator} used to compare array elements
   * 
   * @return {@code this} assertion object.
   * 
   * @throws AssertionError if the actual array is not sorted according to the given comparator.
   * @throws AssertionError if the actual array is {@code null}.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @throws AssertionError if the actual array elements are not mutually comparable according to given Comparator.
   */
  SELF isSortedAccordingTo(Comparator<? super ELEMENT> comparator);

}
