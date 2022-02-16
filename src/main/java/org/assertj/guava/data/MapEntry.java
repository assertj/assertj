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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.data;

import static org.assertj.core.util.Objects.HASH_CODE_PRIME;
import static org.assertj.core.util.Objects.areEqual;
import static org.assertj.core.util.Objects.hashCodeFor;
import static org.assertj.core.util.Strings.quote;

/**
 * This is generic version of {@link org.assertj.core.data.MapEntry}
 *
 * @param <K> key type
 * @param <V> value type
 *
 * @deprecated use {@link org.assertj.core.data.MapEntry org.assertj.core.data.MapEntry} instead.
 */
@Deprecated
public final class MapEntry<K, V> {
  public final K key;

  public final V value;

  /**
   * Creates a new {@link MapEntry}.
   *
   * @param <K> key type
   * @param <V> value type
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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    MapEntry<?, ?> other = (MapEntry<?, ?>) obj;
    return areEqual(key, other.key) && areEqual(value, other.value);
  }

  @Override
  public int hashCode() {
    int result = 1;
    result = HASH_CODE_PRIME * result + hashCodeFor(key);
    result = HASH_CODE_PRIME * result + hashCodeFor(value);
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s[key=%s, value=%s]", getClass().getSimpleName(), quote(key), quote(value));
  }
}
