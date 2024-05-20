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
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_with_java_types_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest
  @MethodSource
  void should_pass_when_comparing_java_types(Object actual, Object expected) {
    then(actual).usingRecursiveComparison()
                .isEqualTo(expected);
  }

  static Stream<Arguments> should_pass_when_comparing_java_types() {
    return Stream.of(arguments(new AtomicInteger(123),
                               new AtomicInteger(123)),
                     arguments(new AtomicLong(123),
                               new AtomicLong(123)),
                     arguments(new AtomicBoolean(true),
                               new AtomicBoolean(true)),
                     arguments(new AtomicBoolean(Boolean.FALSE),
                               new AtomicBoolean(Boolean.FALSE)),
                     arguments(new AtomicIntegerArray(new int[] { 1, 2, 3 }),
                               new AtomicIntegerArray(new int[] { 1, 2, 3 })),
                     arguments(new AtomicLongArray(new long[] { 1, 2, 3 }),
                               new AtomicLongArray(new long[] { 1, 2, 3 })),
                     arguments(new AtomicReference<>("test"),
                               new AtomicReference<>("test")),
                     arguments(new AtomicReference<>(new Person("Joe")),
                               new AtomicReference<>(new Person("Joe"))),
                     arguments(new AtomicReferenceArray<>(array("test")),
                               new AtomicReferenceArray<>(array("test"))),
                     arguments(TimeZone.getTimeZone("UTC"),
                               TimeZone.getTimeZone("UTC")),
                     arguments(new Wrapper(new AtomicInteger(123)),
                               new Wrapper(new AtomicInteger(123))),
                     arguments(new Wrapper(new AtomicLong(123)),
                               new Wrapper(new AtomicLong(123))),
                     arguments(new Wrapper(new AtomicBoolean(true)),
                               new Wrapper(new AtomicBoolean(true))),
                     arguments(new Wrapper(new AtomicBoolean(Boolean.FALSE)),
                               new Wrapper(new AtomicBoolean(Boolean.FALSE))),
                     arguments(new Wrapper(new AtomicIntegerArray(new int[] { 1, 2, 3 })),
                               new Wrapper(new AtomicIntegerArray(new int[] { 1, 2, 3 }))),
                     arguments(new Wrapper(new AtomicLongArray(new long[] { 1, 2, 3 })),
                               new Wrapper(new AtomicLongArray(new long[] { 1, 2, 3 }))),
                     arguments(new Wrapper(new AtomicReference<>("test")),
                               new Wrapper(new AtomicReference<>("test"))),
                     arguments(new Wrapper(new AtomicReferenceArray<>(array("test"))),
                               new Wrapper(new AtomicReferenceArray<>(array("test")))));
  }

  @Test
  void wrapper_comparison_should_fail_and_report_the_proper_difference() {
    // GIVEN
    Wrapper actual = new Wrapper(new AtomicReference<>("test1"));
    Wrapper expected = new Wrapper(new AtomicReference<>("test2"));
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, diff("value.value", "test1", "test2"));
  }

  @Test
  void should_fail_and_report_the_proper_difference() {
    // GIVEN
    AtomicReference<?> actual = new AtomicReference<>("test1");
    AtomicReference<?> expected = new AtomicReference<>("test2");
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, diff("value", "test1", "test2"));
  }

  @Test
  void should_honor_java_equals_and_fail_comparison() {
    // GIVEN
    ReentrantReadWriteLock actualLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock expectedLock = new ReentrantReadWriteLock();
    Wrapper actual = new Wrapper(actualLock);
    Wrapper expected = new Wrapper(expectedLock);
    // WHEN/THEN
    compareRecursivelyFailsWithDifferences(actual, expected, diff("value", actualLock, expectedLock));
  }

  static class Wrapper {
    Object value;

    Wrapper(Object value) {
      this.value = value;
    }
  }

}
