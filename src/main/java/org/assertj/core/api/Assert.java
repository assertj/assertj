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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;

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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;abc&quot;);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isEqualTo(new HashMap&lt;String, Integer&gt;());
   * 
   * // assertions will fail
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).isEqualTo(1);</code></pre>
   * 
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  S isEqualTo(Object expected);

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;abc&quot;).isNotEqualTo(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).isNotEqualTo(1);
   * 
   * // assertions will fail
   * assertThat(&quot;abc&quot;).isNotEqualTo(&quot;abc&quot;);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotEqualTo(new HashMap&lt;String, Integer&gt;());</code></pre>
   * 
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  S isNotEqualTo(Object other);

  /**
   * Verifies that the actual value is {@code null}.
   * <p>
   * Example:
   * <pre><code class='java'> String value = null;
   * // assertion will pass
   * assertThat(value).isNull();
   * 
   * // assertions will fail
   * assertThat(&quot;abc&quot;).isNull();
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNull();</code></pre>
   * 
   * @throws AssertionError if the actual value is not {@code null}.
   */
  void isNull();

  /**
   * Verifies that the actual value is not {@code null}.
  * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(&quot;abc&quot;).isNotNull();
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotNull();
   * 
   * // assertion will fail
   * String value = null;
   * assertThat(value).isNotNull();</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = new Ring[] { vilya, nenya, narya };
   * 
   * // assertion will pass:
   * assertThat(nenya).isIn(elvesRings);
   * 
   * // assertion will fail:
   * assertThat(oneRing).isIn(elvesRings);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = new Ring[] { vilya, nenya, narya };
   * 
   * // assertion will pass:
   * assertThat(oneRing).isNotIn(elvesRings);
   * 
   * // assertion will fail:
   * assertThat(nenya).isNotIn(elvesRings);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertion will pass:
   * assertThat(nenya).isIn(elvesRings);
   * 
   * // assertion will fail:
   * assertThat(oneRing).isIn(elvesRings);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = newArrayList(vilya, nenya, narya);
   * 
   * // assertion will pass:
   * assertThat(oneRing).isNotIn(elvesRings);
   * 
   * // assertion will fail:
   * assertThat(nenya).isNotIn(elvesRings);</code></pre>
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
   * 
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
   * 
   * {@link #usingComparator(Comparator)}.
   * 
   * @return {@code this} assertion object.
   */
  S usingDefaultComparator();

  /**
   * Verifies that the actual value is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;abc&quot;).isInstanceOf(String.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOf(HashMap.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOf(Map.class);
   * 
   * // assertions will fail
   * assertThat(1).isInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOf(LinkedList.class);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;abc&quot;).isInstanceOfAny(String.class, Integer.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOfAny(LinkedList.class, ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOfAny(TreeMap.class, Map.class);
   * 
   * // assertions will fail
   * assertThat(1).isInstanceOfAny(Double.class, Float.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOfAny(LinkedList.class, Vector.class);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(1).isNotInstanceOf(Double.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOf(LinkedList.class);
   * 
   * // assertions will fail
   * assertThat(&quot;abc&quot;).isNotInstanceOf(String.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotInstanceOf(HashMap.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotInstanceOf(Map.class);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(1).isNotInstanceOfAny(Double.class, Float.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOfAny(LinkedList.class, Vector.class);
   * 
   * // assertions will fail
   * assertThat(1).isNotInstanceOfAny(Double.class, Integer.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOfAny(LinkedList.class, ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotInstanceOfAny(TreeMap.class, Map.class);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(1).hasSameClassAs(2);
   * assertThat(&quot;abc&quot;).hasSameClassAs(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).hasSameClassAs(new ArrayList&lt;Integer&gt;());
   * 
   * // assertions will fail
   * assertThat(1).hasSameClassAs(&quot;abc&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).hasSameClassAs(new LinkedList&lt;String&gt;());</code></pre>
   * 
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if the actual has not the same type has the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S hasSameClassAs(Object other);

  /**
   * Verifies that actual {@code actual.toString()} is equal to the given {@code String}.
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
   * @throws AssertionError if {@code actual.toString()} result is not to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   */
  S hasToString(String expectedToString);

  /**
   * Verifies that the actual value does not have the same class as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(1).doesNotHaveSameClassAs(&quot;abc&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).doesNotHaveSameClassAs(new LinkedList&lt;String&gt;());
   * 
   * // assertions will fail
   * assertThat(1).doesNotHaveSameClassAs(2);
   * assertThat(&quot;abc&quot;).doesNotHaveSameClassAs(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).doesNotHaveSameClassAs(new ArrayList&lt;Integer&gt;());</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(&quot;abc&quot;).isExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isExactlyInstanceOf(ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isExactlyInstanceOf(HashMap.class);
   * 
   * // assertions will fail
   * assertThat(1).isExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isExactlyInstanceOf(List.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isExactlyInstanceOf(Map.class);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(1).isNotExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotExactlyInstanceOf(List.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotExactlyInstanceOf(Map.class);
   * 
   * // assertions will fail
   * assertThat(&quot;abc&quot;).isNotExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotExactlyInstanceOf(ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotExactlyInstanceOf(HashMap.class);</code></pre>
   * 
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual is exactly an instance of given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  S isNotExactlyInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value type is in given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new HashMap&lt;String, Integer&gt;()).isOfAnyClassIn(HashMap.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isOfAnyClassIn(ArrayList.class, LinkedList.class);
   * 
   * // assertions will fail
   * assertThat(new HashMap&lt;String, Integer&gt;()).isOfAnyClassIn(TreeMap.class, Map.class);
   * assertThat(new ArrayList&lt;String&gt;()).isOfAnyClassIn(LinkedList.class, List.class);</code></pre>
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
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotOfAnyClassIn(Map.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotOfAnyClassIn(LinkedList.class, List.class);
   * 
   * // assertions will fail
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotOfAnyClassIn(HashMap.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotOfAnyClassIn(ArrayList.class, LinkedList.class);</code></pre>
   * 
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is in given types.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  S isNotOfAnyClassIn(Class<?>... types);

  /**
   * Verifies that the actual value is an instance of {@link BigDecimal} and allow to chain BigDecimal specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object bigDecimalAsObject = new BigDecimal("10.0");
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(bigDecimalAsObject).isEqualByComparingTo("10");
   * 
   * // assertion will pass
   * assertThat(bigDecimalAsObject).asBigDecimal().isEqualByComparingTo("10");</code></pre>
   * 
   * @return a BigDecimal assertion object
   */
  AbstractBigDecimalAssert<?> asBigDecimal();

  /**
   * Verifies that the actual value is an instance of {@link Boolean} and allow to chain Boolean specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object booleanAsObject = true;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(booleanAsObject).isTrue();
   * 
   * // assertion will pass
   * assertThat(booleanAsObject).asBoolean().isTrue();</code></pre>
   * 
   * @return a Boolean assertion object
   */
  AbstractBooleanAssert<?> asBoolean();

  /**
   * Verifies that the actual value is an instance of {@code boolean[]} and allow to chain {@code boolean[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object booleanArrayAsObject = new boolean[] {true, false};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(booleanArrayAsObject).contains(true);
   * 
   * // assertion will pass
   * assertThat(booleanArrayAsObject).asBooleanArray().contains(true);</code></pre>
   * 
   * @return a boolean array assertion object
   */
  AbstractBooleanArrayAssert<?> asBooleanArray();

  /**
   * Verifies that the actual value is an instance of {@link Byte} and allow to chain Byte specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object byteAsObject = (byte) -1;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(byteAsObject).isNegative();
   * 
   * // assertion will pass
   * assertThat(byteAsObject).asByte().isNegative();</code></pre>
   * 
   * @return a Byte assertion object
   */
  AbstractByteAssert<?> asByte();

  /**
   * Verifies that the actual value is an instance of {@code byte[]} and allow to chain {@code byte[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> byte one = 1;
   * byte two = 2; 
   * Object byteArrayAsObject = new byte[] {one, two};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(byteArrayAsObject).contains(one, two);
   * 
   * // assertion will pass
   * assertThat(byteArrayAsObject).asByteArray().contains(one, two);</code></pre>
   * 
   * @return a byte array assertion object
   */
  AbstractByteArrayAssert<?> asByteArray();

  /**
   * Verifies that the actual value is an instance of {@link Char} and allow to chain Char specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object charAsObject = 'a';
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(charAsObject).isLowerCase();
   * 
   * // assertion will pass
   * assertThat(charAsObject).asCharacter().isLowerCase();</code></pre>
   * 
   * @return a Character assertion object
   */
  AbstractCharacterAssert<?> asCharacter();

  /**
   * Verifies that the actual value is an instance of {@code char[]} and allow to chain {@code char[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object charArrayAsObject = new char[] {'a', 'b'};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(charArrayAsObject).contains('a', 'b');
   * 
   * // assertion will pass
   * assertThat(charArrayAsObject).asCharArray().contains('a', 'b');</code></pre>
   * 
   * @return a char array assertion object
   */
  AbstractCharArrayAssert<?> asCharArray();

  /**
   * Verifies that the actual value is an instance of {@link Class} and allow to chain Class specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object classAsObject = String.class;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(classAsObject).isFinal();
   * 
   * // assertion will pass
   * assertThat(classAsObject).asClass().isFinal();</code></pre>
   * 
   * @return a Class assertion object
   */
  AbstractClassAssert<?> asClass();

  /**
   * Verifies that the actual value is an instance of {@link Comparable} and allow to chain Comparable specific assertions after the call.
   * <p>
   * Example :
   * <pre><code comparable='java'> Object comparableAsObject = new Date(10);
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(comparableAsObject).isLessThan(new Date(1000));
   * 
   * // assertion will pass
   * assertThat(comparableAsObject).asComparable(Date.class).isLessThan(new Date(1000));</code></pre>
   * 
   * @param comparableClass the comparable type to use in comparable assertions 
   * 
   * @return a Comparable assertion object
   */
  <T extends Comparable<? super T>> AbstractComparableAssert<?, T> asComparable(Class<T> comparableClass);

  /**
   * Verifies that the actual value is an instance of {@link Date} and allow to chain Date specific assertions after the call.
   * <p>
   * Example :
   * <pre><code date='java'> Object dateAsObject = new Date();
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(dateAsObject).isAfter("2000-01-01");
   * 
   * // assertion will pass
   * assertThat(dateAsObject).asDate().isAfter("2000-01-01");</code></pre>
   * 
   * @return a Date assertion object
   */
  AbstractDateAssert<?> asDate();

  /**
   * Verifies that the actual value is an instance of {@link Double} and allow to chain Double specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object doubleAsObject = -1.0D;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(doubleAsObject).isNegative();
   * 
   * // assertion will pass
   * assertThat(doubleAsObject).asDouble().isNegative();</code></pre>
   * 
   * @return a Double assertion object
   */
  AbstractDoubleAssert<?> asDouble();

  /**
   * Verifies that the actual value is an instance of {@code double[]} and allow to chain {@code double[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> double one = 1.0;
   * double two = 2.0; 
   * Object doubleArrayAsObject = new double[] {one, two};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(doubleArrayAsObject).contains(one, two);
   * 
   * // assertion will pass
   * assertThat(doubleArrayAsObject).asDoubleArray().contains(one, two);</code></pre>
   * 
   * @return a double array assertion object
   */
  AbstractDoubleArrayAssert<?> asDoubleArray();

  /**
   * Verifies that the actual value is an instance of {@link File} and allow to chain File specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object fileAsObject = File tmpFile = File.createTempFile(&quot;tmp&quot;, &quot;txt&quot;);
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(fileAsObject).exists();
   * 
   * // assertion will pass
   * assertThat(fileAsObject).asFile().exists();</code></pre>
   * 
   * @return a File assertion object
   */
  AbstractFileAssert<?> asFile();

  /**
   * Verifies that the actual value is an instance of {@link Float} and allow to chain Float specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object floatAsObject = -1.0F;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(floatAsObject).isNegative();
   * 
   * // assertion will pass
   * assertThat(floatAsObject).asFloat().isNegative();</code></pre>
   * 
   * @return a Float assertion object
   */
  AbstractFloatAssert<?> asFloat();

  /**
   * Verifies that the actual value is an instance of {@code float[]} and allow to chain {@code float[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> float one = 1.0f;
   * float two = 2.0f; 
   * Object floatArrayAsObject = new float[] {one, two};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(floatArrayAsObject).contains(one, two);
   * 
   * // assertion will pass
   * assertThat(floatArrayAsObject).asFloatArray().contains(one, two);</code></pre>
   * 
   * @return a float array assertion object
   */
  AbstractFloatArrayAssert<?> asFloatArray();

  /**
   * Verifies that the actual value is an instance of {@link InputStream} and allow to chain InputStream specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object inputstreamAsObject = new ByteArrayInputStream(new byte[] {0xa});
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(inputstreamAsObject).hasSameContentAs(new ByteArrayInputStream(new byte[] {0xa}));
   * 
   * // assertion will pass
   * assertThat(inputstreamAsObject).asInputStream().hasSameContentAs(new ByteArrayInputStream(new byte[] {0xa}));</code></pre>
   * 
   * @return a InputStream assertion object
   */
  AbstractInputStreamAssert<?, ?> asInputStream();

  /**
   * Verifies that the actual value is an instance of {@link Integer} and allow to chain Integer specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object intAsObject = -1;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(intAsObject).isNegative();
   * 
   * // assertion will pass
   * assertThat(intAsObject).asInt().isNegative();</code></pre>
   * 
   * @return a Integer assertion object
   */
  AbstractIntegerAssert<?> asInteger();

  /**
   * Verifies that the actual value is an instance of {@code int[]} and allow to chain {@code int[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> int one = 1;
   * int two = 2; 
   * Object intArrayAsObject = new int[] {one, two};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(intArrayAsObject).contains(one, two);
   * 
   * // assertion will pass
   * assertThat(intArrayAsObject).asIntArray().contains(one, two);</code></pre>
   * 
   * @return a int array assertion object
   */
  AbstractIntArrayAssert<?> asIntArray();

  /**
   * Verifies that the actual value is an instance of {@link Iterable} and allow to chain Iterable specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object iterableAsObject = Arrays.asList("abc", "xyz");
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(iterableAsObject).contains("abc");
   * 
   * // assertion will pass
   * assertThat(iterableAsObject).asIterable().contains("abc");</code></pre>
   * 
   * @return a Iterable assertion object
   */
  AbstractIterableAssert<?, Iterable<? extends Object>, Object, ObjectAssert<Object>> asIterable();

  /**
   * Verifies that the actual value is an instance of {@link Iterator} and allow to chain Iterator specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object iteratorAsObject = Arrays.asList("abc", "xyz").iterator();
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(iteratorAsObject).contains("abc");
   * 
   * // assertion will pass
   * assertThat(iteratorAsObject).asIterator().contains("abc");</code></pre>
   * 
   * @return a Iterator assertion object
   */
  AbstractIterableAssert<?, Iterable<? extends Object>, Object, ObjectAssert<Object>> asIterator();

  /**
   * Verifies that the actual value is an instance of {@link List} and allow to chain List specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object sortedListAsObject = Arrays.asList(1, 2, 3);
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(sortedListAsObject).isSorted();
   * 
   * // assertion will pass
   * assertThat(sortedListAsObject).asList().isSorted();
   * 
   * Object unsortedListAsObject = Arrays.asList(3, 1, 2);
   * 
   * // assertion will fail
   * assertThat(unsortedListAsObject).asList().isSorted();</code></pre>
   * 
   * @return a list assertion object
   */
  AbstractListAssert<?, List<? extends Object>, Object, ObjectAssert<Object>> asList();

  /**
   * Verifies that the actual value is an instance of {@link Long} and allow to chain Long specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object longAsObject = -1L;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(longAsObject).isNegative();
   * 
   * // assertion will pass
   * assertThat(longAsObject).asLong().isNegative();</code></pre>
   * 
   * @return a Long assertion object
   */
  AbstractLongAssert<?> asLong();

  /**
   * Verifies that the actual value is an instance of {@code long[]} and allow to chain {@code long[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> long one = 1;
   * long two = 2.0; 
   * Object longArrayAsObject = new long[] {one, two};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(longArrayAsObject).contains(one, two);
   * 
   * // assertion will pass
   * assertThat(longArrayAsObject).asLongArray().contains(one, two);</code></pre>
   * 
   * @return a long array assertion object
   */
  AbstractLongArrayAssert<?> asLongArray();

  /**
   * Verifies that the actual value is an instance of {@link Map} and allow to chain Map specific assertions after the call.
   * <p>
   * Note that the returned map assertions are performed with keys and values as Object.   
   * <p>
   * Example :
   * <pre><code class='java'> Object mapAsObject = new HashMap&lt;&gt;();
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(mapAsObject).isEmpty();
   * 
   * // assertion will pass
   * assertThat(mapAsObject).asMap().isEmpty();</code></pre>
   * 
   * @return a Map assertion object
   */
  MapAssert<Object, Object> asMap();

  /**
   * Verifies that the actual value is an Object array and allow to chain Object array specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object objectArray = new String[] {"a", "b", "c"};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(objectArray).contains("a", "b", "c");
   * 
   * // assertion will pass
   * assertThat(objectArray).asArrayOf(String.class).contains("a", "b", "c");</code></pre>
   * 
   * @param elementClass the type of 
   * 
   * @return a Object array assertion object
   */
  <T> AbstractObjectArrayAssert<?, T> asArrayOf(Class<T> elementClass);

  /**
   * Verifies that the actual value is an instance of {@link Path} and allow to chain Path specific assertions after the call.
   * <p>
   * Note that the returned path assertions are performed with keys and values as Object.   
   * <p>
   * Example :
   * <pre><code class='java'> Object pathAsObject = Files.write(Paths.get("xfile.txt"), "The Truth Is Out There".getBytes());
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(pathAsObject).hasContent("The Truth Is Out There");
   * 
   * // assertion will pass
   * assertThat(pathAsObject).asPath().hasContent("The Truth Is Out There");</code></pre>
   * 
   * @return a Path assertion object
   */
  AbstractPathAssert<?> asPath();

  /**
   * Verifies that the actual value is an instance of {@link Short} and allow to chain Short specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object shortAsObject = (short) -1;
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(shortAsObject).isNegative();
   * 
   * // assertion will pass
   * assertThat(shortAsObject).asShort().isNegative();</code></pre>
   * 
   * @return a Short assertion object
   */
  AbstractShortAssert<?> asShort();

  /**
   * Verifies that the actual value is an instance of {@code short[]} and allow to chain {@code short[]} specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> short one = 1;
   * short two = 2; 
   * Object shortArrayAsObject = new short[] {one, two};
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(shortArrayAsObject).contains(one, two);
   * 
   * // assertion will pass
   * assertThat(shortArrayAsObject).asShortArray().contains(one, two);</code></pre>
   * 
   * @return a Short array assertion object
   */
  AbstractShortArrayAssert<?> asShortArray();

  /**
   * Verifies that the actual value is an instance of {@link String} and allow to chain String specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object stringAsObject = "hello world";
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(stringAsObject).contains("hello");
   * 
   * // assertion will pass
   * assertThat(stringAsObject).asString().contains("hello");
   * 
   * // assertion will fail
   * assertThat(stringAsObject).asString().contains("holla");</code></pre>
   * 
   * @return a string assertion object
   */
  AbstractCharSequenceAssert<?, String> asString();

  /**
   * Verifies that the actual value is an instance of {@link Throwable} and allow to chain Throwable specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object throwableAsObject = new RuntimeException("Say no to assertEquals !");
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(throwableAsObject).hasMessage("Say no to assertEquals !");
   * 
   * // assertion will pass
   * assertThat(throwableAsObject).asThrowable().hasMessage("Say no to assertEquals !");</code></pre>
   * 
   * @return a Throwable assertion object
   */
  AbstractThrowableAssert<?, ? extends Throwable> asThrowable();

  /**
   * Verifies that the actual value is an instance of {@link Uri} and allow to chain Uri specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object uriAsObject = new URI("http://helloworld.org/pages");
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(uriAsObject).hasPath("/pages");
   * 
   * // assertion will pass
   * assertThat(uriAsObject).asUri().hasPath("/pages");</code></pre>
   * 
   * @return a Uri assertion object
   */
  AbstractUriAssert<?> asUri();

  /**
   * Verifies that the actual value is an instance of {@link Url} and allow to chain Url specific assertions after the call.
   * <p>
   * Example :
   * <pre><code class='java'> Object urlAsObject = new URL("http://helloworld.org/pages");
   *
   * // DOES NOT COMPILE: only Object assertions can be chained from a variable declared as an Object.
   * assertThat(urlAsObject).hasProtocol("http");
   * 
   * // assertion will pass
   * assertThat(urlAsObject).asUrl().hasProtocol("http");</code></pre>
   * 
   * @return a Url assertion object
   */
  AbstractUrlAssert<?> asUrl();
  
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
   * <pre>{@code "JDWP Command Reader"
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
   * 		at org.assertj.examples.ThreadDumpOnErrorExample.main(ThreadDumpOnErrorExample.java:28)}</pre>
   * 
   * @return this assertion object.
   */
  S withThreadDumpOnError();

  /**
   * Use the given {@link Representation} to describe/represent values in AssertJ error messages.
   * <p>
   * The usual to introduce a new {@link Representation} is to extend {@link StandardRepresentation} 
   * and override any existing {@code toStringOf} methods that don't suit you, as an example you could control 
   * {@link Date} format by overriding {@link StandardRepresentation#toStringOf(Date)}).
   * <p>
   * You can also control other types format by overriding {@link StandardRepresentation#toStringOf(Object)}) 
   * calling your formatting method first and then fallback to the default represention by calling {@code super.toStringOf(Object)}.   
   * <p>
   * Example :
   * <pre><code class='java'> private class Example {}
   *
   * private class CustomRepresentation extends StandardRepresentation {
   * 
   *   // override needed to hook specific formatting  
   *   {@literal @}Override
   *   public String toStringOf(Object o) {
   *     if (o instanceof Example) return "Example";
   *     // fallback to default formatting.  
   *     return super.toStringOf(o);
   *   }
   *   
   *   // change String representation  
   *   {@literal @}Override
   *   protected String toStringOf(String s) {
   *     return "$" + s + "$";
   *   }
   * }
   * 
   * // next assertion fails with error : "expected:&lt;[null]&gt; but was:&lt;[Example]&gt;"
   * Example example = new Example();
   * assertThat(example).withRepresentation(new CustomRepresentation())
   *                    .isNull(); // example is not null !
   * 
   * // next assertion fails ... 
   * assertThat("foo").withRepresentation(new CustomRepresentation())
   *                  .startsWith("bar");
   * // ... with error :
   * Expecting:
   *  &lt;$foo$&gt;
   * to start with:
   *  &lt;$bar$&gt;</code></pre>
   * 
   * @param representation Describe/represent values in AssertJ error messages.
   * @return
   */
  S withRepresentation(Representation representation);
}
