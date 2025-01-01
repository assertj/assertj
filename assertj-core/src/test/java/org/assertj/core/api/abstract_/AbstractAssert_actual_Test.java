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
package org.assertj.core.api.abstract_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.testkit.TolkienCharacter.Race.HOBBIT;

import org.assertj.core.testkit.TolkienCharacter;
import org.junit.jupiter.api.Test;

class AbstractAssert_actual_Test {

  @Test
  void should_return_initial_actual() {
    // GIVEN
    String actual = "foo";
    // WHEN
    String initialActual = assertThat(actual).actual();
    // THEN
    then(actual).isSameAs(initialActual);
  }

  @Test
  void should_return_actual_after_changing_the_object_under_test() {
    // GIVEN
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
    // WHEN
    Object currentActual = assertThat(frodo).extracting("name").actual();
    // THEN
    then(currentActual).isSameAs(frodo.name);
  }

  @Test
  void should_return_actual_after_changing_the_object_under_test_and_casting_it() {
    // GIVEN
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
    // WHEN
    String currentActual = assertThat(frodo).extracting("name").asInstanceOf(STRING).actual();
    // THEN
    then(currentActual).isSameAs(frodo.name);
  }

  @Test
  void should_return_strongly_typed_actual_after_changing_the_object_under_test() {
    // GIVEN
    TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
    // WHEN
    String rootCause = assertThat(frodo).extracting(TolkienCharacter::getName).actual();
    // THEN
    then(rootCause).isSameAs(frodo.name);
  }
}
