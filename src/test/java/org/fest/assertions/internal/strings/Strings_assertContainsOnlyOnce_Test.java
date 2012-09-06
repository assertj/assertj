package org.fest.assertions.internal.strings;

import static org.fest.assertions.error.ShouldContainStringOnlyOnce.shouldContainOnlyOnce;
import static org.fest.assertions.test.ErrorMessages.sequenceToLookForIsNull;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.fest.util.FailureMessages.actualIsNull;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.StringsBaseTest;

public class Strings_assertContainsOnlyOnce_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_is_contains_given_string_only_once() {
    strings.assertContainsOnlyOnce(someInfo(), "Yoda", "Yo");
  }

  @Test
  public void should_fail_if_actual_contains_given_string_more_than_once() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContainsOnlyOnce(info, "Yodayoda", "oda");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyOnce("Yodayoda", "oda", 2));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_sequence_only_once_but_in_different_case() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContainsOnlyOnce(info, "Yoda", "yo");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyOnce("Yoda", "yo", 0));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_does_not_contain_given_string() {
    AssertionInfo info = someInfo();
    try {
      strings.assertContainsOnlyOnce(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyOnce("Yoda", "Luke", 0));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_throw_error_if_sequence_is_null() {
    thrown.expectNullPointerException(sequenceToLookForIsNull());
    strings.assertContainsOnlyOnce(someInfo(), "Yoda", null);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    strings.assertContainsOnlyOnce(someInfo(), null, "Yoda");
  }

  @Test
  public void should_pass_if_actual_contains_sequence_only_once_according_to_custom_comparison_strategy() {
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), "Yoda", "Yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), "Yoda", "yo");
    stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(someInfo(), "Yoda", "YO");
  }

  @Test
  public void should_fail_if_actual_does_not_contain_sequence_only_once_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyOnce("Yoda", "Luke", 0, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_actual_contains_sequence_several_times_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertContainsOnlyOnce(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldContainOnlyOnce("Yoda", "Luke", 0, comparisonStrategy));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

}
