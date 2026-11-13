package example.core;

import static org.assertj.core.api.Assertions.assertThat;
import example.data.Race;

import example.data.TolkienCharacter;
import org.junit.jupiter.api.Test;

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

}
