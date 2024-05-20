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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldContainValues.shouldContainValues;
import static org.assertj.core.testkit.Maps.mapOf;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Map;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

class ShouldContainValues_create_Test {
  @Test
  void should_create_error_message_with_multiple_values() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainValues(map, newLinkedHashSet("VeryOld", "Vader"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(String.format("[Test] %n" +
                                          "Expecting actual:%n" +
                                          "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n" +
                                          "to contain values:%n" +
                                          "  [\"VeryOld\", \"Vader\"]"));
  }

  @Test
  void should_create_error_message_with_single_value() {
    // GIVEN
    Map<?, ?> map = mapOf(entry("name", "Yoda"), entry("color", "green"));
    ErrorMessageFactory factory = shouldContainValues(map, newLinkedHashSet("VeryOld"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(String.format("[Test] %n" +
                                          "Expecting actual:%n" +
                                          "  {\"color\"=\"green\", \"name\"=\"Yoda\"}%n" +
                                          "to contain value:%n" +
                                          "  \"VeryOld\""));
  }

}
