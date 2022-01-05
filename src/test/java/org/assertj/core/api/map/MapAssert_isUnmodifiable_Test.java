package org.assertj.core.api.map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.map.UnmodifiableMap;
import org.apache.commons.collections4.map.UnmodifiableSortedMap;
import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.core.test.jdk11.Jdk11;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
    return Stream.of(arguments(new HashMap<>(), shouldBeUnmodifiable("Map.clear()")),
                     arguments(new TreeMap<>(), shouldBeUnmodifiable("Map.clear()")),
                     arguments(new LinkedHashMap<>(), shouldBeUnmodifiable("Map.clear()")));
  }

  @ParameterizedTest
  @MethodSource("unmodifiableMaps")
  void should_pass(Map<?, ?> actual) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> assertThat(actual).isUnmodifiable());
  }

  private static Stream<Map<?, ?>> unmodifiableMaps() {
    return Stream.of(Collections.unmodifiableMap(new HashMap<>()),
                     Collections.unmodifiableNavigableMap(new TreeMap<>()),
                     Collections.unmodifiableSortedMap(new ConcurrentSkipListMap<>()),
                     ImmutableMap.of(1, "element"),
                     ImmutableSortedMap.of(1, "element"),
                     Jdk11.Map.of(),
                     Jdk11.Map.of(1, "element"),
                     Jdk11.Map.of(1, "element", 2, "element"));
  }

  /**
   * Unmodifiable maps created using utils from libraries below seems to be not really unmodifiable.
   */

  @ParameterizedTest
  @MethodSource("commonsCollectionsUnmodifiableMaps")
  void should_fail_with_unmodifiable_commons_collections(Map<Integer, String> actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldBeUnmodifiable("Map.compute(null, (k, v) -> v)",
                                                         new NullPointerException()).create());
  }

  private static Stream<Map<Integer, String>> commonsCollectionsUnmodifiableMaps() {
    return Stream.of(UnmodifiableMap.unmodifiableMap(new TreeMap<>()),
                     UnmodifiableSortedMap.unmodifiableSortedMap(new ConcurrentSkipListMap<>()));
  }

  @Test
  void should_fail_with_guava_UnmodifiableNavigableMap() {
    // GIVEN
    Map<Integer, String> actual = Maps.unmodifiableNavigableMap(new TreeMap<>());
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isUnmodifiable());
    // THEN
    then(assertionError).hasMessage(shouldBeUnmodifiable("Map.compute(null, (k, v) -> v)",
                                                         new NullPointerException()).create());
  }

}
