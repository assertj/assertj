/*
 * Created on Dec 21, 2010
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
package org.assertj.core.api;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.internal.Arrays.assertIsArray;
import static org.assertj.core.util.Arrays.array;

import java.util.Comparator;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Arrays;
import org.assertj.core.internal.Maps;
import org.assertj.core.util.VisibleForTesting;

/**
 * Base class for all implementations of assertions for {@link Map}s.
 * @param <S> the "self" type of this assertion class. Please read &quot;<a href="http://bit.ly/anMa4g"
 *          target="_blank">Emulating 'self types' using Java Generics to simplify fluent API implementation</a>&quot;
 *          for more details.
 * @param <A> the type of the "actual" value.
 * @param <K> the type of keys in map.
 * @param <V> the type of values in map.
 * 
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public abstract class AbstractMapAssert<S extends AbstractMapAssert<S, A, K, V>, A extends Map<K, V>, K, V>
		extends AbstractAssert<S, A> implements EnumerableAssert<S, MapEntry> {

  @VisibleForTesting
  Maps maps = Maps.instance();

  protected AbstractMapAssert(A actual, Class<?> selfType) {
    super(actual, selfType);
  }

  /** {@inheritDoc} */
  @Override
  public void isNullOrEmpty() {
    maps.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public void isEmpty() {
    maps.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  @Override
  public S isNotEmpty() {
    maps.assertNotEmpty(info, actual);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSize(int expected) {
    maps.assertHasSize(info, actual, expected);
    return myself;
  }

  /** {@inheritDoc} */
	public S hasSameSizeAs(Object other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

  /** {@inheritDoc} */
  @Override
  public S hasSameSizeAs(Iterable<?> other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return myself;
  }

	/**
	 * Verifies that the actual map contains the given entries, in any order.
	 * <p>
	 * Example :
	 *
	 * <pre>
	 * Map<Ring, TolkienCharacter> ringBearers = ... // init omitted
	 * assertThat(ringBearers).contains(entry(oneRing, frodo), entry(nenya, galadriel));
	 * </pre>
	 *
	 * @param entries the given entries.
	 * @return {@code this} assertion object.
	 * @throws NullPointerException if the given argument is {@code null}.
	 * @throws IllegalArgumentException if the given argument is an empty array.
	 * @throws NullPointerException if any of the entries in the given array is {@code null}.
	 * @throws AssertionError if the actual map is {@code null}.
	 * @throws AssertionError if the actual map does not contain the given entries.
	 */
	public S contains(MapEntry... entries) {
		maps.assertContains(info, actual, entries);
		return myself;
	}

  /**
   * Verifies that the actual map contains the given entry.
   * <p>
   * Example :
   * 
   * <pre>
   * Map<Ring, TolkienCharacter> ringBearers = ... // init omitted
   * assertThat(ringBearers).containsEntry(oneRing, frodo).containsEntry(nenya, galadriel);
   * </pre>
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
  public S containsEntry(K key, V value) {
    maps.assertContains(info, actual, array(entry(key, value)));
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given entries.
   * <p>
   * Example :
   * 
   * <pre>
   * Map<Ring, TolkienCharacter> ringBearers = ... // init omitted
   * assertThat(ringBearers).doesNotContain(entry(oneRing, aragorn));
   * </pre>
   * 
   * @param entries the given entries.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   */
  public S doesNotContain(MapEntry... entries) {
    maps.assertDoesNotContain(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given entry.
   * <p>
   * Example :
   * 
   * <pre>
   * Map<Ring, TolkienCharacter> ringBearers = ... // init omitted
   * assertThat(ringBearers).doesNotContainEntry(oneRing, aragorn);
   * </pre>
   * 
   * @param key key of the entry.
   * @param value value of the entry.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   */
  public S doesNotContainEntry(K key, V value) {
    maps.assertDoesNotContain(info, actual, array(entry(key, value)));
    return myself;
  }

  /**
   * Verifies that the actual map contains the given key.
   * 
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given key.
   */
  @SuppressWarnings("unchecked")
  public S containsKey(K key) {
    return containsKeys(key);
  }

  /**
   * Verifies that the actual map contains the given keys.
   * 
   * @param keys the given keys
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given key.
   * @throws IllegalArgumentException if the given argument is an empty array.
   */
  public S containsKeys(K... keys) {
    maps.assertContainsKeys(info, actual, keys);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given key.
   * 
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given key.
   */
  public S doesNotContainKey(K key) {
    maps.assertDoesNotContainKey(info, actual, key);
    return myself;
  }

  /**
   * Verifies that the actual map contains the given value.
   * 
   * @param value the value to look for.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given value.
   */
  public S containsValue(V value) {
    maps.assertContainsValue(info, actual, value);
    return myself;
  }

  /**
   * Verifies that the actual map does not contain the given value.
   * 
   * @param value the value that should not be in actual map.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given value.
   */
  public S doesNotContainValue(V value) {
    maps.assertDoesNotContainValue(info, actual, value);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given entries and nothing else, in any order.
   * 
   * <p>
   * Examples :
   * 
   * <pre>
   * Map<Ring, TolkienCharacter> ringBearers = ... // init omitted
   * 
   * // assertion will pass
   * assertThat(ringBearers).containsOnly(entry(oneRing, frodo), entry(nenya, galadriel), entry(narya, gandalf), entry(vilya, elrond));
   * 
   * // assertion will fail
   * assertThat(ringBearers).containsOnly(entry(oneRing, frodo), entry(nenya, galadriel));
   * </pre>
   * 
   * @param entries the entries that should be in the actual map.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map does not contain the given entries, i.e. the actual map contains some or
   *           none of the given entries, or the actual map contains more entries than the given ones.
   */
  public S containsOnly(MapEntry... entries) {
    maps.assertContainsOnly(info, actual, entries);
    return myself;
  }

  /**
   * Verifies that the actual map contains only the given entries and nothing else, <b>in order</b>.<br>
   * This assertion should only be used with map that have a consistent iteration order (i.e. don't use it with
   * {@link java.util.HashMap}, prefer {@link #containsOnly(org.assertj.core.data.MapEntry...)} in that case).
   * <p>
   * Example :
   * 
   * <pre>
   * Map&lt;Ring, TolkienCharacter&gt; ringBearers = newLinkedHashMap(entry(oneRing, frodo), entry(nenya, galadriel),
   *     entry(narya, gandalf));
   * 
   * // assertion will pass
   * assertThat(ringBearers).containsExactly(entry(oneRing, frodo), entry(nenya, galadriel), entry(narya, gandalf));
   * 
   * // assertion will fail as actual and expected orders differ.
   * assertThat(ringBearers).containsExactly(entry(nenya, galadriel), entry(narya, gandalf), entry(oneRing, frodo));
   * </pre>
   * 
   * @param entries the given entries.
   * @throws NullPointerException if the given entries array is {@code null}.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws IllegalArgumentException if the given entries array is empty.
   * @throws AssertionError if the actual map does not contain the given entries with same order, i.e. the actual map
   *           contains some or none of the given entries, or the actual map contains more entries than the given ones
   *           or entries are the same but the order is not.
   */
  public S containsExactly(MapEntry... entries) {
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
  public final S usingElementComparator(Comparator<? super MapEntry> customComparator) {
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
  public final S usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }
}
