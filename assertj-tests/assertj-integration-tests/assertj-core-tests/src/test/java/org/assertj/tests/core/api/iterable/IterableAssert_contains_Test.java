/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.iterable;

import static java.lang.String.CASE_INSENSITIVE_ORDER;
import static java.util.Collections.emptySet;
import static java.util.Collections.singleton;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ActualIsNotEmpty.actualIsNotEmpty;
import static org.assertj.core.error.ShouldContain.shouldContain;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.internal.ErrorMessages.valuesToLookForIsNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.set;
import static org.assertj.tests.core.testkit.SinglyIterableFactory.createSinglyIterable;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.api.comparisonstrategy.ComparatorBasedComparisonStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.collect.Sets;

/// @author Alex Ruiz
/// @author Joel Costigliola
class IterableAssert_contains_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    List<String> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).contains("Yoda"));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_throw_error_if_given_values_array_is_null() {
    // GIVEN
    String[] values = null;
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(list("Luke", "Yoda", "Leia")).contains(values));
    // THEN
    then(exception).hasMessage(valuesToLookForIsNull());
  }

  @Test
  void should_fail_if_given_values_array_is_empty_and_actual_is_not() {
    // GIVEN
    List<String> actual = list("Luke", "Yoda", "Leia");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).contains());
    // THEN
    then(assertionError).hasMessage(actualIsNotEmpty(actual).create());
  }

  @ParameterizedTest
  @MethodSource("successfulTestCases")
  void should_pass(Iterable<String> actual, String[] expected) {
    // WHEN/THEN
    then(actual).as(actual.getClass().getName())
                .contains(expected);
  }

  @ParameterizedTest
  @MethodSource({
      "successfulTestCases",
      "caseInsensitiveSuccessfulTestCases"
  })
  void should_pass_with_case_insensitive_comparison_strategy(Iterable<String> actual, String[] expected) {
    // WHEN/THEN
    then(actual).as(actual.getClass().getName())
                .usingElementComparator(CASE_INSENSITIVE_ORDER)
                .contains(expected);
  }

  private static Stream<Arguments> successfulTestCases() {
    return Stream.of(arguments(emptySet(), new String[0]),
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
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).contains(expected));
    // THEN
    then(assertionError).as(actual.getClass().getName())
                        .hasMessage(shouldContain(actual, expected, notFound).create());
  }

  @ParameterizedTest
  @MethodSource({
      "failureTestCases",
      "caseInsensitiveFailureTestCases"
  })
  void should_fail_with_case_insensitive_comparison_strategy(Iterable<String> actual, String[] expected, Set<String> notFound) {
    // GIVEN
    var comparisonStrategy = new ComparatorBasedComparisonStrategy(CASE_INSENSITIVE_ORDER);
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingElementComparator(CASE_INSENSITIVE_ORDER)
                                                                      .contains(expected));
    // THEN
    then(assertionError).as(actual.getClass().getName())
                        .hasMessage(shouldContain(actual, expected, notFound, comparisonStrategy).create());
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
