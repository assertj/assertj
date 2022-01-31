package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeHexadecimal.shouldBeHexadecimal;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class ShouldBeHexadecimal_create_Test {
  @Test
  void should_create_error_message_for_special_character() {
    // WHEN
    String message = shouldBeHexadecimal('Z').create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting 'Z' to be hexadecimal"));
  }

  @Test
  void should_create_error_message_for_strings_with_special_chars() {
    // WHEN
    String message = shouldBeHexadecimal("123.abc").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"123.abc\" to be hexadecimal"));
  }
}
