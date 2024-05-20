/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Comparator.naturalOrder;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.testkit.WithPlayerData;
import org.hibernate.collection.spi.CollectionSemantics;
import org.hibernate.collection.spi.PersistentMap;
import org.hibernate.collection.spi.PersistentSortedMap;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.util.LinkedCaseInsensitiveMap;

/**
 * Base class for {@link Maps} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Maps} attributes appropriately.
 * 
 * @author Joel Costigliola
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
                                                                                             LinkedHashMap::new,
                                                                                             MapsBaseTest::persistentMap,
                                                                                             MapsBaseTest::persistentSortedMap);

  private static <K, V> PersistentMap<K, V> persistentMap() {
    return hibernateMap(PersistentMap::new, HashMap::new);
  }

  private static <K extends Comparable<? super K>, V> PersistentSortedMap<K, V> persistentSortedMap() {
    return hibernateMap(session -> new PersistentSortedMap<K, V>(session, naturalOrder()), TreeMap::new);
  }

  private static <K, V, T extends PersistentMap<K, V>> T hibernateMap(Function<SharedSessionContractImplementor, T> supplier,
                                                                      Supplier<Map<K, V>> innerMapSupplier) {
    SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class, RETURNS_DEEP_STUBS);
    T persistentMap = supplier.apply(session);

    CollectionPersister persister = mock(CollectionPersister.class, RETURNS_DEEP_STUBS);
    when(getCollectionSemantics(persister).instantiateRaw(0, persister)).thenReturn(innerMapSupplier.get());

    persistentMap.initializeEmptyCollection(persister);
    return persistentMap;
  }

  @SuppressWarnings("unchecked")
  private static CollectionSemantics<Object, ?> getCollectionSemantics(CollectionPersister persister) {
    return (CollectionSemantics<Object, ?>) persister.getCollectionSemantics();
  }

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
