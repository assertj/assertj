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
package org.assertj.core.internal.doubles;

import static java.lang.Double.NEGATIVE_INFINITY;
import static java.lang.Double.NaN;
import static java.lang.Double.POSITIVE_INFINITY;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.stream.Stream;

import org.assertj.core.internal.DoublesBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class Doubles_isNanOrInfinite_Test extends DoublesBaseTest {

  @ParameterizedTest
  @MethodSource
  void should_evaluate_as_NaN_or_infinity(double value) {
    // WHEN
    boolean isNanOrInfinite = doubles.isNanOrInfinite(value);
    // THEN
    then(isNanOrInfinite).isTrue();
  }

  @ParameterizedTest
  @CsvSource({ "-1.0", "0.0", "1.0" })
  void should_not_evaluate_as_NaN_or_infinity(double actual) {
    // THEN
    boolean isNanOrInfinite = doubles.isNanOrInfinite(actual);
    // THEN
    then(isNanOrInfinite).isFalse();
  }

  static Stream<Double> should_evaluate_as_NaN_or_infinity() {
    return Stream.of(NaN, POSITIVE_INFINITY, NEGATIVE_INFINITY);
  }
}
