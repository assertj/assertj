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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.AllowMethod.forMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Streams;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/** Tests finding mutating methods in maps. */
final class MutatingMethodFinder_Map_Test {
  /** The instance to test. */
  private final CollectionVisitor<Optional<String>> finder = new MutatingMethodFinder();

  @Test
  void should_not_allow_a_null_list() {
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> finder.visitMap(null));
    // THEN
    then(exception).hasMessageContainingAll("target");
  }

  @ParameterizedTest
  @MethodSource("one_mutating_map_method_is_detected_source")
  void one_mutating_map_method_is_detected(final String method, final int argumentCount) {
    final Map<String, String> target = new HashMap<>();
    target.put("key", "value");
    testOneMutatingMethodInMap(Map.class, target, method, argumentCount);
  }

  /** Mutating map methods. */
  static Stream<Arguments> one_mutating_map_method_is_detected_source() {
    return Stream.of(Arguments.of("clear", 0),
                     Arguments.of("compute", 2),
                     Arguments.of("computeIfAbsent", 2),
                     Arguments.of("computeIfPresent", 2),
                     Arguments.of("merge", 3),
                     Arguments.of("put", 2),
                     Arguments.of("putAll", 1),
                     Arguments.of("putIfAbsent", 2),
                     Arguments.of("replace", 3),
                     Arguments.of("replace", 2),
                     Arguments.of("remove", 1),
                     Arguments.of("remove", 2),
                     Arguments.of("replaceAll", 1));
  }

  @ParameterizedTest
  @MethodSource("one_mutating_navigable_map_method_is_detected_source")
  void one_mutating_navigable_map_method_is_detected(final String method, final int argumentCount) {
    final NavigableMap<String, String> target = new TreeMap<>();
    target.put("key", "value");
    testOneMutatingMethodInMap(NavigableMap.class, target, method, argumentCount);
  }

  /** Mutating navigable map methods. */
  static Stream<Arguments> one_mutating_navigable_map_method_is_detected_source() {
    return Streams.concat(one_mutating_map_method_is_detected_source(),
                          Stream.of(Arguments.of("pollFirstEntry", 0), Arguments.of("pollLastEntry", 0)));
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("an_immutable_map_is_identified_source")
  void an_immutable_map_is_identified(final Map<String, String> map, final Class<?> mapClass) {
    assertThat(finder.visitMap(map)).isEmpty();
  }

  static Stream<Arguments> an_immutable_map_is_identified_source() {
    return Stream.of(Collections.emptyMap(), ImmutableMap.of())
                 .map(map -> Arguments.of(map, map.getClass()));
  }

  @ParameterizedTest(name = "{1}")
  @MethodSource("a_mutable_map_is_identified_source")
  void a_mutable_map_is_identified(final Map<String, String> map, final Class<?> mapClass) {
    assertThat(finder.visitMap(map)).isNotEmpty();
  }

  static Stream<Arguments> a_mutable_map_is_identified_source() {
    return Stream.of(new HashMap<>(), new TreeMap<>())
                 .map(map -> Arguments.of(map, map.getClass()));
  }

  /**
   * Tests a map with a single mutating method.
   *
   * @param interfaceType the collection interface to test
   * @param target a mutating instance to delegate the single mutating method to
   * @param method the mutating method to try to detect
   * @param argumentCount the number of arguments this method takes, us to disambiguate multiple
   *     methods with the same name
   */
  private void testOneMutatingMethodInMap(final Class<?> interfaceType,
                                          final Map<String, String> target,
                                          final String method,
                                          final int argumentCount) {
    // GIVEN
    Map<String, String> proxyInstance = forMap(interfaceType, target, method, argumentCount);
    // WHEN
    Optional<String> actual = finder.visitMap(proxyInstance);
    // THEN
    assertThat(actual).hasValueSatisfying(msg -> assertThat(msg).contains(method));
  }
}
