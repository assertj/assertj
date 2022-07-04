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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FieldLocation_hasParent_Test {

  @ParameterizedTest(name = "{0} hasParent {1}")
  @MethodSource("hasParent")
  void hasParent_should_return_true(List<String> fieldPath, String parent) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    // WHEN
    boolean result = field.hasParent(new FieldLocation(parent));
    // THEN
    then(result).as("%s hasParent <%s>", field, parent).isTrue();
  }

  private static Stream<Arguments> hasParent() {
    return Stream.of(arguments(list("name", "first"), "name"),
                     arguments(list("name", "[2]", "first"), "name"),
                     arguments(list("person", "[1]", "first", "second"), "person.first"),
                     arguments(list("father", "name", "first"), "father"));
  }

  @ParameterizedTest(name = "{0} does not have parent {1}")
  @MethodSource("notParent")
  void hasParent_should_return_false(List<String> fieldPath, String parent) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    // WHEN
    boolean result = field.hasParent(new FieldLocation(parent));
    // THEN
    then(result).as("%s does not have parent <%s>", field, parent).isFalse();
  }

  private static Stream<Arguments> notParent() {
    return Stream.of(arguments(list("defaultRole"), "defaultRoleName"),
                     arguments(list("name"), "name"),
                     arguments(list("name"), "name.first"),
                     arguments(list("name", "[2]", "first"), "name.first"),
                     arguments(list("person", "first"), "person.first.second"),
                     arguments(list("father"), "father.name.first"));
  }

}
