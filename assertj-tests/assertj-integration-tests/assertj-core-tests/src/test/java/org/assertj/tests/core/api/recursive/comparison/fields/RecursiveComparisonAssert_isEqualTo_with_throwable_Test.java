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
package org.assertj.tests.core.api.recursive.comparison.fields;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_with_throwable_Test extends WithComparingFieldsIntrospectionStrategyBaseTest {

  @ParameterizedTest
  @MethodSource
  void should_compare_throwable_recursively(Object actual, Object expected) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .isEqualTo(expected);
  }

  static Stream<Arguments> should_compare_throwable_recursively() {
    Exception withSuppressedException1 = new Exception("boom");
    withSuppressedException1.addSuppressed(new Throwable("bam"));
    Exception withSuppressedException2 = new Exception("boom");
    withSuppressedException2.addSuppressed(new Throwable("bam"));
    return Stream.of(arguments(new RuntimeException("boom"),
                               new RuntimeException("boom")),
                     arguments(new RuntimeException("boom"),
                               new Exception("boom")),
                     arguments(withSuppressedException1, withSuppressedException2),
                     arguments(new RuntimeException("boom", new Throwable("bam")),
                               new RuntimeException("boom", new Throwable("bam"))),
                     arguments(new RuntimeException("boom", new RuntimeException("bam")),
                               new Throwable("boom", new Throwable("bam"))),
                     arguments(new Throwable("bam"),
                               new Throwable("bam")));
  }

  @Test
  void should_fail_when_expected_is_not_a_throwable_in_strictTypeChecking_mode() {
    // GIVEN
    var actual = new Throwable("bam");
    var expected = new Person("Tom");
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("", actual, expected,
                                           "the compared values are considered different since the recursive comparison enforces strict type checking and the actual value type java.lang.Throwable is not equal to the expected value type org.assertj.tests.core.api.recursive.data.Person");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_expected_is_not_a_throwable() {
    // GIVEN
    var actual = new Throwable("bam");
    var expected = new Person("Tom");
    // WHEN/THEN
    ComparisonDifference difference = diff("", actual, expected,
                                           "Actual was compared to expected with equals because it is a java type (java.lang.Throwable) and expected is not (org.assertj.tests.core.api.recursive.data.Person)");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_actual_and_expected_have_different_messages() {
    // GIVEN
    var actual = new Throwable("bam");
    var expected = new Throwable("boom");
    // WHEN/THEN
    ComparisonDifference difference = javaTypeDiff("detailMessage", actual.getMessage(), expected.getMessage());
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_actual_and_expected_have_different_causes() {
    // GIVEN
    var actual = new Throwable("boom", new Throwable("bam"));
    var expected = new Throwable("boom", new Throwable("bim"));
    // WHEN/THEN
    ComparisonDifference difference = javaTypeDiff("cause.detailMessage", actual.getCause().getMessage(),
                                                   expected.getCause().getMessage());
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_actual_and_expected_have_different_suppressed_exceptions() {
    // GIVEN
    var actual = new Exception("boom");
    actual.addSuppressed(new Throwable("bam"));
    var expected = new Exception("boom");
    expected.addSuppressed(new Throwable("bim"));
    // WHEN/THEN
    ComparisonDifference difference = javaTypeDiff("suppressed[0].detailMessage", "bam", "bim");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_if_actual_type_inherits_from_expected_type_in_strictTypeChecking_mode() {
    // GIVEN
    var actual = new RuntimeException("boom");
    var expected = new Exception("boom");
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("", actual, expected,
                                           "the compared values are considered different since the recursive comparison enforces strict type checking and the actual value type java.lang.RuntimeException is not equal to the expected value type java.lang.Exception");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_actual_and_expected_have_unrelated_types() {
    // GIVEN
    var actual = new RuntimeException("boom");
    var expected = new IOException("boom");
    // WHEN/THEN
    ComparisonDifference difference = diff("", actual, expected,
                                           "actual is a java.lang.RuntimeException but expected is not (java.io.IOException)");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_fail_when_actual_and_expected_have_unrelated_types_type_in_strictTypeChecking_mode() {
    // GIVEN
    var actual = new RuntimeException("boom");
    var expected = new IOException("boom");
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // WHEN/THEN
    ComparisonDifference difference = diff("", actual, expected,
                                           "the compared values are considered different since the recursive comparison enforces strict type checking and the actual value type java.lang.RuntimeException is not equal to the expected value type java.io.IOException");
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

}
