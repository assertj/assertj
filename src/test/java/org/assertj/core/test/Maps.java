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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.test;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.assertj.core.data.MapEntry;

/**
 * @author Alex Ruiz
 */
public final class Maps {

  @SafeVarargs
  public static <K, V> LinkedHashMap<K, V> mapOf(MapEntry<K, V>... entries) {
    return mapOf(LinkedHashMap::new, entries);
  }

  @SafeVarargs
  public static <K extends Comparable<? super K>, V> TreeMap<K, V> treeMapOf(MapEntry<K, V>... entries) {
    return mapOf(TreeMap::new, entries);
  }

  @SafeVarargs
  public static <K, V, M extends Map<K, V>> M mapOf(Supplier<M> supplier, Map.Entry<K, V>... entries) {
    M map = supplier.get();
    for (Map.Entry<K, V> entry : entries) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }

  private Maps() {}

}
