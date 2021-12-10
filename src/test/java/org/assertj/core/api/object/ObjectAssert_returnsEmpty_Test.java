package org.assertj.core.api.object;

import java.util.Optional;
import java.util.function.Function;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.assertj.core.test.ObjectWithOptionalField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.OptionalShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.junit.jupiter.api.Test;

/**
  * Tests for <code>{@link AbstractObjectAssert#returnsEmpty(Function)}</code>.
  *
  * @author Ahmad Sadeed
  */

public class ObjectAssert_returnsEmpty_Test {
  @Test
  void should_pass_if_optional_is_empty() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(Optional.empty());

    // THEN
    assertThat(out).returnsEmpty(ObjectWithOptionalField::getOptString);
  }

  @Test
  void should_fail_when_optional_is_present() {
    // GIVEN
    Optional<String> optional = Optional.of("test string");
    ObjectWithOptionalField out = new ObjectWithOptionalField(optional);

    // THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(out).returnsEmpty(ObjectWithOptionalField::getOptString))
      .withMessage(shouldBeEmpty(optional).create());
  }

  @Test
  void should_fail_when_optional_is_null() {
    ObjectWithOptionalField out = new ObjectWithOptionalField(null);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(out).returnsEmpty(ObjectWithOptionalField::getOptString))
      .withMessage(actualIsNull());
  }

  @Test
  void should_throw_NullPointerException_if_method_is_null() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(Optional.empty());

    // THEN
    ThrowingCallable error = () -> assertThat(out).returnsEmpty(null);
    assertThatThrownBy(error).isExactlyInstanceOf(NullPointerException.class)
      .hasMessage("The given getter method/Function must not be null");
  }
}
