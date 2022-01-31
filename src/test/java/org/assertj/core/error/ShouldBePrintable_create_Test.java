package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBePrintable.shouldBePrintable;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

public class ShouldBePrintable_create_Test {

  @Test
  void should_create_error_message_for_character() {
    // WHEN
    String message = shouldBePrintable("\\t").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"\\t\" to be printable"));
  }

  @Test
  void should_create_error_message_for_number() {
    // WHEN
    String message = shouldBePrintable("12\n3").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"12\n3\" to be printable"));
  }
}
