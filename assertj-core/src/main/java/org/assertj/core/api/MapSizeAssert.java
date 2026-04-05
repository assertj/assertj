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

import org.assertj.core.annotation.CheckReturnValue;

import java.util.Map;

public class MapSizeAssert<KEY, VALUE> extends AbstractMapSizeAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> {

  public MapSizeAssert(AbstractMapAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> originAssert) {
    super(originAssert);
  }

  /**
   * @deprecated use {@link #MapSizeAssert(AbstractMapAssert)} instead.
   */
  @Deprecated
  public MapSizeAssert(AbstractMapAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> originAssert,
                       @SuppressWarnings("unused") Integer size) {
    this(originAssert);
  }

  @Override
  @CheckReturnValue
  public AbstractMapAssert<MapAssert<KEY, VALUE>, Map<KEY, VALUE>, KEY, VALUE> returnToMap() {
    return super.returnToMap();
  }

}
