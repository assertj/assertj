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
package org.assertj.core.condition;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.condition.NestableConditionFixtures.address;
import static org.assertj.core.condition.NestableConditionFixtures.customer;
import static org.assertj.core.condition.NestableConditionFixtures.first;
import static org.assertj.core.condition.NestableConditionFixtures.firstLine;
import static org.assertj.core.condition.NestableConditionFixtures.name;
import static org.assertj.core.condition.NestableConditionFixtures.postcode;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link NestableCondition#description()}}</code>.
 * 
 * @author Alessandro Ciccimarra
 */
class NestableCondition_description_Test {
  @Test
  void should_return_description_for_nested_conditions() {
    // GIVEN
    Condition<Customer> condition = customer(name(first("John")),
                                             address(firstLine("11, Downing Street"),
                                                     postcode("SW1A 2AA")));
    // THEN
    then(condition.description().value()).isEqualTo(format("customer:[%n" +
                                                           "   name:[%n" +
                                                           "      first: John%n" +
                                                           "   ],%n" +
                                                           "   address:[%n" +
                                                           "      first line: 11, Downing Street,%n" +
                                                           "      postcode: SW1A 2AA%n" +
                                                           "   ]%n" +
                                                           "]"));
  }
}
