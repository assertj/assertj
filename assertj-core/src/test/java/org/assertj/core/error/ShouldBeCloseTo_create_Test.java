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
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeCloseTo.shouldBeCloseTo;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.DateUtil.parseDatetimeWithMs;

import java.time.Duration;
import java.time.LocalTime;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBeCloseTo#create(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Joel Costigliola
 */
class ShouldBeCloseTo_create_Test {

  @Test
  void should_create_error_message_with_period_boundaries_included() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeCloseTo(parseDatetimeWithMs("2011-01-01T00:00:00.000"),
                                                  parseDatetimeWithMs("2011-01-01T00:00:00.101"),
                                                  100, 101);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2011-01-01T00:00:00.000%n" +
                                   "to be close to:%n" +
                                   "  2011-01-01T00:00:00.101%n" +
                                   "by less than 100ms but difference was 101ms"));
  }

  @Test
  void should_create_error_message_with_TemporalAmount() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeCloseTo(Duration.ofHours(1), Duration.ofHours(2), Duration.ofMinutes(10),
                                                  Duration.ofHours(1));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  PT1H%n" +
                                   "to be close to:%n" +
                                   "  PT2H%n" +
                                   "within PT10M but difference was PT1H"));
  }

  @Test
  void should_create_error_message_with_Temporal() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeCloseTo(LocalTime.of(13, 22, 37), LocalTime.of(13, 22, 32), "but difference was 5s");
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  13:22:37%n" +
                                   "to be close to:%n" +
                                   "  13:22:32%n" +
                                   "but difference was 5s"));
  }

}
