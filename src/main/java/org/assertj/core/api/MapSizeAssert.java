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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.api;

import java.util.Map;

public class MapSizeAssert<K, V> extends AbstractMapSizeAssert<MapAssert<K, V>, Map<K, V>, K, V> {

  private AbstractMapAssert<MapAssert<K, V>, Map<K, V>, K, V> source;

  public MapSizeAssert(AbstractMapAssert<MapAssert<K, V>, Map<K, V>, K, V> source, Integer i) {
    super(i, MapSizeAssert.class);
    this.source = source;
  }

  @Override
  public AbstractMapAssert<MapAssert<K, V>, Map<K, V>, K, V> returnToMap() {
    return source;
  }
}
