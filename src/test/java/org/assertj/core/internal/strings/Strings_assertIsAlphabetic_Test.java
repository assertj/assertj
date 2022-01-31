package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeAlphabetic.shouldBeAlphabetic;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

class Strings_assertIsAlphabetic_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_alphabetic() {
    strings.assertAlphabetic(someInfo(), "Lego");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertAlphabetic(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_alphabetic() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertAlphabetic(someInfo(), "L3go!"))
      .withMessage(shouldBeAlphabetic("L3go!").create());
  }

  @Test
  void should_fail_if_actual_is_numeric() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertAlphabetic(someInfo(), "0123456789"))
      .withMessage(shouldBeAlphabetic("0123456789").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertAlphabetic(someInfo(), null))
      .withMessage(actualIsNull());
  }
}
