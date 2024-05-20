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
import static java.util.Collections.singletonMap;
import static java.util.Collections.unmodifiableMap;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldNotContainKey.shouldNotContainKey;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
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

/**
 * @author Nicolas François
 * @author Joel Costigliola
 */
class Maps_assertDoesNotContainKey_Test extends MapsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String key = "name";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertDoesNotContainKey(someInfo(), null, key));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases",
      "caseInsensitiveMapsSuccessfulTestCases",
  })
  void should_pass(Map<String, String> actual, String expected) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertDoesNotContainKey(info, actual, expected));
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(emptyMap(), "name"),
                     arguments(singletonMap("name", "Yoda"), "color"),
                     arguments(new SingletonMap<>("name", "Yoda"), "color"),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), "color"),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), "color"),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), "color"),
                     // implementation not permitting null keys
                     arguments(Jdk11.Map.of("name", "Yoda"), null));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda")),
                                                          "color"),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          "color")));
  }

  private static Stream<Arguments> caseInsensitiveMapsSuccessfulTestCases() {
    return Stream.of(ArrayUtils.add(CASE_INSENSITIVE_MAPS, CaseInsensitiveMap::new))
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          "color"),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          "Color")));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases",
      "caseInsensitiveMapsFailureTestCases",
  })
  void should_fail(Map<String, String> actual, String expected) {
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> maps.assertDoesNotContainKey(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldNotContainKey(actual, expected).create());
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(singletonMap("name", "Yoda"), "name"),
                     arguments(new SingletonMap<>("name", "Yoda"), "name"),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), "name"),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), "name"),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), "name"));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")), "name"),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")), "job")));
  }

  private static Stream<Arguments> caseInsensitiveMapsFailureTestCases() {
    return Stream.of(ArrayUtils.add(CASE_INSENSITIVE_MAPS, CaseInsensitiveMap::new))
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          "name"),
                                                arguments(mapOf(supplier, entry("NAME", "Yoda"), entry("Job", "Jedi")),
                                                          "Name")));
  }

}
