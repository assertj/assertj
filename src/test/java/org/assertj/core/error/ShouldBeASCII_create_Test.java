package org.assertj.core.error;


import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeASCII.shouldBeASCII;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class ShouldBeASCII_create_Test {

  @Test
  void should_create_error_message_for_non_ASCII_character() {
    // WHEN
    String message = shouldBeASCII("\u2303").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"\u2303\" to be ASCII"));
  }

  @Test
  void should_create_error_message_for_strings_with_ASCII_character() {
    // WHEN
    String message = shouldBeASCII("123\u230300abc").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"123\u230300abc\" to be ASCII"));
  }
}
