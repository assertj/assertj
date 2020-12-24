package org.assertj.core.internal.strings;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.test.TestData.someInfo;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingUnicode.shouldNotBeEqualNormalizingUnicode;



public class Strings_assertNotEqualsNormalizingUnicode_Test extends StringsBaseTest  {
  @Test
  void should_fail_if_actual_is_not_null_and_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertNotEqualsToNormalizingUnicode(someInfo(), "\u00C4",
      null))
      .withMessage(charSequenceToLookForIsNull());
  }

  @ParameterizedTest
  @MethodSource("notEqualNormalizingUnicode")
  void should_pass_if_both_Strings_are_not_equal_after_unicode_is_normalized(String actual, String expected) {
    strings.assertNotEqualsToNormalizingUnicode(someInfo(), actual, expected);
  }

  public static Stream<Arguments> notEqualNormalizingUnicode() {
    return Stream.of(Arguments.of("\u00C4", "\u0308"));
  }

  @ParameterizedTest
  @MethodSource("equalNormalizingUnicode")
  void should_fail_if_both_Strings_are_equal_after_unicode_is_normalized(String actual, String expected) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertNotEqualsToNormalizingUnicode(someInfo(),
      actual,
      expected))
      .withMessage(format(shouldNotBeEqualNormalizingUnicode(actual,
        expected).create()));
  }

  public static Stream<Arguments> equalNormalizingUnicode() {
    return Stream.of(Arguments.of("\u00C4", "\u0041\u0308"));
  }
}
