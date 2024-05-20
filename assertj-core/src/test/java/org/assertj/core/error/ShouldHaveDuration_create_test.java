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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveDays;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveHours;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveMillis;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveMinutes;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveNanos;
import static org.assertj.core.error.ShouldHaveDuration.shouldHaveSeconds;

import java.time.Duration;
import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
class ShouldHaveDuration_create_test {

  @Test
  void should_create_error_message_for_nanos() {
    // GIVEN
    Duration duration = Duration.ofNanos(1893);
    long actualNanos = 1893;
    long expectedNanos = 190;
    // WHEN
    String errorMessage = shouldHaveNanos(duration, actualNanos, expectedNanos).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  0.000001893S%nto have 190L nanos but had 1893L"));
  }

  @Test
  void should_create_error_message_for_1_nano() {
    // GIVEN
    Duration duration = Duration.ofNanos(1893);
    long actualNanos = 1893;
    long expectedNanos = 1;
    // WHEN
    String errorMessage = shouldHaveNanos(duration, actualNanos, expectedNanos).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  0.000001893S%nto have 1L nano but had 1893L"));
  }

  @Test
  void should_create_error_message_for_negative_1_nano() {
    // GIVEN
    Duration duration = Duration.ofNanos(1893);
    long actualNanos = 1893;
    long expectedNanos = -1;
    // WHEN
    String errorMessage = shouldHaveNanos(duration, actualNanos, expectedNanos).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  0.000001893S%nto have -1L nano but had 1893L"));
  }

  @Test
  void should_create_error_message_for_millis() {
    // GIVEN
    Duration duration = Duration.ofMillis(1893);
    long actualMillis = 1893;
    long expectedMillis = 190;
    // WHEN
    String errorMessage = shouldHaveMillis(duration, actualMillis, expectedMillis).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  1.893S%nto have 190L millis but had 1893L"));
  }

  @Test
  void should_create_error_message_for_1_milli() {
    // GIVEN
    Duration duration = Duration.ofMillis(1893);
    long actualMillis = 1893;
    long expectedMillis = 1;
    // WHEN
    String errorMessage = shouldHaveMillis(duration, actualMillis, expectedMillis).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  1.893S%nto have 1L milli but had 1893L"));
  }

  @Test
  void should_create_error_message_for_negative_1_milli() {
    // GIVEN
    Duration duration = Duration.ofMillis(1893);
    long actualMillis = 1893;
    long expectedMillis = -1;
    // WHEN
    String errorMessage = shouldHaveMillis(duration, actualMillis, expectedMillis).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  1.893S%nto have -1L milli but had 1893L"));
  }

  @Test
  void should_create_error_message_for_seconds() {
    // GIVEN
    Duration duration = Duration.ofSeconds(120);
    long actualSeconds = 120;
    long expectedSeconds = 190;
    // WHEN
    String errorMessage = shouldHaveSeconds(duration, actualSeconds, expectedSeconds).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  2M%nto have 190L seconds but had 120L"));
  }

  @Test
  void should_create_error_message_for_1_second() {
    // GIVEN
    Duration duration = Duration.ofSeconds(120);
    long actualSeconds = 120;
    long expectedSeconds = 1;
    // WHEN
    String errorMessage = shouldHaveSeconds(duration, actualSeconds, expectedSeconds).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  2M%nto have 1L second but had 120L"));
  }

  @Test
  void should_create_error_message_for_negative_1_second() {
    // GIVEN
    Duration duration = Duration.ofSeconds(120);
    long actualSeconds = 120;
    long expectedSeconds = -1;
    // WHEN
    String errorMessage = shouldHaveSeconds(duration, actualSeconds, expectedSeconds).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  2M%nto have -1L second but had 120L"));
  }

  @Test
  void should_create_error_message_for_minutes() {
    // GIVEN
    Duration duration = Duration.ofMinutes(65);
    long actualMinutes = 65;
    long expectedMinutes = 190;
    // WHEN
    String errorMessage = shouldHaveMinutes(duration, actualMinutes, expectedMinutes).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  1H5M%nto have 190L minutes but had 65L"));
  }

  @Test
  void should_create_error_message_for_1_minute() {
    Duration duration = Duration.ofMinutes(65);
    long actualMinutes = 65;
    long expectedMinutes = 1;
    // WHEN
    String errorMessage = shouldHaveMinutes(duration, actualMinutes, expectedMinutes).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  1H5M%nto have 1L minute but had 65L"));
  }

  @Test
  void should_create_error_message_for_negative_1_minute() {
    Duration duration = Duration.ofMinutes(65);
    long actualMinutes = 65;
    long expectedMinutes = -1;
    // WHEN
    String errorMessage = shouldHaveMinutes(duration, actualMinutes, expectedMinutes).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  1H5M%nto have -1L minute but had 65L"));
  }

  @Test
  void should_create_error_message_for_hours() {
    Duration duration = Duration.ofMinutes(125);
    long actualHours = 2;
    long expectedHours = 190;
    // WHEN
    String errorMessage = shouldHaveHours(duration, actualHours, expectedHours).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  2H5M%nto have 190L hours but had 2L"));
  }

  @Test
  void should_create_error_message_for_1_hour() {
    Duration duration = Duration.ofMinutes(125);
    long actualHours = 2;
    long expectedHours = 1;
    // WHEN
    String errorMessage = shouldHaveHours(duration, actualHours, expectedHours).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  2H5M%nto have 1L hour but had 2L"));
  }

  @Test
  void should_create_error_message_for_negative_1_hour() {
    Duration duration = Duration.ofMinutes(125);
    long actualHours = 2;
    long expectedHours = -1;
    // WHEN
    String errorMessage = shouldHaveHours(duration, actualHours, expectedHours).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  2H5M%nto have -1L hour but had 2L"));
  }

  @Test
  void should_create_error_message_for_days() {
    Duration duration = Duration.ofHours(50);
    long actualDays = 2;
    long expectedDays = 190;
    // WHEN
    String errorMessage = shouldHaveDays(duration, actualDays, expectedDays).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  50H%nto have 190L days but had 2L"));
  }

  @Test
  void should_create_error_message_for_1_day() {
    Duration duration = Duration.ofHours(50);
    long actualDays = 2;
    long expectedDays = 1;
    // WHEN
    String errorMessage = shouldHaveDays(duration, actualDays, expectedDays).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  50H%nto have 1L day but had 2L"));
  }

  @Test
  void should_create_error_message_for_negative_1_day() {
    Duration duration = Duration.ofHours(50);
    long actualDays = 2;
    long expectedDays = -1;
    // WHEN
    String errorMessage = shouldHaveDays(duration, actualDays, expectedDays).create();
    // THEN
    then(errorMessage).isEqualTo(format("%nExpecting Duration:%n  50H%nto have -1L day but had 2L"));
  }

}
