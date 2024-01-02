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
package org.assertj.core.api.recursive;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class FieldLocation_hierarchyMatchesRegex_Test {

  @ParameterizedTest(name = "{0} field hierarchy matches regex {1}")
  @MethodSource
  void hierarchyMatchesRegex_should_return_true(List<String> fieldPath, String regex) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    Pattern pattern = Pattern.compile(regex);
    // WHEN
    boolean result = field.hierarchyMatchesRegex(pattern);
    // THEN
    then(result).as("%s field hierarchy matches regex %s", field, regex).isTrue();
  }

  private static Stream<Arguments> hierarchyMatchesRegex_should_return_true() {
    return Stream.of(arguments(list("name"), "name"),
                     arguments(list("name", "first"), "name"),
                     arguments(list("name", "first"), ".ame"),
                     arguments(list("name", "first", "nickname"), "name"),
                     arguments(list("name", "first", "nickname"), ".*me"),
                     arguments(list("name", "first", "nickname"), "name\\.first"),
                     arguments(list("name", "first", "nickname"), ".*first"),
                     arguments(list("name", "first"), "name\\.first"),
                     arguments(list("name", "first"), "nam....rst"));
  }

  @ParameterizedTest(name = "{0} field hierarchy does not match regex {1}")
  @MethodSource
  void hierarchyMatchesRegex_should_return_false(List<String> fieldPath, String regex) {
    // GIVEN
    FieldLocation field = new FieldLocation(fieldPath);
    Pattern pattern = Pattern.compile(regex);
    // WHEN
    boolean result = field.hierarchyMatchesRegex(pattern);
    // THEN
    then(result).as("%s field hierarchy does not match regex %s", field, regex).isFalse();
  }

  private static Stream<Arguments> hierarchyMatchesRegex_should_return_false() {
    return Stream.of(arguments(list("person", "name"), "name"),
                     arguments(list("person", "name"), "na.*"),
                     arguments(list("names"), "name"),
                     arguments(list("names"), ".*name"),
                     arguments(list("nickname"), "..name"),
                     arguments(list("name"), "ni.*"),
                     arguments(list("name"), "name\\.first"),
                     arguments(list("name"), "name.first"),
                     arguments(list("name"), "name.+"),
                     arguments(list("first", "nickname"), "name"),
                     arguments(list("first", "nickname"), "...name"));
  }
}
