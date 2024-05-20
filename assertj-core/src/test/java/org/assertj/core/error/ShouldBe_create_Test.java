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
import static org.assertj.core.api.Assertions.allOf;
import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBe.shouldBe;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.assertj.core.description.Description;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShouldBe#create(Description, org.assertj.core.presentation.Representation)}</code>.
 *
 * @author Yvonne Wang
 */
class ShouldBe_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldBe("Yoda", new TestCondition<>("green"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to be green"));
  }

  @Test
  void should_create_error_message_for_allOf_condition() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("a Jedi");
    TestCondition<Object> condition2 = new TestCondition<>("very tall");
    TestCondition<Object> condition3 = new TestCondition<>("young");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    condition3.shouldMatch(false);
    Condition<Object> allOf = allOf(condition1, condition2, condition3);
    ErrorMessageFactory factory = shouldBe("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to be:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✓] a Jedi,%n" +
                                   "   [✗] very tall,%n" +
                                   "   [✗] young%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_for_allOf_condition_single_failed_condition() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("a Jedi");
    TestCondition<Object> condition2 = new TestCondition<>("very tall");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    Condition<Object> allOf = allOf(condition1, condition2);
    ErrorMessageFactory factory = shouldBe("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to be:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✓] a Jedi,%n" +
                                   "   [✗] very tall%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_for_allOf_condition_with_all_nested_failed_conditions() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("a Sith");
    TestCondition<Object> condition2 = new TestCondition<>("very tall");
    TestCondition<Object> condition3 = new TestCondition<>("young");
    condition1.shouldMatch(false);
    condition2.shouldMatch(false);
    condition3.shouldMatch(false);
    Condition<Object> allOf = allOf(condition1,
                                    allOf(condition1, condition2),
                                    anyOf(condition2, condition3));
    ErrorMessageFactory factory = shouldBe("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to be:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✗] a Sith,%n" +
                                   "   [✗] all of:[%n" +
                                   "      [✗] a Sith,%n" +
                                   "      [✗] very tall%n" +
                                   "   ],%n" +
                                   "   [✗] any of:[%n" +
                                   "      [✗] very tall,%n" +
                                   "      [✗] young%n" +
                                   "   ]%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_reporting_which_allOf_nested_conditions_failed() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("a Jedi");
    TestCondition<Object> condition2 = new TestCondition<>("very tall");
    TestCondition<Object> condition3 = new TestCondition<>("very old");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    condition3.shouldMatch(true);
    Condition<Object> allOf = allOf(condition1,
                                    allOf(condition1, condition2),
                                    anyOf(condition2, condition3));
    ErrorMessageFactory factory = shouldBe("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to be:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✓] a Jedi,%n" +
                                   "   [✗] all of:[%n" +
                                   "      [✓] a Jedi,%n" +
                                   "      [✗] very tall%n" +
                                   "   ],%n" +
                                   "   [✓] any of:[%n" +
                                   "      [✗] very tall,%n" +
                                   "      [✓] very old%n" +
                                   "   ]%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_reporting_which_allOf_deep_nested_conditions_failed() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("a Jedi");
    TestCondition<Object> condition2 = new TestCondition<>("very tall");
    TestCondition<Object> condition3 = new TestCondition<>("old");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    condition3.shouldMatch(true);
    Condition<Object> allOf = allOf(allOf(condition1,
                                          allOf(condition1,
                                                allOf(condition2, condition3))));
    ErrorMessageFactory factory = shouldBe("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to be:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✗] all of:[%n" +
                                   "      [✓] a Jedi,%n" +
                                   "      [✗] all of:[%n" +
                                   "         [✓] a Jedi,%n" +
                                   "         [✗] all of:[%n" +
                                   "            [✗] very tall,%n" +
                                   "            [✓] old%n" +
                                   "         ]%n" +
                                   "      ]%n" +
                                   "   ]%n" +
                                   "]"));
  }

}
