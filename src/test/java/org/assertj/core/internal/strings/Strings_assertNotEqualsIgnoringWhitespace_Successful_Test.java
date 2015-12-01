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
 * Copyright 2015 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.test.CharArrays.arrayOf;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Strings#assertNotEqualsIgnoringWhitespace(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)} </code>
 * .
 *
 * @author Dan Corder
 */
@RunWith(Parameterized.class)
public class Strings_assertNotEqualsIgnoringWhitespace_Successful_Test extends StringsBaseTest {

  private final String actual;
  private final String expected;

  public Strings_assertNotEqualsIgnoringWhitespace_Successful_Test(String actual, String expected) {
    this.actual = actual;
    this.expected = expected;
  }

  @Parameterized.Parameters
  public static List<Object[]> parameters() {
    return newArrayList(new Object[][] {
        { "foo", "bar" },
        { "my foo", "myfoo" },
        { "foo", new String(arrayOf('b', 'a', 'r')) },
        { null, "bar" }
    });
  }

  @Test
  public void should_pass_if_both_Strings_are_not_equal_ignoring_whitespace() {
    strings.assertNotEqualsIgnoringWhitespace(someInfo(), actual, expected);
  }
}
