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

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FieldLocation_startsWith_Test {

  @ParameterizedTest(name = "{0} matches {1}")
  @MethodSource
  void startsWith_should_match_fields_starting_with_given_field_path(List<String> fieldPath, String startFieldPath) {
    // GIVEN
    FieldLocation underTest = new FieldLocation(fieldPath);
    // WHEN
    boolean match = underTest.startsWith(startFieldPath);
    // THEN
    then(match).as("%s starts with %s", underTest, startFieldPath).isTrue();
  }

  private static Stream<Arguments> startsWith_should_match_fields_starting_with_given_field_path() {
    return Stream.of(arguments(list("name"), "name"),
                     arguments(list("name", "first"), "name"),
                     arguments(list("name", "first"), "name.first"),
                     arguments(list("name", "[2]", "first"), "name"),
                     arguments(list("name", "[2]", "first"), "name.first"),
                     arguments(list("person", "[1]", "first", "second"), "person.first"),
                     arguments(list("father", "name", "first"), "father"));
  }

}
