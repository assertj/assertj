package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInTheFuture.shouldBeInTheFuture;

import java.time.LocalDate;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * @author Stefan Bratanov
 */
class ShouldBeInTheFutureTest_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    LocalDate pastDate = LocalDate.of(1993, 7, 28);
    ErrorMessageFactory factory = shouldBeInTheFuture(pastDate);
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  1993-07-28 (java.time.LocalDate)%n" +
                                   "to be in the future but was not."));
  }

}
