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
import static org.assertj.core.error.ShouldContainOnlyValues.shouldContainOnlyValues;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsEmpty;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.SingletonMap;
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
class Maps_assertContainsOnlyValues_with_Array_Test extends MapsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] values = { "value" };
    // WHEN
    AssertionError error = expectAssertionError(() -> maps.assertContainsOnlyValues(someInfo(), null, values));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_values_array_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsOnlyValues(someInfo(), actual, values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(valuesToLookForIsNull("array of values"));
  }

  @Test
  void should_fail_if_given_values_array_is_empty() {
    // GIVEN
    String[] values = emptyValues();
    // WHEN
    Throwable thrown = catchThrowable(() -> maps.assertContainsValues(someInfo(), actual, values));
    // THEN
    then(thrown).isInstanceOf(IllegalArgumentException.class).hasMessage(valuesToLookForIsEmpty("array of values"));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases"
  })
  void should_pass(Map<String, String> actual, String[] expected) {
    // GIVEN
    int initialSize = actual.size();
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertContainsOnlyValues(info, actual, expected));

    then(actual).hasSize(initialSize);
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(emptyMap(), emptyKeys()),
                     arguments(singletonMap("name", "Yoda"), array("Yoda")),
                     arguments(new SingletonMap<>("name", "Yoda"), array("Yoda")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), array("Yoda", "Jedi")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), array("Jedi", "Yoda")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), array("Yoda", "Jedi")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), array("Jedi", "Yoda")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), array("Yoda", "Jedi")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), array("Jedi", "Yoda")));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("Yoda", "Jedi")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("Jedi", "Yoda"))));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases"
  })
  void should_fail(Map<String, String> actual, String[] expected, Set<String> notFound, Set<String> notExpected) {
    // GIVEN
    int initialSize = actual.size();
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> maps.assertContainsOnlyValues(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldContainOnlyValues(actual, expected,
                                                                                        notFound, notExpected).create());

    then(actual).hasSize(initialSize);
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(emptyMap(),
                               array("name"),
                               set("name"),
                               emptySet()),
                     arguments(singletonMap("name", "Yoda"),
                               array("green"),
                               set("green"),
                               set("Yoda")),
                     arguments(new SingletonMap<>("name", "Yoda"),
                               array("green"),
                               set("green"),
                               set("Yoda")),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))),
                               array("Yoda", "green"),
                               set("green"),
                               set("Jedi")),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"),
                               array("Yoda", "green"),
                               set("green"),
                               set("Jedi")),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"),
                               array("Yoda", "green"),
                               set("green"),
                               set("Jedi")));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda")),
                                                          array("Yoda", "green"),
                                                          set("green"),
                                                          emptySet()),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("Yoda"),
                                                          emptySet(),
                                                          set("Jedi")),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          array("Yoda", "green"),
                                                          set("green"),
                                                          set("Jedi"))));
  }
}
