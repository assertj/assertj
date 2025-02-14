package org.example.core;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.testkit.TolkienCharacter;
import org.junit.jupiter.api.Test;

public class DescribingAssertionsExample {

  @Test
  void describing_assertions() {
    try {
// tag::user_guide[]
      TolkienCharacter frodo = TolkienCharacter.of("Frodo", 33, TolkienCharacter.Race.HOBBIT);
      
      // failing assertion, remember to call as() before the assertion!
      assertThat(frodo.getAge()).as("check %s's age", frodo.getName())
                                .isEqualTo(100);
// end::user_guide[]
    } catch (AssertionError error) {
      // do nothing
    }
  }

}
