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
import static org.assertj.core.util.Arrays.array;

import java.util.Comparator;
import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Maps;
import org.assertj.core.util.VisibleForTesting;

/**
 * Assertions for <code>{@link Map}</code>s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Map)}</code>.
 * </p>
 * 
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class MapAssert<K, V> extends AbstractAssert<MapAssert<K, V>, Map<K, V>> implements
    EnumerableAssert<MapAssert<K, V>, MapEntry> {

  @VisibleForTesting
  Maps maps = Maps.instance();

  protected MapAssert(Map<K, V> actual) {
    super(actual, MapAssert.class);
  }

  /** {@inheritDoc} */
  public void isNullOrEmpty() {
    maps.assertNullOrEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public void isEmpty() {
    maps.assertEmpty(info, actual);
  }

  /** {@inheritDoc} */
  public MapAssert<K, V> isNotEmpty() {
    maps.assertNotEmpty(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public MapAssert<K, V> hasSize(int expected) {
    maps.assertHasSize(info, actual, expected);
    return this;
  }

  /** {@inheritDoc} */
  public MapAssert<K, V> hasSameSizeAs(Object[] other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return this;
  }

  /** {@inheritDoc} */
  public MapAssert<K, V> hasSameSizeAs(Iterable<?> other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return this;
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
  public MapAssert<K, V> contains(MapEntry... entries) {
    maps.assertContains(info, actual, entries);
    return this;
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
  public MapAssert<K, V> containsEntry(K key, V value) {
    maps.assertContains(info, actual, array(entry(key, value)));
    return this;
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
  public MapAssert<K, V> doesNotContain(MapEntry... entries) {
    maps.assertDoesNotContain(info, actual, entries);
    return this;
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
   * @param entries the given entries.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   */
  public MapAssert<K, V> doesNotContainEntry(K key, V value) {
    maps.assertDoesNotContain(info, actual, array(entry(key, value)));
    return this;
  }

  /**
   * Verifies that the actual map contains the given key.
   * 
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given key.
   */
  @SuppressWarnings("unchecked")
  public MapAssert<K, V> containsKey(K key) {
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
  public MapAssert<K, V> containsKeys(K... keys) {
    maps.assertContainsKeys(info, actual, keys);
    return this;
  }

  /**
   * Verifies that the actual map does not contain the given key.
   * 
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given key.
   */
  public MapAssert<K, V> doesNotContainKey(K key) {
    maps.assertDoesNotContainKey(info, actual, key);
    return this;
  }

  /**
   * Verifies that the actual map contains the given value.
   * 
   * @param value the value to look for.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given value.
   */
  public MapAssert<K, V> containsValue(V value) {
    maps.assertContainsValue(info, actual, value);
    return this;
  }

  /**
   * Verifies that the actual map does not contain the given value.
   * 
   * @param value the value that should not be in actual map.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given value.
   */
  public MapAssert<K, V> doesNotContainValue(V value) {
    maps.assertDoesNotContainValue(info, actual, value);
    return this;
  }

  /**
   * Do not use this method.
   * 
   * @deprecated Custom element Comparator is not supported for MapEntry comparison.
   * @throws UnsupportedOperationException if this method is called.
   */
  @Override
  @Deprecated
  public final MapAssert<K, V> usingElementComparator(Comparator<? super MapEntry> customComparator) {
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
  public final MapAssert<K, V> usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }
}
