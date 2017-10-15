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

import static org.assertj.core.api.Assertions.assertThat;
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
public class Strings_assertEqualsToIgnoringNewLines_Test extends StringsBaseTest {

  @Test
  public void should_pass_if_both_texts_contain_new_lines_of_any_kind() {
    String actual = "Some text\nWith new lines";
    String expectedOnLinux = "Some text\nWith new lines";
    String expectedOnWindows = "Some text\r\nWith new lines";
    String expectedWithConsecutiveNewlines= "Some text\n\nWith new lines";

    assertThat(actual).isEqualToIgnoringNewLines(expectedOnLinux)
      .isEqualToIgnoringNewLines(expectedOnWindows)
      .isEqualToIgnoringNewLines(expectedWithConsecutiveNewlines);
  }

  @Test
  public void should_fail_if_actual_contains_new_lines_and_expected_has_no_new_lines() {
    String expected = "Some text With new lines";
    VerifyThatAssertationErrorWasThrown("Some text\nWith new lines", expected);
    VerifyThatAssertationErrorWasThrown("Some text\r\nWith new lines", expected);
  }

  private void VerifyThatAssertationErrorWasThrown(String actual, String expected) {
    try {
      strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLines(actual, expected));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
