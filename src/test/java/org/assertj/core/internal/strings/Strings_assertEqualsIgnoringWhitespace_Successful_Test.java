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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.internal.strings;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static org.assertj.core.error.ShouldBeEqualIgnoringWhitespace.shouldBeEqualIgnoringWhitespace;
import static org.assertj.core.test.CharArrays.arrayOf;
import static org.assertj.core.test.ErrorMessages.charSequenceToLookForIsNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Lists.newArrayList;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertEqualsIgnoringWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>.
 *
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Alexander Bischof
 */
@RunWith(Parameterized.class)
public class Strings_assertEqualsIgnoringWhitespace_Successful_Test extends StringsBaseTest {

  @Parameterized.Parameters
  public static List<Object[]> parameters() {
      return newArrayList(new Object[][] { { " my\tfoo bar ", " my foo bar" },
            { " my\tfoo bar ", new String(arrayOf(' ', 'm', 'y', ' ', 'f', 'o', 'o', ' ', 'b', 'a', 'r'))},
            { " my\tfoo bar ", " my\tfoo bar "},         //same
            { null, null},         //null
            { " \t \t", " "},
            { " abc", "abc "}
      });
  }

  private final String actual;
  private final String expected;

  public Strings_assertEqualsIgnoringWhitespace_Successful_Test(String actual, String expected) {
    this.actual = actual;
    this.expected = expected;
  }

  @Test
  public void should_pass_if_both_Strings_are_equal_ignoring_whitespace() {
    strings.assertEqualsIgnoringWhitespace(someInfo(), actual, expected);
  }
}
