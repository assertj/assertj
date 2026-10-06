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

import org.assertj.core.testkit.TolkienCharacter;
import org.assertj.core.testkit.TolkienCharacter.Race;
import org.junit.jupiter.api.Test;

class OverridingErrorMessageExample {

  @Test
  void overriding_assertion_error_message() {
    try {
      // tag::user_guide[]
      TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, Race.HOBBIT);
      TolkienCharacter sam = TolkienCharacter.of("Sam", 38, Race.HOBBIT);
      // failing assertion, remember to call withFailMessage/overridingErrorMessage before the assertion!
      assertThat(frodo.getAge()).withFailMessage("should be %s", frodo)
                                .isEqualTo(sam);
      // end::user_guide[]
    } catch (AssertionError error) {
      // do nothing
    }
  }

}
