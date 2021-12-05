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
package org.assertj.core.internal;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.test.WithPlayerData;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * Base class for {@link Maps} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Maps} attributes appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class MapsBaseTest extends WithPlayerData {

  private static final Supplier<Map<String, String>> CASE_INSENSITIVE_TREE_MAP = () -> new TreeMap<>(CASE_INSENSITIVE_ORDER);

  @SuppressWarnings("unchecked")
  protected static final Supplier<Map<String, String>>[] CASE_INSENSITIVE_MAPS = new Supplier[] {
      // org.apache.commons.collections4.map.CaseInsensitiveMap not included due to slightly different behavior
      LinkedCaseInsensitiveMap::new,
      CASE_INSENSITIVE_TREE_MAP
  };

  @SuppressWarnings("unchecked")
  protected static final Supplier<Map<String, String>>[] MODIFIABLE_MAPS = ArrayUtils.addAll(CASE_INSENSITIVE_MAPS,
                                                                                             CaseInsensitiveMap::new,
                                                                                             HashMap::new,
                                                                                             IdentityHashMap::new,
                                                                                             LinkedHashMap::new);

  protected Map<String, String> actual;
  protected Failures failures;
  protected Maps maps;

  protected AssertionInfo info;

  @BeforeEach
  protected void setUp() {
    actual = mapOf(entry("name", "Yoda"), entry("color", "green"));
    failures = spy(new Failures());
    maps = new Maps();
    maps.failures = failures;
    info = someInfo();
  }

  @SuppressWarnings("unchecked")
  protected static <K, V> MapEntry<K, V>[] emptyEntries() {
    return new MapEntry[0];
  }

  protected static String[] emptyKeys() {
    return new String[0];
  }

}
