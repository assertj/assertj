package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeVisible.shouldBeVisible;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

public class Strings_assertIsVisible_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_visible() {
    strings.assertVisible(someInfo(), "Lego");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertVisible(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_visible() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertVisible(someInfo(), "\t"))
      .withMessage(shouldBeVisible("\t").create());
  }

  @Test
  void should_fail_if_actual_contains_non_visible_characters() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertVisible(someInfo(), "01234\n56789"))
      .withMessage(shouldBeVisible("01234\n56789").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertVisible(someInfo(), null))
      .withMessage(actualIsNull());
  }
}
