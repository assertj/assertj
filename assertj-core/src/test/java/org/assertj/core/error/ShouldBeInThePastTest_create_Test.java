package org.assertj.core.error;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInThePast.shouldBeInThePast;

import java.time.LocalDate;

import org.assertj.core.internal.TestDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

/**
 * @author Stefan Bratanov
 */
class ShouldBeInThePastTest_create_Test {

  @Test
  void should_create_error_message() {
    // GIVEN
    LocalDate futureDate = LocalDate.of(2999, 7, 28);
    ErrorMessageFactory factory = shouldBeInThePast(futureDate);
    // WHEN
    String message = factory.create(new TestDescription("Test"), new StandardRepresentation());
    // THEN
    then(message).isEqualTo(format("[Test] %n" +
                                   "Expecting actual:%n" +
                                   "  2999-07-28 (java.time.LocalDate)%n" +
                                   "to be in the past but was not."));
  }

}
