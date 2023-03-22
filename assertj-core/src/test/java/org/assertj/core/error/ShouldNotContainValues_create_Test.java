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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static java.util.Collections.singleton;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldNotContainValues.shouldNotContainValues;
import static org.assertj.core.test.Maps.mapOf;
import static org.assertj.core.util.Sets.set;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

class ShouldNotContainValues_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContainValues(mapOf(entry("name", "Yoda"), entry("color", "green")),
                                                         singleton("C3PO"));
    // WHEN
    String message = factory.create(new TestDescription("Test"), StandardRepresentation.STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n" +
                                   "not to contain value:%n" +
                                   "  \"C3PO\""));
  }

  @Test
  void should_create_error_message_multiple_values() {
    // GIVEN
    ErrorMessageFactory factory = shouldNotContainValues(mapOf(entry("name", "Yoda"), entry("color", "green")),
                                                         set("C3PO", "gold"));
    // WHEN
    String message = factory.create(new TestDescription("Test"), StandardRepresentation.STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n" +
                                   "not to contain values:%n" +
                                   "  [\"C3PO\", \"gold\"]"));
  }
}
