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

import static org.assertj.core.error.ShouldBeEqualIgnoringNewLines.shouldBeEqualIgnoringNewLines;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;

/**
 * Tests for <code>{@link Strings#assertIsEqualToIgnoringNewLines(AssertionInfo, CharSequence, CharSequence)}</code>.
 *
 * @author Daniel Weber
 */
public class Strings_assertIsEqualsToIgnoringNewLines_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_both_texts_contain_new_lines_of_any_kind() {
    String actual = "Some text\nWith new lines";
    String actualWithoutNewLines = "Some textWith new lines";
    String actualOnLinux = "Some text\nWith new lines";
    String actuaOnWindows = "Some text\r\nWith new lines";
    String actuaWithConsecutiveNewlines= "Some text\n\nWith new lines";

    strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, actualWithoutNewLines);
    strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, actualOnLinux);
    strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, actuaOnWindows);
    strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, actuaWithConsecutiveNewlines);
  }

  @Test
  public void should_fail_if_actual_contains_new_lines_and_expected_has_no_new_lines() {
    String expected = "Some text With new lines";
    verifyThatAssertationErrorWasThrown("Some text\nWith new lines", expected);
    verifyThatAssertationErrorWasThrown("Some text\r\nWith new lines", expected);
  }

  private void verifyThatAssertationErrorWasThrown(String actual, String expected) {
    try {
      strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLines(actual, expected));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
