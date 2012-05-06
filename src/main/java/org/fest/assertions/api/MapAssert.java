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
package org.fest.assertions.api;

import java.util.Comparator;
import java.util.Map;

import org.fest.assertions.core.EnumerableAssert;
import org.fest.assertions.data.MapEntry;
import org.fest.assertions.internal.Maps;
import org.fest.util.VisibleForTesting;

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
 */
public class MapAssert extends AbstractAssert<MapAssert, Map<?, ?>> implements EnumerableAssert<MapAssert, MapEntry> {

  @VisibleForTesting Maps maps = Maps.instance();

  protected MapAssert(Map<?, ?> actual) {
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
  public MapAssert isNotEmpty() {
    maps.assertNotEmpty(info, actual);
    return this;
  }

  /** {@inheritDoc} */
  public MapAssert hasSize(int expected) {
    maps.assertHasSize(info, actual, expected);
    return this;
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
  public MapAssert contains(MapEntry...entries) {
    maps.assertContains(info, actual, entries);
    return this;
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
  public MapAssert doesNotContain(MapEntry...entries) {
    maps.assertDoesNotContain(info, actual, entries);
    return this;
  }

  /** {@inheritDoc} */
  public MapAssert usingElementComparator(Comparator<? super MapEntry> customComparator) {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }

  /** {@inheritDoc} */
  public MapAssert usingDefaultElementComparator() {
    throw new UnsupportedOperationException("custom element Comparator is not supported for MapEntry comparison");
  }
}
