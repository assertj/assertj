/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison.dualvalue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.tests.core.api.recursive.data.DualValueUtil.dualValue;
import static org.assertj.tests.core.api.recursive.data.DualValueUtil.rootDualValue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DualValue_sameValues_Test {

  @ParameterizedTest
  @MethodSource
  void sameValues_should_return_true_when_dual_values_refer_to_the_same_instances(DualValue dualValue1, DualValue dualValue2) {
    assertThat(dualValue1.sameValues(dualValue2)).isTrue();
  }

  static Stream<Arguments> sameValues_should_return_true_when_dual_values_refer_to_the_same_instances() {
    Object value1 = new Object();
    Object value2 = new Object();
    var dualValue1 = rootDualValue(value1, value2);
    var dualValue2 = rootDualValue(value1, value2);
    var dualValue3 = dualValue("foo", value1, value2);
    return Stream.of(arguments(dualValue1, dualValue2),
                     arguments(dualValue1, dualValue1),
                     arguments(dualValue2, dualValue1),
                     arguments(dualValue1, dualValue3),
                     arguments(dualValue1, dualValue3));
  }

  @ParameterizedTest
  @MethodSource
  void sameValues_should_return_false_when_dual_values_refer_to_different_instances(DualValue dualValue1, DualValue dualValue2) {
    assertThat(dualValue1.sameValues(dualValue2)).isFalse();
  }

  static Stream<Arguments> sameValues_should_return_false_when_dual_values_refer_to_different_instances() {
    Object value1 = new Object();
    Object value2 = new Object();
    Object value3 = new Object();
    var dualValue1 = rootDualValue(value1, value2);
    var dualValue2 = rootDualValue(value1, value3);
    var dualValue3 = dualValue("foo", value1, value3);
    var dualValue4 = rootDualValue(new Object(), value2);
    return Stream.of(arguments(dualValue1, dualValue2),
                     arguments(dualValue2, dualValue1),
                     arguments(dualValue1, dualValue3),
                     arguments(dualValue1, dualValue4));
  }

}
