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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.configuration.ConfigurationProvider.CONFIGURATION_PROVIDER;

import java.util.HashMap;
import java.util.Map;

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
   * 
   * @deprecated use {@link StandardRepresentation#toStringOf(Map)} instead.
   */
  @Deprecated
  public static String format(Map<?, ?> map) {
    return CONFIGURATION_PROVIDER.representation().toStringOf(map);
  }

  /**
   * Returns the {@code String} representation of the given map, or {@code null} if the given map is {@code null}.
   *
   * @param p the {@link Representation} to use.
   * @param map the map to format.
   * @return the {@code String} representation of the given map.
   * 
   * @deprecated use {@link StandardRepresentation#toStringOf(Map)} instead.
   */
  @Deprecated
  public static String format(Representation p, Map<?, ?> map) {
    return CONFIGURATION_PROVIDER.representation().toStringOf(map);
  }

  public static <K, V> Map<K, V> newHashMap(K key, V value) {
      Map<K, V> map = new HashMap<>();
      map.put(key, value);
      return map;
  }
  
  private Maps() {}
}
