package org.assertj.core.api.object;

import java.util.Optional;
import java.util.function.Function;

import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.test.ObjectWithOptionalField;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractObjectAssert#returnsValue(Object, Function)}</code>.
 *
 * @author TODO
 */

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
    ObjectWithOptionalField out = new ObjectWithOptionalField(Optional.of("other string"));

    // THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(out).returnsValue("test string", ObjectWithOptionalField::getOptString));
  }

  @Test
  void should_fail_with_optional_null() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(null);

    // THEN
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(out).returnsValue("", ObjectWithOptionalField::getOptString));
  }

  @Test
  void should_throw_NullPointerException_if_method_is_null() {
    // GIVEN
    ObjectWithOptionalField out = new ObjectWithOptionalField(Optional.of("test string"));

    // THEN
    ThrowableAssert.ThrowingCallable error = () -> assertThat(out).returnsValue("test string", null);
    assertThatThrownBy(error).isExactlyInstanceOf(NullPointerException.class)
      .hasMessage("The given getter method/Function must not be null");
  }
}
