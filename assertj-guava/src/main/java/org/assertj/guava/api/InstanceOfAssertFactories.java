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
package org.assertj.guava.api;

import org.assertj.core.api.InstanceOfAssertFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multiset;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.RangeSet;
import com.google.common.collect.Table;
import com.google.common.io.ByteSource;

/**
 * {@link InstanceOfAssertFactory} instances for Guava types.
 *
 * @author Stefano Cordio
 * @see org.assertj.core.api.InstanceOfAssertFactories
 * @since 3.3.0
 */
public interface InstanceOfAssertFactories {

  /**
   * {@link InstanceOfAssertFactory} for a {@link ByteSource}.
   */
  InstanceOfAssertFactory<ByteSource, ByteSourceAssert> BYTE_SOURCE = new InstanceOfAssertFactory<>(ByteSource.class,
                                                                                                    Assertions::assertThat);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Multimap}, assuming {@code Object} as key and value types.
   *
   * @see #multimap(Class, Class)
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Multimap, MultimapAssert<Object, Object>> MULTIMAP = multimap(Object.class, Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Multimap}.
   *
   * @param <K>       the {@code Multimap} key type.
   * @param <V>       the {@code Multimap} value type.
   * @param keyType   the key type instance.
   * @param valueType the value type instance.
   * @return the factory instance.
   *
   * @see #MULTIMAP
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <K, V> InstanceOfAssertFactory<Multimap, MultimapAssert<K, V>> multimap(Class<K> keyType, Class<V> valueType) {
    return new InstanceOfAssertFactory<>(Multimap.class, Assertions::<K, V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for an {@link Optional}, assuming {@code Object} as value type.
   *
   * @see #optional(Class)
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Optional, OptionalAssert<Object>> OPTIONAL = optional(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for an {@link Optional}.
   *
   * @param <VALUE>    the {@code Optional} value type.
   * @param resultType the value type instance.
   * @return the factory instance.
   *
   * @see #OPTIONAL
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<Optional, OptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
    return new InstanceOfAssertFactory<>(Optional.class, Assertions::<VALUE> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Range}.
   *
   * @param <C>            the {@code Comparable} type.
   * @param comparableType the comparable type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <C extends Comparable<C>> InstanceOfAssertFactory<Range, RangeAssert<C>> range(Class<C> comparableType) {
    return new InstanceOfAssertFactory<>(Range.class, Assertions::<C> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link RangeMap}.
   *
   * @param <K>       the {@code RangeMap} key type.
   * @param <V>       the {@code RangeMap} value type.
   * @param keyType   the key type instance.
   * @param valueType the value type instance.
   * @return the factory instance.
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <K extends Comparable<K>, V> InstanceOfAssertFactory<RangeMap, RangeMapAssert<K, V>> rangeMap(Class<K> keyType,
                                                                                                       Class<V> valueType) {
    return new InstanceOfAssertFactory<>(RangeMap.class, Assertions::<K, V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link RangeSet}.
   *
   * @param comparableType the comparable type instance.
   * @param <T> the {@code Comparable} type.
   * @return the factory instance
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <T extends Comparable<T>> InstanceOfAssertFactory<RangeSet, RangeSetAssert<T>> rangeSet(Class<T> comparableType) {
    return new InstanceOfAssertFactory<>(RangeSet.class, Assertions::<T> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Table}, assuming {@code Object} as row key type, column key type and
   * value type.
   *
   * @see #table(Class, Class, Class)
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Table, TableAssert<Object, Object, Object>> TABLE = table(Object.class, Object.class, Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Table}.
   *
   * @param <R>           the {@code Table} row key type.
   * @param <C>           the {@code Table} column key type.
   * @param <V>           the {@code Table} value type.
   * @param rowKeyType    the row key type instance.
   * @param columnKeyType the column key type instance.
   * @param valueType     the value type instance.
   * @return the factory instance.
   *
   * @see #TABLE
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <R, C, V> InstanceOfAssertFactory<Table, TableAssert<R, C, V>> table(Class<R> rowKeyType, Class<C> columnKeyType,
                                                                              Class<V> valueType) {
    return new InstanceOfAssertFactory<>(Table.class, Assertions::<R, C, V> assertThat);
  }

  /**
   * {@link InstanceOfAssertFactory} for a {@link Multiset}, assuming {@code Object} as element type.
   *
   * @see #multiset(Class)
   */
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Multiset, MultisetAssert<Object>> MULTISET = multiset(Object.class);

  /**
   * {@link InstanceOfAssertFactory} for a {@link Multiset}.
   *
   * @param <ELEMENT>   the {@code Multiset} element type.
   * @param elementType the element type instance.
   * @return the factory instance.
   *
   * @see #MULTISET
   */
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Multiset, MultisetAssert<ELEMENT>> multiset(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Multiset.class, Assertions::<ELEMENT> assertThat);
  }

}
