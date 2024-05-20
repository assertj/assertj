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
import static org.assertj.core.error.ShouldBeEqualWithTimePrecision.shouldBeEqual;
import static org.assertj.core.util.DateUtil.parseDatetimeWithMs;

import java.util.concurrent.TimeUnit;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link ShouldBeEqualWithTimePrecision#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>
 * .
 *
 * @author Joel Costigliola
 */
class ShouldBeEqualWithTimePrecision_create_Test {

  @Test
  void should_create_error_message_ignoring_milliseconds() {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.MILLISECONDS);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n" +
                                   "  2011-01-01T05:00:00.000 (java.util.Date)%n" +
                                   "to have same year, month, day, hour, minute and second as:%n" +
                                   "  2011-01-01T06:05:17.003 (java.util.Date)%n" +
                                   "but had not."));
  }

  @Test
  void should_create_error_message_ignoring_seconds() {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.SECONDS);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n" +
                                   "  2011-01-01T05:00:00.000 (java.util.Date)%n" +
                                   "to have same year, month, day, hour and minute as:%n" +
                                   "  2011-01-01T06:05:17.003 (java.util.Date)%n" +
                                   "but had not."));
  }

  @Test
  void should_create_error_message_ignoring_minutes() {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.MINUTES);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n" +
                                   "  2011-01-01T05:00:00.000 (java.util.Date)%n" +
                                   "to have same year, month, day and hour as:%n" +
                                   "  2011-01-01T06:05:17.003 (java.util.Date)%n" +
                                   "but had not."));
  }

  @Test
  void should_create_error_message_ignoring_hours() {
    ErrorMessageFactory factory = shouldBeEqual(parseDatetimeWithMs("2011-01-01T05:00:00.000"),
                                                parseDatetimeWithMs("2011-01-01T06:05:17.003"), TimeUnit.HOURS);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    then(message).isEqualTo(format("[Test] %nExpecting actual:%n" +
                                   "  2011-01-01T05:00:00.000 (java.util.Date)%n" +
                                   "to have same year, month and day as:%n" +
                                   "  2011-01-01T06:05:17.003 (java.util.Date)%n" +
                                   "but had not."));
  }

}
