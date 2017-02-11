/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldNotBeEqualIgnoringCase.shouldNotBeEqualIgnoringCase;
import static org.assertj.core.test.CharArrays.arrayOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Strings#assertNotEqualsIgnoringCase(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)}</code>
 * .
 *
 * @author Alexander Bischof
 */
public class Strings_assertNotEqualsIgnoringCase_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_actual_is_null_and_expected_is_not() {
    strings.assertNotEqualsIgnoringCase(someInfo(), null, "Luke");
  }

  @Test
  public void should_pass_if_actual_is_not_null_and_expected_is() {
    strings.assertNotEqualsIgnoringCase(someInfo(), "Luke", null);
  }

  @Test
  public void should_pass_if_both_Strings_are_not_equal_regardless_of_case() {
    strings.assertNotEqualsIgnoringCase(someInfo(), "Yoda", "Luke");
  }

  private void verifyFailureThrownWhenStringsAreNotEqual(AssertionInfo info, String actual, String expected) {
    verify(failures).failure(info, shouldNotBeEqualIgnoringCase(actual, expected));
  }

  @Test
  public void should_fail_if_both_Strings_are_null() {
    thrown.expectAssertionError(shouldNotBeEqualIgnoringCase(null, null));
    strings.assertNotEqualsIgnoringCase(someInfo(), null, null);
  }

  @Test
  public void should_fail_if_both_Strings_are_the_same() {
    String s = "Yoda";
    thrown.expectAssertionError(shouldNotBeEqualIgnoringCase(s, s));
    strings.assertNotEqualsIgnoringCase(someInfo(), s, s);
  }

  @Test
  public void should_fail_if_both_Strings_are_equal_but_not_same() {
    thrown.expectAssertionError(shouldNotBeEqualIgnoringCase("Yoda", "Yoda"));
    strings.assertNotEqualsIgnoringCase(someInfo(), "Yoda", new String(arrayOf('Y', 'o', 'd', 'a')));
  }

  @Test
  public void should_fail_if_both_Strings_are_equal_ignoring_case() {
    thrown.expectAssertionError(shouldNotBeEqualIgnoringCase("Yoda", "YODA"));
    strings.assertNotEqualsIgnoringCase(someInfo(), "Yoda", "YODA");
  }

  @Test
  public void should_pass_if_actual_is_null_and_expected_is_not_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(someInfo(), null, "Luke");
  }

  @Test
  public void should_pass_if_both_Strings_are_not_equal_regardless_of_case_whatever_custom_comparison_strategy_is() {
    stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(someInfo(), "Yoda", "Luke");
  }

  @Test
  public void should_fail_if_both_Strings_are_null_whatever_custom_comparison_strategy_is() {
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(someInfo(), null, null);
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(someInfo(), null, null);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_both_Strings_are_the_same_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    String s = "Yoda";
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(info, s, s);
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(info, s, s);
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_both_Strings_are_equal_but_not_same_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(info, "Yoda",
                                                                               new String(
                                                                                          arrayOf('Y', 'o', 'd', 'a')));
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(info, "Yoda", "Yoda");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_if_both_Strings_are_equal_ignoring_case_whatever_custom_comparison_strategy_is() {
    AssertionInfo info = someInfo();
    try {
      stringsWithCaseInsensitiveComparisonStrategy.assertNotEqualsIgnoringCase(info, "Yoda", "YODA");
    } catch (AssertionError e) {
      verifyFailureThrownWhenStringsAreNotEqual(info, "Yoda", "YODA");
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
