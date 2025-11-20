/*
 * Copyright 2012-2025 the original author or authors.
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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DualValue_isActualAThrowable_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest
  @MethodSource
  void isActualAThrowable_should_return_true_when_actual_is_a_throwable(Throwable actual) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, "unused");
    // WHEN
    boolean isActualAThrowable = dualValue.isActualAThrowable();
    // THEN
    then(isActualAThrowable).isTrue();
  }

  static Stream<Throwable> isActualAThrowable_should_return_true_when_actual_is_a_throwable() throws Exception {
    return Stream.of(new RuntimeException("boom"),
                     new Throwable("bam"));
  }

  @Test
  void isActualAThrowable_should_return_false_when_actual_is_not_a_throwable() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, Pair.of(1, "a"), "unused");
    // WHEN
    boolean isActualAThrowable = dualValue.isActualAThrowable();
    // THEN
    then(isActualAThrowable).isFalse();
  }

  @Test
  void isActualAThrowable_should_return_false_when_actual_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, "unused");
    // WHEN
    boolean isActualAThrowable = dualValue.isActualAThrowable();
    // THEN
    then(isActualAThrowable).isFalse();
  }

}
