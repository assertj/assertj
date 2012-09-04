/*
 * Created on Dec 24, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal.strings;

import static org.fest.assertions.error.ShouldBeEqualIgnoringCase.shouldBeEqual;
import static org.fest.assertions.test.CharArrays.arrayOf;
import static org.fest.assertions.test.TestData.someInfo;
import static org.fest.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;

import static org.mockito.Mockito.verify;

import org.junit.Test;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.StringsBaseTest;
import org.fest.assertions.internal.Strings;

/**
 * Tests for <code>{@link Strings#assertEqualsIgnoringCase(AssertionInfo, String, String)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Strings_assertEqualsIgnoringCase_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_null_and_expected_is_not() {
    AssertionInfo info = someInfo();
    try {
      strings.assertEqualsIgnoringCase(info, null, "Luke");
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(info, null, "Luke");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_both_Strings_are_not_equal_regardless_of_case() {
    AssertionInfo info = someInfo();
    try {
      strings.assertEqualsIgnoringCase(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(info, "Yoda", "Luke");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  private void verifyFailureThrownWhenStringsAreNotEqual(AssertionInfo info, String actual, String expected) {
    verify(failures).failure(info, shouldBeEqual(actual, expected));
  }

  @Test
  public void should_pass_if_both_Strings_are_null() {
    strings.assertEqualsIgnoringCase(someInfo(), null, null);
  }

  @Test
  public void should_pass_if_both_Strings_are_the_same() {
    String s = "Yoda";
    strings.assertEqualsIgnoringCase(someInfo(), s, s);
  }

  @Test
  public void should_pass_if_both_Strings_are_equal_but_not_same() {
    strings.assertEqualsIgnoringCase(someInfo(), "Yoda", new String(arrayOf('Y', 'o', 'd', 'a')));
  }

  @Test
  public void should_pass_if_both_Strings_are_equal_ignoring_case() {
    strings.assertEqualsIgnoringCase(someInfo(), "Yoda", "YODA");
  }

  @Test
  public void should_fail_if_actual_is_null_and_expected_is_not_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(info, null, "Luke");
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(info, null, "Luke");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_both_Strings_are_not_equal_regardless_of_case_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(info, "Yoda", "Luke");
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(info, "Yoda", "Luke");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_pass_if_both_Strings_are_null_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), null, null);
  }

  @Test
  public void should_pass_if_both_Strings_are_the_same_whatever_custom_comparison_strategy_is() {
    String s = "Yoda";
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), s, s);
  }

  @Test
  public void should_pass_if_both_Strings_are_equal_but_not_same_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), "Yoda", new String(
        arrayOf('Y', 'o', 'd', 'a')));
  }

  @Test
  public void should_pass_if_both_Strings_are_equal_ignoring_case_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertEqualsIgnoringCase(someInfo(), "Yoda", "YODA");
  }
}
