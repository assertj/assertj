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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.data;

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

import java.util.Map;
import java.util.Objects;

/**
 * Understands an entry in a <code>{@link Map}</code>.
 *
 * @param <K> the type of the key of this entry.
 * @param <V> the type of the value of this entry.
 *
 * @author Yvonne Wang
 */
public class MapEntry<K, V> implements Map.Entry<K, V> {

  public final K key;
  public final V value;

  /**
   * Creates a new {@link MapEntry}.
   *
   * @param <K> the type of the key of this entry.
   * @param <V> the type of the value of this entry.
   * @param key the key of the entry to create.
   * @param value the value of the entry to create.
   * @return the created {@code MapEntry}.
   */
  public static <K, V> MapEntry<K, V> entry(K key, V value) {
    return new MapEntry<>(key, value);
  }

  private MapEntry(K key, V value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public boolean equals(Object object) {
    if (!(object instanceof Map.Entry)) return false;
    Map.Entry<?, ?> that = (Map.Entry<?, ?>) object;
    return Objects.equals(this.getKey(), that.getKey())
           && Objects.equals(this.getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getKey()) ^ Objects.hashCode(getValue());
  }

  @Override
  public String toString() {
    return CONFIGURATION_PROVIDER.representation().toStringOf(this);
  }

  @Override
  public K getKey() {
    return key;
  }

  @Override
  public V getValue() {
    return value;
  }

  /**
   * Always throws <tt>UnsupportedOperationException</tt>,
   * as this class represents an <i>immutable</i> map entry.
   *
   * @param value ignored
   * @return (Does not return)
   * @throws UnsupportedOperationException always
   */
  @Override
  public V setValue(V value) {
    throw new UnsupportedOperationException();
  }
}
