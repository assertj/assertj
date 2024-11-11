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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.NestableCondition.nestable;
import static org.assertj.core.condition.NestableConditionFixtures.address;
import static org.assertj.core.condition.NestableConditionFixtures.customer;
import static org.assertj.core.condition.NestableConditionFixtures.first;
import static org.assertj.core.condition.NestableConditionFixtures.firstLine;
import static org.assertj.core.condition.NestableConditionFixtures.name;
import static org.assertj.core.condition.NestableConditionFixtures.postcode;
import static org.assertj.core.condition.NestableConditionFixtures.value;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link NestableCondition#matches(Object)}</code>.
 * 
 * @author Alessandro Ciccimarra
 */
class NestableCondition_matches_Test {
  private final Customer boris = new Customer(new Name("Boris", "Johnson"),
                                              new Address("10, Downing Street",
                                                          "SW1A 2AA",
                                                          new Country("United Kingdom")));

  @Test
  void should_match_if_all_conditions_match() {
    // GIVEN
    Condition<Customer> condition = customer(name(first("Boris")),
                                             address(firstLine("10, Downing Street"),
                                                     postcode("SW1A 2AA")));
    // THEN
    then(condition.matches(boris)).isTrue();
  }

  @Test
  void should_not_match_if_any_condition_at_top_level_does_not_match() {
    // GIVEN
    Condition<Customer> condition = customer(name(first("Matt")),
                                             address(firstLine("10, Downing Street"),
                                                     postcode("SW1A 2AA")));
    // THEN
    then(condition.matches(boris)).isFalse();
  }

  @Test
  void should_not_match_if_any_condition_in_nested_level_does_not_match() {
    // GIVEN
    Condition<Customer> condition = customer(name(first("Boris")),
                                             address(firstLine("11, Downing Street"),
                                                     postcode("SW1A 2AA")));
    // THEN
    then(condition.matches(boris)).isFalse();
  }

  @Test
  void should_accept_conditions_on_supertypes() {
    // GIVEN
    final ValueCustomer boris = new ValueCustomer(new Name("Boris", "Johnson"),
                                                  new Address("10, Downing Street",
                                                              "SW1A 2AA",
                                                              new Country("United Kingdom")),
                                                  12);
    Condition<ValueCustomer> valueCustomer = nestable("value customer",
                                                      name(first("Boris")),
                                                      value(12));
    // THEN
    then(valueCustomer.matches(boris)).isTrue();
  }

  @Test
  void should_accept_extracting_function_from_supertype() {
    // GIVEN
    final ValueCustomer boris = new ValueCustomer(new Name("Boris", "Johnson"),
                                                  new Address("10, Downing Street",
                                                              "SW1A 2AA",
                                                              new Country("United Kingdom")),
                                                  12);
    Condition<ValueCustomer> customerFirstName = nestable("customer name", customer -> customer.name, first("Boris"));
    // THEN
    then(customerFirstName.matches(boris)).isTrue();
  }
}
