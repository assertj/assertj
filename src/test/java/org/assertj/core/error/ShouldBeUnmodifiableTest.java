package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeUnmodifiable.shouldBeUnmodifiable;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

class ShouldBeUnmodifiableTest {

  @Test
  void should_create_error_message_with_String() {
    // GIVEN
    ErrorMessageFactory underTest = shouldBeUnmodifiable("method()");
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual to be unmodifiable, but invoking%n" +
                                   "  \"method()\"%n" +
                                   "succeeded instead of throwing java.lang.UnsupportedOperationException"));
  }

  @Test
  void should_create_error_message_with_String_and_RuntimeException_without_message() {
    // GIVEN
    ErrorMessageFactory underTest = shouldBeUnmodifiable("method()", new RuntimeException());
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual to be unmodifiable, but invoking%n" +
                                   "  \"method()\"%n" +
                                   "thrown%n" +
                                   "  \"java.lang.RuntimeException\"%n" +
                                   "instead of java.lang.UnsupportedOperationException"));
  }

  @Test
  void should_create_error_message_with_String_and_RuntimeException_with_message() {
    // GIVEN
    ErrorMessageFactory underTest = shouldBeUnmodifiable("method()", new RuntimeException("message"));
    // WHEN
    String message = underTest.create(new TestDescription("Test"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual to be unmodifiable, but invoking%n" +
                                   "  \"method()\"%n" +
                                   "thrown%n" +
                                   "  \"java.lang.RuntimeException: message\"%n" +
                                   "instead of java.lang.UnsupportedOperationException"));
  }

}
