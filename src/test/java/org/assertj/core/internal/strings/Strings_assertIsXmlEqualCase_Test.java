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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeEqual.shouldBeEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.xml.XmlStringPrettyFormatter.xmlPrettyFormat;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Strings#assertXmlEqualsTo(org.assertj.core.api.AssertionInfo, CharSequence, CharSequence)}</code>
 * .
 * 
 * @author Joel Costigliola
 */
class Strings_assertIsXmlEqualCase_Test extends StringsBaseTest {

  @Test
  void should_pass_if_both_Strings_are_XML_equals() {
    String actual = "<rss version=\"2.0\"><channel>  <title>Java Tutorials and Examples 1</title>  <language>en-us</language></channel></rss>";
    String expected = String.format("<rss version=\"2.0\">%n"
                                    + "<channel><title>Java Tutorials and Examples 1</title><language>en-us</language></channel>%n"
                                    + "</rss>");
    strings.assertXmlEqualsTo(someInfo(), actual, expected);
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertXmlEqualsTo(someInfo(), null,
                                                                                               "<jedi>yoda</jedi>"))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_expected_is_null() {
    assertThatNullPointerException().isThrownBy(() -> strings.assertXmlEqualsTo(someInfo(), "<jedi>yoda</jedi>", null))
                                    .withMessage("The char sequence to look for should not be null");
  }

  @Test
  void should_fail_if_both_Strings_are_not_XML_equals() {
    String actual = "<rss version=\"2.0\"><channel><title>Java Tutorials</title></channel></rss>";
    String expected = "<rss version=\"2.0\"><channel><title>Java Tutorials and Examples</title></channel></rss>";
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> strings.assertXmlEqualsTo(info, actual, expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(xmlPrettyFormat(actual), xmlPrettyFormat(expected), info.representation()));
  }

  @Test
  void should_pass_if_both_Strings_are_XML_equals_case_insensitively() {
    String actual = "<rss version=\"2.0\"><Channel><title>Java Tutorials</title></Channel></rss>";
    String expected = "<rss version=\"2.0\"><channel><TITLE>JAVA Tutorials</TITLE></channel></rss>";
    stringsWithCaseInsensitiveComparisonStrategy.assertXmlEqualsTo(someInfo(), actual, expected);
  }

  @Test
  void should_fail_if_both_Strings_are_not_XML_equal_regardless_of_case() {
    AssertionInfo info = someInfo();
    String actual = "<rss version=\"2.0\"><channel><title>Java Tutorials</title></channel></rss>";
    String expected = "<rss version=\"2.0\"><channel><title>Java Tutorials and Examples</title></channel></rss>";

    Throwable error = catchThrowable(() -> stringsWithCaseInsensitiveComparisonStrategy.assertXmlEqualsTo(someInfo(), actual,
                                                                                                          expected));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeEqual(xmlPrettyFormat(actual), xmlPrettyFormat(expected),
                                                 info.representation()));
  }

}
