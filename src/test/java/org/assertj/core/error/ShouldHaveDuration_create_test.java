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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
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
    Duration duration = Duration.ofNanos(1893);
    String errorMessage = shouldHaveNanos(duration, 1893, 190).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT0.000001893S>%nto have 190L nanos but had 1893L"));
  }

  @Test
  void should_create_error_message_for_1_nano() {
    Duration duration = Duration.ofNanos(1893);
    String errorMessage = shouldHaveNanos(duration, 1893, 1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT0.000001893S>%nto have 1L nano but had 1893L"));
  }

  @Test
  void should_create_error_message_for_negative_1_nano() {
    Duration duration = Duration.ofNanos(1893);
    String errorMessage = shouldHaveNanos(duration, 1893, -1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT0.000001893S>%nto have -1L nano but had 1893L"));
  }

  @Test
  void should_create_error_message_for_millis() {
    Duration duration = Duration.ofMillis(1893);
    String errorMessage = shouldHaveMillis(duration, 1893, 190).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT1.893S>%nto have 190L millis but had 1893L"));
  }

  @Test
  void should_create_error_message_for_1_milli() {
    Duration duration = Duration.ofMillis(1893);
    String errorMessage = shouldHaveMillis(duration, 1893, 1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT1.893S>%nto have 1L milli but had 1893L"));
  }

  @Test
  void should_create_error_message_for_negative_1_milli() {
    Duration duration = Duration.ofMillis(1893);
    String errorMessage = shouldHaveMillis(duration, 1893, -1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT1.893S>%nto have -1L milli but had 1893L"));
  }

  @Test
  void should_create_error_message_for_seconds() {
    Duration duration = Duration.ofSeconds(120);
    String errorMessage = shouldHaveSeconds(duration, 120, 190).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT2M>%nto have 190L seconds but had 120L"));
  }

  @Test
  void should_create_error_message_for_1_second() {
    Duration duration = Duration.ofSeconds(120);
    String errorMessage = shouldHaveSeconds(duration, 120, 1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT2M>%nto have 1L second but had 120L"));
  }

  @Test
  void should_create_error_message_for_negative_1_second() {
    Duration duration = Duration.ofSeconds(120);
    String errorMessage = shouldHaveSeconds(duration, 120, -1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT2M>%nto have -1L second but had 120L"));
  }
  
  @Test
  void should_create_error_message_for_minutes() {
    Duration duration = Duration.ofMinutes(65);
    String errorMessage = shouldHaveMinutes(duration, 65, 190).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT1H5M>%nto have 190L minutes but had 65L"));
  }

  @Test
  void should_create_error_message_for_1_minute() {
    Duration duration = Duration.ofMinutes(65);
    String errorMessage = shouldHaveMinutes(duration, 65, 1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT1H5M>%nto have 1L minute but had 65L"));
  }

  @Test
  void should_create_error_message_for_negative_1_minute() {
    Duration duration = Duration.ofMinutes(65);
    String errorMessage = shouldHaveMinutes(duration, 65, -1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT1H5M>%nto have -1L minute but had 65L"));
  }

  @Test
  void should_create_error_message_for_hours() {
    Duration duration = Duration.ofMinutes(125);
    String errorMessage = shouldHaveHours(duration, 2, 190).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT2H5M>%nto have 190L hours but had 2L"));
  }

  @Test
  void should_create_error_message_for_1_hour() {
    Duration duration = Duration.ofMinutes(125);
    String errorMessage = shouldHaveHours(duration, 2, 1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT2H5M>%nto have 1L hour but had 2L"));
  }

  @Test
  void should_create_error_message_for_negative_1_hour() {
    Duration duration = Duration.ofMinutes(125);
    String errorMessage = shouldHaveHours(duration, 2, -1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT2H5M>%nto have -1L hour but had 2L"));
  }
  
  @Test
  void should_create_error_message_for_days() {
    Duration duration = Duration.ofHours(50);
    String errorMessage = shouldHaveDays(duration, 2, 190).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT50H>%nto have 190L days but had 2L"));
  }

  @Test
  void should_create_error_message_for_1_day() {
    Duration duration = Duration.ofHours(50);
    String errorMessage = shouldHaveDays(duration, 2, 1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT50H>%nto have 1L day but had 2L"));
  }

  @Test
  void should_create_error_message_for_negative_1_day() {
    Duration duration = Duration.ofHours(50);
    String errorMessage = shouldHaveDays(duration, 2, -1).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Duration:%n <PT50H>%nto have -1L day but had 2L"));
  }

}
