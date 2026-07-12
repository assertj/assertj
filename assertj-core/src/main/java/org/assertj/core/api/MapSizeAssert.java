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

import org.assertj.core.annotation.CheckReturnValue;

/**
 * Assertions for the size of a map.
 *
 * @param <KEY> the map key type
 * @param <VALUE> the map value type
 */
public class MapSizeAssert<KEY, VALUE> extends AbstractMapSizeAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> {

  /**
   * Creates a map size assertion.
   *
   * @param originAssert the origin map assertion
   */
  public MapSizeAssert(AbstractMapAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> originAssert) {
    super(originAssert);
  }

  /**
   * Creates a map size assertion with a known size.
   *
   * @param originAssert the origin map assertion
   * @param size the known map size
   */
  protected MapSizeAssert(AbstractMapAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> originAssert,
                          @SuppressWarnings("unused") Integer size) {
    super(originAssert, size);
  }

  @Override
  @CheckReturnValue
  public AbstractMapAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> returnToMap() {
    return super.returnToMap();
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  static MapSizeAssert nullMapSizeAssert(AbstractMapAssert originAssert) {
    return new MapSizeAssert(originAssert, null);
  }

}
