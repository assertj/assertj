package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeAlphanumeric.shouldBeAlphanumeric;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

public class Strings_assertIsAlphanumeric_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_alphanumeric() {
    strings.assertAlphanumeric(someInfo(), "Lego123");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertAlphanumeric(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_alphanumeric() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertAlphanumeric(someInfo(), "!!!!"))
      .withMessage(shouldBeAlphanumeric("!!!!").create());
  }

  @Test
  void should_fail_if_actual_has_special_chars() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertAlphanumeric(someInfo(), "01234!abc"))
      .withMessage(shouldBeAlphanumeric("01234!abc").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertAlphanumeric(someInfo(), null))
      .withMessage(actualIsNull());
  }
}
