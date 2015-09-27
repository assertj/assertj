package org.assertj.core.error;

import org.junit.Test;

import java.util.Optional;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.OptionalShouldContainInstanceOf.shouldContainInstanceOf;

public class OptionalShouldContainInstanceOf_create_Test {

  @Test
  public void should_create_error_message_with_expected_type() {
    String errorMessage = shouldContainInstanceOf(Optional.empty(), Object.class).create();
    assertThat(errorMessage).isEqualTo(format("%nExpecting Optional to contain a value of type java.lang.Object."));
  }
}