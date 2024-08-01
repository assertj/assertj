package org.assertj.core.error;

import org.assertj.core.internal.TestDescription;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeInFloatingPointFormat.shouldBeInFloatingPointFormat;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;

public class ShouldBeFloatingPointFormat_create_Test {
  @Test
  void should_create_error_message() {
    // GIVEN
    BigDecimal actual = new BigDecimal("12");
    // WHEN
    String message = shouldBeInFloatingPointFormat(actual).create(new TestDescription("TEST"), STANDARD_REPRESENTATION);
    // THEN
    then(message).isEqualTo(format("[TEST] %nExpecting:%n  <%s>%nto be in floating point format but it was not.", actual));
  }
}
