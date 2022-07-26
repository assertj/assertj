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
package org.assertj.core.api.stacktrace;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldHaveSizeGreaterThan.shouldHaveSizeGreaterThan;
import static org.assertj.core.error.ShouldNotBeEmpty.shouldNotBeEmpty;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;

import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.assertj.core.api.AbstractStackTraceAssert;
import org.assertj.core.api.AbstractStackTraceElementAssert;
import org.assertj.core.api.StackTraceAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link StackTraceAssert#element(int)}.
 *
 * @author Ashley Scopes
 */
class StackTraceAssert_element_Test {

  @Test
  void fails_when_actual_is_null() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat((StackTraceElement[]) null);
    // Then
    assertThatThrownBy(() -> assertion.element(0))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void fails_when_actual_is_empty() {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat(new StackTraceElement[0]);
    // Then
    assertThatThrownBy(() -> assertion.element(0))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeEmpty().create());
  }

  @MethodSource("outOfBoundsCases")
  @ParameterizedTest
  void fails_when_offset_is_out_of_bounds(
    StackTraceElement[] stackTrace,
    int givenOffset,
    int expectedOffset
  ) {
    // Given
    AbstractStackTraceAssert<?, ?> assertion = assertThat(stackTrace);
    // Then
    assertThatThrownBy(() -> assertion.element(givenOffset))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveSizeGreaterThan(stackTrace, stackTrace.length, expectedOffset).create());
  }

  @MethodSource("successCases")
  @ParameterizedTest
  void succeeds_with_expected_values_when_offset_in_bounds(
    StackTraceElement[] stackTrace,
    int offset,
    StackTraceElement expected
  ) {
    // Given
    AbstractStackTraceElementAssert<?> assertion = assertThat(stackTrace).element(offset);
    // Then
    assertion.isEqualTo(expected);
  }

  static Stream<Arguments> outOfBoundsCases() {
    StackTraceElement[] stackTrace = someStackTrace();

    return Stream.of(
      Arguments.of(stackTrace, stackTrace.length, stackTrace.length),
      Arguments.of(stackTrace, -stackTrace.length - 1, stackTrace.length),
      Arguments.of(stackTrace, 500_000, 500_000),
      Arguments.of(stackTrace, -793_542, 793_541)
    );
  }

  static Stream<Arguments> successCases() {
    StackTraceElement[] stackTrace = someStackTrace();

    Stream<Arguments> positiveCases = IntStream
      .range(0, stackTrace.length)
      .mapToObj(i -> Arguments.of(
        stackTrace,
        i,
        stackTrace[i]
      ));

    Stream<Arguments> negativeCases = IntStream
      .range(-stackTrace.length + 1, 0)
      .mapToObj(i -> Arguments.of(
        stackTrace,
        i,
        stackTrace[stackTrace.length + i]
      ));

    return Stream.concat(positiveCases, negativeCases);
  }

  static StackTraceElement[] someStackTrace() {
    return new StackTraceElement[]{
      new StackTraceElement("java.util.ArrayList", "forEach", "ArrayList.java", 123),
      new StackTraceElement("java.util.HashMap", "computeIfAbsent", "HashMap.java", 456),
      new StackTraceElement("java.lang.StringBuilder", "append", "StringBuilder.java", 4003),
      new StackTraceElement("java.lang.String", "toLowerCase", "String.java", 9017),
    };
  }
}
