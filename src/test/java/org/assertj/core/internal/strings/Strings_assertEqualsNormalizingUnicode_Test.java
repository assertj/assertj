package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldBeEqualNormalizingUnicode.shouldBeEqualNormalizingUnicode;

import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertEqualsToNormalizingUnicode(AssertionInfo, CharSequence, CharSequence)} (org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Julieta Navarro
 */
public class Strings_assertEqualsNormalizingUnicode_Test extends StringsBaseTest {
  @Test
  void should_fail_if_actual_is_not_null_and_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertEqualsToNormalizingUnicode(someInfo(), "\u0041\u0308",
      null))
      .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_both_Strings_are_not_equal_after_unicode_is_normalized() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertEqualsToNormalizingUnicode(someInfo(),
      "\u00C4",
      "\u0041"))
      .withMessage(format(shouldBeEqualNormalizingUnicode("\u00C4",
        "\u0041").create()));
  }

  @ParameterizedTest
  @MethodSource("equalNormalizingUnicodeGenerator")
  void should_pass_if_both_Strings_are_equal_after_unicode_is_normalized(String actual, String expected) {
    strings.assertEqualsToNormalizingUnicode(someInfo(), actual, expected);
  }

  public static Stream<Arguments> equalNormalizingUnicodeGenerator() {
    return Stream.of(Arguments.of("\u00C4", "\u0041\u0308"));
  }
}
