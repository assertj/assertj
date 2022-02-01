package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeASCII.shouldBeASCII;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

public class Strings_assertIsASCII_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_ASCII() {
    strings.assertASCII(someInfo(), "Lego123");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertASCII(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_ASCII() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertASCII(someInfo(), "\u2303"))
      .withMessage(shouldBeASCII("\u2303").create());
  }

  @Test
  void should_fail_if_actual_has_non_ASCII_chars() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertASCII(someInfo(), "01234\u2303abc"))
      .withMessage(shouldBeASCII("01234\u2303abc").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertASCII(someInfo(), null))
      .withMessage(actualIsNull());
  }
}
