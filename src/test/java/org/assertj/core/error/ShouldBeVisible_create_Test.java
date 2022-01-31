package org.assertj.core.error;

import org.assertj.core.description.TextDescription;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeVisible.shouldBeVisible;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

public class ShouldBeVisible_create_Test {

  @Test
  void should_create_error_message_for_character() {
    // WHEN
    String message = shouldBeVisible("\\t").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"\\t\" to be visible"));
  }

  @Test
  void should_create_error_message_for_number() {
    // WHEN
    String message = shouldBeVisible("12\\n3").create(new TextDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %nExpecting \"12\\n3\" to be visible"));
  }
}
