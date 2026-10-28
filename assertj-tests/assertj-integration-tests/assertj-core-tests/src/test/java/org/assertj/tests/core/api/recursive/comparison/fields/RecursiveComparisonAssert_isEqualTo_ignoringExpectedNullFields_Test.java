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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_ignoringExpectedNullFields_Test
    extends WithComparingFieldsIntrospectionStrategyBaseTest {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource
  void should_pass_when_expected_null_fields_are_ignored(Object actual, Object expected,
                                                         @SuppressWarnings("unused") String testDescription) {

    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .ignoringExpectedNullFields()
                .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_when_expected_null_fields_are_ignored() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;

    Person person2 = new Person(null);

    Person person3 = new Person("John");
    person3.home.address = null;

    Person person4 = new Person(null);
    person3.home.address = null;

    return Stream.of(arguments(person1, person2, "first level expected null field"),
                     arguments(person1, person3, "nested expected null field"),
                     arguments(person1, person4, "multiple expected null fields"));
  }

  @Test
  void should_fail_if_actual_has_null_fields_but_expected_does_not() {
    // GIVEN
    Person actual = new Person(null);
    Person expected = new Person("Sherlock");
    expected.neighbour = new Person("John");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .ignoringExpectedNullFields()
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContainingAll("field/property 'name'",
                                                 "field/property 'neighbour'")
                        .hasMessageNotContaining("field/property 'dateOfBirth'");
  }

  @Test
  void should_not_ignore_other_differences_when_ignoring_expected_null_fields() {
    // GIVEN
    Person actual = new Person("Sherlock");
    actual.home.address.number = 221;
    Person expected = new Person(null);
    expected.home.address.number = 7;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .ignoringExpectedNullFields()
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("field/property 'home.address.number'")
                        .hasMessageNotContaining("field/property 'name'");
  }

}
