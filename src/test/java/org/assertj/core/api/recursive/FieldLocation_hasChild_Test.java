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

class FieldLocation_hasChild_Test {

  @ParameterizedTest(name = "{0} hasChild {1}")
  @MethodSource("hasChild")
  void hasChild_should_return_true(List<String> fieldPath, String child) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    // WHEN
    boolean result = field.hasChild(new FieldLocation(child));
    // THEN
    then(result).as("%s hasChild <%s>", field, child).isTrue();
  }

  private static Stream<Arguments> hasChild() {
    return Stream.of(arguments(list("name"), "name.first"),
                     arguments(list("name"), "name.first.second"),
                     arguments(list("one"), "one.two.three"),
                     arguments(list("name", "first"), "name.first.second"),
                     arguments(list("name", "[2]", "first"), "name.first.second"));
  }

  @ParameterizedTest(name = "{0} does not have child {1}")
  @MethodSource("notChild")
  void hasChild_should_return_false(List<String> fieldPath, String other) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    // WHEN
    boolean result = field.hasChild(new FieldLocation(other));
    // THEN
    then(result).as("%s does not have child <%s>", field, other).isFalse();
  }

  private static Stream<Arguments> notChild() {
    return Stream.of(arguments(list("defaultRole"), "defaultRoleName"),
                     arguments(list("name"), "name"),
                     arguments(list("name", "first"), "name.first"),
                     arguments(list("name", "first"), "name"),
                     arguments(list("name", "[2]", "first"), "name"),
                     arguments(list("person", "[1]", "first", "second"), "person.first"),
                     arguments(list("father", "name", "first"), "father"));
  }

}
