package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeNumeric.shouldBeNumeric;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

public class Strings_assertIsNumeric_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_numeric() {
    strings.assertNumeric(someInfo(), "1");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertNumeric(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_numeric() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertNumeric(someInfo(), "L3go!"))
      .withMessage(shouldBeNumeric("L3go!").create());
  }

  @Test
  void should_fail_if_actual_is_alphabetic() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertNumeric(someInfo(), "abc"))
      .withMessage(shouldBeNumeric("abc").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertNumeric(someInfo(), null))
      .withMessage(actualIsNull());
  }
}
