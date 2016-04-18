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

public abstract class AbstractMapSizeAssert<S extends AbstractMapAssert<S, A, K, V>, A extends Map<K, V>, K, V>
    extends AbstractIntegerAssert<AbstractMapSizeAssert<S, A, K, V>> {

  protected AbstractMapSizeAssert(Integer actual, Class<?> selfType) {
    super(actual, selfType);
  }

  public abstract AbstractMapAssert<S, A, K, V> returnToMap();
}
