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
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEqualIgnoringNanos.shouldBeEqualIgnoringNanos;

import java.time.LocalTime;
import java.time.OffsetTime;
import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.error.ShouldBeEqualIgnoringNanos#create(org.assertj.core.description.Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Alexander Bischof
 */
class ShouldBeEqualIgnoringNanos_create_Test {

  @Test
  void should_create_error_message_for_LocalTime() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeEqualIgnoringNanos(LocalTime.of(12, 0), LocalTime.of(13, 0));
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  12:00%n" +
                                   "to have same hour, minute and second as:%n" +
                                   "  13:00%n" +
                                   "but had not."));
  }

  @Test
  void should_create_error_message_for_OffsetTime() {
    // GIVEN
    ErrorMessageFactory factory = shouldBeEqualIgnoringNanos(OffsetTime.of(12, 0, 0, 0, UTC), OffsetTime.of(13, 0, 0, 0, UTC));
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  12:00Z%n" +
                                   "to have same hour, minute and second as:%n" +
                                   "  13:00Z%n" +
                                   "but had not."));
  }
}
