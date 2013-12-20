/*
 * Created on Jan 25, 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2008-2012 the original author or authors.
 */
package org.assertj.core.util;

import org.assertj.core.presentation.Representation;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Utility methods related to maps.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Maps {
  /**
   * Indicates whether the given {@code Map} is {@code null} or empty.
   * 
   * @param map the map to check.
   * @return {@code true} if the given {@code Map} is {@code null} or empty, otherwise {@code false}.
   */
  public static boolean isNullOrEmpty(Map<?, ?> map) {
    return map == null || map.isEmpty();
  }

  /**
   * Returns the {@code String} representation of the given map, or {@code null} if the given map is {@code null}.
   * 
   * @param map the map to format.
   * @return the {@code String} representation of the given map.
   */
  public static String format(Representation p, Map<?, ?> map) {
    if (map == null) {
      return null;
    }
    Iterator<?> i = map.entrySet().iterator();
    if (!i.hasNext()) {
      return "{}";
    }
    StringBuilder buffer = new StringBuilder();
    buffer.append("{");
    for (;;) {
      Entry<?, ?> e = (Entry<?, ?>) i.next();
      buffer.append(format(map, e.getKey(), p));
      buffer.append('=');
      buffer.append(format(map, e.getValue(), p));
      if (!i.hasNext()) {
        return buffer.append("}").toString();
      }
      buffer.append(", ");
    }
  }

  private static Object format(Map<?, ?> map, Object o, Representation p) {
    return o == map ? "(this Map)" : p.toStringOf(o);
  }

  private Maps() {}
}
