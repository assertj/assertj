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
package example.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import example.data.Race;
import example.data.TolkienCharacter;

public class OverridingErrorMessageExample {

  @Test
  void overriding_assertion_error_message() {
    try {
      // tag::user_guide[]
      TolkienCharacter frodo = new TolkienCharacter("Frodo", 33, Race.HOBBIT);
      TolkienCharacter sam = new TolkienCharacter("Sam", 38, Race.HOBBIT);
      // failing assertion, remember to call withFailMessage/overridingErrorMessage before the assertion!
      assertThat(frodo.getAge()).withFailMessage("should be %s", frodo)
                                .isEqualTo(sam);
      // end::user_guide[]
    } catch (AssertionError error) {
      // do nothing
    }
  }

  @Test
  void lazy_error_message() {
    Player player = new Player();
    // tag::lazy_error_message[]
    assertThat(player.isRookie()).overridingErrorMessage(() -> "Expecting Player to be a rookie but was not.")
                                 .isTrue();

    assertThat(player.isRookie()).withFailMessage(() -> "Expecting Player to be a rookie but was not.")
                                 .isTrue();
    // end::lazy_error_message[]
  }

  private static class Player {

    boolean isRookie() {
      return true;
    }
  }

}
