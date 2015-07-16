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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Comparator;

/**
 * Base contract of all assertion objects: the minimum functionality that any assertion object should provide.
 * 
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <A> the type of the "actual" value.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 */
public interface Assert<S extends Assert<S, A>, A> extends Descriptable<S>, ExtensionPoints<S, A> {

  /**
   * Verifies that the actual value is equal to the given one.
   * 
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  S isEqualTo(Object expected);

  /**
   * Verifies that the actual value is not equal to the given one.
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  S isNotEqualTo(Object other);

  /**
   * Verifies that the actual value is {@code null}.
   * 
   * @throws AssertionError if the actual value is not {@code null}.
   */
  void isNull();

  /**
   * Verifies that the actual value is not {@code null}.
   * 
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   */
  S isNotNull();

  /**
   * Verifies that the actual value is the same as the given one, ie using == comparison.
   * <p>
   * Example:
   * <pre><code class='java'> // Name is a class with first and last fields, two Names are equals if both first and last are equals.
   * Name tyrion = new Name("Tyrion", "Lannister");
   * Name alias  = tyrion;
   * Name clone  = new Name("Tyrion", "Lannister");
   * 
   * // assertions succeed:
   * assertThat(tyrion).isSameAs(alias)
   *                   .isEqualTo(clone);
   *                      
   * // assertion fails:
   * assertThat(tyrion).isSameAs(clone);</code></pre>
   * 
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not the same as the given one.
   */
  S isSameAs(Object expected);

  /**
   * Verifies that the actual value is not the same as the given one, ie using == comparison.
   * <p>
   * Example:
   * <pre><code class='java'> // Name is a class with first and last fields, two Names are equals if both first and last are equals.
   * Name tyrion = new Name("Tyrion", "Lannister");
   * Name alias  = tyrion;
   * Name clone  = new Name("Tyrion", "Lannister");
   * 
   * // assertions succeed:
   * assertThat(clone).isNotSameAs(tyrion)
   *                  .isEqualTo(tyrion);
   *                      
   * // assertion fails:
   * assertThat(alias).isNotSameAs(tyrion);</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is the same as the given one.
   */
  S isNotSameAs(Object other);

  /**
   * Verifies that the actual value is present in the given array of values.
   * 
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the actual value is not present in the given array.
   */
  S isIn(Object... values);

  /**
   * Verifies that the actual value is not present in the given array of values.
   * 
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws IllegalArgumentException if the given array is empty.
   * @throws AssertionError if the actual value is present in the given array.
   */
  S isNotIn(Object... values);

  /**
   * Verifies that the actual value is present in the given values.
   * 
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the actual value is not present in the given collection.
   */
  S isIn(Iterable<?> values);

  /**
   * Verifies that the actual value is not present in the given values.
   * 
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws IllegalArgumentException if the given collection is empty.
   * @throws AssertionError if the actual value is present in the given collection.
   */
  S isNotIn(Iterable<?> values);

  /**
   * Use given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * Custom comparator is bound to assertion instance, meaning that if a new assertion is created, it will use default
   * comparison strategy.
   * <p>
   * Examples :
   * <pre><code class='java'> // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt; 
   * assertThat(frodo).usingComparator(raceComparator).isEqualTo(sam);</code></pre>
   * 
   * @param customComparator the comparator to use for incoming assertion checks.
   * @throws NullPointerException if the given comparator is {@code null}.
   * @return {@code this} assertion object.
   */
  S usingComparator(Comparator<? super A> customComparator);

  /**
   * Revert to standard comparison for incoming assertion checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling
   * {@link #usingComparator(Comparator)}.
   * 
   * @return {@code this} assertion object.
   */
  S usingDefaultComparator();

  /**
   * Verifies that the actual value is an instance of the given type.
   * 
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of the given type.
   */
  S isInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is an instance of any of the given types.
   * 
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
   * 
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is an instance of the given type.
   */
  S isNotInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is not an instance of any of the given types.
   * 
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
   * 
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if the actual has not the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S hasSameClassAs(Object other);

  /**
   * Verifies that actual <code>actual.toString()</code> is equal to the given <code>String</code>.
   * <p>
   * Example :
   * <pre><code class='java'> CartoonCaracter homer = new CartoonCaracter("Homer");
   *
   * // Instead of writing ...  
   * assertThat(homer.toString()).isEqualTo("Homer");
   * // ... you can simply write: 
   * assertThat(homer).hasToString("Homer");</code></pre>
   * 
   * @param expectedToString the expected String description of actual.
   * @return this assertion object.
   * @throws AssertionError if <code>actual.toString()</code> result is not to the given <code>String</code>.
   * @throws AssertionError if actual is {@code null}.
   */
  S hasToString(String expectedToString);

  /**
   * Verifies that the actual value does not have the same class as the given object.
   * 
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if the actual has the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S doesNotHaveSameClassAs(Object other);

  /**
   * Verifies that the actual value is <b>exactly</b> an instance of the given type.
   * 
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual is not <b>exactly</b> an instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S isExactlyInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is not <b>exactly</b> an instance of given type.
   * 
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual is exactly a instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S isNotExactlyInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value type is in given types.
   * 
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is not in given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  S isOfAnyClassIn(Class<?>... types);

  /**
   * Verifies that the actual value type is not in given types.
   * 
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is in given types.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  S isNotOfAnyClassIn(Class<?>... types);

  /**
   * Verifies that the actual value is an instance of List,
   * and returns a list assertion, to allow chaining of list-specific
   * assertions from this call.
   * <p>
   * Example :
   * <pre><code class='java'> Object listAsObject = newArrayList(1, 2, 3);
   *
   * assertThat(listAsObject).asList().isSorted();</code></pre>
   *
   * @return a list assertion object
   */
  AbstractListAssert<?, ?, Object> asList();

  /**
   * Verifies that the actual value is an instance of String,
   * and returns a String assertion, to allow chaining of String-specific
   * assertions from this call.
   * <p>
   * Example :
   * <pre><code class='java'> Object stringAsObject = "hello world";
   *
   * assertThat(stringAsObject).asString().contains("hello");</code></pre>
   *
   * @return a string assertion object
   */
  AbstractCharSequenceAssert<?, String> asString();

  /**
   * @deprecated
   *             Throws <code>{@link UnsupportedOperationException}</code> if called. It is easy to accidentally call
   *             <code>{@link #equals(Object)}</code> instead of <code>{@link #isEqualTo(Object)}</code>.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  boolean equals(Object obj);

  /**
   * In case of assertion error, the thread dump will be printed on {@link System#err}.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat("Messi").withThreadDumpOnError().isEqualTo("Ronaldo");</code></pre>
   * 
   * will print the thread dump, something looking like:
   * <pre><code>"JDWP Command Reader"
   * 	java.lang.Thread.State: RUNNABLE
   * 
   * "JDWP Event Helper Thread"
   * 	java.lang.Thread.State: RUNNABLE
   * 
   * "JDWP Transport Listener: dt_socket"
   * 	java.lang.Thread.State: RUNNABLE
   * 
   * "Signal Dispatcher"
   * 	java.lang.Thread.State: RUNNABLE
   * 
   * "Finalizer"
   * 	java.lang.Thread.State: WAITING
   * 		at java.lang.Object.wait(Native Method)
   * 		at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:135)
   * 		at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:151)
   * 		at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:189)
   * 
   * "Reference Handler"
   * 	java.lang.Thread.State: WAITING
   * 		at java.lang.Object.wait(Native Method)
   * 		at java.lang.Object.wait(Object.java:503)
   * 		at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:133)
   * 
   * "main"
   * 	java.lang.Thread.State: RUNNABLE
   * 		at sun.management.ThreadImpl.dumpThreads0(Native Method)
   * 		at sun.management.ThreadImpl.dumpAllThreads(ThreadImpl.java:446)
   * 		at org.assertj.core.internal.Failures.threadDumpDescription(Failures.java:193)
   * 		at org.assertj.core.internal.Failures.printThreadDumpIfNeeded(Failures.java:141)
   * 		at org.assertj.core.internal.Failures.failure(Failures.java:91)
   * 		at org.assertj.core.internal.Objects.assertEqual(Objects.java:314)
   * 		at org.assertj.core.api.AbstractAssert.isEqualTo(AbstractAssert.java:198)
   * 		at org.assertj.examples.ThreadDumpOnErrorExample.main(ThreadDumpOnErrorExample.java:28)</code></pre>
   * 
   * @return this assertion object.
   */
  S withThreadDumpOnError();
}
