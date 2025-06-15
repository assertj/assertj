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
 * Copyright 2012-2025 the original author or authors.
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

class DualValue_isExpectedAThrowable_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest
  @MethodSource
  void isExpectedAThrowable_should_return_true_when_expected_is_a_throwable(Throwable expected) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "unused", expected);
    // WHEN
    boolean isExpectedAThrowable = dualValue.isExpectedAThrowable();
    // THEN
    then(isExpectedAThrowable).isTrue();
  }

  static Stream<Throwable> isExpectedAThrowable_should_return_true_when_expected_is_a_throwable() throws Exception {
    return Stream.of(new RuntimeException("boom"),
                     new Throwable("bam"));
  }

  @Test
  void isExpectedAThrowable_should_return_false_when_expected_is_not_a_throwable() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "unused", Pair.of(1, "a"));
    // WHEN
    boolean isExpectedAThrowable = dualValue.isExpectedAThrowable();
    // THEN
    then(isExpectedAThrowable).isFalse();
  }

  @Test
  void isExpectedAThrowable_should_return_false_when_expected_is_null() {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, "unused", null);
    // WHEN
    boolean isExpectedAThrowable = dualValue.isExpectedAThrowable();
    // THEN
    then(isExpectedAThrowable).isFalse();
  }

}
