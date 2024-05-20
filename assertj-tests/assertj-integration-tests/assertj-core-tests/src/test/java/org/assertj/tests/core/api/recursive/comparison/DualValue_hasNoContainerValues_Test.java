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

class DualValue_hasNoContainerValues_Test {

  private static final List<String> PATH = list("foo", "bar");

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource("values")
  void should_return_false_when_actual_or_expected_is_a_container_value_and_true_otherwise(Object actual, Object expected,
                                                                                           boolean expectedResult) {
    // GIVEN
    DualValue dualValue = new DualValue(PATH, actual, expected);
    // WHEN
    boolean hasNoContainerTypes = dualValue.hasNoContainerValues();
    // THEN
    then(hasNoContainerTypes).isEqualTo(expectedResult);
  }

  static Stream<Arguments> values() {
    return Stream.of(Arguments.of(list("foo"), list(1, 2), false),
                     Arguments.of(list("foo"), "abc", false),
                     Arguments.of("abc", list("foo"), false),
                     Arguments.of(array("foo"), array(1, 2), false),
                     Arguments.of(array("foo"), "abc", false),
                     Arguments.of("abc", array("foo"), false),
                     Arguments.of(newHashMap("foo", "bar"), newHashMap("foo", "bar"), false),
                     Arguments.of(newHashMap("foo", "bar"), "abc", false),
                     Arguments.of("abc", newHashMap("foo", "bar"), false),
                     Arguments.of(Optional.of("foo"), Optional.of("bar"), false),
                     Arguments.of(Optional.of("foo"), "abc", false),
                     Arguments.of("abc", Optional.of("foo"), false),
                     Arguments.of(new AtomicReference<>("abc"), new AtomicReference<>("foo"), false),
                     Arguments.of(new AtomicReference<>("abc"), Optional.of("foo"), false),
                     Arguments.of(new AtomicInteger(123), new AtomicInteger(456), false),
                     Arguments.of(new AtomicLong(123), new AtomicLong(456), false),
                     Arguments.of(new AtomicInteger(123), new AtomicLong(456), false),
                     Arguments.of(new AtomicLong(123), new AtomicInteger(456), false),
                     Arguments.of(new AtomicBoolean(true), new AtomicBoolean(false), false),
                     Arguments.of(new AtomicBoolean(Boolean.TRUE), new AtomicBoolean(Boolean.FALSE), false),
                     Arguments.of(new AtomicIntegerArray(new int[] { 1, 2, 3 }), new AtomicIntegerArray(new int[] { 1, 2 }),
                                  false),
                     Arguments.of(new AtomicLongArray(new long[] { 1, 2, 3 }), new AtomicLongArray(new long[] { 1, 2 }), false),
                     Arguments.of(new AtomicReferenceArray<>(array("test")), new AtomicReferenceArray<>(array("foo")), false),
                     Arguments.of("abc", null, true),
                     Arguments.of(null, "abc", true),
                     Arguments.of("abc", "abc", true));
  }
}
