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

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainKeys.shouldContainKeys;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.internal.MapsBaseTest;
import org.assertj.core.test.jdk11.Jdk11;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.ImmutableMap;

/**
 * @author Ilya Koshaleu
 */
class Maps_assertContainsKeys_with_Iterable_Test extends MapsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Iterable<String> keys = list("name");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertContainsKeys(someInfo(), null, keys));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_keys_iterable_is_null() {
    // GIVEN
    Iterable<String> keys = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsKeys(someInfo(), actual, keys));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(keysToLookForIsNull("keys iterable"));
  }

  @Test
  void should_fail_if_given_keys_iterable_is_empty() {
    // GIVEN
    Iterable<String> keys = emptyList();
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsKeys(someInfo(), actual, keys));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage(keysToLookForIsEmpty("keys iterable"));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases",
      "caseInsensitiveMapsSuccessfulTestCases",
  })
  void should_pass(Map<String, String> actual, Iterable<String> expected) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertContainsKeys(info, actual, expected));
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(emptyMap(), emptyList()),
                     arguments(singletonMap("name", "Yoda"), list("name")),
                     arguments(new SingletonMap<>("name", "Yoda"), list("name")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), list("name", "job")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), list("job", "name")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), list("name", "job")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), list("job", "name")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), list("name", "job")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), list("job", "name")));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          list("name")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          list("name", "job")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          list("job", "name"))));
  }

  private static Stream<Arguments> caseInsensitiveMapsSuccessfulTestCases() {
    return Stream.of(ArrayUtils.add(CASE_INSENSITIVE_MAPS, CaseInsensitiveMap::new))
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          list("name", "job")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          list("job", "name")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          list("Name", "Job")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          list("Job", "Name"))));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases",
      "caseInsensitiveMapsFailureTestCases",
      "commonsCollectionsCaseInsensitiveMapFailureTestCases",
  })
  void should_fail(Map<String, String> actual, Iterable<String> expected, Set<String> notFound) {
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> maps.assertContainsKeys(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldContainKeys(actual, notFound).create());
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(emptyMap(),
                               list("name"),
                               set("name")),
                     arguments(singletonMap("name", "Yoda"),
                               list("color"),
                               set("color")),
                     arguments(new SingletonMap<>("name", "Yoda"),
                               list("color"),
                               set("color")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               list("name", "color"),
                               set("color")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               list("name", "color"),
                               set("color")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"),
                               list("name", "color"),
                               set("color")),
                     arguments(Jdk11.Map.of("name", "Yoda"),
                               list((String) null), // implementation not permitting null keys
                               set((String) null)));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda")),
                                                          list("name", "color"),
                                                          set("color")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          list("name", "color"),
                                                          set("color"))));
  }

  private static Stream<Arguments> caseInsensitiveMapsFailureTestCases() {
    return Stream.of(CASE_INSENSITIVE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          list("name", "color"),
                                                          set("color")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          list("Name", "Color"),
                                                          set("Color"))));
  }

  private static Stream<Arguments> commonsCollectionsCaseInsensitiveMapFailureTestCases() {
    return Stream.of(arguments(mapOf(CaseInsensitiveMap::new, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                               list("name", "color"),
                               set("color")), // internal keys are always lowercase
                     arguments(mapOf(CaseInsensitiveMap::new, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                               list("Name", "Color"),
                               set("Color"))); // internal keys are always lowercase
  }
}
