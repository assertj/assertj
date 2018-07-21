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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FloatComparatorTest {

  private static FloatComparator comparator = new FloatComparator(0.01f);

  public static boolean nearlyEqual(Float a, Float b) {
    return comparator.compare(a, b) == 0;
  }

  public static boolean nearlyEqual(Float a, Float b, float epsilon) {
    return new FloatComparator(epsilon).compare(a, b) == 0;
  }

  @ParameterizedTest
  @CsvSource({
    "1.0, 1.0",
    "1.001, 1.0",
    "1.0, 1.001",
    "0.001, 0.0",
    "0.01, 0.0",
    "0.0, 0.001",
    "-1.001, -1.0",
    "-1.0, -1.001",
    ","
  })
  public void should_be_equal_if_difference_is_less_than_or_equal_to_epsilon(Float actual, Float other) {
    assertThat(nearlyEqual(actual, other)).as("comparing %f to %f with epsilon %f", actual, other,
                                              comparator.getEpsilon())
                                          .isTrue();
  }

  @ParameterizedTest
  @CsvSource({
    "1.0, 2.0",
    "1.010001, 1.0",
    "1.0, 1.010001",
    "0.0, 0.010001",
    "-1.010001, -1.0",
    "-1.0, -1.010001",
    ", 1.0",
    "1.0,"
  })
  public void should_not_be_equal_if_difference_is_more_than_epsilon(Float actual, Float other) {
    assertThat(nearlyEqual(actual, other)).as("comparing %f to %f with epsilon %f", actual, other,
                                              comparator.getEpsilon())
                                          .isFalse();
  }

}
