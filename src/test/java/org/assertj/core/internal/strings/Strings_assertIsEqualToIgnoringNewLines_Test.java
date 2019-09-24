/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.error.ShouldBeEqualIgnoringNewLines.shouldBeEqualIgnoringNewLines;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link Strings#assertIsEqualToIgnoringNewLines(AssertionInfo, CharSequence, CharSequence)}</code>.
 *
 * @author Daniel Weber
 */
class Strings_assertIsEqualToIgnoringNewLines_Test extends StringsBaseTest {

  private static final String ACTUAL_WITHOUT_NEW_LINES = "Some textWith new lines";
  private static final String ACTUAL_ON_UNIX = "Some text\nWith new lines";
  private static final String ACTUAL_ON_WINDOWS = "Some text\r\nWith new lines";
  private static final String ACTUAL_WITH_CONSECUTIVE_NEWLINES = "Some text\n\nWith new lines";

  @ParameterizedTest
  @ValueSource(strings = { ACTUAL_WITHOUT_NEW_LINES, ACTUAL_ON_UNIX, ACTUAL_ON_WINDOWS, ACTUAL_WITH_CONSECUTIVE_NEWLINES })
  void should_pass_if_both_texts_contain_new_lines_of_any_kind(String expected) {
    // GIVEN
    String actual = "Some text\nWith new lines";
    // WHEN
    strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, expected);
  }

  @ParameterizedTest
  @ValueSource(strings = { ACTUAL_WITHOUT_NEW_LINES, ACTUAL_ON_UNIX, ACTUAL_ON_WINDOWS, ACTUAL_WITH_CONSECUTIVE_NEWLINES })
  void should_fail_if_actual_contains_new_lines_and_expected_has_no_new_lines(String actual) {
    // GIVEN
    String expected = "Some text With new lines";
    // WHEN
    expectAssertionError(() -> strings.assertIsEqualToIgnoringNewLines(someInfo(), actual, expected));
    // THEN
    verify(failures).failure(someInfo(), shouldBeEqualIgnoringNewLines(actual, expected), actual, expected);
  }

}
