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
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableMap;
import jakarta.ws.rs.core.MultivaluedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.testkit.jdk11.Jdk11;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.MultiValueMapAdapter;

/**
 * @author Christopher Arnott
 */
class Maps_assertContainsOnlyKeys_Test extends MapsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] keys = { "name" };
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnlyKeys(someInfo(), null, keys));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_keys_array_is_null() {
    // GIVEN
    String[] keys = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsOnlyKeys(someInfo(), actual, keys));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(keysToLookForIsNull("array of keys"));
  }

  @Test
  void should_fail_if_given_keys_array_is_empty() {
    // GIVEN
    String[] keys = emptyKeys();
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsOnlyKeys(someInfo(), actual, keys));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage(keysToLookForIsEmpty("array of keys"));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases",
      "caseInsensitiveMapsSuccessfulTestCases",
  })
  void should_pass(Map<String, String> actual, String[] expected) {
    // GIVEN
    int initialSize = actual.size();
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertContainsOnlyKeys(info, actual, expected));

    then(actual).hasSize(initialSize);
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(emptyMap(), emptyKeys()),
                     arguments(singletonMap("name", "Yoda"), array("name")),
                     arguments(new SingletonMap<>("name", "Yoda"), array("name")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), array("name", "job")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), array("job", "name")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), array("name", "job")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), array("job", "name")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), array("name", "job")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), array("job", "name")));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("name", "job")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("job", "name"))));
  }

  private static Stream<Arguments> caseInsensitiveMapsSuccessfulTestCases() {
    return Stream.of(ArrayUtils.add(CASE_INSENSITIVE_MAPS, CaseInsensitiveMap::new))
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("name", "job")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("job", "name")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("Name", "Job")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("Job", "Name"))));
  }

  @Test
  void should_pass_with_MultiValueMapAdapter() {
    // GIVEN
    MultiValueMapAdapter<String, String> actual = new MultiValueMapAdapter<>(mapOf(entry("name", list("Yoda"))));
    String[] expected = array("name");
    int initialSize = actual.size();
    // WHEN
    maps.assertContainsOnlyKeys(info, actual, expected);
    // THEN
    then(actual).hasSize(initialSize);
  }

  @Test
  void should_pass_with_MultivaluedHashMap() {
    // GIVEN
    MultivaluedHashMap<String, String> actual = new MultivaluedHashMap<>(mapOf(entry("name", "Yoda")));
    String[] expected = array("name");
    int initialSize = actual.size();
    // WHEN
    maps.assertContainsOnlyKeys(info, actual, expected);
    // THEN
    then(actual).hasSize(initialSize);
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases",
      "caseInsensitiveMapsFailureTestCases",
      "commonsCollectionsCaseInsensitiveMapFailureTestCases",
  })
  void should_fail(Map<String, String> actual, String[] expected, Set<String> notFound, Set<String> notExpected) {
    // GIVEN
    int initialSize = actual.size();
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> maps.assertContainsOnlyKeys(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldContainOnlyKeys(actual, expected,
                                                                                      notFound, notExpected).create());

    then(actual).hasSize(initialSize);
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(emptyMap(),
                               array("name"),
                               set("name"),
                               emptySet()),
                     arguments(singletonMap("name", "Yoda"),
                               array("color"),
                               set("color"),
                               set("name")),
                     arguments(new SingletonMap<>("name", "Yoda"),
                               array("color"),
                               set("color"),
                               set("name")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array("name", "color"),
                               set("color"),
                               set("job")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               array("name", "color"),
                               set("color"),
                               set("job")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"),
                               array("name", "color"),
                               set("color"),
                               set("job")),
                     arguments(Jdk11.Map.of("name", "Yoda"),
                               array((String) null), // implementation not permitting null keys
                               set((String) null),
                               set("name")));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda")),
                                                          array("name", "color"),
                                                          set("color"),
                                                          emptySet()),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("name"),
                                                          emptySet(),
                                                          set("job")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("name", "color"),
                                                          set("color"),
                                                          set("job"))));
  }

  private static Stream<Arguments> caseInsensitiveMapsFailureTestCases() {
    return Stream.of(CASE_INSENSITIVE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("name", "color"),
                                                          set("color"),
                                                          set("Job")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("Name", "Color"),
                                                          set("Color"),
                                                          set("Job"))));
  }

  private static Stream<Arguments> commonsCollectionsCaseInsensitiveMapFailureTestCases() {
    return Stream.of(arguments(mapOf(CaseInsensitiveMap::new, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                               array("name", "color"),
                               set("color"),
                               set("job")), // internal keys are always lowercase
                     arguments(mapOf(CaseInsensitiveMap::new, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                               array("Name", "Color"),
                               set("Color"),
                               set("job"))); // internal keys are always lowercase
  }

  @Test
  void should_fail_with_MultiValueMapAdapter() {
    // GIVEN
    MultiValueMapAdapter<String, String> actual = new MultiValueMapAdapter<>(mapOf(entry("name", list("Yoda")),
                                                                                   entry("job", list("Jedi"))));
    String[] expected = array("name", "color");
    Set<String> notFound = set("color");
    Set<String> notExpected = set("job");
    int initialSize = actual.size();
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnlyKeys(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainOnlyKeys(actual, expected, notFound, notExpected).create());
    then(actual).hasSize(initialSize);
  }

  @Test
  void should_fail_with_MultivaluedHashMap() {
    // GIVEN
    MultivaluedHashMap<String, String> actual = new MultivaluedHashMap<>(mapOf(entry("name", "Yoda"),
                                                                               entry("job", "Jedi")));
    String[] expected = array("name", "color");
    Set<String> notFound = set("color");
    Set<String> notExpected = set("job");
    int initialSize = actual.size();
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnlyKeys(info, actual, expected));
    // THEN
    then(error).hasMessage(shouldContainOnlyKeys(actual, expected, notFound, notExpected).create());
    then(actual).hasSize(initialSize);
  }

}
