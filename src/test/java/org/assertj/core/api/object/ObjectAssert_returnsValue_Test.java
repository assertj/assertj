package org.assertj.core.api.object;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.assertj.core.test.ObjectWithOptionalField;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.test.ErrorMessagesForTest.shouldBeEqualMessage;

import org.junit.jupiter.api.Test;

public class ObjectAssert_returnsValue_Test {
  @Test
  void should_pass_optional_returns() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(Optional.of("test string"));

    // THEN
    assertThat(out).returnsValue("test string", ObjectWithOptionalField::getOptString);
  }

  @Test
  void should_fail_when_optional_empty() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(Optional.empty());

    // THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(out).returnsValue("test string", ObjectWithOptionalField::getOptString))
      .withMessage(shouldBePresent(Optional.empty()).create());
  }

  @Test
  void should_fail_when_return_different() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(Optional.of("test string"));

    // THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(out).returnsValue("test string1", ObjectWithOptionalField::getOptString));
  }

  @Test
  void should_fail_with_optional_null() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(null);

    // THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(out).returnsValue("test string1", ObjectWithOptionalField::getOptString));
  }
}
