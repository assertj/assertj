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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonConfiguration_isOrIsChildOfAnyComparedFields_Test {

  private final RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();

  @ParameterizedTest(name = "fieldNamesToCompare={0} / currentField={1}")
  @MethodSource
  void should_return_true_if_given_field_location_is_or_is_a_child_of_a_compared_field(String[] fieldNamesToCompare,
                                                                                       String currentField) {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields(fieldNamesToCompare);
    FieldLocation currentFieldLocation = new FieldLocation(currentField);
    // WHEN
    boolean isOrIsChildOfAnyComparedFields = recursiveComparisonConfiguration.isOrIsChildOfAnyComparedFields(currentFieldLocation);
    // THEN
    then(isOrIsChildOfAnyComparedFields).isTrue();
  }

  private static Stream<Arguments> should_return_true_if_given_field_location_is_or_is_a_child_of_a_compared_field() {

    return Stream.of(arguments(array("name"), "name"),
                     arguments(array("name", "age"), "name"),
                     arguments(array("neighbour"), "neighbour.name"),
                     arguments(array("neighbour"), "neighbour.name.firstName"),
                     arguments(array("neighbour.name", "neighbour.number"), "neighbour.name"),
                     arguments(array("neighbour.name", "neighbour.number"), "neighbour.name.firstName"));
  }

  @ParameterizedTest(name = "fieldNamesToCompare={0} / currentField={1}")
  @MethodSource
  void should_return_false_if_given_field_location_is_parent_of_a_compared_field(String[] fieldNamesToCompare,
                                                                                 String currentField) {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields(fieldNamesToCompare);
    FieldLocation currentFieldLocation = new FieldLocation(currentField);
    // WHEN
    boolean isOrIsChildOfAnyComparedFields = recursiveComparisonConfiguration.isOrIsChildOfAnyComparedFields(currentFieldLocation);
    // THEN
    then(isOrIsChildOfAnyComparedFields).isFalse();
  }

  private static Stream<Arguments> should_return_false_if_given_field_location_is_parent_of_a_compared_field() {

    return Stream.of(arguments(array("name.firstName"), "name"),
                     arguments(array("neighbour.name", "neighbour.number"), "neighbour"),
                     arguments(array("neighbour.name.firstName", "neighbour.number"), "neighbour.name"));
  }

}
