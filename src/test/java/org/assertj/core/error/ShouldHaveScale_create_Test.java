package org.assertj.core.error;

import org.assertj.core.api.BDDAssertions;
import org.assertj.core.description.TextDescription;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;

import static java.lang.String.format;
import static org.assertj.core.api.BDDAssertions.then;

public class ShouldHaveScale_create_Test {

  @Test
  public void should_create_error_message() {
    ErrorMessageFactory factory = ShouldHaveScale.shouldHaveScale(3, 4);

    String message = factory.create(new TextDescription("Test"), new StandardRepresentation());

    then(message).isEqualTo(format("[Test] %nexpecting actual to have a scale of: %s%n  but has a scale of: %s", 4 ,3));
  }


}
