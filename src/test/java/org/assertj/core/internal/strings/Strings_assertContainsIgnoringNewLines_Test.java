package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.containsIgnoringNewLines;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.internal.StandardComparisonStrategy;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Strings#assertContainsIgnoringNewLines(AssertionInfo, CharSequence, CharSequence...)} </code>.
 */
public class Strings_assertContainsIgnoringNewLines_Test extends StringsBaseTest {
  private static final WritableAssertionInfo INFO = someInfo();

  @ParameterizedTest
  @ValueSource(strings = { "Al", "ice\nandBob", "Alice\nandBob", "Alice\nand\nBob" })
  void should_pass_if_actual_contains_value_when_new_lines_are_ignored(String value) {
    // GIVEN
    String actual = "Alice\nand\nBob";
    // WHEN / THEN
    strings.assertContainsIgnoringNewLines(INFO, actual, value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings() {
    // GIVEN
    String actual = "Alice\nand\nBob";
    String[] values = array("Ali", "ce", "a", "nd", "Bob");
    // WHEN / THEN
    strings.assertContainsIgnoringNewLines(INFO, actual, values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value() {
    // GIVEN
    String actual = "Al";
    String value = "Bob";

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));

    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Al", "Bob", null, null,
                                                             StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_value_but_in_different_case() {
    // GIVEN
    String actual = "Al";
    String value = "al";

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));

    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Al", "al", null, null,
                                                             StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_fail_if_actual_contains_value_with_new_lines_but_in_different_case() {
    // GIVEN
    String actual = "Alice\nand\nbob";
    String value = "and\nBob";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Alice\nand\nbob", "and\nBob", null, null,
                                                             StandardComparisonStrategy.instance()).create());
  }

  @Test
  void should_throw_error_if_value_is_null() {
    // GIVEN
    String actual = "Al";
    String value = null;
    // WHEN / THEN
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value))
                                    .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    String value = "Bob";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, value));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String actual = "Alice";
    String[] values = array("Al", "ice", "Bob");
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertContainsIgnoringNewLines(INFO, actual, values));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines(actual, null, values, newLinkedHashSet("Bob"),
                                                             StandardComparisonStrategy.instance()).create());
  }

  @ParameterizedTest
  @ValueSource(strings = { "Al", "al", "AL", "and\nbob", "and\nBob", "and\nB\no\nb" })
  void should_pass_if_actual_contains_value_according_to_custom_comparison_strategy(String value) {
    // GIVEN
    String actual = "Alice\nand\nBob";
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO, actual, value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Alice\nand\nBob";
    String[] values = array("Al", "iCe", "and", "Bob");
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO, actual, values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Al";
    String value = "Bob";
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO,
                                                                                                                                           actual,
                                                                                                                                           value));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines("Al", "Bob", null, null, comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Alice";
    String[] values = array("Al", "ice", "\nBob");

    // WHEN
    AssertionError assertionError = expectAssertionError(() -> stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringNewLines(INFO,
                                                                                                                                           actual,
                                                                                                                                           values));
    // THEN
    then(assertionError).hasMessage(containsIgnoringNewLines(actual, null, values, newLinkedHashSet("Bob"),
                                                             comparisonStrategy).create());
  }
}
