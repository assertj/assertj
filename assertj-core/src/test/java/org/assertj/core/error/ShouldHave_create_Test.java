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
import static org.assertj.core.error.ShouldHave.shouldHave;

import org.assertj.core.api.Condition;
import org.assertj.core.api.TestCondition;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * @author Yvonne Wang
 */
class ShouldHave_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    ErrorMessageFactory factory = shouldHave("Yoda", new TestCondition<>("green lightsaber"));
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to have green lightsaber"));
  }

  @Test
  void should_create_error_message_for_allOf_condition() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("jedi power");
    TestCondition<Object> condition2 = new TestCondition<>("sith power");
    TestCondition<Object> condition3 = new TestCondition<>("a short life");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    condition3.shouldMatch(false);
    Condition<Object> allOf = allOf(condition1, condition2, condition3);
    ErrorMessageFactory factory = shouldHave("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to have:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✓] jedi power,%n" +
                                   "   [✗] sith power,%n" +
                                   "   [✗] a short life%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_for_allOf_condition_single_failed_condition() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("jedi power");
    TestCondition<Object> condition2 = new TestCondition<>("sith power");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    Condition<Object> allOf = allOf(condition1, condition2);
    ErrorMessageFactory factory = shouldHave("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to have:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✓] jedi power,%n" +
                                   "   [✗] sith power%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_for_allOf_condition_with_all_nested_failed_conditions() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("a Sith");
    TestCondition<Object> condition2 = new TestCondition<>("sith power");
    TestCondition<Object> condition3 = new TestCondition<>("a short life");
    condition1.shouldMatch(false);
    condition2.shouldMatch(false);
    condition3.shouldMatch(false);
    Condition<Object> allOf = allOf(condition1,
                                    allOf(condition1, condition2),
                                    anyOf(condition2, condition3));
    ErrorMessageFactory factory = shouldHave("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to have:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✗] a Sith,%n" +
                                   "   [✗] all of:[%n" +
                                   "      [✗] a Sith,%n" +
                                   "      [✗] sith power%n" +
                                   "   ],%n" +
                                   "   [✗] any of:[%n" +
                                   "      [✗] sith power,%n" +
                                   "      [✗] a short life%n" +
                                   "   ]%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_reporting_which_allOf_nested_conditions_failed() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("jedi power");
    TestCondition<Object> condition2 = new TestCondition<>("sith power");
    TestCondition<Object> condition3 = new TestCondition<>("long life");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    condition3.shouldMatch(true);
    Condition<Object> allOf = allOf(condition1,
                                    allOf(condition1, condition2),
                                    anyOf(condition2, condition3));
    ErrorMessageFactory factory = shouldHave("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to have:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✓] jedi power,%n" +
                                   "   [✗] all of:[%n" +
                                   "      [✓] jedi power,%n" +
                                   "      [✗] sith power%n" +
                                   "   ],%n" +
                                   "   [✓] any of:[%n" +
                                   "      [✗] sith power,%n" +
                                   "      [✓] long life%n" +
                                   "   ]%n" +
                                   "]"));
  }

  @Test
  void should_create_error_message_reporting_which_allOf_deep_nested_conditions_failed() {
    // GIVEN
    TestCondition<Object> condition1 = new TestCondition<>("jedi power");
    TestCondition<Object> condition2 = new TestCondition<>("sith power");
    TestCondition<Object> condition3 = new TestCondition<>("a long life");
    condition1.shouldMatch(true);
    condition2.shouldMatch(false);
    condition3.shouldMatch(true);
    Condition<Object> allOf = allOf(allOf(condition1,
                                          allOf(condition1,
                                                allOf(condition2, condition3))));
    ErrorMessageFactory factory = shouldHave("Yoda", allOf);
    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  \"Yoda\"%n" +
                                   "to have:%n" +
                                   "[✗] all of:[%n" +
                                   "   [✗] all of:[%n" +
                                   "      [✓] jedi power,%n" +
                                   "      [✗] all of:[%n" +
                                   "         [✓] jedi power,%n" +
                                   "         [✗] all of:[%n" +
                                   "            [✗] sith power,%n" +
                                   "            [✓] a long life%n" +
                                   "         ]%n" +
                                   "      ]%n" +
                                   "   ]%n" +
                                   "]"));
  }

}
