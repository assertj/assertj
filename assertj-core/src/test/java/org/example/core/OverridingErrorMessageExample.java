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
