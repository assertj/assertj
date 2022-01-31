package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBePrintable.shouldBePrintable;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

public class Strings_assertIsPrintable_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_alphabetic() {
    strings.assertPrintable(someInfo(), "Lego");
  }

  @Test
  void should_pass_if_actual_is_empty() {
      strings.assertPrintable(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_printable() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertPrintable(someInfo(), "\t"))
      .withMessage(shouldBePrintable("\t").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertPrintable(someInfo(), null))
      .withMessage(actualIsNull());
  }
}

