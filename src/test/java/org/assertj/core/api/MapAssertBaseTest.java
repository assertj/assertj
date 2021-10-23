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
package org.assertj.core.api;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static org.assertj.core.util.Maps.newHashMap;
import static org.mockito.Mockito.mock;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import org.assertj.core.internal.Maps;
<<<<<<< HEAD
import org.assertj.core.test.jdk11.Jdk11;
=======
>>>>>>> bfc61fb026ecb271a42f86e11867023c46b17304
import org.junit.jupiter.params.provider.Arguments;

/**
 * Base class for {@link MapAssert} tests.
 *
 * @author Olivier Michallat
 */
public abstract class MapAssertBaseTest extends BaseTestTemplate<MapAssert<Object, Object>, Map<Object, Object>> {
  protected Maps maps;

  @Override
  protected MapAssert<Object, Object> create_assertions() {
    return new MapAssert<>(emptyMap());
  }

  @Override
  protected void inject_internal_objects() {
    super.inject_internal_objects();
    maps = mock(Maps.class);
    assertions.maps = maps;
  }

  protected static <K, V> Map.Entry<K, V> javaMapEntry(K key, V value) {
    return new SimpleImmutableEntry<>(key, value);
  }

  protected static <K, V> Map<K, V> map(K key, V value) {
    return singletonMap(key, value);
  }

  protected static <K, V> Map<K, V> map(K k1, V v1, K k2, V v2) {
    Map<K, V> map = new LinkedHashMap<>();
    map.put(k1, v1);
    map.put(k2, v2);
    return map;
  }

  protected static Stream<Arguments> maps_without_null_keys_or_values() {
    Map<String, String> map = newHashMap("Whatever", "Don't care");
    return Stream.of(
                     Arguments.of(map),
                     Arguments.of(Jdk11.Map.of("Whatever", "Don't care")),
                     Arguments.of(concurrentMap()),
                     Arguments.of(Collections.unmodifiableMap(map)));
  }

  private static Map<String, String> concurrentMap() {
    Map<String, String> concurrentMap = new ConcurrentHashMap<>();
    concurrentMap.put("Whatever", "Don't care");
    return concurrentMap;
  }

}
