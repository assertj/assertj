/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

/// [InstanceOfAssertFactory] instances for Guava types.
///
/// @author Stefano Cordio
/// @since 3.3.0
/// @see org.assertj.core.api.InstanceOfAssertFactories
public interface InstanceOfAssertFactories {

  /// [InstanceOfAssertFactory] for a [ByteSource].
  InstanceOfAssertFactory<ByteSource, ByteSourceAssert> BYTE_SOURCE = new InstanceOfAssertFactory<>(ByteSource.class,
                                                                                                    Assertions::assertThat);

  /// [InstanceOfAssertFactory] for a [Multimap], assuming `Object` as key and value types.
  ///
  /// @see #multimap(Class, Class)
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Multimap, MultimapAssert<Object, Object>> MULTIMAP = multimap(Object.class, Object.class);

  /// [InstanceOfAssertFactory] for a [Multimap].
  ///
  /// @param <K>       the `Multimap` key type.
  /// @param <V>       the `Multimap` value type.
  /// @param keyType   the key type instance.
  /// @param valueType the value type instance.
  /// @return the factory instance.
  ///
  /// @see #MULTIMAP
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <K, V> InstanceOfAssertFactory<Multimap, MultimapAssert<K, V>> multimap(Class<K> keyType, Class<V> valueType) {
    return new InstanceOfAssertFactory<>(Multimap.class, Assertions::<K, V> assertThat);
  }

  /// [InstanceOfAssertFactory] for an [Optional], assuming `Object` as value type.
  ///
  /// @see #optional(Class)
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Optional, OptionalAssert<Object>> OPTIONAL = optional(Object.class);

  /// [InstanceOfAssertFactory] for an [Optional].
  ///
  /// @param <VALUE>    the `Optional` value type.
  /// @param resultType the value type instance.
  /// @return the factory instance.
  ///
  /// @see #OPTIONAL
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <VALUE> InstanceOfAssertFactory<Optional, OptionalAssert<VALUE>> optional(Class<VALUE> resultType) {
    return new InstanceOfAssertFactory<>(Optional.class, Assertions::<VALUE> assertThat);
  }

  /// [InstanceOfAssertFactory] for a [Range].
  ///
  /// @param <C>            the `Comparable` type.
  /// @param comparableType the comparable type instance.
  /// @return the factory instance.
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <C extends Comparable<C>> InstanceOfAssertFactory<Range, RangeAssert<C>> range(Class<C> comparableType) {
    return new InstanceOfAssertFactory<>(Range.class, Assertions::<C> assertThat);
  }

  /// [InstanceOfAssertFactory] for a [RangeMap].
  ///
  /// @param <K>       the `RangeMap` key type.
  /// @param <V>       the `RangeMap` value type.
  /// @param keyType   the key type instance.
  /// @param valueType the value type instance.
  /// @return the factory instance.
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <K extends Comparable<K>, V> InstanceOfAssertFactory<RangeMap, RangeMapAssert<K, V>> rangeMap(Class<K> keyType,
                                                                                                       Class<V> valueType) {
    return new InstanceOfAssertFactory<>(RangeMap.class, Assertions::<K, V> assertThat);
  }

  /// [InstanceOfAssertFactory] for a [RangeSet].
  ///
  /// @param comparableType the comparable type instance.
  /// @param <T> the `Comparable` type.
  /// @return the factory instance
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <T extends Comparable<T>> InstanceOfAssertFactory<RangeSet, RangeSetAssert<T>> rangeSet(Class<T> comparableType) {
    return new InstanceOfAssertFactory<>(RangeSet.class, Assertions::<T> assertThat);
  }

  /// [InstanceOfAssertFactory] for a [Table], assuming `Object` as row key type, column key type and
  /// value type.
  ///
  /// @see #table(Class, Class, Class)
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Table, TableAssert<Object, Object, Object>> TABLE = table(Object.class, Object.class, Object.class);

  /// [InstanceOfAssertFactory] for a [Table].
  ///
  /// @param <R>           the `Table` row key type.
  /// @param <C>           the `Table` column key type.
  /// @param <V>           the `Table` value type.
  /// @param rowKeyType    the row key type instance.
  /// @param columnKeyType the column key type instance.
  /// @param valueType     the value type instance.
  /// @return the factory instance.
  ///
  /// @see #TABLE
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <R, C, V> InstanceOfAssertFactory<Table, TableAssert<R, C, V>> table(Class<R> rowKeyType, Class<C> columnKeyType,
                                                                              Class<V> valueType) {
    return new InstanceOfAssertFactory<>(Table.class, Assertions::<R, C, V> assertThat);
  }

  /// [InstanceOfAssertFactory] for a [Multiset], assuming `Object` as element type.
  ///
  /// @see #multiset(Class)
  @SuppressWarnings("rawtypes") // rawtypes: using Class instance
  InstanceOfAssertFactory<Multiset, MultisetAssert<Object>> MULTISET = multiset(Object.class);

  /// [InstanceOfAssertFactory] for a [Multiset].
  ///
  /// @param <ELEMENT>   the `Multiset` element type.
  /// @param elementType the element type instance.
  /// @return the factory instance.
  ///
  /// @see #MULTISET
  @SuppressWarnings({ "rawtypes", "unused" }) // rawtypes: using Class instance, unused: parameter needed for type inference
  static <ELEMENT> InstanceOfAssertFactory<Multiset, MultisetAssert<ELEMENT>> multiset(Class<ELEMENT> elementType) {
    return new InstanceOfAssertFactory<>(Multiset.class, Assertions::<ELEMENT> assertThat);
  }

}
