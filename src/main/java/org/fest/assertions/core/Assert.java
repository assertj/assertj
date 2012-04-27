/*
 * Created on Jul 15, 2010
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

import java.util.Comparator;

/**
 * Base contract of all assertion objects: the minimum functionality that any assertion object should provide.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
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
   * Verifies that the actual value is present in the given collection of values.
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the actual value is not present in the given collection.
   */
  S isIn(Iterable<?> values);

  /**
   * Verifies that the actual value is not present in the given collection of values.
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the actual value is present in the given collection.
   */
  S isNotIn(Iterable<?> values);

  /**
   * Use given custom comparator instead of relying on actual type A equals method for incoming assertion checks.<br>
   * Custom comparator is bound to assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy. </p> Example :
   * 
   * <pre>
   * // compares invoices by payee 
   * assertThat(invoiceList).usingComparator(invoicePayeeComparator).isEqualTo(expectedInvoiceList).
   * 
   * // compares invoices by date, doesNotHaveDuplicates and contains both use the given invoice date comparator
   * assertThat(invoiceList).usingComparator(invoiceDateComparator).doesNotHaveDuplicates().contains(may2010Invoice)
   * 
   * // as assertThat(invoiceList) creates a new assertion, it uses standard comparison strategy (Invoice's equal method) to compare invoiceList elements to lowestInvoice.                                                      
   * assertThat(invoiceList).contains(lowestInvoice).
   * </pre>
   * 
   * Custom comparator is not parameterized with actual type A (ie. Comparator&lt;A&gt;) because if it was, we could not
   * write the following code :
   * 
   * <pre>
   * // frodo and sam are instances of Character (a Character having a Race)
   * // raceComparator implements Comparator&lt;Character&gt; 
   * // assertThat(frodo) returns an ObjectAssert and not a custom CharacterAssert implementing Assert&lt;CharacterAssert, Character&gt;  
   * assertThat(frodo).usingComparator(raceComparator).isEqualTo(sam); // won't compile !
   * 
   * The code does not compile because assertThat(frodo) returns an ObjectAssert, thus usingComparator expects a Comparator&lt;Object&gt; 
   * and Comparator&lt;Character&gt; is not a Comparator&lt;Object&gt; as generics are not reified.
   * 
   * Note that, it would have worked if assertThat(frodo) returned a CharacterAssert implementing Assert&lt;CharacterAssert, Character&gt;. 
   * </pre>
   * 
   * @param customComparator the comparator to use for incoming assertion checks.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  S usingComparator(Comparator<?> customComparator);

  /**
   * Revert to standard comparison for incoming assertion checks.<br>
   * This method should be used to disable a custom comparison strategy set by calling
   * {@link #usingComparator(Comparator)}.
   * @return {@code this} assertion object.
   */
  S usingDefaultComparator();
  
  /**
   * Throws <code>{@link UnsupportedOperationException}</code> if called. It is easy to accidentally call
   * <code>{@link #equals(Object)}</code> instead of <code>isEqualTo</code>.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override boolean equals(Object obj);
}
