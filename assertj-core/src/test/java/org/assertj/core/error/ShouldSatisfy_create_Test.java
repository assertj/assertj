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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfy;
import static org.assertj.core.error.ShouldSatisfy.shouldSatisfyExactlyInAnyOrder;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.Lists.newArrayList;

import org.assertj.core.api.TestCondition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldSatisfy#create(Description)}</code>.
 */
@DisplayName("ShouldSatisfy create")
class ShouldSatisfy_create_Test {

  @Test
  void should_create_error_message_if_condition_is_not_satisfied() {
    // GIVEN
    ErrorMessageFactory factory = shouldSatisfy("Yoda", new TestCondition<>("green lightsaber bearer"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual:%n"
                                   + "  \"Yoda\"%n"
                                   + "to satisfy:%n"
                                   + "  green lightsaber bearer"));
  }

  @Test
  void should_create_error_message_if_consumers_are_not_all_satisfied() {
    // GIVEN
    ErrorMessageFactory factory = shouldSatisfyExactlyInAnyOrder(newArrayList("Luke", "Leia", "Yoda"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n"
                                   + "Expecting actual:%n"
                                   + "  [\"Luke\", \"Leia\", \"Yoda\"]%n"
                                   + "to satisfy all the consumers in any order."));
  }
}
