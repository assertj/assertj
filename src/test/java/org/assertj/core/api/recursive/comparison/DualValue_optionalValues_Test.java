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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class DualValue_optionalValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @Test
  public void isActualFieldAnOptional_should_return_true_when_actual_is_an_optional() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Optional.empty(), "abc");
    // WHEN
    boolean actualFieldIsArray = dualValue.isActualFieldAnOptional();
    // THEN
    assertThat(actualFieldIsArray).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  public void isActualFieldAnOptional_should_return_false_when_actual_is_not_an_optional(Object actualField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), actualField);
    // WHEN
    boolean actualFieldIsArray = dualValue.isActualFieldAnOptional();
    // THEN
    assertThat(actualFieldIsArray).isFalse();
  }

  @Test
  public void isExpectedFieldAnOptional_should_return_true_when_expected_is_an_optional() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "abc", Optional.of(""));
    // WHEN
    boolean expectedFieldIsArray = dualValue.isExpectedFieldAnOptional();
    // THEN
    assertThat(expectedFieldIsArray).isTrue();
  }

  @ParameterizedTest
  @MethodSource("nonOptional")
  public void isExpectedFieldAnOptional_should_return_false_when_expected_is_not_an_optional(Object expectedField) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), expectedField);
    // WHEN
    boolean expectedFieldIsArray = dualValue.isExpectedFieldAnOptional();
    // THEN
    assertThat(expectedFieldIsArray).isFalse();
  }

  static Stream<Object> nonOptional() {
    return Stream.of(123, null, "abc");
  }

}
