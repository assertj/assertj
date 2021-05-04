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
package org.assertj.core.internal.maps;

import static java.util.Collections.emptySet;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContainOnly.shouldContainOnly;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.api.AssertionInfo;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.Maps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.google.common.collect.ImmutableMap;

/**
 * Tests for <code>{@link org.assertj.core.internal.Maps#assertContainsOnly(AssertionInfo, Map, Entry[])}</code>.
 *
 * @author Jean-Christophe Gay
 */
class Maps_assertContainsOnly_Test extends MapsBaseTest {

  @SuppressWarnings("unchecked")
  private static final Supplier<Map<String, String>>[] CASE_INSENSITIVE_MAP_SUPPLIERS = new Supplier[] {
      CaseInsensitiveMap::new,
      LinkedCaseInsensitiveMap::new,
      () -> new TreeMap<>(String.CASE_INSENSITIVE_ORDER)
  };

  @SuppressWarnings("unchecked")
  private static final Supplier<Map<String, String>>[] MODIFIABLE_MAP_SUPPLIERS = ArrayUtils.addAll(CASE_INSENSITIVE_MAP_SUPPLIERS,
                                                                                                    HashMap::new,
                                                                                                    IdentityHashMap::new,
                                                                                                    LinkedHashMap::new);

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Entry<String, String>[] entries = array(entry("name", "Yoda"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsOnly(someInfo(), null, entries));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_entries_array_is_null() {
    // GIVEN
    Entry<String, String>[] entries = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsOnly(someInfo(), actual, entries));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(entriesToLookForIsNull());
  }

  @SuppressWarnings("unchecked")
  @Test
  void should_fail_if_given_entries_array_is_empty() {
    // GIVEN
    Entry<String, String>[] entries = emptyEntries();
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnly(someInfo(), actual, entries));
    // THEN
    then(error).hasMessage(shouldBeEmpty(actual).create());
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases",
      "caseInsensitiveMapsSuccessfulTestCases",
  })
  void should_pass(Map<String, String> actual, Entry<String, String>[] expectedEntries) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertContainsOnly(info, actual, expectedEntries));
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(Collections.emptyMap(), emptyEntries()),
                     arguments(Collections.singletonMap("name", "Yoda"),
                               array(entry("name", "Yoda"))),
                     arguments(new SingletonMap<>("name", "Yoda"),
                               array(entry("name", "Yoda"))),
                     arguments(Collections.unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array(entry("name", "Yoda"), entry("job", "Jedi"))),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               array(entry("name", "Yoda"), entry("job", "Jedi"))));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array(entry("name", "Yoda"), entry("job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array(entry("job", "Jedi"), entry("name", "Yoda")))));
  }

  private static Stream<Arguments> caseInsensitiveMapsSuccessfulTestCases() {
    return Stream.of(CASE_INSENSITIVE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("name", "Yoda"), entry("job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("Name", "Yoda"), entry("Job", "Jedi")))));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases",
      "caseInsensitiveMapsFailureTestCases",
  })
  void should_fail(Map<String, String> actual, Entry<String, String>[] expectedEntries, Set<Entry<String, String>> notFound,
                   Set<Entry<String, String>> notExpected) {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnly(info, actual, expectedEntries));
    // THEN
    then(error).as(actual.getClass().getName())
               .hasMessage(shouldContainOnly(actual, expectedEntries, notFound, notExpected).create());
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(Collections.emptyMap(),
                               array(entry("name", "Yoda")),
                               set(entry("name", "Yoda")),
                               emptySet()),
                     arguments(Collections.singletonMap("name", "Yoda"),
                               array(entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("name", "Yoda"))),
                     arguments(new SingletonMap<>("name", "Yoda"),
                               array(entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("name", "Yoda"))),
                     arguments(Collections.unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array(entry("name", "Yoda"), entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("job", "Jedi"))),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               array(entry("name", "Yoda"), entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("job", "Jedi"))));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda")),
                                                          array(entry("name", "Yoda"), entry("color", "Green")),
                                                          set(entry("color", "Green")),
                                                          emptySet()),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array(entry("name", "Yoda")),
                                                          emptySet(),
                                                          set(entry("job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array(entry("name", "Yoda"), entry("color", "Green")),
                                                          set(entry("color", "Green")),
                                                          set(entry("job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array(entry("name", "Jedi"), entry("job", "Yoda")),
                                                          set(entry("name", "Jedi"), entry("job", "Yoda")),
                                                          set(entry("name", "Yoda"), entry("job", "Jedi")))));
  }

  private static Stream<Arguments> caseInsensitiveMapsFailureTestCases() {
    return Stream.of(CASE_INSENSITIVE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("name", "Yoda"), entry("color", "Green")),
                                                          set(entry("color", "Green")),
                                                          set(entry("Job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("Name", "Yoda"), entry("Color", "Green")),
                                                          set(entry("Color", "Green")),
                                                          set(entry("Job", "Jedi")))));
  }

  @Test
  void should_fail_if_actual_contains_unexpected_entry_according_to_case_insensitive_key_comparison() {
    // GIVEN
    AssertionInfo info = someInfo();
    actual = new CaseInsensitiveMap<>();
    actual.put("NAME", "green");
    actual.put("Color", "green");
    MapEntry<String, String>[] expected = array(entry("name", "green"), entry("cool", "green"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsOnly(info, actual, expected));
    // THEN
    // should logically be: entry("Color", "green") but CaseInsensitiveMap lowecase all keys
    Set<MapEntry<String, String>> notExpected = set(entry("color", "green"));
    Set<MapEntry<String, String>> notFound = set(entry("cool", "green"));
    verify(failures).failure(info, shouldContainOnly(actual, expected, notFound, notExpected));
  }

  @Test
  void should_fail_if_actual_contains_unexpected_entry() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsOnly(info, actual, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expected, emptySet(), set(entry("color", "green"))));
  }

  @Test
  void should_fail_if_actual_does_not_contains_every_expected_entries() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = Maps.mapOf(entry("name", "Yoda"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsOnly(info, underTest, expected));
    // THEN
    verify(failures).failure(info, shouldContainOnly(underTest, expected, set(entry("color", "green")), emptySet()));
  }

  @Test
  void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = Maps.mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsOnly(info, underTest, expected));
    // THEN
    verify(failures).failure(info,
                             shouldContainOnly(underTest, expected, set(entry("color", "green")), set(entry("job", "Jedi"))));
  }

  @Test
  void should_fail_if_actual_contains_entry_key_with_different_value() {
    // GIVEN
    AssertionInfo info = someInfo();
    MapEntry<String, String>[] expectedEntries = array(entry("name", "Yoda"), entry("color", "yellow"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsOnly(info, actual, expectedEntries));
    // THEN
    verify(failures).failure(info, shouldContainOnly(actual, expectedEntries, set(entry("color", "yellow")),
                                                     set(entry("color", "green"))));
  }

}
