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
import static org.assertj.core.error.ShouldBeEqualNormalizingWhitespace.shouldBeEqualNormalizingWhitespace;
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
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertEqualsNormalizingWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Alexander Bischof
 * @author Dan Corder
 */
@RunWith(DataProviderRunner.class)
public class Strings_assertEqualsNormalizingWhitespace_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_null_and_expected_is_not() {
    thrown.expectAssertionError(shouldBeEqualNormalizingWhitespace(null, "Luke"));
    strings.assertEqualsNormalizingWhitespace(someInfo(), null, "Luke");  }

  @Test
  public void should_fail_if_actual_is_not_null_and_expected_is_null() {
    thrown.expectNullPointerException(charSequenceToLookForIsNull());
    strings.assertEqualsNormalizingWhitespace(someInfo(), "Luke", null);
  }

  @Test
  public void should_fail_if_both_Strings_are_not_equal_after_whitespace_is_normalized() {
    thrown.expectAssertionError(shouldBeEqualNormalizingWhitespace("Yoda", "Luke"));
    strings.assertEqualsNormalizingWhitespace(someInfo(), "Yoda", "Luke");  }

  @Test
  @UseDataProvider("equalNormalizingWhitespaceGenerator")
  public void should_pass_if_both_Strings_are_equal_after_whitespace_is_normalized(String actual, String expected) {
    strings.assertEqualsNormalizingWhitespace(someInfo(), actual, expected);
  }

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
