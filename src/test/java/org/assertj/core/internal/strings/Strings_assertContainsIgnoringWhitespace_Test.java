package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldContainCharSequence.*;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

/**
 * Tests for <code>{@link Strings#assertContainsIgnoringWhitespace(AssertionInfo, CharSequence, CharSequence...)} </code>.
 *
 * @author Johannes Becker
 */
public class Strings_assertContainsIgnoringWhitespace_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_does_not_contain_sequence() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "Luke"))
      .withMessage(shouldContainIgnoringWhitespace("Yoda", "Luke").create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_but_in_different_case() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "yo"))
      .withMessage(shouldContainIgnoringWhitespace("Yoda", "yo").create());
  }

  @Test
  void should_fail_if_actual_contains_sequence_with_whitespaces_but_in_different_case(){
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda and Luke", "a n dluke"))
      .withMessage(shouldContainIgnoringWhitespace("Yoda and Luke", "a n dluke").create());
  }

  @Test
  void should_throw_error_if_sequence_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda", (String) null))
      .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsIgnoringWhitespace(someInfo(), null, "Yoda"))
      .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_sequence() {
    strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "Yo");
  }

  @Test
  void should_pass_if_actual_contains_sequence_with_whitespaces() {
    strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda\n and\r Luke", "\ta n dLuke");
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "Yo", "da", "Han"))
      .withMessage(shouldContainIgnoringWhitespace("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"), StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings() {
    strings.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "Yo", "da");
  }

  @Test
  void should_pass_if_actual_contains_sequence_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "Yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "YO");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda and Luke", " a n dluke");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda and luke", " a n dLuke");
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda and Luke", "YO", "dA", "Aa", " n d l");
  }

  @Test
  void should_fail_if_actual_does_not_contain_sequence_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "Luke"))
      .withMessage(shouldContainIgnoringWhitespace("Yoda", "Luke", comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), "Yoda", "Yo", "da", "Han"))
      .withMessage(shouldContainIgnoringWhitespace("Yoda", array("Yo", "da", "Han"), newLinkedHashSet("Han"), comparisonStrategy).create());
  }
}
