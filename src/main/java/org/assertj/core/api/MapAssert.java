/*
 * Created on Dec 21, 2010
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
package org.assertj.core.api;

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
public class MapAssert<K, V, S extends MapAssert<K,V,?,?>, A extends Map<K,V>> extends AbstractAssert<S,A> implements
    EnumerableAssert<S, MapEntry> {

  @VisibleForTesting
  Maps maps = Maps.instance();

  protected MapAssert(A actual) {
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
  public S isNotEmpty() {
    maps.assertNotEmpty(info, actual);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S hasSize(int expected) {
    maps.assertHasSize(info, actual, expected);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S hasSameSizeAs(Object[] other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S hasSameSizeAs(Iterable<?> other) {
    maps.assertHasSameSizeAs(info, actual, other);
    return (S) this;
  }

  /**
   * Verifies that the actual map contains the given entries, in any order.
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
    return (S) this;
  }

  /**
   * Verifies that the actual map does not contain the given entries.
   * @param entries the given entries.
   * @return {@code this} assertion object.
   * @throws NullPointerException if the given argument is {@code null}.
   * @throws IllegalArgumentException if the given argument is an empty array.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains any of the given entries.
   */
  public S doesNotContain(MapEntry... entries) {
    maps.assertDoesNotContain(info, actual, entries);
    return (S) this;
  }

  /**
   * Verifies that the actual map contains the given key.
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given key.
   */
  public S containsKey(K key) {
    maps.assertContainsKey(info, actual, key);
    return (S) this;
  }

  /**
   * Verifies that the actual map does not contain the given key.
   * @param key the given key
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given key.
   */
  public S doesNotContainKey(K key) {
    maps.assertDoesNotContainKey(info, actual, key);
    return (S) this;
  }

  /**
   * Verifies that the actual map contains the given value.
   * @param value the value to look for.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map does not contain the given value.
   */
  public S containsValue(V value) {
    maps.assertContainsValue(info, actual, value);
    return (S) this;
  }

  /**
   * Verifies that the actual map does not contain the given value.
   * @param value the value that should not be in actual map.
   * @throws AssertionError if the actual map is {@code null}.
   * @throws AssertionError if the actual map contains the given value.
   */
  public S doesNotContainValue(V value) {
    maps.assertDoesNotContainValue(info, actual, value);
    return (S) this;
  }

  /** {@inheritDoc} */
  public S usingElementComparator(Comparator<? super MapEntry> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }

  /** {@inheritDoc} */
  public S usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }
}
