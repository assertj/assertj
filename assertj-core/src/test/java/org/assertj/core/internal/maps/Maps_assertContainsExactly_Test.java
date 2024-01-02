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
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Stream.concat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.error.ShouldContainExactly.elementsDifferAtIndex;
import static org.assertj.core.error.ShouldContainExactly.shouldContainExactly;
import static org.assertj.core.error.ShouldHaveSameSizeAs.shouldHaveSameSizeAs;
import static org.assertj.core.internal.ErrorMessages.entriesToLookForIsNull;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Arrays.asList;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.SingletonMap;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.jdk11.Jdk11;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.ImmutableMap;

/**
 * @author Jean-Christophe Gay
 */
class Maps_assertContainsExactly_Test extends MapsBaseTest {

  private LinkedHashMap<String, String> linkedActual;

  @BeforeEach
  void initLinkedHashMap() {
    linkedActual = new LinkedHashMap<>(2);
    linkedActual.put("name", "Yoda");
    linkedActual.put("color", "green");
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Entry<String, String>[] entries = array(entry("name", "Yoda"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsExactly(someInfo(), null, entries));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_entries_array_is_null() {
    // GIVEN
    Entry<String, String>[] entries = null;
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> maps.assertContainsExactly(someInfo(), actual, entries))
                                    .withMessage(entriesToLookForIsNull());
  }

  @Test
  void should_fail_if_given_entries_array_is_empty() {
    // GIVEN
    Entry<String, String>[] expected = emptyEntries();
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldBeEmpty(actual));
  }

  @Test
  void should_pass_if_actual_and_entries_are_empty() {
    maps.assertContainsExactly(someInfo(), emptyMap(), array());
  }

  @Test
  void should_pass_if_actual_contains_given_entries_in_order() {
    maps.assertContainsExactly(someInfo(), linkedActual, array(entry("name", "Yoda"), entry("color", "green")));
  }

  @Test
  void should_fail_if_actual_contains_given_entries_in_disorder() {
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(someInfo(), linkedActual,
                                                          array(entry("color", "green"), entry("name", "Yoda"))));
    // THEN
    verify(failures).failure(someInfo(), elementsDifferAtIndex(entry("name", "Yoda"), entry("color", "green"), 0));
  }

  @Test
  void should_fail_if_actual_and_expected_entries_have_different_size() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"));
    // WHEN
    ThrowingCallable code = () -> maps.assertContainsExactly(someInfo(), linkedActual, expected);
    // THEN
    String error = shouldHaveSameSizeAs(linkedActual, expected, linkedActual.size(), expected.length).create();
    assertThatAssertionErrorIsThrownBy(code).withMessage(error);
  }

  @Test
  void should_fail_if_actual_does_not_contains_every_expected_entries_and_contains_unexpected_one() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "green"));
    Map<String, String> underTest = mapOf(entry("name", "Yoda"), entry("job", "Jedi"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(someInfo(), underTest, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldContainExactly(underTest, list(expected),
                                                              set(entry("color", "green")),
                                                              set(entry("job", "Jedi"))));
  }

  @Test
  void should_fail_if_actual_contains_entry_key_with_different_value() {
    // GIVEN
    Entry<String, String>[] expected = array(entry("name", "Yoda"), entry("color", "yellow"));
    // WHEN
    expectAssertionError(() -> maps.assertContainsExactly(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldContainExactly(actual, asList(expected),
                                                              set(entry("color", "yellow")),
                                                              set(entry("color", "green"))));
  }

  @Test
  void should_pass_with_singleton_map_having_array_value() {
    // GIVEN
    Map<String, String[]> actual = singletonMap("color", array("yellow"));
    Entry<String, String[]>[] expected = array(entry("color", array("yellow")));
    // WHEN/THEN
    maps.assertContainsExactly(someInfo(), actual, expected);
  }

  @Test
  void should_fail_with_singleton_map_having_array_value() {
    // GIVEN
    Map<String, String[]> actual = singletonMap("color", array("yellow"));
    Entry<String, String[]>[] expected = array(entry("color", array("green")));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsExactly(someInfo(), actual, expected));
    // THEN
    then(assertionError).hasMessage(elementsDifferAtIndex(actual.entrySet().iterator().next(), expected[0], 0).create());
  }

  @ParameterizedTest
  @MethodSource({
      "orderedSensitiveSuccessfulArguments",
      "orderedInsensitiveSuccessfulArguments",
      "unorderedSensitiveSuccessfulArguments",
      "unorderedInsensitiveSuccessfulArguments"
  })
  void should_pass(Map<String, Object> actual, Entry<String, String>[] expected) {
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertContainsExactly(someInfo(), actual, expected));
  }

  @ParameterizedTest
  @MethodSource({
      "orderedSensitiveFailureArguments",
      "orderedInsensitiveFailureArguments",
      "unorderedSensitiveFailureArguments",
      "unorderedInsensitiveFailureArguments"
  })
  void should_fail(Map<String, Object> actual, Entry<String, String>[] expected) {
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> maps.assertContainsExactly(someInfo(), actual, expected));
  }

  private static Stream<Entry<String, String>[]> orderedFailureTestCases() {
    return Stream.of(array(entry("potato", "vegetable")),
                     array(entry("banana", "fruit"), entry("potato", "vegetable"), entry("tomato", "vegetable")),
                     array(entry("banana", "fruit"), entry("tomato", "vegetable")),
                     array(entry("banana", "fruit"), entry("potato", "food")),
                     array(entry("potato", "vegetable"), entry("banana", "fruit")));
  }

  private static Stream<Entry<String, String>[]> orderedSuccessTestCases() {
    return Stream.<Entry<String, String>[]> of(array(entry("banana", "fruit"), entry("poTATo", "vegetable")));
  }

  private static Stream<Entry<String, String>[]> unorderedFailureTestCases() {
    return Stream.of(array(entry("banana", "fruit"), entry("potato", "vegetable")),
                     array(entry("strawberry", "fruit")),
                     array(entry("banana", "food")),
                     array());
  }

  private static Stream<Entry<String, String>[]> unorderedSuccessTestCases() {
    return Stream.<Entry<String, String>[]> of(array(entry("banana", "fruit")));
  }

  private static Stream<Entry<String, String>[]> unorderedInsensitiveFailureTestCases() {
    return Stream.<Entry<String, String>[]> of(array(entry("banana", "FRUIT")));
  }

  private static Stream<Entry<String, String>[]> unorderedInsensitiveSuccessTestCases() {
    return Stream.<Entry<String, String>[]> of(array(entry("BANANA", "fruit")));
  }

  private static Stream<Entry<String, String>[]> orderedInsensitiveFailureTestCases() {
    return Stream.of(array(entry("banana", "fruit"), entry("tomato", "vegetable")),
                     array(entry("potato", "vegetable"), entry("BANANA", "fruit")),
                     array(entry("banana", "vegetable"), entry("tomato", "fruit")),
                     array(entry("banana", "plane"), entry("poTATo", "train")));
  }

  private static Stream<Entry<String, String>[]> orderedInsensitiveSuccessTestCases() {
    return Stream.<Entry<String, String>[]> of(array(entry("BANANA", "fruit"), entry("potato", "vegetable")));
  }

  private static Stream<Arguments> orderedSensitiveSuccessfulArguments() {
    Stream<Map<String, String>> maps = Stream.of(LinkedHashMap::new, PERSISTENT_SORTED_MAP)
                                             .map(supplier -> mapOf(supplier,
                                                                    entry("banana", "fruit"),
                                                                    entry("poTATo", "vegetable")));
    return mapsAndEntriesToArguments(maps, Maps_assertContainsExactly_Test::orderedSuccessTestCases);
  }

  private static Stream<Arguments> orderedInsensitiveSuccessfulArguments() {
    Stream<Map<String, String>> maps = Stream.of(CASE_INSENSITIVE_MAPS)
                                             .map(supplier -> mapOf(supplier,
                                                                    entry("banana", "fruit"),
                                                                    entry("poTATo", "vegetable")));
    return mapsAndEntriesToArguments(maps, () -> concat(orderedSuccessTestCases(), orderedInsensitiveSuccessTestCases()));
  }

  private static Stream<Arguments> orderedSensitiveFailureArguments() {
    Stream<Map<String, String>> maps = Stream.of(LinkedHashMap::new, PERSISTENT_SORTED_MAP)
                                             .map(supplier -> mapOf(supplier,
                                                                    entry("banana", "fruit"),
                                                                    entry("poTATo", "vegetable")));
    return mapsAndEntriesToArguments(maps, Maps_assertContainsExactly_Test::orderedFailureTestCases);
  }

  private static Stream<Arguments> orderedInsensitiveFailureArguments() {
    Stream<Map<String, String>> maps = Stream.of(CASE_INSENSITIVE_MAPS)
                                             .map(supplier -> mapOf(supplier, entry("banana", "fruit"),
                                                                    entry("poTATo", "vegetable")));
    return mapsAndEntriesToArguments(maps, () -> concat(orderedFailureTestCases(), orderedInsensitiveFailureTestCases()));
  }

  private static Stream<Arguments> unorderedSensitiveSuccessfulArguments() {
    Stream<Map<String, String>> maps = concat(Stream.of(HashMap::new, IdentityHashMap::new, PERSISTENT_MAP)
                                                    .map(supplier -> mapOf(supplier, entry("banana", "fruit"))),
                                              Stream.of(singletonMap("banana", "fruit"),
                                                        new SingletonMap<>("banana", "fruit"),
                                                        unmodifiableMap(mapOf(entry("banana", "fruit"))),
                                                        ImmutableMap.of("banana", "fruit"),
                                                        Jdk11.Map.of("banana", "fruit")));
    return mapsAndEntriesToArguments(maps, Maps_assertContainsExactly_Test::unorderedSuccessTestCases);
  }

  private static Stream<Arguments> unorderedInsensitiveSuccessfulArguments() {
    Stream<Map<String, String>> maps = Stream.of(mapOf(CaseInsensitiveMap::new, entry("banana", "fruit")));
    return mapsAndEntriesToArguments(maps, () -> concat(unorderedSuccessTestCases(), unorderedInsensitiveSuccessTestCases()));
  }

  private static Stream<Arguments> unorderedSensitiveFailureArguments() {
    Stream<Map<String, String>> maps = concat(Stream.of(HashMap::new, IdentityHashMap::new, PERSISTENT_MAP)
                                                    .map(supplier -> mapOf(supplier, entry("banana", "fruit"))),
                                              Stream.of(singletonMap("banana", "fruit"),
                                                        new SingletonMap<>("banana", "fruit"),
                                                        unmodifiableMap(mapOf(entry("banana", "fruit"))),
                                                        ImmutableMap.of("banana", "fruit"),
                                                        Jdk11.Map.of("banana", "fruit")));
    return mapsAndEntriesToArguments(maps, Maps_assertContainsExactly_Test::unorderedInsensitiveSuccessTestCases);
  }

  private static Stream<Arguments> unorderedInsensitiveFailureArguments() {
    Stream<Map<String, String>> maps = Stream.of(mapOf(CaseInsensitiveMap::new, entry("banana", "fruit")));
    return mapsAndEntriesToArguments(maps, () -> concat(unorderedFailureTestCases(), unorderedInsensitiveFailureTestCases()));
  }

  private static Stream<Arguments> mapsAndEntriesToArguments(Stream<Map<String, String>> maps,
                                                             Supplier<Stream<Entry<String, String>[]>> entries) {
    return maps.flatMap(m -> entries.get().map(entryArray -> arguments(m, entryArray)));
  }

}
