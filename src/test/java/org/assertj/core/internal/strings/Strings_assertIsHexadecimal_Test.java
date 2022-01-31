package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeHexadecimal.shouldBeHexadecimal;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

public class Strings_assertIsHexadecimal_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_hexadecimal() {
    strings.assertHexadecimal(someInfo(), "A");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertHexadecimal(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_hexadecimal() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHexadecimal(someInfo(), "ZZ"))
      .withMessage(shouldBeHexadecimal("ZZ").create());
  }

  @Test
  void should_fail_if_actual_has_non_hexadecimal_chars() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHexadecimal(someInfo(), "01234!abc"))
      .withMessage(shouldBeHexadecimal("01234!abc").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHexadecimal(someInfo(), null))
      .withMessage(actualIsNull());
  }
}
