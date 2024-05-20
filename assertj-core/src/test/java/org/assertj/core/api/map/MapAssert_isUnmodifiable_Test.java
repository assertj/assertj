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
package org.assertj.core.api.map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.apache.commons.collections4.map.UnmodifiableMap;
import org.apache.commons.collections4.map.UnmodifiableSortedMap;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.testkit.jdk11.Jdk11;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MapAssert_isUnmodifiable_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Map<?, ?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @MethodSource("modifiableMaps")
  void should_fail_if_actual_can_be_modified(Map<?, ?> actual, ErrorMessageFactory errorMessageFactory) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).as(actual.getClass().getName())
                        .hasMessage(errorMessageFactory.create());
  }

  private static Stream<Arguments> modifiableMaps() {
    return Stream.of(arguments(new EnumMap<>(ChronoUnit.class), shouldBeUnmodifiable("Map.clear()")),
                     arguments(new IdentityHashMap<>(), shouldBeUnmodifiable("Map.clear()")),
                     arguments(new HashMap<>(), shouldBeUnmodifiable("Map.clear()")),
                     arguments(new TreeMap<>(), shouldBeUnmodifiable("Map.clear()")),
                     arguments(new LinkedHashMap<>(), shouldBeUnmodifiable("Map.clear()")));
  }

  @Test
  void should_fail_with_commons_collections_UnmodifiableMap() {
    // GIVEN
    Map<?, ?> actual = UnmodifiableMap.unmodifiableMap(new HashMap<>());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldBeUnmodifiable("Map.compute(null, (k, v) -> v)").create());
  }

  @Test
  void should_fail_with_commons_collections_UnmodifiableSortedMap() {
    // GIVEN
    Map<?, ?> actual = UnmodifiableSortedMap.unmodifiableSortedMap(new TreeMap<>());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldBeUnmodifiable("Map.compute(null, (k, v) -> v)", new NullPointerException()).create());
  }

  @ParameterizedTest
  @MethodSource("unmodifiableMaps")
  void should_pass(Map<?, ?> actual) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> assertThat(actual).isUnmodifiable());
  }

  private static Stream<Map<?, ?>> unmodifiableMaps() {
    return Stream.of(Collections.emptyNavigableMap(),
                     Collections.emptyMap(),
                     Collections.emptySortedMap(),
                     Collections.singletonMap("key", "value"),
                     Collections.unmodifiableNavigableMap(new TreeMap<>()),
                     Collections.unmodifiableMap(new HashMap<>()),
                     Collections.unmodifiableSortedMap(new TreeMap<>()),
                     ImmutableMap.of("key", "value"),
                     ImmutableSortedMap.of("key", "value"),
                     Jdk11.Map.of(),
                     Jdk11.Map.of("key", "value"),
                     Jdk11.Map.of("key1", "value1", "key2", "element2"), // same implementation for 2+ key-value pairs
                     Maps.unmodifiableNavigableMap(new TreeMap<>()));
  }

}
