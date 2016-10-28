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

/**
 * Assertions for {@link Map}s.
 * <p>
 * To create a new instance of this class, invoke <code>{@link Assertions#assertThat(Map)}</code>.
 * </p>
 * 
 * @author David DIDIER
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Mikhail Mazursky
 * @author Nicolas François
 */
public class MapAssert<K, V> extends AbstractMapAssert<MapAssert<K, V>, Map<K, V>, K, V> {

  public MapAssert(Map<K, V> actual) {
    super(actual, MapAssert.class);
  }
  
  // override methods to annotate them with @SafeVarargs, we unfortunately can't do that in AbstractMapAssert as it is
  // used in soft assertions which need to be able to proxy method - @SafeVarargs requiring method to be final prevents
  // using proxies.

  @SafeVarargs
  @Override
  public final MapAssert<K, V> contains(Map.Entry<? extends K, ? extends V>... entries) {
    return super.contains(entries);
  }

  @SafeVarargs
  @Override
  public final MapAssert<K, V> containsAnyOf(Map.Entry<? extends K, ? extends V>... entries) {
    return super.containsAnyOf(entries);
  }
  
  @SafeVarargs
  @Override
  public final MapAssert<K, V> containsOnly(Map.Entry<? extends K, ? extends V>... entries) {
    return super.containsOnly(entries);
  }
  
  @SafeVarargs
  @Override
  public final MapAssert<K, V> containsExactly(Map.Entry<? extends K, ? extends V>... entries) {
    return super.containsExactly(entries);
  }
  
  @SafeVarargs
  @Override
  public final MapAssert<K, V> containsKeys(K... keys) {
    return super.containsKeys(keys);
  }

  @SafeVarargs
  @Override
  public final MapAssert<K, V> containsOnlyKeys(K... keys) {
    return super.containsOnlyKeys(keys);
  }

  @SafeVarargs
  @Override
  public final MapAssert<K, V> containsValues(V... values) {
    return super.containsValues(values);
  }

  @SafeVarargs
  @Override
  public final MapAssert<K, V> doesNotContainKeys(K... keys) {
    return super.doesNotContainKeys(keys);
  }
  
  @SafeVarargs
  @Override
  public final MapAssert<K, V> doesNotContain(Map.Entry<? extends K, ? extends V>... entries) {
    return super.doesNotContain(entries);
  }
}
