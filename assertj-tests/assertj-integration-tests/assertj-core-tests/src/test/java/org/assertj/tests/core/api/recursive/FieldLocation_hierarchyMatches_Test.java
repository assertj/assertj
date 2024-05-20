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
package org.assertj.tests.core.api.recursive;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FieldLocation_hierarchyMatches_Test {

  @ParameterizedTest(name = "{0} is or is a child of {1}")
  @MethodSource
  void hierarchyMatches_should_return_true(List<String> fieldPath, String other) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    // WHEN
    boolean result = field.hierarchyMatches(other);
    // THEN
    then(result).as("%s is or is a child of %s", field, other).isTrue();
  }

  private static Stream<Arguments> hierarchyMatches_should_return_true() {
    return Stream.of(arguments(list("name"), "name"),
                     arguments(list("name", "first"), "name"),
                     arguments(list("name", "first", "nickname"), "name"),
                     arguments(list("name", "first", "nickname"), "name.first"),
                     arguments(list("name", "first"), "name.first"));
  }

  @ParameterizedTest(name = "{0} is not nor a child of {1}")
  @MethodSource
  void hierarchyMatches_should_return_false(List<String> fieldPath, String other) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    // WHEN
    boolean result = field.hierarchyMatches(other);
    // THEN
    then(result).as("%s is not nor a child of %s", field, other).isFalse();
  }

  private static Stream<Arguments> hierarchyMatches_should_return_false() {
    return Stream.of(arguments(list("person", "name"), "name"),
                     arguments(list("names"), "name"),
                     arguments(list("nickname"), "name"),
                     arguments(list("name"), "nickname"),
                     arguments(list("first", "nickname"), "name"));
  }
}
