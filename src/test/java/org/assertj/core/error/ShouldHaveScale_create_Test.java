package org.assertj.core.error;

import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveScale.shouldHaveScale;

public class ShouldHaveScale_create_Test {

  @Test
  public void should_create_error_message() {
    // GIVEN
    BigDecimal BIG_DECIMAL = new BigDecimal("1.000");
    ErrorMessageFactory factory = shouldHaveScale(BIG_DECIMAL, 4);

    // WHEN
    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    // THEN
    then(message).isEqualTo(format("[Test] %nexpecting actual %s to have a scale of:%n  %s%nbut had a scale of:%n  %s", BIG_DECIMAL ,4, BIG_DECIMAL.scale()));
  }

}
