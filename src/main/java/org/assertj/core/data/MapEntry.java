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
package org.assertj.core.data;

import static org.assertj.core.util.Objects.*;
import static org.assertj.core.presentation.DefaultToString.toStringOf;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.util.Map;

/**
 * Understands an entry in a <code>{@link Map}</code>.
 *
 * @author Yvonne Wang
 */
public class MapEntry<K, V> {
  
  public final K key;
  public final V value;

  /**
   * Creates a new {@link MapEntry}.
   *
   * @param key the key of the entry to create.
   * @param value the value of the entry to create.
   * @return the created {@code MapEntry}.
   */
  public static <K,V> MapEntry<K, V> entry(K key, V value) {
    return new MapEntry<>(key, value);
  }

  private MapEntry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true; 
    if (!(obj instanceof MapEntry)) return false;
    @SuppressWarnings("rawtypes")
    MapEntry other = (MapEntry) obj;
    return areEqual(key, other.key) && areEqual(value, other.value);
  }

  @Override
  public int hashCode() {
    return hashCodeFor(key) ^ hashCodeFor(value);
  }

  @Override
  public String toString() {
    return toStringOf(this, STANDARD_REPRESENTATION);
  }
}
