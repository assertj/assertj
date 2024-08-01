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
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;

/**
 * Base contract of all assertion objects: the minimum functionality that any assertion object should provide.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Nicolas François
 * @author Mikhail Mazursky
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating
 *          'self types' using Java Generics to simplify fluent API implementation</a>&quot; for more details.
 * @param <ACTUAL> the type of the "actual" value.
 */
public interface Assert<SELF extends Assert<SELF, ACTUAL>, ACTUAL> extends Descriptable<SELF>, ExtensionPoints<SELF, ACTUAL> {

  /**
   * Verifies that the actual value is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;abc&quot;);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isEqualTo(new HashMap&lt;String, Integer&gt;());
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isEqualTo(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).isEqualTo(1);</code></pre>
   *
   * @param expected the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is not equal to the given one.
   */
  SELF isEqualTo(Object expected);

  /**
   * Verifies that the actual value is not equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isNotEqualTo(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).isNotEqualTo(1);
   *
   * // assertions fail
   * assertThat(&quot;abc&quot;).isNotEqualTo(&quot;abc&quot;);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotEqualTo(new HashMap&lt;String, Integer&gt;());</code></pre>
   *
   * @param other the given value to compare the actual value to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is equal to the given one.
   */
  SELF isNotEqualTo(Object other);

  /**
   * Verifies that the actual value is {@code null}.
   * <p>
   * Example:
   * <pre><code class='java'> String value = null;
   * // assertion succeeds
   * assertThat(value).isNull();
   *
   * // assertions fail
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
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isNotNull();
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotNull();
   *
   * // assertions fails
   * String value = null;
   * assertThat(value).isNotNull();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual value is {@code null}.
   */
  SELF isNotNull();

  /**
   * Verifies that the actual value is the same as the given one, i.e., using == comparison.
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
  SELF isSameAs(Object expected);

  /**
   * Verifies that the actual value is not the same as the given one, i.e., using == comparison.
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
  SELF isNotSameAs(Object other);

  /**
   * Verifies that the actual value is present in the given array of values.
   * <p>
   * This assertion always fails if the given array of values is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = new Ring[] { vilya, nenya, narya };
   *
   * // assertion succeeds
   * assertThat(nenya).isIn(elvesRings);
   *
   * // assertions fail
   * assertThat(oneRing).isIn(elvesRings);
   * assertThat(oneRing).isIn(new Ring[0]);</code></pre>
   *
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the actual value is not present in the given array.
   */
  SELF isIn(Object... values);

  /**
   * Verifies that the actual value is not present in the given array of values.
   * <p>
   * This assertion always succeeds if the given array of values is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Ring[] elvesRings = new Ring[] { vilya, nenya, narya };
   *
   * // assertions succeed
   * assertThat(oneRing).isNotIn(elvesRings);
   * assertThat(oneRing).isNotIn(new Ring[0]);
   *
   * // assertions fails:
   * assertThat(nenya).isNotIn(elvesRings);</code></pre>
   *
   * @param values the given array to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given array is {@code null}.
   * @throws AssertionError if the actual value is present in the given array.
   */
  SELF isNotIn(Object... values);

  /**
   * Verifies that the actual value is present in the given iterable.
   * <p>
   * This assertion always fails if the given iterable is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = list(vilya, nenya, narya);
   *
   * // assertion succeeds
   * assertThat(nenya).isIn(elvesRings);
   *
   * // assertions fail:
   * assertThat(oneRing).isIn(elvesRings);
   * assertThat(oneRing).isIn(emptyList());</code></pre>
   *
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given collection is {@code null}.
   * @throws AssertionError if the actual value is not present in the given iterable.
   */
  SELF isIn(Iterable<?> values);

  /**
   * Verifies that the actual value is not present in the given iterable.
   * <p>
   * This assertion always succeeds if the given iterable is empty.
   * <p>
   * Example:
   * <pre><code class='java'> Iterable&lt;Ring&gt; elvesRings = list(vilya, nenya, narya);
   *
   * // assertions succeed:
   * assertThat(oneRing).isNotIn(elvesRings);
   * assertThat(oneRing).isNotIn(emptyList());
   *
   * // assertions fails:
   * assertThat(nenya).isNotIn(elvesRings);</code></pre>
   *
   * @param values the given iterable to search the actual value in.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given iterable is {@code null}.
   * @throws AssertionError if the actual value is present in the given iterable.
   */
  SELF isNotIn(Iterable<?> values);

  /**
   * Use the given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * The custom comparator is bound to assertion instance, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples :
   * <pre><code class='java'> // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt;
   * assertThat(frodo).usingComparator(raceComparator).isEqualTo(sam);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertion checks.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  SELF usingComparator(Comparator<? super ACTUAL> customComparator);

  /**
   * Use the given custom comparator instead of relying on actual type A equals method for incoming assertion checks.
   * <p>
   * The custom comparator is bound to assertion instance, meaning that if a new assertion instance is created, the default
   * comparison strategy will be used.
   * <p>
   * Examples :
   * <pre><code class='java'> // frodo and sam are instances of Character with Hobbit race (obviously :).
   * // raceComparator implements Comparator&lt;Character&gt;
   * assertThat(frodo).usingComparator(raceComparator, "Hobbit Race Comparator").isEqualTo(sam);</code></pre>
   *
   * @param customComparator the comparator to use for the incoming assertion checks.
   * @param customComparatorDescription comparator description to be used in assertion error messages
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given comparator is {@code null}.
   */
  SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription);

  /**
   * Revert to standard comparison for the incoming assertion checks.
   * <p>
   * This method should be used to disable a custom comparison strategy set by calling {@link #usingComparator(Comparator) usingComparator}.
   *
   * @return {@code this} assertion object.
   */
  SELF usingDefaultComparator();

  /**
   * Uses an {@link InstanceOfAssertFactory} to verify that the actual value is an instance of a given type and to produce
   * a new {@link Assert} narrowed to that type.
   * <p>
   * {@link InstanceOfAssertFactories} provides static factories for all the types supported by {@code Assertions#assertThat}.
   * <p>
   * Additional factories can be created with custom {@code InstanceOfAssertFactory} instances.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeeds
   * Object string = &quot;abc&quot;;
   * assertThat(string).asInstanceOf(InstanceOfAssertFactories.STRING).startsWith(&quot;ab&quot;);
   *
   * Object integer = 1;
   * assertThat(integer).asInstanceOf(InstanceOfAssertFactories.INTEGER).isNotZero();
   *
   * // assertions fails
   * assertThat(&quot;abc&quot;).asInstanceOf(InstanceOfAssertFactories.INTEGER);</code></pre>
   *
   * @param <ASSERT>                the type of the resulting {@code Assert}.
   * @param instanceOfAssertFactory the factory which verifies the type and creates the new {@code Assert}.
   * @throws NullPointerException if the given factory is {@code null}.
   * @return the narrowed {@code Assert} instance.
   *
   * @see InstanceOfAssertFactory
   * @see InstanceOfAssertFactories
   *
   * @since 3.13.0
   */
  <ASSERT extends AbstractAssert<?, ?>> ASSERT asInstanceOf(InstanceOfAssertFactory<?, ASSERT> instanceOfAssertFactory);

  /**
   * Verifies that the actual value is an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isInstanceOf(String.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOf(HashMap.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOf(Map.class);
   *
   * // assertions fail
   * assertThat(1).isInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOf(LinkedList.class);</code></pre>
   *
   * @param type the type to check the actual value against.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of the given type.
   */
  SELF isInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is an instance of the given type satisfying the given requirements expressed as a {@link Consumer}.
   * <p>
   * This is useful to perform a group of assertions on a single object after checking its runtime type.
   * <p>
   * Example:
   * <pre><code class='java'> // second constructor parameter is the light saber color
   * Object yoda = new Jedi("Yoda", "Green");
   * Object luke = new Jedi("Luke Skywalker", "Green");
   *
   * Consumer&lt;Jedi&gt; jediRequirements = jedi -&gt; {
   *   assertThat(jedi.getLightSaberColor()).isEqualTo("Green");
   *   assertThat(jedi.getName()).doesNotContain("Dark");
   * };
   *
   * // assertions succeed:
   * assertThat(yoda).isInstanceOfSatisfying(Jedi.class, jediRequirements);
   * assertThat(luke).isInstanceOfSatisfying(Jedi.class, jediRequirements);
   *
   * // assertions fail:
   * Jedi vader = new Jedi("Vader", "Red");
   * assertThat(vader).isInstanceOfSatisfying(Jedi.class, jediRequirements);
   * // not a Jedi !
   * assertThat("foo").isInstanceOfSatisfying(Jedi.class, jediRequirements);</code></pre>
   *
   * @param <T> the generic type to check the actual value against.
   * @param type the type to check the actual value against.
   * @param requirements the requirements expressed as a {@link Consumer}.
   * @return this assertion object.
   * @throws NullPointerException if the given type is {@code null}.
   * @throws NullPointerException if the given Consumer is {@code null}.
   * @throws AssertionError if the actual value is {@code null}.
   * @throws AssertionError if the actual value is not an instance of the given type.
   */
  <T> SELF isInstanceOfSatisfying(Class<T> type, Consumer<T> requirements);

  /**
   * Verifies that the actual value is an instance of any of the given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isInstanceOfAny(String.class, Integer.class);
   * assertThat(new ArrayList&lt;String&gt;()).isInstanceOfAny(LinkedList.class, ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isInstanceOfAny(TreeMap.class, Map.class);
   *
   * // assertions fail
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
  SELF isInstanceOfAny(Class<?>... types);

  /**
   * Verifies that the actual value is not an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).isNotInstanceOf(Double.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOf(LinkedList.class);
   *
   * // assertions fail
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
  SELF isNotInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is not an instance of any of the given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).isNotInstanceOfAny(Double.class, Float.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotInstanceOfAny(LinkedList.class, Vector.class);
   *
   * // assertions fail
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
  SELF isNotInstanceOfAny(Class<?>... types);

  /**
   * Verifies that the actual value has the same class as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).hasSameClassAs(2);
   * assertThat(&quot;abc&quot;).hasSameClassAs(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).hasSameClassAs(new ArrayList&lt;Integer&gt;());
   *
   * // assertions fail
   * assertThat(1).hasSameClassAs(&quot;abc&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).hasSameClassAs(new LinkedList&lt;String&gt;());</code></pre>
   *
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} has not the same type as the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  SELF hasSameClassAs(Object other);

  /**
   * Verifies that actual {@code actual.toString()} is equal to the given {@code String}.
   * <p>
   * Example :
   * <pre><code class='java'> CartoonCharacter homer = new CartoonCharacter("Homer");
   *
   * // Instead of writing ...
   * assertThat(homer.toString()).isEqualTo("Homer");
   * // ... you can simply write:
   * assertThat(homer).hasToString("Homer");</code></pre>
   *
   * @param expectedToString the expected String description of actual.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is not equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   */
  SELF hasToString(String expectedToString);

  /**
   * Verifies that actual {@code actual.toString()} is equal to the given {@code String} when
   * {@link String#format formatted} with the given arguments.
   * <p>
   * Example:
   * <pre><code class='java'> Foo foo = new Foo();
   * FooWrapper wrapper = new FooWrapper(foo);
   *
   * assertThat(wrapper).hasToString("FooWrapper[foo=%s]", foo); </code></pre>
   *
   * @param expectedStringTemplate the format string to use.
   * @param args the arguments to interpolate into the format string.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is not equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   * @since 3.25.0
   */
  SELF hasToString(String expectedStringTemplate, Object... args);

  /**
   * Verifies that actual {@code actual.toString()} is not equal to the given {@code String}.
   * <p>
   * Example :
   * <pre><code class='java'> CartoonCharacter homer = new CartoonCharacter("Homer");
   *
   * // Instead of writing ...
   * assertThat(homer.toString()).isNotEqualTo("Marge");
   * // ... you can simply write:
   * assertThat(homer).doesNotHaveToString("Marge");</code></pre>
   *
   * @param otherToString the String to check against.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   */
  SELF doesNotHaveToString(String otherToString);

  /**
   * Verifies that actual {@code actual.toString()} is not equal to the given {@code String}.
   * <p>
   * Example:
   * <pre><code class='java'> Foo foo = new Foo();
   * Bar bar = new Bar();
   * FooBarWrapper wrapper = new FooBarWrapper(bar);
   *
   * assertThat(wrapper).doesNotHaveToString("FooBarWrapper[%s]", foo);</code></pre>
   *
   * @param expectedStringTemplate the format string to use.
   * @param args the arguments to interpolate into the format string.
   * @return this assertion object.
   * @throws AssertionError if {@code actual.toString()} result is equal to the given {@code String}.
   * @throws AssertionError if actual is {@code null}.
   * @since 3.25.0
   */
  SELF doesNotHaveToString(String expectedStringTemplate, Object... args);

  /**
   * Verifies that the actual value does not have the same class as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).doesNotHaveSameClassAs(&quot;abc&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).doesNotHaveSameClassAs(new LinkedList&lt;String&gt;());
   *
   * // assertions fail
   * assertThat(1).doesNotHaveSameClassAs(2);
   * assertThat(&quot;abc&quot;).doesNotHaveSameClassAs(&quot;123&quot;);
   * assertThat(new ArrayList&lt;String&gt;()).doesNotHaveSameClassAs(new ArrayList&lt;Integer&gt;());</code></pre>
   *
   * @param other the object to check type against.
   * @return this assertion object.
   * @throws AssertionError if {@code actual} has the same type as the given object.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given object is null.
   */
  SELF doesNotHaveSameClassAs(Object other);

  /**
   * Verifies that the actual value is <b>exactly</b> an instance of the given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(&quot;abc&quot;).isExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isExactlyInstanceOf(ArrayList.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isExactlyInstanceOf(HashMap.class);
   *
   * // assertions fail
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
  SELF isExactlyInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value is not <b>exactly</b> an instance of given type.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(1).isNotExactlyInstanceOf(String.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotExactlyInstanceOf(List.class);
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotExactlyInstanceOf(Map.class);
   *
   * // assertions fail
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
  SELF isNotExactlyInstanceOf(Class<?> type);

  /**
   * Verifies that the actual value type is in given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(new HashMap&lt;String, Integer&gt;()).isOfAnyClassIn(HashMap.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isOfAnyClassIn(ArrayList.class, LinkedList.class);
   *
   * // assertions fail
   * assertThat(new HashMap&lt;String, Integer&gt;()).isOfAnyClassIn(TreeMap.class, Map.class);
   * assertThat(new ArrayList&lt;String&gt;()).isOfAnyClassIn(LinkedList.class, List.class);</code></pre>
   *
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is not in given type.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  SELF isOfAnyClassIn(Class<?>... types);

  /**
   * Verifies that the actual value type is not in given types.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotOfAnyClassIn(Map.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotOfAnyClassIn(LinkedList.class, List.class);
   *
   * // assertions fail
   * assertThat(new HashMap&lt;String, Integer&gt;()).isNotOfAnyClassIn(HashMap.class, TreeMap.class);
   * assertThat(new ArrayList&lt;String&gt;()).isNotOfAnyClassIn(ArrayList.class, LinkedList.class);</code></pre>
   *
   * @param types the types to check the actual value against.
   * @return this assertion object.
   * @throws AssertionError if the actual value type is in given types.
   * @throws NullPointerException if the actual value is null.
   * @throws NullPointerException if the given types is null.
   */
  SELF isNotOfAnyClassIn(Class<?>... types);

  /**
   * Verifies that the actual value is an instance of List,
   * and returns a list assertion, to allow chaining of list-specific
   * assertions from this call.
   * <p>
   * Example :
   * <pre><code class='java'> Object sortedListAsObject = Arrays.asList(1, 2, 3);
   *
   * // assertion succeeds
   * assertThat(sortedListAsObject).asList().isSorted();
   *
   * Object unsortedListAsObject = Arrays.asList(3, 1, 2);
   *
   * // assertions fails
   * assertThat(unsortedListAsObject).asList().isSorted();</code></pre>
   *
   * @return a list assertion object
   * @deprecated use {@link #asInstanceOf(InstanceOfAssertFactory) asInstanceOf(InstanceOfAssertFactories.LIST)} instead
   */
  @Deprecated
  AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> asList();

  /**
   * Returns a String assertion for the <code>toString()</code> of the actual
   * value, to allow chaining of String-specific assertions from this call.
   * <p>
   * Example :
   * <pre><code class='java'> Object stringAsObject = "hello world";
   *
   * // assertion succeeds
   * assertThat(stringAsObject).asString().contains("hello");
   *
   * // assertions fails
   * assertThat(stringAsObject).asString().contains("holla");</code></pre>
   *
   * @return a string assertion object
   */
  AbstractCharSequenceAssert<?, String> asString();

  /**
   * Throws <code>{@link UnsupportedOperationException}</code> if called. It is easy to accidentally call
   * <code>equals(Object)</code> instead of <code>{@link #isEqualTo(Object)}</code>.
   *
   * @throws UnsupportedOperationException if this method is called.
   * @deprecated
   */
  @Override
  @Deprecated
  boolean equals(Object obj);

  /**
   * In case of an assertion error, a thread dump will be printed to {@link System#err}.
   * <p>
   * Example :
   * <pre><code class='java'> assertThat("Messi").withThreadDumpOnError().isEqualTo("Ronaldo");</code></pre>
   *
   * will print a thread dump, something similar to this:
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
  SELF withThreadDumpOnError();

  /**
   * Use the given {@link Representation} to describe/represent values in AssertJ error messages.
   * <p>
   * The usual way to introduce a new {@link Representation} is to extend {@link StandardRepresentation}
   * and override any existing {@code toStringOf} methods that don't suit you. For example you can control
   * {@link Date} formatting by overriding {@link StandardRepresentation#toStringOf(Date)}).
   * <p>
   * You can also control other types format by overriding {@link StandardRepresentation#toStringOf(Object)})
   * calling your formatting method first and then fall back to the default representation by calling {@code super.toStringOf(Object)}.
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
   *     // fall back to default formatting
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
   * @return this assertion object.
   */
  SELF withRepresentation(Representation representation);

  /**
   * Verifies that the actual object has the same hashCode as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(42L).hasSameHashCodeAs(42L);
   * assertThat(&quot;The Force&quot;).hasSameHashCodeAs(&quot;The Force&quot;);
   * assertThat(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;)).hasSameHashCodeAs(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;));
   *
   * // assertions fail
   * assertThat(42L).hasSameHashCodeAs(2501L);
   * assertThat(null).hasSameHashCodeAs(&quot;The Force&quot;);
   * assertThat(&quot;The Force&quot;).hasSameHashCodeAs(&quot;Awakens&quot;);</code></pre>
   *
   * @param other the object to check hashCode against.
   *
   * @return this assertion object.
   *
   * @throws AssertionError if the actual object is null.
   * @throws NullPointerException if the other object is null.
   * @throws AssertionError if the actual object has not the same hashCode as the given object.
   *
   * @since 2.9.0
   */
  SELF hasSameHashCodeAs(Object other);

  /**
   * Verifies that the actual object does not have the same hashCode as the given object.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeed
   * assertThat(42L).doesNotHaveSameHashCodeAs(2501L);
   * assertThat(&quot;The Force&quot;).doesNotHaveSameHashCodeAs(&quot;Awakens&quot;);
   *
   * // assertions fail
   * assertThat(42L).doesNotHaveSameHashCodeAs(42L);
   * assertThat(&quot;The Force&quot;).doesNotHaveSameHashCodeAs(&quot;The Force&quot;);
   * assertThat(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;)).doesNotHaveSameHashCodeAs(new Jedi(&quot;Yoda&quot;, &quot;Blue&quot;)); </code></pre>
   *
   * @param other the object to check hashCode against.
   *
   * @return this assertion object.
   *
   * @throws AssertionError if the actual object is null.
   * @throws NullPointerException if the other object is null.
   * @throws AssertionError if the actual object has the same hashCode as the given object.
   *
   * @since 3.19.0
   */
  SELF doesNotHaveSameHashCodeAs(Object other);
}
