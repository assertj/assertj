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
package org.example.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.testkit.TolkienCharacter.Race.HOBBIT;

import org.assertj.core.api.Assertions;
import org.assertj.core.testkit.TolkienCharacter;
import org.junit.jupiter.api.Test;

class CustomRepresentationExample {

  @Test
  void overriding_assertion_error_message() {
    try {
      // tag::user_guide[]
      Assertions.useRepresentation(new CustomRepresentation());
      TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, HOBBIT);
      assertThat(frodo).isNull();
      // end::user_guide[]
    } catch (AssertionError error) {
      System.out.println(error.getMessage());
    }
  }

}
