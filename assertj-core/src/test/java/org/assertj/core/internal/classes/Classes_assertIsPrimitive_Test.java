package org.assertj.core.internal.classes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBePrimitive.shouldBePrimitive;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertIsPrimitive(AssertionInfo, Class)}</code> .
 *
 * @author Manuel Gutierrez
 */
class Classes_assertIsPrimitive_Test extends ClassesBaseTest {

  @Test
  void should_pass_if_actual_has_byte_type() {
    // GIVEN
    actual = byte.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_short_type() {
    // GIVEN
    actual = short.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_int_type() {
    // GIVEN
    actual = int.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_long_type() {
    // GIVEN
    actual = long.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_float_type() {
    // GIVEN
    actual = float.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_double_type() {
    // GIVEN
    actual = double.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_boolean_type() {
    // GIVEN
    actual = boolean.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_pass_if_actual_has_char_type() {
    // GIVEN
    actual = char.class;
    // WHEN/THEN
    classes.assertIsPrimitive(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPrimitive());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_an_object() {
    // GIVEN
    actual = Object.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPrimitive());
    // THEN
    then(assertionError).hasMessage(shouldBePrimitive(actual).create());
  }
}
