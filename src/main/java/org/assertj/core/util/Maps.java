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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.assertj.core.presentation.Representation;
import org.assertj.core.presentation.StandardRepresentation;

/**
 * Utility methods related to maps.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author gabga
 */
public class Maps {

  /**
   * Returns the {@code String} {@link org.assertj.core.presentation.StandardRepresentation standard representation} of
   * the given map, or {@code null} if the given map is {@code null}.
   * 
   * @param map the map to format.
   * @return the {@code String} representation of the given map.
   */
  public static String format(Map<?, ?> map) {
    return format(new StandardRepresentation(), map);
  }

  /**
   * Returns the {@code String} representation of the given map, or {@code null} if the given map is {@code null}.
   *
   * @param map the map to format.
   * @return the {@code String} representation of the given map.
   */
  public static String format(Representation p, Map<?, ?> map) {
    if (map == null) return null;
    Map<?, ?> sortedMap = toSortedMapIfPossible(map);
    Iterator<?> i = sortedMap.entrySet().iterator();
    if (!i.hasNext()) return "{}";
    StringBuilder builder = new StringBuilder("{");
    for (;;) {
      Entry<?, ?> entry = (Entry<?, ?>) i.next();
      builder.append(format(map, entry.getKey(), p)).append('=').append(format(map, entry.getValue(), p));
      if (!i.hasNext()) return builder.append("}").toString();
      builder.append(", ");
    }
  }

  private static Map<?, ?> toSortedMapIfPossible(Map<?, ?> map) {
    try {
      return new TreeMap<>(map);
    } catch (ClassCastException | NullPointerException e) {
      return map;
    }
  }

  private static Object format(Map<?, ?> map, Object o, Representation p) {
    return o == map ? "(this Map)" : p.toStringOf(o);
  }

  private Maps() {}
}
