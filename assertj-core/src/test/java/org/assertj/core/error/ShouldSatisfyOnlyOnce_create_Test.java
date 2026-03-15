/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
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
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Tests for <code>{@link ShouldSatisfyOnlyOnce#create(Description)}</code>.
 */
class ShouldSatisfyOnlyOnce_create_Test {

  private static final WritableAssertionInfo INFO = new WritableAssertionInfo();

  @Test
  void should_create_error_message_when_no_elements_were_satisfied() {
    // GIVEN
    List<UnsatisfiedRequirement> unsatisfiedRequirements = List.of(
                                                                   new UnsatisfiedRequirement("Luke", expectAssertionError(() -> {
                                                                     throw new AssertionError("expected: Vader but was: Luke");
                                                                   })),
                                                                   new UnsatisfiedRequirement("Leia", expectAssertionError(() -> {
                                                                     throw new AssertionError("expected: Vader but was: Leia");
                                                                   })),
                                                                   new UnsatisfiedRequirement("Yoda", expectAssertionError(() -> {
                                                                     throw new AssertionError("expected: Vader but was: Yoda");
                                                                   })));
    ErrorMessageFactory factory = shouldSatisfyOnlyOnce(List.of("Luke", "Leia", "Yoda"), List.of(), unsatisfiedRequirements,
                                                        INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).contains(format("Expecting exactly one element of actual:%n" +
                                  "  [\"Luke\", \"Leia\", \"Yoda\"]%n" +
                                  "to satisfy the requirements but none did"))
                 .contains(format("\"Luke\"%nerror: "))
                 .contains("expected: Vader but was: Luke")
                 .contains(format("\"Leia\"%nerror: "))
                 .contains("expected: Vader but was: Leia")
                 .contains(format("\"Yoda\"%nerror: "))
                 .contains("expected: Vader but was: Yoda");
  }

  @Test
  void should_create_error_message_when_more_than_one_element_was_satisfied() {
    // GIVEN
    List<UnsatisfiedRequirement> unsatisfiedRequirements = List.of(
                                                                   new UnsatisfiedRequirement("Yoda", expectAssertionError(() -> {
                                                                     throw new AssertionError("expected: Luke but was: Yoda");
                                                                   })));
    ErrorMessageFactory factory = shouldSatisfyOnlyOnce(List.of("Luke", "Leia", "Yoda"), List.of("Luke", "Leia"),
                                                        unsatisfiedRequirements, INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).contains(format("Expecting exactly one element of actual:%n" +
                                  "  [\"Luke\", \"Leia\", \"Yoda\"]%n" +
                                  "to satisfy the requirements but these 2 elements did:%n" +
                                  "  [\"Luke\", \"Leia\"]"))
                 .contains("Elements which did not satisfy the requirements:")
                 .contains(format("\"Yoda\"%nerror: "))
                 .contains("expected: Luke but was: Yoda");
  }

  @Test
  void should_create_error_message_when_more_than_one_element_was_satisfied_and_no_unsatisfied() {
    // GIVEN
    ErrorMessageFactory factory = shouldSatisfyOnlyOnce(List.of("Luke", "Luke"), List.of("Luke", "Luke"), List.of(), INFO);
    // WHEN
    String message = factory.create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting exactly one element of actual:%n" +
                                   "  [\"Luke\", \"Luke\"]%n" +
                                   "to satisfy the requirements but these 2 elements did:%n" +
                                   "  [\"Luke\", \"Luke\"]"));
  }

}
