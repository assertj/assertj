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
import static org.assertj.core.error.ShouldNotContainValue.shouldNotContainValue;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.collections4.map.SingletonMap;
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
class Maps_assertDoesNotContainValue_Test extends MapsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String value = "Yoda";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> maps.assertDoesNotContainValue(someInfo(), null, value));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsSuccessfulTestCases",
      "modifiableMapsSuccessfulTestCases",
  })
  void should_pass(Map<String, String> actual, String expected) {
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> maps.assertDoesNotContainValue(info, actual, expected));
  }

  private static Stream<Arguments> unmodifiableMapsSuccessfulTestCases() {
    return Stream.of(arguments(emptyMap(), "Yoda"),
                     arguments(singletonMap("name", "Yoda"), "green"),
                     arguments(new SingletonMap<>("name", "Yoda"), "green"),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), "green"),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), "green"),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), "green"),
                     // implementation not permitting null keys
                     arguments(Jdk11.Map.of("name", "Yoda"), null));
  }

  private static Stream<Arguments> modifiableMapsSuccessfulTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda")),
                                                          "green"),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")),
                                                          "green")));
  }

  @ParameterizedTest
  @MethodSource({
      "unmodifiableMapsFailureTestCases",
      "modifiableMapsFailureTestCases",
  })
  void should_fail(Map<String, String> actual, String expected) {
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> maps.assertDoesNotContainValue(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldNotContainValue(actual, expected).create());
  }

  private static Stream<Arguments> unmodifiableMapsFailureTestCases() {
    return Stream.of(arguments(singletonMap("name", "Yoda"), "Yoda"),
                     arguments(new SingletonMap<>("name", "Yoda"), "Yoda"),
                     arguments(unmodifiableMap(mapOf(entry("name", "Yoda"), entry("job", "Jedi"))), "Yoda"),
                     arguments(ImmutableMap.of("name", "Yoda", "job", "Jedi"), "Yoda"),
                     arguments(Jdk11.Map.of("name", "Yoda", "job", "Jedi"), "Yoda"));
  }

  private static Stream<Arguments> modifiableMapsFailureTestCases() {
    return Stream.of(MODIFIABLE_MAPS)
                 .flatMap(supplier -> Stream.of(arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")), "Yoda"),
                                                arguments(mapOf(supplier, entry("name", "Yoda"), entry("job", "Jedi")), "Jedi")));
  }

}
