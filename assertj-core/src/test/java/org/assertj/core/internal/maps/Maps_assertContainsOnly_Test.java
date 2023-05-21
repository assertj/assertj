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
package org.assertj.core.internal.maps;

import static java.util.Collections.emptyMap;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.MultiValueMapAdapter;

import com.google.common.collect.ImmutableMap;

import jakarta.ws.rs.core.MultivaluedHashMap;

/**
 * @author Jean-Christophe Gay
 */
class Maps_assertContainsOnly_Test extends MapsBaseTest {

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

  @Test
  void should_fail_if_given_entries_array_is_empty() {
    // GIVEN
    Entry<String, String>[] entries = emptyEntries();
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnly(someInfo(), actual, entries));
    // THEN
    then(error).hasMessage(shouldBeEmpty(actual).create());
  }

  @Test
  void should_pass_if_value_type_is_array() {
    // GIVEN
    Map<String, byte[]> actual = mapOf(entry("key1", new byte[] { 1, 2 }), entry("key2", new byte[] { 3, 4, 5 }));
    Entry<String, byte[]>[] expected = array(entry("key2", new byte[] { 3, 4, 5 }), entry("key1", new byte[] { 1, 2 }));
    // WHEN/THEN
    assertThatNoException().isThrownBy(() -> maps.assertContainsOnly(info, actual, expected));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases",
      "caseInsensitiveMapsSuccessfulTestCases",
  })
  void should_pass(Map<String, String> actual, Entry<String, String>[] expected) {
    // GIVEN
    int initialSize = actual.size();
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertContainsOnly(info, actual, expected));

    then(actual).hasSize(initialSize);
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(emptyMap(), emptyEntries()),
                     arguments(singletonMap("name", "Yoda"),
                               array(entry("name", "Yoda"))),
                     arguments(new SingletonMap<>("name", "Yoda"),
                               array(entry("name", "Yoda"))),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array(entry("name", "Yoda"), entry("job", "Jedi"))),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array(entry("job", "Jedi"), entry("name", "Yoda"))),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               array(entry("name", "Yoda"), entry("job", "Jedi"))),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               array(entry("job", "Jedi"), entry("name", "Yoda"))));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array(entry("name", "Yoda"), entry("job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array(entry("job", "Jedi"), entry("name", "Yoda")))));
  }

  private static Stream<Arguments> caseInsensitiveMapsSuccessfulTestCases() {
    return Stream.of(ArrayUtils.add(CASE_INSENSITIVE_MAPS, CaseInsensitiveMap::new))
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("name", "Yoda"), entry("job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("job", "Jedi"), entry("name", "Yoda"))),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("Name", "Yoda"), entry("Job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("Job", "Jedi"), entry("Name", "Yoda")))));
  }

  @Test
  void should_pass_with_MultiValueMapAdapter() {
    // GIVEN
    MultiValueMapAdapter<String, String> actual = new MultiValueMapAdapter<>(mapOf(entry("name", list("Yoda"))));
    Entry<String, List<String>>[] expected = array(entry("name", list("Yoda")));
    int initialSize = actual.size();
    // WHEN
    maps.assertContainsOnly(info, actual, expected);
    // THEN
    then(actual).hasSize(initialSize);
  }

  @Test
  void should_pass_with_MultivaluedHashMap() {
    // GIVEN
    MultivaluedHashMap<String, String> actual = new MultivaluedHashMap<>(mapOf(entry("name", "Yoda")));
    Entry<String, List<String>>[] expected = array(entry("name", list("Yoda")));
    int initialSize = actual.size();
    // WHEN
    maps.assertContainsOnly(info, actual, expected);
    // THEN
    then(actual).hasSize(initialSize);
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases",
      "caseInsensitiveMapsFailureTestCases",
      "commonsCollectionsCaseInsensitiveMapFailureTestCases",
      "orderDependentFailureTestCases",
  })
  void should_fail(Map<String, String> actual, Entry<String, String>[] expected,
                   Set<Entry<String, String>> notFound, Set<Entry<String, String>> notExpected) {
    // GIVEN
    int initialSize = actual.size();
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> maps.assertContainsOnly(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldContainOnly(actual, expected,
                                                                                  notFound, notExpected).create());

    then(actual).hasSize(initialSize);
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(emptyMap(),
                               array(entry("name", "Yoda")),
                               set(entry("name", "Yoda")),
                               emptySet()),
                     arguments(singletonMap("name", "Yoda"),
                               array(entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("name", "Yoda"))),
                     arguments(new SingletonMap<>("name", "Yoda"),
                               array(entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("name", "Yoda"))),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array(entry("name", "Yoda"), entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("job", "Jedi"))),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               array(entry("name", "Yoda"), entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("job", "Jedi"))));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
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
                                                          set(entry("job", "Jedi")))));
  }

  private static Stream<Arguments> caseInsensitiveMapsFailureTestCases() {
    return Stream.of(CASE_INSENSITIVE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("name", "Yoda"), entry("color", "Green")),
                                                          set(entry("color", "Green")),
                                                          set(entry("Job", "Jedi"))),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array(entry("Name", "Yoda"), entry("Color", "Green")),
                                                          set(entry("Color", "Green")),
                                                          set(entry("Job", "Jedi")))));
  }

  private static Stream<Arguments> commonsCollectionsCaseInsensitiveMapFailureTestCases() {
    return Stream.of(arguments(mapOf(CaseInsensitiveMap::new, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                               array(entry("name", "Yoda"), entry("color", "Green")),
                               set(entry("color", "Green")),
                               set(entry("job", "Jedi"))),  // internal keys are always lowercase
                     arguments(mapOf(CaseInsensitiveMap::new, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                               array(entry("Name", "Yoda"), entry("Color", "Green")),
                               set(entry("Color", "Green")),
                               set(entry("job", "Jedi"))));  // internal keys are always lowercase
  }

  private static Stream<Arguments> orderDependentFailureTestCases() {
    return Stream.of(arguments(mapOf(LinkedHashMap::new, entry("name", "Yoda"), entry("job", "Jedi")),
                               array(entry("name", "Jedi"), entry("job", "Yoda")),
                               set(entry("name", "Jedi"), entry("job", "Yoda")),
                               set(entry("name", "Yoda"), entry("job", "Jedi"))));
  }

  @Test
  void should_fail_with_MultiValueMapAdapter() {
    // GIVEN
    MultiValueMapAdapter<String, String> actual = new MultiValueMapAdapter<>(mapOf(entry("name", list("Yoda")),
                                                                                   entry("job", list("Jedi"))));
    MapEntry<String, List<String>>[] expected = array(entry("name", list("Yoda")), entry("color", list("Green")));
    Set<MapEntry<String, List<String>>> notFound = set(entry("color", list("Green")));
    Set<MapEntry<String, List<String>>> notExpected = set(entry("job", list("Jedi")));
    int initialSize = actual.size();
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnly(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainOnly(actual, expected, notFound, notExpected).create());
    then(actual).hasSize(initialSize);
  }

  @Test
  void should_fail_with_MultivaluedHashMap() {
    // GIVEN
    MultivaluedHashMap<String, String> actual = new MultivaluedHashMap<>(mapOf(entry("name", "Yoda"),
                                                                               entry("job", "Jedi")));
    MapEntry<String, List<String>>[] expected = array(entry("name", list("Yoda")), entry("color", list("Green")));
    Set<MapEntry<String, List<String>>> notFound = set(entry("color", list("Green")));
    Set<MapEntry<String, List<String>>> notExpected = set(entry("job", list("Jedi")));
    int initialSize = actual.size();
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnly(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainOnly(actual, expected, notFound, notExpected).create());
    then(actual).hasSize(initialSize);
  }

}
