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
import static org.assertj.core.error.ShouldBeAtSameInstant.shouldBeAtSameInstant;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

class ShouldBeAtSameInstant_create_Test {

  @Test
  void should_create_error_message() {
    OffsetDateTime actual = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.UTC);
    OffsetDateTime other = OffsetDateTime.of(2000, 12, 14, 0, 0, 0, 0, ZoneOffset.ofHours(-3));

    String message = shouldBeAtSameInstant(actual, other).create(new TextDescription("Test"),
                                                                 STANDARD_REPRESENTATION);

    assertThat(message).isEqualTo(format("[Test] %n" +
                                         "Expecting%n" +
                                         "  <2000-12-14T00:00Z>%n" +
                                         "to be at the same instant as:%n" +
                                         "  <2000-12-14T00:00-03:00>%n" +
                                         "but actual instance was%n" +
                                         "  <2000-12-14T00:00:00Z>%n" +
                                         "and expected instant was:%n" +
                                         "  <2000-12-14T03:00:00Z>"));
  }
}
