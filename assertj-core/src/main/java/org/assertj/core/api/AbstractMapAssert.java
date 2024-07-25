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

import static java.util.Collections.singleton;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.description.Description.mostRelevantDescription;
import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.extractor.Extractors.extractedDescriptionOf;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.isArray;
import static org.assertj.core.util.IterableUtil.toCollection;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import org.assertj.core.annotations.Beta;
import org.assertj.core.api.recursive.assertion.RecursiveAssertionConfiguration;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.description.Description;
import org.assertj.core.groups.Tuple;
import org.assertj.core.internal.Maps;
import org.assertj.core.util.CheckReturnValue;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Map}s.
 *
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 * @author dorzey
 * @author Filip Hrisafov
 *
 * @param <SELF> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/1IZIRcY"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <ACTUAL> the type of the "actual" value.
 * @param <K> the type of keys in the map.
 * @param <V> the type of values in the map.
 */
public abstract class AbstractMapAssert<SELF extends AbstractMapAssert<SELF, ACTUAL, K, V>, ACTUAL extends Map<K, V>, K, V>
    extends AbstractObjectAssert<SELF, ACTUAL> implements EnumerableAssert<SELF, Map.Entry<? extends K, ? extends V>> {

  @VisibleForTesting
  Maps maps = Maps.instance();

  protected AbstractMapAssert(ACTUAL actual, Class<?> selfType) {
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if one or more entries don't satisfy the given requirements.
   * @throws NullPointerException if the given entryRequirements {@link BiConsumer} is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if no entries satisfy the given requirements.
   * @throws NullPointerException if the given entryRequirements {@link BiConsumer} is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if one or more entries satisfies the given requirements.
   * @throws NullPointerException if the given entryRequirements {@link BiConsumer} is {@code null}.
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
   * Examples:
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
   * Examples:
   * <pre><code class='java'> import static com.google.common.collect.ImmutableMap.of;
   *
   * Map&lt;Ring, TolkienCharacter&gt; ringBearers = ImmutableMap.of(nenya, galadriel,
   *                                                           narya, gandalf,
   *                                                           vilya, elrond,
   *                                                           oneRing, frodo);
   *
   * // assertion succeeds:
   * assertThat(ringBearers).hasSameSizeAs(ImmutableMap.of(oneRing, frodo,
   *                                                       narya, gandalf,
   *                                                       nenya, galadriel,
   *                                                       vilya, elrond));
   *
   * // assertions fails:
   * assertThat(ringBearers).hasSameSizeAs(Collections.emptyMap());
   * assertThat(ringBearers).hasSameSizeAs(ImmutableMap.of(nenya, galadriel,
   *                                                       narya, gandalf,
   *                                                       vilya, elrond));</code></pre>
   *
   * @param other the {@code Map} to compare size with actual map
   * @return {@code this} assertion object
   * @throws AssertionError if the actual map is {@code null}
   * @throws AssertionError if the actual map and the given {@code Map} don't have the same size
   * @throws NullPointerException if the other {@code Map} is {@code null}
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
   * Examples:
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   */
  @SafeVarargs
  public final SELF contains(Map.Entry<? extends K, ? extends V>... entries) {
    return containsForProxy(entries);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsForProxy(Map.Entry<? extends K, ? extends V>[] entries) {
    maps.assertContains(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map contains at least one of the given entries.
   * <p>
   * Examples:
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain any of the given entries.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
   * @since 3.6.0
   */
  @SafeVarargs
  public final SELF containsAnyOf(Map.Entry<? extends K, ? extends V>... entries) {
    return containsAnyOfForProxy(entries);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsAnyOfForProxy(Map.Entry<? extends K, ? extends V>[] entries) {
    maps.assertContainsAnyOf(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map contains all entries of the given map, in any order.
   * <p>
   * Examples:
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
   * // assertions succeed
   * assertThat(ringBearers).containsAllEntriesOf(elvesRingBearers);
   * assertThat(ringBearers).containsAllEntriesOf(emptyMap);
   *
   * // assertion fails
   * assertThat(elvesRingBearers).containsAllEntriesOf(ringBearers);</code></pre>
   *
   * @param other the map with the given entries.
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws NullPointerException if any of the entries in the given map is {@code null}.
   */
  public SELF containsAllEntriesOf(Map<? extends K, ? extends V> other) {
    maps.assertContainsAllEntriesOf(info, actual, other);
    return myself;
  }

  /**
   * Same as {@link #containsExactly(Map.Entry[])} but handles the conversion of {@link Map#entrySet()} to array.
   * <p>
   * Verifies that the actual map contains only the entries of the given map and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with maps that have a consistent iteration order (i.e. don't use it with
   * {@link java.util.HashMap}, prefer {@link #containsExactlyInAnyOrderEntriesOf(java.util.Map)} in that case).
   * <p>
   * Examples:
   * <pre><code class='java'> // newLinkedHashMap builds a Map with iteration order corresponding to the insertion order
   * Map&lt;Ring, TolkienCharacter&gt; ringBearers = newLinkedHashMap(entry(oneRing, frodo),
   *                                                            entry(nenya, galadriel),
   *                                                            entry(narya, gandalf));
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the entries of the given map with same order, i.e
   *           the actual map contains some or none of the entries of the given map, or the actual map contains more
   *           entries than the entries of the given map or entries are the same but the order is not.
   * @throws IllegalArgumentException if the given map is empty.
   * @throws NullPointerException if the given map is {@code null}.
   * @since 3.12.0
   */
  public SELF containsExactlyEntriesOf(Map<? extends K, ? extends V> map) {
    return containsExactly(toEntries(map));
  }

  /**
   * Same as {@link #containsOnly(Map.Entry[])} but handles the conversion of {@link Map#entrySet()} to array.
   * <p>
   * Verifies that the actual map contains only the given entries and nothing else, in any order.
   * <p>
   * Examples:
   * <pre><code class='java'> // newLinkedHashMap builds a Map with iteration order corresponding to the insertion order
   * Map&lt;Ring, TolkienCharacter&gt; ringBearers = newLinkedHashMap(entry(oneRing, frodo),
   *                                                            entry(nenya, galadriel),
   *                                                            entry(narya, gandalf));
   *
   * // assertion will pass
   * assertThat(ringBearers).containsExactlyInAnyOrderEntriesOf(newLinkedHashMap(entry(oneRing, frodo),
   *                                                                             entry(nenya, galadriel),
   *                                                                             entry(narya, gandalf)));
   *
   * // assertion will pass although actual and expected order differ
   * assertThat(ringBearers).containsExactlyInAnyOrderEntriesOf(newLinkedHashMap(entry(nenya, galadriel),
   *                                                                             entry(narya, gandalf),
   *                                                                             entry(oneRing, frodo)));
   * // assertion will fail as actual does not contain all expected entries
   * assertThat(ringBearers).containsExactlyInAnyOrderEntriesOf(newLinkedHashMap(entry(oneRing, frodo),
   *                                                                             entry(nenya, galadriel),
   *                                                                             entry(vilya, elrond)));
   * // assertion will fail as actual and expected have different sizes
   * assertThat(ringBearers).containsExactlyInAnyOrderEntriesOf(newLinkedHashMap(entry(oneRing, frodo),
   *                                                                             entry(nenya, galadriel),
   *                                                                             entry(narya, gandalf),
   *                                                                             entry(narya, gandalf)));</code></pre>
   *
   * @param map the given {@link Map} with the expected entries to be found in actual.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the entries of the given map, i.e the actual map contains
   *           some or none of the entries of the given map, or the actual map contains more entries than the entries of
   *           the given map.
   * @throws IllegalArgumentException if the given map is empty.
   * @throws NullPointerException if the given map is {@code null}.
   * @since 3.13.0
   */
  public SELF containsExactlyInAnyOrderEntriesOf(Map<? extends K, ? extends V> map) {
    return containsOnly(toEntries(map));
  }

  @SuppressWarnings("unchecked")
  private Map.Entry<? extends K, ? extends V>[] toEntries(Map<? extends K, ? extends V> map) {
    return map.entrySet().toArray(new Map.Entry[0]);
  }

  /**
   * Verifies that the actual map contains the given entry.
   * <p>
   * Examples:
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws NullPointerException if any of the entries in the given array is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given {@code key}.
   * @throws AssertionError if the actual map contains the given key, but value not match the given {@code valueCondition}.
   * @throws NullPointerException if the given values is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map not contains the given {@code key}.
   * @throws AssertionError if the actual map contains the given key, but value not pass the given {@code valueRequirements}.
   * @throws NullPointerException if the given values is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no entry matching given {@code entryCondition}.
   * @throws NullPointerException if the given condition is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no entry with a key matching {@code keyCondition} and a value matching {@code valueCondition}.
   * @throws NullPointerException if any of the given conditions is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no key matching the given {@code keyCondition}.
   * @throws NullPointerException if the given condition is {@code null}.
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if there is no value matching the given {@code valueCondition}.
   * @throws NullPointerException if the given condition is {@code null}.
   * @since 2.7.0  / 3.7.0
   */
  public SELF hasValueSatisfying(Condition<? super V> valueCondition) {
    maps.assertHasValueSatisfying(info, actual, valueCondition);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given entries.
   * <p>
   * Examples:
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  @SafeVarargs
  public final SELF doesNotContain(Map.Entry<? extends K, ? extends V>... entries) {
    return doesNotContainForProxy(entries);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainForProxy(Map.Entry<? extends K, ? extends V>[] entries) {
    maps.assertDoesNotContain(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given entry.
   * <p>
   * Examples:
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
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  public SELF doesNotContainEntry(K key, V value) {
    maps.assertDoesNotContain(info, actual, array(entry(key, value)));
    return myself;
  }

  /**
   * Verifies that the actual map contains the given key.
   * <p>
   * Examples:
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
  public SELF containsKey(K key) {
    return containsKeys(key);
  }

  /**
   * Verifies that the actual map contains the given keys.
   * <p>
   * Examples:
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
  @SafeVarargs
  public final SELF containsKeys(K... keys) {
    return containsKeysForProxy(keys);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsKeysForProxy(K[] keys) {
    maps.assertContainsKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given key.
   * <p>
   * Examples:
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
  public SELF doesNotContainKey(K key) {
    return doesNotContainKeys(key);
  }

  /**
   * Verifies that the actual map does not contain any of the given keys.
   * <p>
   * Examples:
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
  @SafeVarargs
  public final SELF doesNotContainKeys(K... keys) {
    return doesNotContainKeysForProxy(keys);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF doesNotContainKeysForProxy(K[] keys) {
    maps.assertDoesNotContainKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given keys and nothing else, in any order.
   * <p>
   * The verification tries to honor the key comparison semantic of the underlying map implementation.
   * The map under test has to be cloned to identify unexpected elements, but depending on the map implementation
   * this may not always be possible. In case it is not possible, a regular map is used and the key comparison strategy
   * may not be the same as the map under test.
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
  @SafeVarargs
  public final SELF containsOnlyKeys(K... keys) {
    return containsOnlyKeysForProxy(keys);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsOnlyKeysForProxy(K[] keys) {
    maps.assertContainsOnlyKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given keys and nothing else, in any order.
   * <p>
   * The verification tries to honor the key comparison semantic of the underlying map implementation.
   * The map under test has to be cloned to identify unexpected elements, but depending on the map implementation
   * this may not always be possible. In case it is not possible, a regular map is used and the key comparison strategy
   * may not be the same as the map under test.
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
  @SuppressWarnings("unchecked")
  public SELF containsOnlyKeys(Iterable<? extends K> keys) {
    if (keys instanceof Path) {
      // do not treat Path as an Iterable
      K path = (K) keys;
      maps.assertContainsOnlyKeys(info, actual, singleton(path));
    } else {
      maps.assertContainsOnlyKeys(info, actual, keys);
    }
    return myself;
  }

  /**
   * Verifies that the actual map contains the given value.
   * <p>
   * Examples:
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
   * Examples:
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
  @SafeVarargs
  public final SELF containsValues(V... values) {
    return containsValuesForProxy(values);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsValuesForProxy(V[] values) {
    maps.assertContainsValues(info, actual, values);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given value.
   * <p>
   * Examples:
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
   * <p>
   * The verification tries to honor the key comparison semantic of the underlying map implementation.
   * The map under test has to be cloned to identify unexpected elements, but depending on the map implementation
   * this may not always be possible. In case it is not possible, a regular map is used and the key comparison strategy
   * may not be the same as the map under test.
   * <p>
   * Examples :
   * <pre><code class='java'> Map&lt;Ring, TolkienCharacter&gt; ringBearers = new HashMap&lt;&gt;();
   * ringBearers.put(nenya, galadriel);
   * ringBearers.put(narya, gandalf);
   * ringBearers.put(vilya, elrond);
   * ringBearers.put(oneRing, frodo);
   *
   * // assertions will pass
   * assertThat(ringBearers).containsOnly(entry(oneRing, frodo),
   *                                      entry(nenya, galadriel),
   *                                      entry(narya, gandalf),
   *                                      entry(vilya, elrond));
   *
   * assertThat(Collections.emptyMap()).containsOnly();
   *
   * // assertion will fail
   * assertThat(ringBearers).containsOnly(entry(oneRing, frodo), entry(nenya, galadriel));</code></pre>
   *
   * @param entries the entries that should be in the actual map.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries, i.e. the actual map contains some or
   *           none of the given entries, or the actual map contains more entries than the given ones.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws NullPointerException if the given argument is {@code null}.
   */
  @SafeVarargs
  public final SELF containsOnly(Map.Entry<? extends K, ? extends V>... entries) {
    return containsOnlyForProxy(entries);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsOnlyForProxy(Map.Entry<? extends K, ? extends V>[] entries) {
    maps.assertContainsOnly(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given entries and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with maps that have a consistent iteration order (i.e. don't use it with
   * {@link java.util.HashMap}, prefer {@link #containsOnly(java.util.Map.Entry...)} in that case).
   * <p>
   * Examples:
   * <pre><code class='java'> // newLinkedHashMap builds a Map with iteration order corresponding to the insertion order
   * Map&lt;Ring, TolkienCharacter&gt; ringBearers = newLinkedHashMap(entry(oneRing, frodo),
   *                                                            entry(nenya, galadriel),
   *                                                            entry(narya, gandalf));
   * // assertions will pass
   * assertThat(ringBearers).containsExactly(entry(oneRing, frodo),
   *                                         entry(nenya, galadriel),
   *                                         entry(narya, gandalf));
   *
   * assertThat(Collections.emptyMap()).containsExactly();
   *
   * // assertion will fail as actual and expected order differ
   * assertThat(ringBearers).containsExactly(entry(nenya, galadriel),
   *                                         entry(narya, gandalf),
   *                                         entry(oneRing, frodo));</code></pre>
   *
   * @param entries the given entries.
   * @return {@code this} assertions object
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given entries with same order, i.e. the actual map
   *           contains some or none of the given entries, or the actual map contains more entries than the given ones
   *           or entries are the same but the order is not.
   * @throws IllegalArgumentException if the given entries array is empty.
   * @throws NullPointerException if the given entries array is {@code null}.
   */
  @SafeVarargs
  public final SELF containsExactly(Map.Entry<? extends K, ? extends V>... entries) {
    return containsExactlyForProxy(entries);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected SELF containsExactlyForProxy(Map.Entry<? extends K, ? extends V>[] entries) {
    maps.assertContainsExactly(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map is unmodifiable, i.e. throws an {@link UnsupportedOperationException} with
   * any attempt to modify the map.
   * <p>
   * Example:
   * <pre><code class='java'> // assertions succeeds
   * assertThat(Collections.unmodifiableMap(new HashMap&lt;&gt;())).isUnmodifiable();
   *
   * // assertions fails
   * assertThat(new HashMap&lt;&gt;()).isUnmodifiable();</code></pre>
   *
   * @return {@code this} assertion object.
   * @throws AssertionError if the actual collection is modifiable.
   * @see Collections#unmodifiableMap(Map)
   *
   * @since 3.24.0
   */
  @Beta
  public SELF isUnmodifiable() {
    isNotNull();
    assertIsUnmodifiable();
    return myself;
  }

  @SuppressWarnings("unchecked")
  private void assertIsUnmodifiable() {
    switch (actual.getClass().getName()) {
    case "java.util.Collections$EmptyNavigableMap":
    case "java.util.Collections$EmptyMap":
    case "java.util.Collections$EmptySortedMap":
    case "java.util.Collections$SingletonMap":
      // unmodifiable by contract, although not all methods throw UnsupportedOperationException
      return;
    }

    expectUnsupportedOperationException(() -> actual.clear(), "Map.clear()");
    expectUnsupportedOperationException(() -> actual.compute(null, (k, v) -> v), "Map.compute(null, (k, v) -> v)");
    expectUnsupportedOperationException(() -> actual.computeIfAbsent(null, k -> null), "Map.computeIfAbsent(null, k -> null)");
    expectUnsupportedOperationException(() -> actual.computeIfPresent(null, (k, v) -> v),
                                        "Map.computeIfPresent(null, (k, v) -> v)");
    expectUnsupportedOperationException(() -> actual.merge(null, null, (v1, v2) -> v1), "Map.merge(null, null, (v1, v2) -> v1))");
    expectUnsupportedOperationException(() -> actual.put(null, null), "Map.put(null, null)");
    expectUnsupportedOperationException(() -> actual.putAll(new HashMap<>()), "Map.putAll(new HashMap<>())");
    expectUnsupportedOperationException(() -> actual.putIfAbsent(null, null), "Map.putIfAbsent(null, null)");
    expectUnsupportedOperationException(() -> actual.replace(null, null, null), "Map.replace(null, null, null)");
    expectUnsupportedOperationException(() -> actual.replace(null, null), "Map.replace(null, null)");
    expectUnsupportedOperationException(() -> actual.remove(null), "Map.remove(null)");
    expectUnsupportedOperationException(() -> actual.remove(null, null), "Map.remove(null, null)");
    expectUnsupportedOperationException(() -> actual.replaceAll((k, v) -> v), "Map.replaceAll((k, v) -> v)");

    if (actual instanceof NavigableMap) {
      NavigableMap<K, V> navigableMap = (NavigableMap<K, V>) actual;
      expectUnsupportedOperationException(() -> navigableMap.pollFirstEntry(), "NavigableMap.pollFirstEntry()");
      expectUnsupportedOperationException(() -> navigableMap.pollLastEntry(), "NavigableMap.pollLastEntry()");
    }
  }

  private void expectUnsupportedOperationException(Runnable runnable, String method) {
    try {
      runnable.run();
      throwAssertionError(shouldBeUnmodifiable(method));
    } catch (UnsupportedOperationException e) {
      // happy path
    } catch (RuntimeException e) {
      throwAssertionError(shouldBeUnmodifiable(method, e));
    }
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   * @deprecated Custom element Comparator is not supported for MapEntry comparison.
   */
  @Override
  @Deprecated
  public SELF usingElementComparator(Comparator<? super Map.Entry<? extends K, ? extends V>> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }

  /**
   * Do not use this method.
   *
   * @throws UnsupportedOperationException if this method is called.
   * @deprecated Custom element Comparator is not supported for MapEntry comparison.
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
   * Examples:
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
    requireNonNull(actual, "Can not perform assertions on the size of a null map.");
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
   *
   * @deprecated use {@link #extractingByKeys(Object[])} instead
   */
  @Deprecated
  @CheckReturnValue
  public AbstractListAssert<?, List<?>, Object, ObjectAssert<Object>> extracting(Object... keys) {
    isNotNull();
    List<Object> extractedValues = Stream.of(keys).map(actual::get).collect(toList());
    String extractedPropertiesOrFieldsDescription = extractedDescriptionOf(keys);
    String description = mostRelevantDescription(info.description(), extractedPropertiesOrFieldsDescription);
    return newListAssertInstance(extractedValues).as(description);
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
   * assertThat(map).extractingByKeys("name", "age")
   *                .contains("kawhi", 25);</code></pre>
   * <p>
   * Note that:
   * <ul>
   *   <li>The order of the extracted key values is consistent with the iteration order of given keys.</li>
   *   <li>Providing no keys will result in an empty list.</li>
   * </ul>
   *
   * @param keys the keys used to get values from the map under test
   * @return a new assertion object whose object under test is the array containing the extracted map values
   * @since 3.14.0
   */
  @CheckReturnValue
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> extractingByKeys(K... keys) {
    return extractingByKeysForProxy(keys);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected AbstractListAssert<?, List<? extends V>, V, ObjectAssert<V>> extractingByKeysForProxy(K[] keys) {
    isNotNull();
    List<V> extractedValues = Stream.of(keys).map(actual::get).collect(toList());
    String extractedPropertiesOrFieldsDescription = extractedDescriptionOf((Object[]) keys);
    String description = mostRelevantDescription(info.description(), extractedPropertiesOrFieldsDescription);
    return newListAssertInstance(extractedValues).withAssertionState(myself).as(description);
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
   * @deprecated use {@link #extractingByKey(Object)} instead
   */
  @Deprecated
  @CheckReturnValue
  public AbstractObjectAssert<?, ?> extracting(Object key) {
    isNotNull();
    Object extractedValue = actual.get(key);
    String extractedPropertyOrFieldDescription = extractedDescriptionOf(key);
    String description = mostRelevantDescription(info.description(), extractedPropertyOrFieldDescription);
    return newObjectAssert(extractedValue).as(description);
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
   * assertThat(map).extractingByKey("name")
   *                .isEqualTo("kawhi");</code></pre>
   *
   * @param key the key used to get value from the map under test
   * @return a new {@link ObjectAssert} instance whose object under test is the extracted map value
   *
   * @see #extractingByKey(Object, InstanceOfAssertFactory)
   * @since 3.14.0
   */
  @CheckReturnValue
  public AbstractObjectAssert<?, V> extractingByKey(K key) {
    return internalExtractingByKey(key);
  }

  /**
   * Extract the value of given key from the map under test, the extracted value becoming the new object under test.
   * <p>
   * For example, if you specify "id" key, then the object under test will be the map value for this key.
   * <p>
   * If a given key is not present in the map under test, the assertion will fail.
   * <p>
   * The {@code assertFactory} parameter allows to specify an {@link InstanceOfAssertFactory}, which is used to get the
   * assertions narrowed to the factory type.
   * <p>
   * Wrapping the given {@link InstanceOfAssertFactory} with {@link Assertions#as(InstanceOfAssertFactory)} makes the
   * assertion more readable.
   * <p>
   * Example:
   * <pre><code class='java'> Map&lt;String, Object&gt; map = new HashMap&lt;&gt;();
   * map.put("name", "kawhi");
   *
   * assertThat(map).extractingByKey("name", as(InstanceOfAssertFactories.STRING))
   *                .startsWith("kaw");</code></pre>
   * <p>
   * Nested keys are not yet supported, passing "name.first" won't get a value for "name" and then try to extract
   * "first" from the previously extracted value, instead it will simply look for a value under "name.first" key.
   *
   * @param <ASSERT>      the type of the resulting {@code Assert}
   * @param key           the key used to get value from the map under test
   * @param assertFactory the factory which verifies the type and creates the new {@code Assert}
   * @return a new narrowed {@link Assert} instance whose object under test is the extracted map value
   * @throws NullPointerException if the given factory is {@code null}
   *
   * @since 3.14.0
   */
  @CheckReturnValue
  public <ASSERT extends AbstractAssert<?, ?>> ASSERT extractingByKey(K key, InstanceOfAssertFactory<?, ASSERT> assertFactory) {
    return internalExtractingByKey(key).asInstanceOf(assertFactory);
  }

  private AbstractObjectAssert<?, V> internalExtractingByKey(K key) {
    isNotNull();
    V extractedValue = actual.get(key);
    String extractedPropertyOrFieldDescription = extractedDescriptionOf(key);
    String description = mostRelevantDescription(info.description(), extractedPropertyOrFieldDescription);
    return newObjectAssert(extractedValue).withAssertionState(myself).as(description);
  }

  /**
   * Use the given {@link Function} to extract a value from the {@link Map}'s entries.
   * The extracted values are stored in a new list becoming the object under test.
   * <p>
   * Let's take a look at an example to make things clearer :
   *  <pre><code class='java'> // Build a Map that associates family roles and name of the Simpson family
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
    return newListAssertInstance(extractedObjects).withAssertionState(myself).as(info.description());
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
   * <pre><code class='java'> // Build a Map that associates family roles and name of the Simpson family
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
  @SafeVarargs
  public final AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extractingFromEntries(Function<? super Map.Entry<K, V>, Object>... extractors) {
    return extractingFromEntriesForProxy(extractors);
  }

  // This method is protected in order to be proxied for SoftAssertions / Assumptions.
  // The public method for it (the one not ending with "ForProxy") is marked as final and annotated with @SafeVarargs
  // in order to avoid compiler warning in user code
  protected AbstractListAssert<?, List<? extends Tuple>, Tuple, ObjectAssert<Tuple>> extractingFromEntriesForProxy(Function<? super Map.Entry<K, V>, Object>[] extractors) {
    isNotNull();
    // combine all extractors into one function
    Function<Map.Entry<K, V>, Tuple> tupleExtractor = objectToExtractValueFrom -> new Tuple(Stream.of(extractors)
                                                                                                  .map(extractor -> extractor.apply(objectToExtractValueFrom))
                                                                                                  .toArray());
    List<Tuple> extractedTuples = actual.entrySet().stream()
                                        .map(tupleExtractor)
                                        .collect(toList());
    return newListAssertInstance(extractedTuples).withAssertionState(myself).as(info.description());
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

  /**
   * Enable using a recursive field by field comparison strategy when calling the chained {@link RecursiveComparisonAssert},
   * <p>
   * Example:
   * <pre><code class='java'> public class Person {
   *   String name;
   *   boolean hasPhd;
   * }
   *
   * public class Doctor {
   *  String name;
   *  boolean hasPhd;
   * }
   *
   * Doctor drSheldon = new Doctor("Sheldon Cooper", true);
   * Doctor drLeonard = new Doctor("Leonard Hofstadter", true);
   * Doctor drRaj = new Doctor("Raj Koothrappali", true);
   *
   * Person sheldon = new Person("Sheldon Cooper", true);
   * Person leonard = new Person("Leonard Hofstadter", true);
   * Person raj = new Person("Raj Koothrappali", true);
   *
   * Map&lt;String, Doctor&gt; doctors = mapOf(entry(drSheldon.name, drSheldon),
   *                                           entry(drLeonard.name, drLeonard),
   *                                           entry(drRaj.name, drRaj));
   * Map&lt;String, Person&gt; people = mapOf(entry(sheldon.name, sheldon),
   *                                          entry(leonard.name, leonard),
   *                                          entry(raj.name, raj));
   *
   * // assertion succeeds as both maps contains equivalent items.
   * assertThat(doctors).usingRecursiveComparison()
   *                    .isEqualTo(people);
   *
   * // assertion fails because leonard names are different.
   * leonard.setName("Leonard Ofstater");
   * assertThat(doctors).usingRecursiveComparison()
   *                    .isEqualTo(people);</code></pre>
   *
   * A detailed documentation for the recursive comparison is available here: <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison">https://assertj.github.io/doc/#assertj-core-recursive-comparison</a>.
   * <p>
   * The default recursive comparison behavior is {@link RecursiveComparisonConfiguration configured} as follows:
   * <ul>
   *   <li> different types of map can be compared by default, this allows to compare for example an {@code HashMap<Person>} and a {@code LinkedHashMap<PersonDto>}.<br>
   *        This behavior can be turned off by calling {@link RecursiveComparisonAssert#withStrictTypeChecking() withStrictTypeChecking}.</li>
   *   <li>overridden equals methods are used in the comparison (unless stated otherwise - see <a href="https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals">https://assertj.github.io/doc/#assertj-core-recursive-comparison-ignoring-equals</a>)</li>
   *   <li>the following types are compared with these comparators:
   *     <ul>
   *       <li>{@code java.lang.Double}: {@code DoubleComparator} with precision of 1.0E-15</li>
   *       <li>{@code java.lang.Float}: {@code FloatComparator }with precision of 1.0E-6</li>
   *       <li>any comparators previously registered with {@link AbstractIterableAssert#usingComparatorForType(Comparator, Class)} </li>
   *     </ul>
   *   </li>
   * </ul>
   * <p>
   * At the moment, only `isEqualTo` can be chained after this method but there are plans to provide assertions.
   *
   * @return a new {@link RecursiveComparisonAssert} instance
   * @see RecursiveComparisonConfiguration RecursiveComparisonConfiguration
   */
  @Override
  @Beta
  public RecursiveComparisonAssert<?> usingRecursiveComparison() {
    // overridden for javadoc and to make this method public
    return super.usingRecursiveComparison();
  }

  /**
   * Same as {@link #usingRecursiveComparison()} but allows to specify your own {@link RecursiveComparisonConfiguration}.
   * @param recursiveComparisonConfiguration the {@link RecursiveComparisonConfiguration} used in the chained {@link RecursiveComparisonAssert#isEqualTo(Object) isEqualTo} assertion.
   *
   * @return a new {@link RecursiveComparisonAssert} instance built with the given {@link RecursiveComparisonConfiguration}.
   */
  @Override
  public RecursiveComparisonAssert<?> usingRecursiveComparison(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    // overridden for javadoc and to make this method public
    return super.usingRecursiveComparison(recursiveComparisonConfiguration);
  }

  /**
   * <p>Asserts that the given predicate is met for all fields of the object under test <b>recursively</b> (but not the object itself).</p>
   *
   * <p>For example if the object under test is an instance of class A, A has a B field and B a C field then the assertion checks A's B field and B's C field and all C's fields.</p>
   *
   * <p>The recursive algorithm employs cycle detection, so object graphs with cyclic references can safely be asserted over without causing looping.</p>
   *
   * <p>This method enables recursive asserting using default configuration, which means all fields of all objects have the   
   * {@link java.util.function.Predicate} applied to them (including primitive fields), no fields are excluded, but:
   * <ul>
   *   <li>The recursion does not enter into Java Class Library types (java.*, javax.*)</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Collection} and array elements (but not the collection/array itself)</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Map} values but not the map itself or its keys</li>
   *   <li>The {@link java.util.function.Predicate} is applied to {@link java.util.Optional} and primitive optional values</li>
   * </ul>
   * <p>You can change how the recursive assertion deals with arrays, collections, maps and optionals, see:</p>
   * <ul>
   *   <li>{@link RecursiveAssertionAssert#withCollectionAssertionPolicy(RecursiveAssertionConfiguration.CollectionAssertionPolicy)} for collections and arrays</li>
   *   <li>{@link RecursiveAssertionAssert#withMapAssertionPolicy(RecursiveAssertionConfiguration.MapAssertionPolicy)} for maps</li>
   *   <li>{@link RecursiveAssertionAssert#withOptionalAssertionPolicy(RecursiveAssertionConfiguration.OptionalAssertionPolicy)} for optionals</li>
   * </ul>
   *
   * <p>It is possible to assert several predicates over the object graph in a row.</p>
   *
   * <p>The classes used in recursive asserting are <em>not</em> thread safe. Care must be taken when running tests in parallel
   * not to run assertions over object graphs that are being shared between tests.</p>
   *
   * <p><strong>Example</strong></p>
   * <pre><code style='java'> class Author {
   *   String name;
   *   String email;
   *   List&lt;Book&gt; books = new ArrayList&lt;&gt;();
   *
   *   Author(String name, String email) {
   *     this.name = name;
   *     this.email = email;
   *   }
   * }
   *
   * class Book {
   *   String title;
   *   Author[] authors;
   *
   *   Book(String title, Author[] authors) {
   *     this.title = title;
   *     this.authors = authors;
   *   }
   * }
   *  ...
   *
   * Author pramodSadalage = new Author("Pramod Sadalage", "p.sadalage@recursive.test");
   * Author martinFowler = new Author("Martin Fowler", "m.fowler@recursive.test");
   * Author kentBeck = new Author("Kent Beck", "k.beck@recursive.test");
   *
   * Book noSqlDistilled = new Book("NoSql Distilled", new Author[] {pramodSadalage, martinFowler});
   * pramodSadalage.books.add(noSqlDistilled);
   * martinFowler.books.add(noSqlDistilled);
   *
   * Book refactoring = new Book("Refactoring", new Author[] {martinFowler, kentBeck});
   * martinFowler.books.add(refactoring);
   * kentBeck.books.add(refactoring);
   *
   * // assertion succeeds
   * Map&lt;String, Author&gt; authors = new HashMap&lt;&gt;();
   * authors.put("MF", martinFowler);
   * authors.put("KB", kentBeck);
   * assertThat(authors).usingRecursiveAssertion()
   *                    .allFieldsSatisfy(field -> field != null); </code></pre>
   *
   * <p>In case one or more fields in the object graph fails the predicate test, the entire assertion will fail. Failing fields
   * will be listed in the failure report using a JSON path-ish notation.</p>
   *
   * @return A new instance of {@link RecursiveAssertionAssert} built with a default {@link RecursiveAssertionConfiguration}.
   */
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion() {
    return super.usingRecursiveAssertion();
  }

  /**
   * <p>The same as {@link #usingRecursiveAssertion()}, but this method allows the developer to pass in an explicit recursion
   * configuration. This configuration gives fine-grained control over what to include in the recursion, such as:</p>
   *
   * <ul>
   *   <li>Exclusion of fields that are null</li>
   *   <li>Exclusion of fields by path</li>
   *   <li>Exclusion of fields by type</li>
   *   <li>Exclusion of primitive fields</li>
   *   <li>Inclusion of Java Class Library types in the recursive execution</li>
   *   <li>Treatment of {@link java.util.Collection} and array objects</li>
   *   <li>Treatment of {@link java.util.Map} objects</li>
   *   <li>Treatment of Optional and primitive Optional objects</li>
   * </ul>
   *
   * <p>Please refer to the documentation of {@link RecursiveAssertionConfiguration.Builder} for more details.</p>
   *
   * @param recursiveAssertionConfiguration The recursion configuration described above.
   * @return A new instance of {@link RecursiveAssertionAssert} built with a default {@link RecursiveAssertionConfiguration}.
   */
  @Override
  public RecursiveAssertionAssert usingRecursiveAssertion(RecursiveAssertionConfiguration recursiveAssertionConfiguration) {
    return super.usingRecursiveAssertion(recursiveAssertionConfiguration);
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

  /**
   * <p>Returns an {@link AbstractCollectionAssert} to make assertions on the values of the map</p>
   *
   * <p><strong>Example</strong></p>
   * <pre><code class='java'> TolkienCharacter pippin = new TolkienCharacter("Pippin", 28, HOBBIT);
   * TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, HOBBIT);
   * TolkienCharacter merry = new TolkienCharacter("Merry", 36, HOBBIT);
   *
   * Map&lt;String, TolkienCharacter&gt; characters = mapOf(entry("Pippin", pippin),
   *                                                  entry("Frodo", frodo),
   *                                                  entry("Merry", merry));
   * assertThat(characters).values()
   *                       .contains(frodo, pippin, merry); </code></pre>
   * @return An {@link AbstractCollectionAssert} to make collections assertion only on map values.
   * @throws NullPointerException if the map under test is null
   * @since 3.26.0
   */
  public AbstractCollectionAssert<?, Collection<? extends V>, V, ObjectAssert<V>> values() {
    requireNonNull(actual, "Can not extract values from a null map.");
    return assertThat(actual.values());
  }
}
