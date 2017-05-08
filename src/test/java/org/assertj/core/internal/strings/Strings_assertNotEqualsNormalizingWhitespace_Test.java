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

import static com.tngtech.java.junit.dataprovider.DataProviders.$;
import static com.tngtech.java.junit.dataprovider.DataProviders.$$;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace.shouldNotBeEqualNormalizingWhitespace;
import static org.assertj.core.internal.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.CharArrays.arrayOf;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertNotEqualsNormalizingWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Dan Corder
 */
@RunWith(DataProviderRunner.class)
public class Strings_assertNotEqualsNormalizingWhitespace_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_not_null_and_expected_is_null() {
    thrown.expectNullPointerException(charSequenceToLookForIsNull());
    strings.assertNotEqualsNormalizingWhitespace(someInfo(), "Luke", null);
  }

  @Test
  @UseDataProvider("notEqualNormalizingWhitespaceGenerator")
  public void should_pass_if_both_Strings_are_not_equal_after_whitespace_is_normalized(String actual, String expected) {
    strings.assertNotEqualsNormalizingWhitespace(someInfo(), actual, expected);
  }

  @DataProvider
  public static Object[][] notEqualNormalizingWhitespaceGenerator() {
    // @format:off
    return $$($("foo", "bar"),
              $("my foo", "myfoo"),
              $("foo", new String(arrayOf('b', 'a', 'r'))),
              $(null, "bar"));
    // @format:on
  }

  @Test
  @UseDataProvider("equalNormalizingWhitespaceGenerator")
  public void should_fail_if_both_Strings_are_equal_after_whitespace_is_normalized(String actual, String expected) {
    thrown.expectAssertionError(shouldNotBeEqualNormalizingWhitespace(actual, expected));
    strings.assertNotEqualsNormalizingWhitespace(someInfo(), actual, expected);  }

  @DataProvider
  public static Object[][] equalNormalizingWhitespaceGenerator() {
    // @format:off
    return $$($("my   foo bar", "my foo bar"),
              $("  my foo bar  ", "my foo bar"),
              $(" my\tfoo bar ", " my foo bar"),
              $(" my foo    bar ", "my foo bar"),
              $(" my foo    bar ", "  my foo bar   "),
              $("       ", " "),
              $(" my\tfoo bar ", new String(arrayOf(' ', 'm', 'y', ' ', 'f', 'o', 'o', ' ', 'b', 'a', 'r'))),
              $(" my\tfoo bar ", " my\tfoo bar "),   // same
              $(null, null),   // null
              $(" \t \t", " "),
              $(" abc", "abc "));
    // @format:on
  }
}
