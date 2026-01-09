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
package org.assertj.tests.core.presentation;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.time.Duration;
import java.util.stream.Stream;

import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class StandardRepresentation_duration_Test extends AbstractBaseRepresentationTest {

  @Test
  void should_return_null_if_Duration_is_null() {
    // GIVEN
    Duration duration = null;
    // WHEN
    String durationRepresentation = STANDARD_REPRESENTATION.toStringOf(duration);
    // THEN
    then(durationRepresentation).isNull();
  }

  @ParameterizedTest
  @MethodSource
  void should_format_Duration(Duration duration, String expectedRepresentation) {
    // WHEN
    String durationRepresentation = STANDARD_REPRESENTATION.toStringOf(duration);
    // THEN
    then(durationRepresentation).isEqualTo(expectedRepresentation);
  }

  public static Stream<Arguments> should_format_Duration() {
    return Stream.of(Arguments.of(Duration.ofDays(1).plusMinutes(1).plusSeconds(1), "24h1m1s"),
                     Arguments.of(Duration.ofSeconds(3661), "1h1m1s"),
                     Arguments.of(Duration.ofMinutes(60), "1h"),
                     Arguments.of(Duration.ofMinutes(59), "59m"),
                     Arguments.of(Duration.ofSeconds(59), "59s"),
                     Arguments.of(Duration.ofNanos(100), "0.0000001s"));
  }

  @Test
  void should_allow_to_override_Duration_representation() {
    // GIVEN
    var customRepresentation = new CustomRepresentation();
    // WHEN
    String durationRepresentation = customRepresentation.toStringOf(Duration.ofMinutes(15));
    // THEN
    then(durationRepresentation).startsWith("Duration:");
  }

  private static class CustomRepresentation extends StandardRepresentation {

    @Override
    protected String toStringOf(Duration duration) {
      return "Duration:" + duration;
    }
  }

}
