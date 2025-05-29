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
package org.assertj.tests.core.api.recursive.comparison.legacy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.tests.core.api.recursive.data.WithObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@SuppressWarnings("unused")
class RecursiveComparisonAssert_isEqualTo_ignoringTransientFields_Test extends WithLegacyIntrospectionStrategyBaseTest {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_pass_when_actual_transient_fields_are_ignored(Object actual, Object expected, String testDescription) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringTransientFields()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_when_actual_transient_fields_are_ignored() {
    var actual1 = new WithTransientFields("Jack transient", "Jack");
    var expected1 = new WithTransientFields("John transient", "Jack");
    var actual2 = new WithObject(new WithTransientFields("Jack transient", "Jack"));
    var expected2 = new WithObject(new WithTransientFields("John transient", "Jack"));

    return Stream.of(arguments(actual1, expected1, "same data, except for transient fields"),
                     arguments(actual1, expected1, "same data, except for nested transient fields"));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_all_transient_fields_are_ignored() {
    // GIVEN
    var actual = new WithTransientFields("Jack transient", "Jack");
    var expected = new WithTransientFields("John transient", "Jeff");
    recursiveComparisonConfiguration.ignoreTransientFields();
    // WHEN/THEN
    ComparisonDifference comparisonDifference = javaTypeDiff("name", "Jack", "Jeff");
    compareRecursivelyFailsWithDifferences(actual, expected, comparisonDifference);
  }

  @SuppressWarnings("ClassCanBeRecord")
  static class WithTransientFields {
    final transient String transientName;
    final String name;

    WithTransientFields(String transientName, String name) {
      this.transientName = transientName;
      this.name = name;
    }
  }

}
