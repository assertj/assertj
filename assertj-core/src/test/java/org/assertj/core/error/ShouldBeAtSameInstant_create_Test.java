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
import static org.assertj.core.error.ShouldBeAtSameInstant.shouldBeAtSameInstant;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ShouldBeAtSameInstant create")
class ShouldBeAtSameInstant_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    OffsetDateTime actual = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime other = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.ofHours(-3));
    // WHEN
    String message = shouldBeAtSameInstant(actual, other).create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting%n" +
                                   "  2000-12-14T00:00Z (java.time.OffsetDateTime)%n" +
                                   "to be at the same instant as:%n" +
                                   "  2000-12-14T00:00-03:00 (java.time.OffsetDateTime)%n" +
                                   "but actual instance was%n" +
                                   "  2000-12-14T00:00:00Z%n" +
                                   "and expected instant was:%n" +
                                   "  2000-12-14T03:00:00Z"));
  }
}
