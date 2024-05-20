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

import static org.assertj.core.api.BDDAssertions.then;

import org.assertj.core.api.Condition;
import org.assertj.core.description.Description;
import org.junit.jupiter.api.Test;

class Condition_conditionDescriptionWithStatus_Test {

  private Condition<String> jediCondition = new JediCondition();

  @Test
  void should_return_description_with_success_status() {
    // GIVEN
    String yoda = "Yoda";
    // WHEN
    Description conditionDescriptionWithStatus = jediCondition.conditionDescriptionWithStatus(yoda);
    // THEN
    then(conditionDescriptionWithStatus).hasToString("[✓] Jedi");
  }

  @Test
  void should_return_description_with_failed_status() {
    // GIVEN
    String vader = "Vader";
    // WHEN
    Description conditionDescriptionWithStatus = jediCondition.conditionDescriptionWithStatus(vader);
    // THEN
    then(conditionDescriptionWithStatus).hasToString("[✗] Jedi");
  }

}
