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
package org.assertj.core.api.recursive.comparison;

import java.util.Map;

// not using Entry because as a jdk type, it would be compared with equals and not recursively
// no need to parameterized KeyValue as the recursive comparison ignores type parameters.

/**
 * Replacing {@link java.util.Map.Entry} in the recursive comparison as the latter is not compared recursively
 * (java types are not by default). It is not parameterized as the recursive comparison ignores type parameters.
 *
 * @param key the key
 * @param value the value
 */
public record KeyValue(Object key, Object value) {

  // regular getters to be compatible with getter driven recursive comparison

  /**
   * Regular getters to be compatible with getter driven recursive comparison.
   *
   * @return the key
   */
  public Object getKey() {
    return key;
  }

  /**
   * Regular getters to be compatible with getter driven recursive comparison.
   *
   * @return the value
   */
  public Object getValue() {
    return value;
  }

  static KeyValue from(Map.Entry<?, ?> entry) {
    return new KeyValue(entry.getKey(), entry.getValue());
  }
}
