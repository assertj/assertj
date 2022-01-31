package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAlphabetic.shouldBeAlphabetic;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class ShouldBeAlphabetic_create_Test {

  @Test
  void should_create_error_message_for_character() {
    // WHEN
    String message = shouldBeAlphabetic('1').create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting '1' to be alphabetic"));
  }

  @Test
  void should_create_error_message_for_number() {
    // WHEN
    String message = shouldBeAlphabetic("123").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"123\" to be alphabetic"));
  }
}
