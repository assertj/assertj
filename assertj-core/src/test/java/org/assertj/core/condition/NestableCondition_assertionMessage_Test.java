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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.NestableConditionFixtures.address;
import static org.assertj.core.condition.NestableConditionFixtures.country;
import static org.assertj.core.condition.NestableConditionFixtures.customer;
import static org.assertj.core.condition.NestableConditionFixtures.first;
import static org.assertj.core.condition.NestableConditionFixtures.firstLine;
import static org.assertj.core.condition.NestableConditionFixtures.last;
import static org.assertj.core.condition.NestableConditionFixtures.name;
import static org.assertj.core.condition.NestableConditionFixtures.postcode;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link NestableCondition#toString()}</code>.
 * 
 * @author Alessandro Ciccimarra
 */
class NestableCondition_assertionMessage_Test {
  private final Customer boris = new Customer(new Name("Boris", "Johnson"),
                                              new Address("10, Downing Street",
                                                          "SW1A 2AA",
                                                          new Country("United Kingdom")));

  @Test
  void should_show_correct_error_message_with_two_nested_objects() {
    // GIVEN
    Condition<Customer> condition = customer(
                                             name(
                                                  first("Boris"),
                                                  last("Johnson")),
                                             address(
                                                     firstLine("10, Downing Street"),
                                                     postcode("SW2A 2AA")));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(boris).is(condition));

    // THEN
    then(assertionError).hasMessageContaining(format("[✗] customer:[%n" +
                                                     "   [✓] name:[%n" +
                                                     "      [✓] first: Boris,%n" +
                                                     "      [✓] last: Johnson%n" +
                                                     "   ],%n" +
                                                     "   [✗] address:[%n" +
                                                     "      [✓] first line: 10, Downing Street,%n" +
                                                     "      [✗] postcode: SW2A 2AA but was SW1A 2AA%n" +
                                                     "   ]%n" +
                                                     "]"));
  }

  @Test
  void should_show_correct_error_message_with_two_levels_of_nesting() {
    // GIVEN
    Condition<Customer> condition = customer(
                                             address(
                                                     firstLine("10, Downing Street"),
                                                     country(name("Gibraltar"))));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(boris).is(condition));

    // THEN
    then(assertionError).hasMessageContaining(format("[✗] customer:[%n" +
                                                     "   [✗] address:[%n" +
                                                     "      [✓] first line: 10, Downing Street,%n" +
                                                     "      [✗] country:[%n" +
                                                     "         [✗] name: Gibraltar but was United Kingdom%n" +
                                                     "      ]%n" +
                                                     "   ]%n" +
                                                     "]"));
  }
}
