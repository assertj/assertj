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
import static org.assertj.core.error.ShouldSatisfyOnlyOnce.shouldSatisfyOnlyOnce;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.testkit.jdk11.Jdk11.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldSatisfyOnlyOnce#create(Description)}</code>.
 */
class ShouldSatisfyOnlyOnce_create_Test {

  @Test
  void should_create_error_message_when_no_elements_were_satisfied() {
    // GIVEN
    ErrorMessageFactory factory = shouldSatisfyOnlyOnce(List.of("Luke", "Leia", "Yoda"), List.of());
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting exactly one element of actual:%n" +
                                   "  [\"Luke\", \"Leia\", \"Yoda\"]%n" +
                                   "to satisfy the requirements but none did"));
  }

  @Test
  void should_create_error_message_when_more_than_one_element_was_satisfied() {
    // GIVEN
    ErrorMessageFactory factory = shouldSatisfyOnlyOnce(List.of("Luke", "Leia", "Yoda"), List.of("Luke", "Leia"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting exactly one element of actual:%n" +
                                   "  [\"Luke\", \"Leia\", \"Yoda\"]%n" +
                                   "to satisfy the requirements but these 2 elements did:%n" +
                                   "  [\"Luke\", \"Leia\"]"));
  }

}
