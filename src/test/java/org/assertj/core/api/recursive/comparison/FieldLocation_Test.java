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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import nl.jqno.equalsverifier.EqualsVerifier;

class FieldLocation_Test {

  @Test
  void should_honor_equals_contract() {
    // WHEN/THEN
    EqualsVerifier.forClass(FieldLocation.class)
                  .verify();
  }

  @Test
  void compareTo_should_order_field_location_by_alphabetical_path() {
    // GIVEN
    FieldLocation fieldLocation1 = new FieldLocation(list("a"));
    FieldLocation fieldLocation2 = new FieldLocation(list("a.b"));
    FieldLocation fieldLocation3 = new FieldLocation(list("aaa"));
    FieldLocation fieldLocation4 = new FieldLocation(list("z"));
    List<FieldLocation> fieldLocations = list(fieldLocation2, fieldLocation1, fieldLocation3, fieldLocation4);
    Collections.shuffle(fieldLocations);
    // WHEN
    Collections.sort(fieldLocations);
    // THEN
    then(fieldLocations).containsExactly(fieldLocation1, fieldLocation2, fieldLocation3, fieldLocation4);
  }

  @ParameterizedTest(name = "{0} matches {1}")
  @MethodSource
  void matches_should_match_fields(List<String> fieldPath, String matchingFieldPath) {
    // GIVEN
    FieldLocation underTest = new FieldLocation(fieldPath);
    // WHEN
    boolean match = underTest.matches(matchingFieldPath);
    // THEN
    then(match).as("%s matches %s", underTest, matchingFieldPath).isTrue();
  }

  private static Stream<Arguments> matches_should_match_fields() {
    return Stream.of(arguments(list("name"), "name"),
                     arguments(list("name", "first"), "name.first"),
                     arguments(list("name", "[2]", "first"), "name.first"),
                     arguments(list("[0]", "first"), "first"),
                     arguments(list("[1]", "first", "second"), "first.second"),
                     arguments(list("person", "[1]", "first", "second"), "person.first.second"),
                     arguments(list("father", "name", "first"), "father.name.first"));
  }

  @Test
  void toString_should_succeed() {
    // GIVEN
    FieldLocation underTest = new FieldLocation(list("location"));
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("FieldLocation [pathToUseInRules=location, decomposedPath=[location]]");
  }

}
