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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal.iterables;

import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.internal.iterables.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.internal.Iterables;
import org.assertj.core.internal.IterablesBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Sets;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Iterables_assertContains_Test extends IterablesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String[] values = array("Yoda");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertContains(someInfo(), null, values));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_given_values_array_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> iterables.assertContains(someInfo(), actual, values));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_given_values_array_is_empty() {
    // GIVEN
    String[] values = array();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> iterables.assertContains(someInfo(), actual, values));
    // THEN
    then(assertionError).isNotNull();
  }

  @ParameterizedTest
  @MethodSource("successfulTestCases")
  void should_pass(Iterable<String> actual, Object[] expected) {
    // GIVEN
    Iterables underTest = iterables;
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> underTest.assertContains(info, actual, expected));
  }

  @ParameterizedTest
  @MethodSource({
      "successfulTestCases",
      "caseInsensitiveSuccessfulTestCases"
  })
  void should_pass_with_case_insensitive_comparison_strategy(Iterable<String> actual, Object[] expected) {
    // GIVEN
    Iterables underTest = iterablesWithCaseInsensitiveComparisonStrategy;
    // WHEN/THEN
    assertThatNoException().as(actual.getClass().getName())
                           .isThrownBy(() -> underTest.assertContains(info, actual, expected));
  }

  private static Stream<Arguments> successfulTestCases() {
    return Stream.of(arguments(emptySet(), array()),
                     arguments(list("Luke", "Yoda", "Leia"), array("Luke")),
                     arguments(list("Luke", "Yoda", "Leia"), array("Leia", "Yoda")),
                     arguments(list("Luke", "Yoda", "Leia"), array("Luke", "Yoda")),
                     arguments(list("Luke", "Yoda", "Leia"), array("Luke", "Luke")),
                     arguments(list("Luke", "Yoda", "Leia", "Luke", "Luke"), array("Luke")),
                     arguments(createSinglyIterable(list("Luke", "Yoda", "Leia")), array("Luke", "Yoda", "Leia")),
                     arguments(singleton("Luke"), array("Luke")),
                     arguments(Sets.union(singleton("Luke"), singleton("Yoda")), array("Luke")));
  }

  private static Stream<Arguments> caseInsensitiveSuccessfulTestCases() {
    return Stream.of(arguments(list("Luke", "Yoda", "Leia"), array("LUKE")),
                     arguments(list("Luke", "Yoda", "Leia"), array("LEIA", "yODa")),
                     arguments(list("Luke", "Yoda", "Leia"), array("luke", "YODA")),
                     arguments(list("Luke", "Yoda", "Leia"), array("LUke", "LuKe")),
                     arguments(list("Luke", "Luke"), array("LUke")));
  }

  @ParameterizedTest
  @MethodSource("failureTestCases")
  void should_fail(Iterable<String> actual, String[] expected, Set<String> notFound) {
    // GIVEN
    Iterables underTest = iterables;
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> underTest.assertContains(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldContain(actual, expected, notFound).create());
  }

  @ParameterizedTest
  @MethodSource({
      "failureTestCases",
      "caseInsensitiveFailureTestCases"
  })
  void should_fail_with_case_insensitive_comparison_strategy(Iterable<String> actual, String[] expected, Set<String> notFound) {
    // GIVEN
    Iterables underTest = iterablesWithCaseInsensitiveComparisonStrategy;
    // WHEN
    assertThatExceptionOfType(AssertionError.class).as(actual.getClass().getName())
                                                   .isThrownBy(() -> underTest.assertContains(info, actual, expected))
                                                   // THEN
                                                   .withMessage(shouldContain(actual, expected, notFound,
                                                                              underTest.getComparisonStrategy()).create());
  }

  private static Stream<Arguments> failureTestCases() {
    return Stream.of(arguments(set("Luke", "Yoda", "Leia"),
                               array("Han", "Luke"),
                               set("Han")),
                     arguments(list("Luke", "Yoda", "Leia"),
                               array("Han", "Luke"),
                               set("Han")),
                     arguments(Sets.union(singleton("Luke"), singleton("Yoda")),
                               array("Han", "Luke"),
                               set("Han")));
  }

  private static Stream<Arguments> caseInsensitiveFailureTestCases() {
    return Stream.of(arguments(set("Luke", "Yoda", "Leia"),
                               array("Han", "LUKE"),
                               set("Han")));
  }

}
