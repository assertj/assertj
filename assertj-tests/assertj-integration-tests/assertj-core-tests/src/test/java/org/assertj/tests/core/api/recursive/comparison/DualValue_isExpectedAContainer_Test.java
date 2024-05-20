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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DualValue_isExpectedAContainer_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource("values")
  void should_indicates_whether_expected_is_a_container_or_not(Object expected, boolean expectedResult) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, null, expected);
    // WHEN
    boolean isExpectedAContainer = dualValue.isExpectedAContainer();
    // THEN
    then(isExpectedAContainer).isEqualTo(expectedResult);
  }

  static Stream<Arguments> values() {
    return Stream.of(Arguments.of(list(1, 2), true),
                     Arguments.of(array("foo"), true),
                     Arguments.of(new int[] { 1, 2, 3 }, true),
                     Arguments.of(new long[] { 1, 2, 3 }, true),
                     Arguments.of(new float[] { 1, 2, 3 }, true),
                     Arguments.of(new double[] { 1, 2, 3 }, true),
                     Arguments.of(new byte[] { 1, 2, 3 }, true),
                     Arguments.of(new char[] { 1, 2, 3 }, true),
                     Arguments.of(new boolean[] { true, false }, true),
                     Arguments.of(newLinkedHashSet("foo"), true),
                     Arguments.of(newHashMap("foo", "bar"), true),
                     Arguments.of(Optional.of("bar"), true),
                     Arguments.of(new AtomicReference<>("foo"), true),
                     Arguments.of(new AtomicInteger(123), true),
                     Arguments.of(new AtomicLong(123), true),
                     Arguments.of(new AtomicBoolean(true), true),
                     Arguments.of(new AtomicIntegerArray(new int[] { 1, 2, 3 }), true),
                     Arguments.of(new AtomicLongArray(new long[] { 1, 2, 3 }), true),
                     Arguments.of(new AtomicReferenceArray<>(array("test")), true),
                     Arguments.of("abc", false),
                     Arguments.of(null, false));
  }
}
