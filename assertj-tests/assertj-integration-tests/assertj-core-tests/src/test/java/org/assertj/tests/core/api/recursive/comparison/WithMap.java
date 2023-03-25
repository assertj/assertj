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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static java.lang.String.format;

import java.util.Map;

public class WithMap<K, V> {
  public Map<K, V> map;

  public WithMap(Map<K, V> map) {
    this.map = map;
  }

  public static <K, V> WithMap<K, V> withMap(Map<K, V> map) {
    return new WithMap<>(map);
  }

  @Override
  public String toString() {
    return format("WithMap map=r%s", map);
  }

}
