/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import java.util.Map;

/**
 * Assertions for {@link Map}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Map)}</code>.
 * </p>
 *
 * @param <KEY>>      the type of keys in the map.
 * @param <VALUE>>      the type of values in the map.
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class MapAssert<KEY, VALUE> extends AbstractMapAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> {

  /**
   * Creates a new map assertion.
   *
   * @param <K> the key type
   * @param <V> the value type
   * @param actual the actual map to verify
   * @return the created assertion
   */
  public static <K, V> MapAssert<K, V> assertThatMap(Map<K, V> actual) {
    return new MapAssert<>(actual);
  }

  /**
   * Creates a new map assertion.
   *
   * @param actual the actual map to verify
   */
  public MapAssert(Map<KEY, VALUE> actual) {
    super(actual, MapAssert.class);
  }

}
