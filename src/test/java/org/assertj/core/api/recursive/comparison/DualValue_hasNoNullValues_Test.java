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
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("DualValue hasNoContainerValues")
class DualValue_hasNoNullValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource("values")
  void should_return_false_when_actual_or_expected_is_null_and_true_otherwise(Object actual, Object expected,
                                                                              boolean expectedResult) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, expected);
    // WHEN
    boolean hasNoNullValues = dualValue.hasNoNullValues();
    // THEN
    then(hasNoNullValues).isEqualTo(expectedResult);
  }

  static Stream<Arguments> values() {
    return Stream.of(Arguments.of("abc", "foo", true),
                     Arguments.of("abc", null, false),
                     Arguments.of(null, "abc", false),
                     Arguments.of(null, null, false));
  }
}
