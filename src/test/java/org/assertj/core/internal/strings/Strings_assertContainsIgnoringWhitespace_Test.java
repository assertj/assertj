package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldContainCharSequence.shouldContainIgnoringWhitespace;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

/**
 * Tests for <code>{@link Strings#assertContainsIgnoringWhitespace(AssertionInfo, CharSequence, CharSequence...)} </code>.
 *
 * @author Johannes Becker
 */
public class Strings_assertContainsIgnoringWhitespace_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_does_not_contain_value() {
    // GIVEN
    String actual = "Yoda";
    String value = "Luke";
    // WHEN
    AssertionError assertionError = expectAssertionError(() ->
      strings.assertContainsIgnoringWhitespace(someInfo(), actual, value)
    );
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespace("Yoda", "Luke").create());
  }

  @Test
  void should_fail_if_actual_contains_value_but_in_different_case() {
    // GIVEN
    String actual = "Yoda";
    String value = "yo";

    // WHEN
    AssertionError assertionError = expectAssertionError(() ->
      strings.assertContainsIgnoringWhitespace(someInfo(), actual, value)
    );
    //THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespace("Yoda", "yo").create());
  }

  @Test
  void should_fail_if_actual_contains_value_with_whitespaces_but_in_different_case() {
    // GIVEN
    String actual = "Yoda and Luke";
    String value = "a n dluke";

    // WHEN
    AssertionError assertionError = expectAssertionError(() ->
      strings.assertContainsIgnoringWhitespace(someInfo(), actual, value)
    );
    //THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespace("Yoda and Luke", "a n dluke").create());
  }

  @Test
  void should_throw_error_if_value_is_null() {
    // GIVEN
    String actual = "Yoda";
    String value = null;
    //WHEN / THEN
    assertThatNullPointerException().isThrownBy(() -> strings.assertContainsIgnoringWhitespace(someInfo(), actual, value))
      .withMessage(charSequenceToLookForIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    String value = "Yoda";

    // WHEN
    AssertionError assertionError = expectAssertionError(() ->
      strings.assertContainsIgnoringWhitespace(someInfo(), actual, value)
    );
    //THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_contains_value() {
    // GIVEN
    String actual = "Yoda";
    String value = "Yo";
    // WHEN / THEN
    strings.assertContainsIgnoringWhitespace(someInfo(), actual, value);
  }

  @Test
  void should_pass_if_actual_contains_value_with_whitespaces() {
    // GIVEN
    String actual = "Yoda\n and\r Luke";
    String value = "\ta n dLuke";
    // WHEN / THEN
    strings.assertContainsIgnoringWhitespace(someInfo(), actual, value);
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings() {
    // GIVEN
    String actual = "Yoda";
    String[] values = array("Yo", "da", "Han");

    // WHEN
    AssertionError assertionError = expectAssertionError(() ->
      strings.assertContainsIgnoringWhitespace(someInfo(), actual, values)
    );
    //THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespace(actual, values, newLinkedHashSet("Han")).create());
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings() {
    // GIVEN
    String actual = "Yoda";
    String[] values = array("Yo", "da");

    // WHEN / THEN
    strings.assertContainsIgnoringWhitespace(someInfo(), actual, values);
  }

  @ParameterizedTest
  @ValueSource(strings = {"Yo", "yo", "YO", "a n dluke", "A N Dluke", "and L u   k"})
  void should_pass_if_actual_contains_value_according_to_custom_comparison_strategy(String value) {
    //GIVEN
    String actual = "Yoda and Luke";
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), actual, value);
  }

  @Test
  void should_pass_if_actual_contains_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda and Luke";
    String[] values = array("YO", "dA", "Aa", " n d l");
    // WHEN / THEN
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), actual, values);
  }

  @Test
  void should_fail_if_actual_does_not_contain_value_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String value = "Luke";
    // WHEN
    AssertionError assertionError = expectAssertionError(() ->
      stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), actual, value)
    );
    // THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespace("Yoda", "Luke", comparisonStrategy).create());
  }

  @Test
  void should_fail_if_actual_does_not_contain_all_given_strings_according_to_custom_comparison_strategy() {
    // GIVEN
    String actual = "Yoda";
    String[] values = array("Yo", "da", "Han");

    // WHEN
    AssertionError assertionError = expectAssertionError(() ->
      stringsWithCaseInsensitiveComparisonStrategy.assertContainsIgnoringWhitespace(someInfo(), actual, values)
    );
    //THEN
    then(assertionError).hasMessage(shouldContainIgnoringWhitespace(actual, values, newLinkedHashSet("Han"), comparisonStrategy).create());
  }
}
