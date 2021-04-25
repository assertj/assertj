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
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainOnlyKeys.shouldContainOnlyKeys;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.keysToLookForIsNull;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.CaseInsensitiveMap;
import org.apache.commons.collections4.map.SingletonMap;
import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.util.LinkedCaseInsensitiveMap;

import com.google.common.collect.ImmutableMap;

@DisplayName("Maps assertContainsOnlyKeys(Object[])")
class Maps_assertContainsOnlyKeys_Test extends MapsBaseTest {

  @SuppressWarnings("unchecked")
  private static final Supplier<Map<String, String>>[] CASE_INSENSITIVE_MAP_SUPPLIERS = new Supplier[] {
      CaseInsensitiveMap::new,
      LinkedCaseInsensitiveMap::new };

  @SuppressWarnings("unchecked")
  private static final Supplier<Map<String, String>>[] MODIFIABLE_MAP_SUPPLIERS = ArrayUtils.addAll(CASE_INSENSITIVE_MAP_SUPPLIERS,
                                                                                                    HashMap::new,
                                                                                                    IdentityHashMap::new,
                                                                                                    LinkedHashMap::new);

  private static final String ARRAY_OF_KEYS = "array of keys";

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnlyKeys(someInfo(), null, "name"));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_keys_array_is_null() {
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsOnlyKeys(someInfo(), actual, (String[]) null));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(keysToLookForIsNull(ARRAY_OF_KEYS));
  }

  @Test
  void should_fail_if_given_keys_array_is_empty() {
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsOnlyKeys(someInfo(), actual, emptyKeys()));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage(keysToLookForIsEmpty(ARRAY_OF_KEYS));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases",
      "caseInsensitiveMapsSuccessfulTestCases",
  })
  void should_succeed(Map<String, String> actual, String[] keys) {
    // WHEN/THEN
    maps.assertContainsOnlyKeys(info, actual, keys);
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(Collections.emptyMap(), emptyKeys()),
                     arguments(Collections.singletonMap("name", "Yoda"), array("name")),
                     arguments(new SingletonMap<>("name", "Yoda"), array("name")),
                     arguments(Collections.unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array("name", "job")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), array("name", "job")));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("name", "job")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("job", "name"))));
  }

  private static Stream<Arguments> caseInsensitiveMapsSuccessfulTestCases() {
    return Stream.of(CASE_INSENSITIVE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("name", "job")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("Name", "Job"))));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases",
      "caseInsensitiveMapsFailureTestCases",
  })
  void should_fail(Map<String, String> actual, String[] expectedKeys, Set<String> notFound, Set<String> notExpected) {
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnlyKeys(info, actual, expectedKeys));
    // THEN
    then(error).hasMessage(shouldContainOnlyKeys(actual, expectedKeys, notFound, notExpected).create());
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(Collections.emptyMap(), array("name"),
                               set("name"), emptySet()),
                     arguments(Collections.singletonMap("name", "Yoda"), array("color"),
                               set("color"), set("name")),
                     arguments(new SingletonMap<>("name", "Yoda"), array("color"),
                               set("color"), set("name")),
                     arguments(Collections.unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array("name", "color"),
                               set("color"), set("job")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), array("name", "color"),
                               set("color"), set("job")));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda")),
                                                          array("name", "color"), set("color"), emptySet()),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("name"), emptySet(), set("job")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("name", "color"), set("color"), set("job"))));
  }

  private static Stream<Arguments> caseInsensitiveMapsFailureTestCases() {
    return Stream.of(CASE_INSENSITIVE_MAP_SUPPLIERS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("name", "color"), set("color"), set("Job")),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          array("Name", "Color"), set("Color"), set("Job"))));
  }

}
