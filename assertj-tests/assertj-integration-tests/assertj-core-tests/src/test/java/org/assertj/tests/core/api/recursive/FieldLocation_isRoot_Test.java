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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.util.Lists.list;

import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class FieldLocation_isRoot_Test {

  @ParameterizedTest(name = "{0}")
  @MethodSource
  void should_evaluate_object_as_root(FieldLocation fieldLocation) {
    assertThat(fieldLocation.isRoot()).isTrue();
  }

  private static Stream<FieldLocation> should_evaluate_object_as_root() {
    return Stream.of(rootFieldLocation(),
                     new FieldLocation(list("[0]")),
                     new FieldLocation(list("[1]")));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource
  void should_not_evaluate_object_as_root(FieldLocation fieldLocation) {
    assertThat(fieldLocation.isRoot()).isFalse();
  }

  private static Stream<FieldLocation> should_not_evaluate_object_as_root() {
    return Stream.of(new FieldLocation(list("[0]", "name")),
                     new FieldLocation(list("name")));

  }
}
