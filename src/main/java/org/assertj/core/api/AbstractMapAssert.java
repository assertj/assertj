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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.IterableUtil.toCollection;
import static org.assertj.core.util.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.description.Description;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.Maps;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Map}s.
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @param <K> the type of keys in the map.
 * @param <V> the type of values in the map.
 *
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author dorzey
 * @author Filip Hrisafov
 */
public abstract class AbstractMapAssert<SELF extends AbstractMapAssert<SELF, ACTUAL, K, V>, ACTUAL extends Map<K, V>, K, V>
    extends AbstractObjectAssert<SELF, ACTUAL> implements EnumerableAssert<SELF, Map.Entry<? extends K, ? extends V>> {

  @VisibleForTesting
  Maps maps = Maps.instance();

  public AbstractMapAssert(ACTUAL actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /**
   * Verifies that all the actual map entries satisfy the given {@code entryRequirements} .
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;TolkienCharacter, Ring&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(galadriel, nenya);
   * elvesRingBearers.put(gandalf, narya);
   * elvesRingBearers.put(elrond, vilya);
   *
   * // this assertion succeeds:
   * assertThat(elvesRingBearers).allSatisfy((character, ring) -&gt; {
   *   assertThat(character.getRace()).isIn(ELF, MAIA);
   *   assertThat(ring).isIn(nenya, narya, vilya);
   * });
   *
   * // this assertion fails as Gandalf is a maia and not an elf:
   * assertThat(elvesRingBearers).allSatisfy((character, ring) -&gt; {
   *   assertThat(character.getRace()).isEqualTo(ELF);
   *   assertThat(ring).isIn(nenya, narya, vilya);
   * });</code></pre>
   * <p>
   * If the actual map is empty, this assertion succeeds as there is nothing to check.
   *
   * @param entryRequirements the given requirements that each entry must satisfy.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given entryRequirements {@link BiConsumer} is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if one or more entries don't satisfy the given requirements.
   * @since 3.9.0
   */
  public SELF allSatisfy(BiConsumer<? super K, ? super V> entryRequirements) {
    maps.assertAllSatisfy(info, actual, entryRequirements);
    return myself;
  }

  /**
   * Verifies that at least one map entry satisfies the given {@code entryRequirements} .
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;TolkienCharacter, Ring&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(galadriel, nenya);
   * elvesRingBearers.put(gandalf, narya);
   * elvesRingBearers.put(elrond, vilya);
   *
   * // this assertion succeeds as gandalf is a maia wearing narya:
   * assertThat(elvesRingBearers).anySatisfy((character, ring) -&gt; {
   *   assertThat(character.getRace()).isEqualTo(MAIA);
   *   assertThat(ring).isEqualTo(narya);
   * });
   *
   * // this assertion fails, gandalf is a maia but he does not wear the One Ring:
   * assertThat(elvesRingBearers).anySatisfy((character, ring) -&gt; {
   *   assertThat(character.getRace()).isIn(MAIA, HOBBIT);
   *   assertThat(ring).isEqualTo(oneRing);
   * });</code></pre>
   *
   * @param entryRequirements the given requirements that at least one entry must satisfy.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given entryRequirements {@link BiConsumer} is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if no entries satisfy the given requirements.
   * @since 3.12.0
   */
  public SELF anySatisfy(BiConsumer<? super K, ? super V> entryRequirements) {
    maps.assertAnySatisfy(info, actual, entryRequirements);
    return myself;
  }

  /**
   * Verifies that no map entry satisfies the given {@code entryRequirements} .
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;TolkienCharacter, Ring&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(galadriel, nenya);
   * elvesRingBearers.put(gandalf, narya);
   * elvesRingBearers.put(elrond, vilya);
   *
   * // this assertion succeeds:
   * assertThat(elvesRingBearers).noneSatisfy((character, ring) -&gt; {
   *   assertThat(character.getRace()).isIn(HOBBIT, DWARF);M
   *   assertThat(ring).isIn(nenya, narya, vilya);
   * });
   *
   * // this assertion fails as Gandalf is a maia.
   * assertThat(elvesRingBearers).noneSatisfy((character, ring) -&gt; {
   *   assertThat(character.getRace()).isEqualTo(MAIA);
   *   assertThat(ring).isIn(nenya, narya, vilya);
   * });</code></pre>
   * <p>
   * If the actual map is empty, this assertion succeeds as there is nothing to check.
   *
   * @param entryRequirements the given requirements that each entry must not satisfy.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given entryRequirements {@link BiConsumer} is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if one or more entries satisfies the given requirements.
   * @since 3.12.0
   */
  public SELF noneSatisfy(BiConsumer<? super K, ? super V> entryRequirements) {
    maps.assertNoneSatisfy(info, actual, entryRequirements);
    return myself;
  }

  /**
   * Verifies that the {@link Map} is {@code null} or empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions will pass
   * Map&lt;Integer, String&gt; map = null;
   * assertThat(map).isNullOrEmpty();
   * assertThat(new HashMap()).isNullOrEmpty();
   *
   * // assertion will fail
   * Map&lt;String, String&gt; keyToValue = new HashMap();
   * keyToValue.put(&quot;key&quot;, &quot;value&quot;);
   * assertThat(keyToValue).isNullOrEmpty()</code></pre>
   *
   * @throws AssertionError if the {@link Map} is not {@code null} or not empty.
   */
  @Override
  public void isNullOrEmpty() {
    maps.assertNullOrEmpty(info, actual);
  }

  /**
   * Verifies that the {@link Map} is empty.
   * <p>
   * Example:
   * <pre><code class='java'> // assertion will pass
   * assertThat(new HashMap()).isEmpty();
   *
   * // assertion will fail
   * Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   * assertThat(map).isEmpty();</code></pre>
   *
   * @throws AssertionError if the {@link Map} of values is not empty.
   */
  @Override
  public void isEmpty() {
    maps.assertEmpty(info, actual);
  }

  /**
   * Verifies that the {@link Map} is not empty.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   *
   * // assertion will pass
   * assertThat(map).isNotEmpty();
   *
   * // assertion will fail
   * assertThat(new HashMap()).isNotEmpty();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the {@link Map} is empty.
   */
  @Override
  public SELF isNotEmpty() {
    maps.assertNotEmpty(info, actual);
    return myself;
  }

  /**
   * Verifies that the number of values in the {@link Map} is equal to the given one.
   * <p>
   * Example:
   * <pre><code class='java'>
   * Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   *
   * // assertion will pass
   * assertThat(map).hasSize(1);
   *
   * // assertions will fail
   * assertThat(map).hasSize(0);
   * assertThat(map).hasSize(2);</code></pre>
   *
   * @param expected the expected number of values in the {@link Map}.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the {@link Map} is not equal to the given one.
   */
  @Override
  public SELF hasSize(int expected) {
    maps.assertHasSize(info, actual, expected);
    return myself;
  }

  /**
   * Verifies that the number of values in the {@link Map} is greater than the boundary.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   * map.put(&quot;key2&quot;, &quot;value2&quot;);
   *
   * // assertion will pass
   * assertThat(map).hasSizeGreaterThan(1);
   *
   * // assertions will fail
   * assertThat(map).hasSizeGreaterThan(3);</code></pre>
   *
   * @param boundary the given value to compare the size of {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the {@link Map} is not greater than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThan(int boundary) {
    maps.assertHasSizeGreaterThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the {@link Map} is greater than or equal to the boundary.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   * map.put(&quot;key2&quot;, &quot;value2&quot;);
   *
   * // assertions will pass
   * assertThat(map).hasSizeGreaterThanOrEqualTo(1)
   *                .hasSizeGreaterThanOrEqualTo(2);
   *
   * // assertions will fail
   * assertThat(map).hasSizeGreaterThanOrEqualTo(3);
   * assertThat(map).hasSizeGreaterThanOrEqualTo(5);</code></pre>
   *
   * @param boundary the given value to compare the size of {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the {@link Map} is not greater than or equal to the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeGreaterThanOrEqualTo(int boundary) {
    maps.assertHasSizeGreaterThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the {@link Map} is less than the boundary.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   * map.put(&quot;key2&quot;, &quot;value2&quot;);
   *
   * // assertion will pass
   * assertThat(map).hasSizeLessThan(3);
   *
   * // assertions will fail
   * assertThat(map).hasSizeLessThan(1);
   * assertThat(map).hasSizeLessThan(2);</code></pre>
   *
   * @param boundary the given value to compare the size of {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the {@link Map} is not less than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThan(int boundary) {
    maps.assertHasSizeLessThan(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the {@link Map} is less than or equal to the boundary.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   * map.put(&quot;key2&quot;, &quot;value2&quot;);
   *
   * // assertions will pass
   * assertThat(map).hasSizeLessThanOrEqualTo(2)
   *                .hasSizeLessThanOrEqualTo(3);
   *
   * // assertions will fail
   * assertThat(map).hasSizeLessThanOrEqualTo(0);
   * assertThat(map).hasSizeLessThanOrEqualTo(1);</code></pre>
   *
   * @param boundary the given value to compare the size of {@code actual} to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the {@link Map} is not less than the boundary.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeLessThanOrEqualTo(int boundary) {
    maps.assertHasSizeLessThanOrEqualTo(info, actual, boundary);
    return myself;
  }

  /**
   * Verifies that the number of values in the {@link Map} is between the given boundaries (inclusive).
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, String&gt; map = new HashMap();
   * map.put(&quot;key&quot;, &quot;value&quot;);
   * map.put(&quot;key2&quot;, &quot;value2&quot;);
   *
   * // assertions will pass
   * assertThat(map).hasSizeBetween(1, 3)
   *                .hasSizeBetween(2, 2);
   *
   * // assertions will fail
   * assertThat(map).hasSizeBetween(3, 4);</code></pre>
   *
   * @param lowerBoundary the lower boundary compared to which actual size should be greater than or equal to.
   * @param higherBoundary the higher boundary compared to which actual size should be less than or equal to.
   * @return {@code this} assertion object.
   * @throws AssertionError if the number of values of the {@link Map} is not between the boundaries.
   * @since 3.12.0
   */
  @Override
  public SELF hasSizeBetween(int lowerBoundary, int higherBoundary) {
    maps.assertHasSizeBetween(info, actual, lowerBoundary, higherBoundary);
    return myself;
  }

  /**
   * Verifies that the actual map has the same size as the given array.
   * <p>
   * Parameter is declared as Object to accept both Object[] and primitive arrays (e.g. int[]).
   * <p>
   * Example:
   * <pre><code class='java'> int[] oneTwoThree = {1, 2, 3};
   *
   * Map&lt;Ring, TolkienCharacter&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(nenya, galadriel);
   * elvesRingBearers.put(narya, gandalf);
   * elvesRingBearers.put(vilya, elrond);
   *
   * // assertion will pass
   * assertThat(elvesRingBearers).hasSameSizeAs(oneTwoThree);
   *
   * // assertions will fail
   * assertThat(elvesRingBearers).hasSameSizeAs(new int[] {1});
   * assertThat(keyToValue).hasSameSizeAs(new char[] {'a', 'b', 'c', 'd'});</code></pre>
   *
   * @param other the array to compare size with actual group.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual group is {@code null}.
   * @throws AssertionError if the array parameter is {@code null} or is not a true array.
   * @throws AssertionError if actual group and given array don't have the same size.
   */
  @Override
  public SELF hasSameSizeAs(Object other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual map has the same size as the given {@link Iterable}.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(nenya, galadriel);
   * elvesRingBearers.put(narya, gandalf);
   * elvesRingBearers.put(vilya, elrond);
   *
   * // assertion will pass
   * assertThat(elvesRingBearers).hasSameSizeAs(Array.asList(vilya, nenya, narya));
   *
   * // assertions will fail
   * assertThat(elvesRingBearers).hasSameSizeAs(Array.asList(1));
   * assertThat(keyToValue).hasSameSizeAs(Array.asList('a', 'b', 'c', 'd'));</code></pre>
   *
   * @param other the {@code Iterable} to compare size with actual group.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the other {@code Iterable} is {@code null}.
   * @throws AssertionError if the actual map and the given {@code Iterable} don't have the same size
   */
  @Override
  public SELF hasSameSizeAs(Iterable<?> other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual map has the same size as the given {@link Map}.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).hasSameSizeAs(mapOf(entry(oneRing, frodo),
   *                                             entry(narya, gandalf),
   *                                             entry(nenya, galadriel),
   *                                             entry(vilya, elrond)));
   *
   * // assertions will fail
   * assertThat(elvesRingBearers).hasSameSizeAs(new HashMap());
   * Map&lt;String, String&gt; keyToValue = new HashMap();
   * keyToValue.put(&quot;key&quot;, &quot;value&quot;);
   * assertThat(keyToValue).hasSameSizeAs(keyToValue);</code></pre>
   *
   * @param other the {@code Map} to compare size with actual map
   * @return {@code this} assertion object
   * @throws NullPointerException if the other {@code Map} is {@code null}
   * @throws AssertionError if the actual map is {@code null}
   * @throws AssertionError if the actual map and the given {@code Map} don't have the same size
   */
  public SELF hasSameSizeAs(Map<?, ?> other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /**
   * Verifies that the actual map contains the given entries, in any order.
   * <p>
   * This assertion succeeds if both actual map and given entries are empty.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertions will pass
   * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));
   * assertThat(emptyMap).contains();
   *
   * // assertions will fail
   * assertThat(ringBearers).contains(entry(oneRing, sauron));
   * assertThat(ringBearers).contains(entry(oneRing, sauron), entry(oneRing, aragorn));
   * assertThat(ringBearers).contains(entry(narya, gandalf), entry(oneRing, sauron));</code></pre>
   *
   * @param entries the given entries.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries.
   */
  public SELF contains(@SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
    maps.assertContains(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map contains at least one of the given entries.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertions will pass
   * assertThat(ringBearers).containsAnyOf(entry(oneRing, frodo), entry(oneRing, sauron));
   * assertThat(emptyMap).containsAnyOf();
   *
   * // assertion will fail
   * assertThat(ringBearers).containsAnyOf(entry(oneRing, gandalf), entry(oneRing, aragorn));</code></pre>
   *
   * @param entries the given entries.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain any of the given entries.
   * @since 3.6.0
   */
  public SELF containsAnyOf(@SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
    maps.assertContainsAnyOf(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map contains all entries of the given map, in any order.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * Map&lt;Ring, TolkienCharacter&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(nenya, galadriel);
   * elvesRingBearers.put(narya, gandalf);
   * elvesRingBearers.put(vilya, elrond);
   *
   * // assertion will succeed
   * assertThat(ringBearers).containsAllEntriesOf(elvesRingBearers);
   *
   * // assertion will fail
   * assertThat(elvesRingBearers).containsAllEntriesOf(ringBearers);</code></pre>
   *
   * @param other the map with the given entries.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws NullPointerException if any of the entries in the given map is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries.
   */
  public SELF containsAllEntriesOf(Map<? extends K, ? extends V> other) {
    Map.Entry<? extends K, ? extends V>[] entries = other.entrySet().toArray(new Map.Entry[0]);
    maps.assertContains(info, actual, entries);
    return myself;
  }

  /**
   * Same as {@link #containsExactly(Map.Entry[])} but handles the conversion of {@link Map#entrySet()} to array.
   * <p>
   * Verifies that the actual map contains only the entries of the given map and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with maps that have a consistent iteration order (i.e. don't use it with
   * {@link java.util.HashMap}, prefer {@link #containsOnly(java.util.Map.Entry...)} in that case).
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = newLinkedHashMap(entry(oneRing, frodo),
   *                                                            entry(nenya, galadriel),
   *                                                            entry(narya, gandalf));
   *
   * // assertion will pass
   * assertThat(ringBearers).containsExactlyEntriesOf(newLinkedHashMap(entry(oneRing, frodo),
   *                                                                   entry(nenya, galadriel),
   *                                                                   entry(narya, gandalf)));
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(ringBearers).containsExactlyEntriesOf(newLinkedHashMap(entry(nenya, galadriel),
   *                                                                   entry(narya, gandalf),
   *                                                                   entry(oneRing, frodo)));
   * // assertion will fail as actual and expected have different sizes
   * assertThat(ringBearers).containsExactlyEntriesOf(newLinkedHashMap(entry(oneRing, frodo),
   *                                                                   entry(nenya, galadriel),
   *                                                                   entry(narya, gandalf),
   *                                                                   entry(narya, gandalf)));</code></pre>
   *
   * @param map the given {@link Map} with the expected entries to be found in actual.
   * @return {@code this} assertions object
   * @throws NullPointerException if the given map is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws IllegalArgumentException if the given map is empty.
   * @throws AssertionError if the actual map does not contain the entries of the given map with same order, i.e
   *           the actual map contains some or none of the entries of the given map, or the actual map contains more
   *           entries than the entries of the given map or entries are the same but the order is not.
   * @since 3.12.0
   */
  public SELF containsExactlyEntriesOf(Map<? extends K, ? extends V> map) {
    Map.Entry<? extends K, ? extends V>[] entries = map.entrySet().toArray(new Map.Entry[0]);
    return containsExactly(entries);
  }

  /**
   * Verifies that the actual map contains the given entry.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertions will pass
   * assertThat(ringBearers).containsEntry(oneRing, frodo).containsEntry(nenya, galadriel);
   *
   * // assertion will fail
   * assertThat(ringBearers).containsEntry(oneRing, sauron);</code></pre>
   *
   * @param key the given key to check.
   * @param value the given value to check.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries.
   */
  public SELF containsEntry(K key, V value) {
    maps.assertContains(info, actual, array(entry(key, value)));
    return myself;
  }

  /**
   * Verifies that the actual map contains a value for the given {@code key} that satisfies the given {@code valueCondition}.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * Condition&lt;TolkienCharacter&gt; elfBearer = new Condition&lt;&gt;(&quot;an elf bearer&quot;) {
   *   public boolean matches(TolkienCharacter character) {
   *     return character.getRace() == ELF;
   *   }
   * };
   *
   * // this assertion will pass
   * assertThat(ringBearers).hasEntrySatisfying(nenya, elfBearer);
   *
   * // this assertion will fail
   * assertThat(ringBearers).hasEntrySatisfying(oneRing, elfBearer);</code></pre>
   *
   * @param key he given key to check.
   * @param valueCondition the given condition for check value.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given {@code key}.
   * @throws AssertionError if the actual map contains the given key, but value not match the given {@code valueCondition}.
   * @since 2.6.0 / 3.6.0
   */
  public SELF hasEntrySatisfying(K key, Condition<? super V> valueCondition) {
    maps.assertHasEntrySatisfying(info, actual, key, valueCondition);
    return myself;
  }

  /**
   * Verifies that the actual map contains the value for given {@code key} that satisfy given {@code valueRequirements}.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // this assertion will pass
   * assertThat(ringBearers).hasEntrySatisfying(nenya, character -&gt; {
   *     assertThat(character.getName()).contains("driel");
   *     assertThat(character.getRace()).isEqualTo(ELF);
   * });
   *
   * // this assertion will fail
   * assertThat(ringBearers).hasEntrySatisfying(oneRing, character -&gt; {
   *     assertThat(character.getRace()).isEqualTo(ELF);
   * });</code></pre>
   *
   * @param key he given key to check.
   * @param valueRequirements the given requirements for check value.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given values is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given {@code key}.
   * @throws AssertionError if the actual map contains the given key, but value not pass the given {@code valueRequirements}.
   * @since 3.6.0
   */
  public SELF hasEntrySatisfying(K key, Consumer<? super V> valueRequirements) {
    maps.assertHasEntrySatisfying(info, actual, key, valueRequirements);
    return myself;
  }

  /**
   * Verifies that the actual map contains an entry satisfying the given {@code entryCondition}.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;TolkienCharacter, Ring&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(galadriel, nenya);
   * ringBearers.put(gandalf, narya);
   * ringBearers.put(elrond, vilya);
   * ringBearers.put(frodo, oneRing);
   *
   * Condition&lt;Map.Entry&lt;TolkienCharacter, Ring&gt;&gt; oneRingManBearer =
   *   new Condition&lt;Map.Entry&lt;TolkienCharacter, Ring&gt;&gt;("One ring man bearer") {
   *     public boolean matches(Map.Entry&lt;TolkienCharacter, Ring&gt; entry) {
   *       return entry.getKey().getRace() == MAN &amp;&amp; entry.getValue() == oneRing;
   *     }
   *   };
   *
   * // assertion will fail
   * assertThat(ringBearers).hasEntrySatisfying(oneRingManBearer);
   *
   * ringBearers.put(isildur, oneRing);
   *
   * // now assertion will pass
   * assertThat(ringBearers).hasEntrySatisfying(oneRingManBearer);</code></pre>
   *
   * @param entryCondition the condition for searching entry.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no entry matching given {@code entryCondition}.
   * @since 2.7.0 / 3.7.0
   */
  public SELF hasEntrySatisfying(Condition<? super Map.Entry<K, V>> entryCondition) {
    maps.assertHasEntrySatisfying(info, actual, entryCondition);
    return myself;
  }

  /**
   * Verifies that the actual map contains an entry with a key satisfying the given {@code keyCondition}
   * and a value satisfying the given {@code valueCondition}.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;TolkienCharacter, Ring&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(galadriel, nenya);
   * ringBearers.put(gandalf, narya);
   * ringBearers.put(elrond, vilya);
   * ringBearers.put(frodo, oneRing);
   *
   * Condition&lt;TolkienCharacter&gt; isMan = new Condition&lt;TolkienCharacter&gt;("is man") {
   *   public boolean matches(TolkienCharacter tolkienCharacter) {
   *     return tolkienCharacter.getRace() == MAN;
   *   }
   * };
   *
   * Condition&lt;Ring&gt; oneRingBearer = new Condition&lt;Ring&gt;("One ring bearer") {
   *   public boolean matches(Ring ring) {
   *     return ring == oneRing;
   *   }
   * };
   *
   * // assertion will fail
   * assertThat(ringBearers).hasEntrySatisfying(isMan, oneRingBearer);
   *
   * ringBearers.put(isildur, oneRing);
   *
   * // now assertion will pass
   * assertThat(ringBearers).hasEntrySatisfying(isMan, oneRingBearer);</code></pre>
   *
   * @param keyCondition the condition to be matched by the entry's key.
   * @param valueCondition the condition to be matched by the entry's value.
   * @return {@code this} assertion object.
   * @throws NullPointerException if any of the given conditions is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no entry with a key matching {@code keyCondition} and a value matching {@code valueCondition}.
   * @since 2.7.0  / 3.7.0
   */
  public SELF hasEntrySatisfying(Condition<? super K> keyCondition, Condition<? super V> valueCondition) {
    maps.assertHasEntrySatisfyingConditions(info, actual, keyCondition, valueCondition);
    return myself;
  }

  /**
   * Verifies that the actual map contains an entry with a key satisfying the given {@code keyCondition}.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;TolkienCharacter, Ring&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(galadriel, nenya);
   * ringBearers.put(gandalf, narya);
   * ringBearers.put(elrond, vilya);
   * ringBearers.put(frodo, oneRing);
   *
   * Condition&lt;TolkienCharacter&gt; isElf = new Condition&lt;TolkienCharacter&gt;("is elf") {
   *   public boolean matches(TolkienCharacter tolkienCharacter) {
   *     return tolkienCharacter.getRace() == ELF;
   *   }
   * };
   *
   * Condition&lt;TolkienCharacter&gt; isOrc = new Condition&lt;TolkienCharacter&gt;("is orc") {
   *   public boolean matches(TolkienCharacter tolkienCharacter) {
   *     return tolkienCharacter.getRace() == ORC;
   *   }
   * };
   *
   * // assertion will pass
   * assertThat(ringBearers).hasKeySatisfying(isElf);
   *
   * // assertion will fail
   * assertThat(ringBearers).hasKeySatisfying(isOrc);</code></pre>
   *
   * @param keyCondition the condition to be matched by the entry's key.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no key matching the given {@code keyCondition}.
   * @since 2.7.0  / 3.7.0
   */
  public SELF hasKeySatisfying(Condition<? super K> keyCondition) {
    maps.assertHasKeySatisfying(info, actual, keyCondition);
    return myself;
  }

  /**
   * Verifies that the actual map contains an entry with a value satisfying the given {@code valueCondition}.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * Condition&lt;TolkienCharacter&gt; isElf = new Condition&lt;TolkienCharacter&gt;("is elf") {
   *   public boolean matches(TolkienCharacter tolkienCharacter) {
   *     return tolkienCharacter.getRace() == ELF;
   *   }
   * };
   *
   * Condition&lt;TolkienCharacter&gt; isOrc = new Condition&lt;TolkienCharacter&gt;("is orc") {
   *   public boolean matches(TolkienCharacter tolkienCharacter) {
   *     return tolkienCharacter.getRace() == ORC;
   *   }
   * };
   *
   * // assertion will pass
   *
   * assertThat(ringBearers).hasValueSatisfying(isElf);
   * // assertion will fail
   * assertThat(ringBearers).hasValueSatisfying(isOrc);</code></pre>
   *
   * @param valueCondition the condition to be matched by the entry's value.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given condition is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no value matching the given {@code valueCondition}.
   * @since 2.7.0  / 3.7.0
   */
  public SELF hasValueSatisfying(Condition<? super V> valueCondition) {
    maps.assertHasValueSatisfying(info, actual, valueCondition);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given entries.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).doesNotContain(entry(oneRing, aragorn), entry(oneRing, sauron));
   *
   * // assertions will fail
   * assertThat(ringBearers).doesNotContain(entry(oneRing, frodo));
   * assertThat(ringBearers).doesNotContain(entry(oneRing, frodo), entry(oneRing, aragorn));</code></pre>
   *
   * @param entries the given entries.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   */
  public SELF doesNotContain(@SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
    maps.assertDoesNotContain(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given entry.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).doesNotContainEntry(oneRing, aragorn);
   *
   * // assertion will fail
   * assertThat(ringBearers).doesNotContain(oneRing, frodo);</code></pre>
   *
   * @param key key of the entry.
   * @param value value of the entry.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   */
  public SELF doesNotContainEntry(K key, V value) {
    maps.assertDoesNotContain(info, actual, array(entry(key, value)));
    return myself;
  }

  /**
   * Verifies that the actual map contains the given key.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   *
   * // assertion will pass
   * assertThat(ringBearers).containsKey(vilya);
   *
   * // assertion will fail
   * assertThat(ringBearers).containsKey(oneRing);</code></pre>
   *
   * @param key the given key
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given key.
   */
  @SuppressWarnings("unchecked")
  public SELF containsKey(K key) {
    return containsKeys(key);
  }

  /**
   * Verifies that the actual map contains the given keys.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertions will pass
   * assertThat(ringBearers).containsKeys(nenya, oneRing);
   *
   * // assertions will fail
   * assertThat(ringBearers).containsKeys(vilya);
   * assertThat(ringBearers).containsKeys(vilya, oneRing);</code></pre>
   *
   * @param keys the given keys
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given key.
   * @throws IllegalArgumentException if the given argument is an empty array.
   */
  public SELF containsKeys(@SuppressWarnings("unchecked") K... keys) {
    maps.assertContainsKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given key.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(nenya, galadriel);
   * elvesRingBearers.put(narya, gandalf);
   * elvesRingBearers.put(vilya, elrond);
   *
   * // assertion will pass
   * assertThat(elvesRingBearers).doesNotContainKey(oneRing);
   *
   * // assertion will fail
   * assertThat(elvesRingBearers).doesNotContainKey(vilya);</code></pre>
   *
   * @param key the given key
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given key.
   */
  @SuppressWarnings("unchecked")
  public SELF doesNotContainKey(K key) {
    return doesNotContainKeys(key);
  }

  /**
   * Verifies that the actual map does not contain any of the given keys.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; elvesRingBearers = new HashMap&lt;&gt;();
   * elvesRingBearers.put(nenya, galadriel);
   * elvesRingBearers.put(narya, gandalf);
   * elvesRingBearers.put(vilya, elrond);
   *
   * // assertion will pass
   * assertThat(elvesRingBearers).doesNotContainKeys(oneRing, someManRing);
   *
   * // assertions will fail
   * assertThat(elvesRingBearers).doesNotContainKeys(vilya, nenya);
   * assertThat(elvesRingBearers).doesNotContainKeys(vilya, oneRing);</code></pre>
   *
   * @param keys the given keys
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given key.
   */
  public SELF doesNotContainKeys(@SuppressWarnings("unchecked") K... keys) {
    maps.assertDoesNotContainKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given keys and nothing else, in any order.
   *
   * <p>
   * Examples :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).containsOnlyKeys(oneRing, nenya, narya, vilya);
   *
   * // assertion will fail
   * assertThat(ringBearers).containsOnlyKeys(oneRing, nenya);</code></pre>
   *
   * @param keys the given keys that should be in the actual map.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given keys, i.e. the actual map contains some or none
   *           of the given keys, or the actual map contains more entries than the given ones.
   * @throws IllegalArgumentException if the given argument is an empty array.
   */

  public SELF containsOnlyKeys(@SuppressWarnings("unchecked") K... keys) {
    maps.assertContainsOnlyKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given keys and nothing else, in any order.
   *
   * <p>
   * Examples :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).containsOnlyKeys(Arrays.asList(oneRing, nenya, narya, vilya));
   *
   * // assertions will fail
   * assertThat(ringBearers).containsOnlyKeys(Arrays.asList(oneRing, nenya));
   * assertThat(ringBearers).containsOnlyKeys(Arrays.asList(oneRing, nenya, narya, vilya, nibelungRing));</code></pre>
   *
   * @param keys the given keys that should be in the actual map.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null} or empty.
   * @throws AssertionError if the actual map does not contain the given keys, i.e. the actual map contains some or none
   *           of the given keys, or the actual map's keys contains keys not in the given ones.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @since 3.12.0
   */
  public SELF containsOnlyKeys(Iterable<? extends K> keys) {
    maps.assertContainsOnlyKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map contains the given value.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).containsValue(frodo);
   *
   * // assertion will fail
   * assertThat(ringBearers).containsValue(sauron);</code></pre>
   *
   * @param value the value to look for.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given value.
   */
  public SELF containsValue(V value) {
    maps.assertContainsValue(info, actual, value);
    return myself;
  }

  /**
   * Verifies that the actual map contains the given values.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).containsValues(frodo, galadriel);
   *
   * // assertions will fail
   * assertThat(ringBearers).containsValues(sauron, aragorn);
   * assertThat(ringBearers).containsValues(sauron, frodo);</code></pre>
   *
   * @param values the values to look for in the actual map.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given values.
   */
  public SELF containsValues(@SuppressWarnings("unchecked") V... values) {
    maps.assertContainsValues(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given value.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).doesNotContainValue(aragorn);
   *
   * // assertion will fail
   * assertThat(ringBearers).doesNotContainValue(frodo);</code></pre>
   *
   * @param value the value that should not be in actual map.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given value.
   */
  public SELF doesNotContainValue(V value) {
    maps.assertDoesNotContainValue(info, actual, value);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given entries and nothing else, in any order.
   *
   * <p>
   * Examples :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertion will pass
   * assertThat(ringBearers).containsOnly(entry(oneRing, frodo), entry(nenya, galadriel), entry(narya, gandalf), entry(vilya, elrond));
   *
   * // assertion will fail
   * assertThat(ringBearers).containsOnly(entry(oneRing, frodo), entry(nenya, galadriel));</code></pre>
   *
   * @param entries the entries that should be in the actual map.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map does not contain the given entries, i.e. the actual map contains some or
   *           none of the given entries, or the actual map contains more entries than the given ones.
   */
  public SELF containsOnly(@SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
    maps.assertContainsOnly(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given entries and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with maps that have a consistent iteration order (i.e. don't use it with
   * {@link java.util.HashMap}, prefer {@link #containsOnly(java.util.Map.Entry...)} in that case).
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = newLinkedHashMap(entry(oneRing, frodo),
   *                                                            entry(nenya, galadriel),
   *                                                            entry(narya, gandalf));
   *
   * // assertion will pass
   * assertThat(ringBearers).containsExactly(entry(oneRing, frodo),
   *                                         entry(nenya, galadriel),
   *                                         entry(narya, gandalf));
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(ringBearers).containsExactly(entry(nenya, galadriel),
   *                                         entry(narya, gandalf),
   *                                         entry(oneRing, frodo));</code></pre>
   *
   * @param entries the given entries.
   * @return {@code this} assertions object
   * @throws NullPointerException if the given entries array is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws IllegalArgumentException if the given entries array is empty.
   * @throws AssertionError if the actual map does not contain the given entries with same order, i.e. the actual map
   *           contains some or none of the given entries, or the actual map contains more entries than the given ones
   *           or entries are the same but the order is not.
   */
  public SELF containsExactly(@SuppressWarnings("unchecked") Map.Entry<? extends K, ? extends V>... entries) {
    maps.assertContainsExactly(info, actual, entries);
    return myself;
  }

  /**
   * Do not use this method.
   *
   * @deprecated Custom element Comparator is not supported for MapEntry comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public SELF usingElementComparator(Comparator<? super Map.Entry<? extends K, ? extends V>> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }

  /**
   * Do not use this method.
   *
   * @deprecated Custom element Comparator is not supported for MapEntry comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public SELF usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }

  // override methods to avoid compilation error when chaining an AbstractAssert method with a AbstractMapAssert one
  // this is pretty sad, a better fix for that would be welcome

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
    return usingComparator(customComparator, null);
  }

  @Override
  @CheckReturnValue
  public SELF usingComparator(Comparator<? super ACTUAL> customComparator, String customComparatorDescription) {
    return super.usingComparator(customComparator, customComparatorDescription);
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

  /**
   * Returns an {@code Assert} object that allows performing assertions on the size of the {@link Map} under test.
   * <p>
   * Once this method is called, the object under test is no longer the {@link Map} but its size,
   * to perform assertions on the {@link Map}, call {@link AbstractMapSizeAssert#returnToMap()}.
   * <p>
   * Example :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = newHashMap(entry(oneRing, frodo),
   *                                                      entry(nenya, galadriel),
   *                                                      entry(narya, gandalf));
   *
   * // assertion will pass:
   * assertThat(ringBearers).size().isGreaterThan(1)
   *                               .isLessThanOrEqualTo(3)
   *                        returnToMap().contains(entry(oneRing, frodo),
   *                                               entry(nenya, galadriel),
   *                                               entry(narya, gandalf));
   *
   * // assertion will fail:
   * assertThat(ringBearers).size().isGreaterThan(5);</code></pre>
   *
   * @return a {@link AbstractMapSizeAssert} to allow assertions on the number of key-value mappings in this map
   * @throws NullPointerException if the given map is {@code null}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @CheckReturnValue
  public AbstractMapSizeAssert<SELF, ACTUAL, K, V> size() {
    checkNotNull(actual, "Can not perform assertions on the size of a null map.");
    return new MapSizeAssert(this, actual.size());
  }

  /**
   * Extract the values of given keys from the map under test into an array, this new array becoming
   * the object under test.
   * <p>
   * For example, if you specify "id", "name" and "email" keys then the array will contain the map values for
   * these keys, you can then perform array assertions on the extracted values.
   * <p>
   * If a given key is not present in the map under test, a null value is extracted.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, Object&gt; map = new HashMap&lt;&gt;();
   * map.put("name", "kawhi");
   * map.put("age", 25);
   *
   * assertThat(map).extracting("name", "age")
   *                .contains("kawhi", 25);</code></pre>
   * <p>
   * Note that the order of extracted keys value is consistent with the iteration order of the array under test.
   * <p>
   * Nested keys are not yet supported, passing "name.first" won't get a value for "name" and then try to extract
   * "first" from the previously extracted value, instead it will simply look for a value under "name.first" key.
   *
   * @param keys the keys used to get values from the map under test
   * @return a new assertion object whose object under test is the array containing the extracted map values
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> extracting(Object... keys) {
    isNotNull();
    List<Object> extractedValues = Stream.of(keys).map(actual::get).collect(Collectors.toList());
    String extractedPropertiesOrFieldsDescription = extractedDescriptionOf(keys);
    String description = mostRelevantDescription(info.description(), extractedPropertiesOrFieldsDescription);
    return newListAssertInstance(extractedValues).as(description);
  }

  /**
   * Extract the value of given key from the map under test, the extracted value becoming the new object under test.
   * <p>
   * For example, if you specify "id" key, then the object under test will be the map value for this key.
   * <p>
   * If a given key is not present in the map under test, a null value is extracted.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, Object&gt; map = new HashMap&lt;&gt;();
   * map.put("name", "kawhi");
   *
   * assertThat(map).extracting("name")
   *                .isEqualTo("kawhi");</code></pre>
   * <p>
   * Nested keys are not yet supported, passing "name.first" won't get a value for "name" and then try to extract
   * "first" from the previously extracted value, instead it will simply look for a value under "name.first" key.
   *
   * @param key the key used to get value from the map under test
   * @return a new {@link ObjectAssert} instance whose object under test is the extracted map value
   *
   * @since 3.13.0
   */
  @CheckReturnValue
  public AbstractObjectAssert<?, ?> extracting(Object key) {
    isNotNull();
    Object extractedValue = actual.get(key);
    String extractedPropertyOrFieldDescription = extractedDescriptionOf(key);
    String description = mostRelevantDescription(info.description(), extractedPropertyOrFieldDescription);
    return newObjectAssert(extractedValue).as(description);
  }

  /**
   * Use the given {@link Function} to extract a value from the {@link Map}'s entries.
   * The extracted values are stored in a new list becoming the object under test.
   * <p>
   * Let's take a look at an example to make things clearer :
   *  <pre><code class='java'> // Build a Map that associates family roles and name of the Simpson familly
   * Map&lt;String, CartoonCharacter&gt; characters = new HashMap&lt;&gt;();
   * characters.put(&quot;dad&quot;, new CartoonCharacter(&quot;Homer&quot;));
   * characters.put(&quot;mom&quot;, new CartoonCharacter(&quot;Marge&quot;));
   * characters.put(&quot;girl&quot;, new CartoonCharacter(&quot;Lisa&quot;));
   * characters.put(&quot;boy&quot;, new CartoonCharacter(&quot;Bart&quot;));
   *
   * assertThat(characters).extractingFromEntries(e -&gt; e.getValue().getName())
   *                       .containsOnly(&quot;Homer&quot;, &quot;Marge&quot;, &quot;Lisa&quot;, &quot;Bart&quot;);</code></pre>
   *
   * @param extractor the extractor function to extract a value from an entry of the Map under test.
   * @return a new assertion object whose object under test is the list of values extracted
   * @since 3.12.0
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> extractingFromEntries(Function<? super Map.Entry<K, V>, Object> extractor) {
    isNotNull();
    List<Object> extractedObjects = actual.entrySet().stream()
                                          .map(extractor)
                                          .collect(toList());
    return newListAssertInstance(extractedObjects).as(info.description());
  }

  /**
   * Use the given {@link Function}s to extract values from the {@link Map}'s entries.
   * The extracted values are stored in a new list composed of {@link Tuple}s (a simple data structure containing the extracted values),
   * this new list becoming the object under test.
   * <p>
   * This method works as {@link AbstractMapAssert#extractingFromEntries(java.util.function.Function)} except
   * that it is designed to extract multiple values from the {@link Map} entries.
   * That's why here the new object under test is a List of {@link Tuple}s.
   * <p>
   * The Tuple data corresponds to the extracted values from the Map's entries, for instance if you pass functions
   * extracting the "id", "name" and "email" values then each Tuple data will be composed of an id, a name and an email
   * extracted from the entry of the Map (the Tuple's data order is the same as the given functions order).
   * <p>
   * Let's take a look at an example to make things clearer :
   * <pre><code class='java'> // Build a Map that associates family roles and name of the Simpson familly
   * Map&lt;String, CartoonCharacter&gt; characters = new HashMap&lt;&gt;();
   * characters.put(&quot;dad&quot;, new CartoonCharacter(&quot;Homer&quot;));
   * characters.put(&quot;mom&quot;, new CartoonCharacter(&quot;Marge&quot;));
   * characters.put(&quot;girl&quot;, new CartoonCharacter(&quot;Lisa&quot;));
   * characters.put(&quot;boy&quot;, new CartoonCharacter(&quot;Bart&quot;));
   *
   * assertThat(characters).extractingFromEntries(e -&gt; e.getKey(), e -&gt; e.getValue().getName())
   *                       .containsOnly(tuple(&quot;dad&quot;, &quot;Homer&quot;),
   *                                     tuple(&quot;mom&quot;, &quot;Marge&quot;),
   *                                     tuple(&quot;girl&quot;, &quot;Lisa&quot;),
   *                                     tuple(&quot;boy&quot;, &quot;Bart&quot;));</code></pre>
   *
   * Note that the order of extracted property/field values is consistent with the iteration order of the Iterable under
   * test, for example if it's a {@link HashSet}, you won't be able to make any assumptions on the extracted values
   * order.
   *
   * @param extractors the extractor functions to extract values from an entry of the Map under test.
   * @return a new assertion object whose object under test is the list of Tuples containing the extracted values.
   * @since 3.12.0
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extractingFromEntries(@SuppressWarnings("unchecked") Function<? super Map.Entry<K, V>, Object>... extractors) {
    isNotNull();
    // combine all extractors into one function
    Function<Map.Entry<K, V>, Tuple> tupleExtractor = objectToExtractValueFrom -> new Tuple(Stream.of(extractors)
                                                                                                  .map(extractor -> extractor.apply(objectToExtractValueFrom))
                                                                                                  .toArray());
    List<Tuple> extractedTuples = actual.entrySet().stream()
                                        .map(tupleExtractor)
                                        .collect(toList());
    return newListAssertInstance(extractedTuples).as(info.description());
  }

  /**
   * Flatten the values of the given keys from the actual map under test into a new array, this new array becoming the object under test.
   * <p>
   * If a given key is not present in the map under test, a {@code null} value is extracted.
   * <p>
   * If a given key value is not an {@link Iterable} or an array, it is simply extracted but (obviously) not flattened.
   * <p>
   * Example:
   * <pre><code class='java'> List&lt;String&gt; names = asList("Dave", "Jeff");
   * LinkedHashSet&lt;String&gt; jobs = newLinkedHashSet("Plumber", "Builder");
   * Iterable&lt;String&gt; cities = asList("Dover", "Boston", "Paris");
   * int[] ranks = { 1, 2, 3 };
   *
   * Map&lt;String, Object&gt; map = new LinkedHashMap&lt;&gt;();
   * map.put("name", names);
   * map.put("job", jobs);
   * map.put("city", cities);
   * map.put("rank", ranks);
   *
   * assertThat(map).flatExtracting("name","job","city", "rank")
   *                .containsExactly("Dave", "Jeff",
   *                                 "Plumber", "Builder",
   *                                 "Dover", "Boston", "Paris",
   *                                 1, 2, 3);
   *
   * // the order of values in the resulting array is the order of map keys then key values:
   * assertThat(map).flatExtracting("city", "job", "name")
   *                .containsExactly("Dover", "Boston", "Paris",
   *                                 "Plumber", "Builder",
   *                                 "Dave", "Jeff");
   *
   * // contains exactly null twice (one for each unknown keys)
   * assertThat(map).flatExtracting("foo", "name", "bar")
   *                .containsExactly(null, "Dave", "Jeff", null);
   *
   * // if the key value is not an iterable/array, it will be simply extracted but not flattened.
   * map.put("year", 2017));
   * assertThat(map).flatExtracting("name","job","year")
   *                .containsExactly("Dave", "Jeff", "Plumber", "Builder", "Dover", 2017);</code></pre>
   * <p>
   * Note that the order of values in the resulting array is the order of the map keys iteration then key values.
   *
   * @param keys the keys used to get values from the map under test
   * @return a new assertion object whose object under test is the array containing the extracted flattened map values
   */
  @CheckReturnValue
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> flatExtracting(String... keys) {
    Tuple values = byName(keys).apply(actual);
    List<Object> valuesFlattened = flatten(values.toList());
    String extractedPropertiesOrFieldsDescription = extractedDescriptionOf(keys);
    String description = mostRelevantDescription(info.description(), extractedPropertiesOrFieldsDescription);
    return newListAssertInstance(valuesFlattened).as(description);
  }

  private static List<Object> flatten(Iterable<Object> collectionToFlatten) {
    List<Object> result = new ArrayList<>();
    for (Object item : collectionToFlatten) {
      if (item instanceof Iterable<?>) result.addAll(toCollection((Iterable<?>) item));
      else if (isArray(item)) result.addAll(org.assertj.core.util.Arrays.asList(item));
      else result.add(item);
    }
    return result;
  }
}
