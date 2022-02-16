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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.guava.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.guava.api.InstanceOfAssertFactories.BYTE_SOURCE;
import static org.assertj.guava.api.InstanceOfAssertFactories.MULTIMAP;
import static org.assertj.guava.api.InstanceOfAssertFactories.MULTISET;
import static org.assertj.guava.api.InstanceOfAssertFactories.OPTIONAL;
import static org.assertj.guava.api.InstanceOfAssertFactories.TABLE;
import static org.assertj.guava.api.InstanceOfAssertFactories.multimap;
import static org.assertj.guava.api.InstanceOfAssertFactories.multiset;
import static org.assertj.guava.api.InstanceOfAssertFactories.optional;
import static org.assertj.guava.api.InstanceOfAssertFactories.range;
import static org.assertj.guava.api.InstanceOfAssertFactories.rangeMap;
import static org.assertj.guava.api.InstanceOfAssertFactories.rangeSet;
import static org.assertj.guava.api.InstanceOfAssertFactories.table;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableRangeMap;
import com.google.common.collect.ImmutableRangeSet;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Range;
import com.google.common.io.ByteSource;

/**
 * @author Stefano Cordio
 * @since 3.3.0
 */
class InstanceOfAssertFactoriesTest {

  @Test
  void byte_source_factory_should_allow_byte_source_assertions() throws IOException {
    // GIVEN
    Object value = ByteSource.empty();
    // WHEN
    ByteSourceAssert result = assertThat(value).asInstanceOf(BYTE_SOURCE);
    // THEN
    result.isEmpty();
  }

  @Test
  void multimap_factory_should_allow_multimap_assertions() {
    // GIVEN
    Object value = ImmutableMultimap.of("key", "value");
    // WHEN
    MultimapAssert<Object, Object> result = assertThat(value).asInstanceOf(MULTIMAP);
    // THEN
    result.contains(Assertions.entry("key", "value"));
  }

  @Test
  void typed_multimap_factory_should_allow_typed_multimap_assertions() {
    // GIVEN
    Object value = ImmutableMultimap.of("key", "value");
    // WHEN
    MultimapAssert<String, String> result = assertThat(value).asInstanceOf(multimap(String.class, String.class));
    // THEN
    result.contains(Assertions.entry("key", "value"));
  }

  @Test
  void optional_factory_should_allow_optional_assertions() {
    // GIVEN
    Object value = Optional.of("something");
    // WHEN
    OptionalAssert<Object> result = assertThat(value).asInstanceOf(OPTIONAL);
    // THEN
    result.isPresent();
  }

  @Test
  void typed_optional_factory_should_allow_typed_optional_assertions() {
    // GIVEN
    Object value = Optional.of("something");
    // WHEN
    OptionalAssert<String> result = assertThat(value).asInstanceOf(optional(String.class));
    // THEN
    result.isPresent();
  }

  @Test
  void range_factory_should_allow_range_assertions() {
    // GIVEN
    Object value = Range.atLeast(0);
    // WHEN
    RangeAssert<Integer> result = assertThat(value).asInstanceOf(range(Integer.class));
    // THEN
    result.contains(0);
  }

  @Test
  void range_map_factory_should_allow_range_map_assertions() {
    // GIVEN
    Object value = ImmutableRangeMap.of(Range.atLeast(0), "value");
    // WHEN
    RangeMapAssert<Integer, String> result = assertThat(value).asInstanceOf(rangeMap(Integer.class, String.class));
    // THEN
    result.contains(entry(0, "value"));
  }

  @Test
  void range_set_factory_should_allow_range_set_assertions() {
    // GIVEN
    Object value = ImmutableRangeSet.of(Range.closed(0, 1));
    // WHEN
    RangeSetAssert<Integer> result = assertThat(value).asInstanceOf(rangeSet(Integer.class));
    // THEN
    result.contains(0);
  }

  @Test
  void table_factory_should_allow_table_assertions() {
    // GIVEN
    Object value = ImmutableTable.of(0, 0.0, "value");
    // WHEN
    TableAssert<Object, Object, Object> result = assertThat(value).asInstanceOf(TABLE);
    // THEN
    result.containsCell(0, 0.0, "value");
  }

  @Test
  void typed_table_factory_should_allow_typed_table_assertions() {
    // GIVEN
    Object value = ImmutableTable.of(0, 0.0, "value");
    // WHEN
    TableAssert<Integer, Double, String> result = assertThat(value).asInstanceOf(table(Integer.class, Double.class,
                                                                                       String.class));
    // THEN
    result.containsCell(0, 0.0, "value");
  }

  @Test
  void multiset_factory_should_allow_multiset_assertions() {
    // GIVEN
    Object value = ImmutableMultiset.of("value");
    // WHEN
    MultisetAssert<Object> result = assertThat(value).asInstanceOf(MULTISET);
    // THEN
    result.containsAtLeast(1, "value");
  }

  @Test
  void typed_multiset_factory_should_allow_typed_multiset_assertions() {
    // GIVEN
    Object value = ImmutableMultiset.of("value");
    // WHEN
    MultisetAssert<String> result = assertThat(value).asInstanceOf(multiset(String.class));
    // THEN
    result.containsAtLeast(1, "value");
  }

}
