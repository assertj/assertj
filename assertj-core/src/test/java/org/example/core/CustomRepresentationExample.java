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
