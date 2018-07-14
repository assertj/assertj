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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldContainOneOrMoreWhitespaces.shouldContainOneOrMoreWhitespaces;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;

@RunWith(DataProviderRunner.class)
public class Strings_assertContainsWhitespaces_Test extends StringsBaseTest {

  @Test
  @DataProvider(value = {
      " ",
      "\u005Ct", // tab
      "\u005Cn", // line feed
      "\u005Cr", // carriage return
      " \u005Cn\u005Cr  "
  }, trimValues = false)
  public void should_pass_if_string_contains_only_whitespaces(String actual) {
    strings.assertContainsWhitespaces(someInfo(), actual);
  }

  @Test
  @DataProvider(value = {
      "a ",
      "a b",
      "a  b",
      "a\u005Ctb", // tab
      "a\u005Cnb", // line feed
      "a\u005Crb", // carriage return
      "a \u005Cn\u005Cr  b"
  }, trimValues = false)
  public void should_pass_if_string_contains_one_or_more_whitespaces(String actual) {
    strings.assertContainsWhitespaces(someInfo(), actual);
  }

  @Test
  @DataProvider(value = {
      "null",
      "",
      "a",
      "bc",
  }, trimValues = false)
  public void should_fail_if_string_does_not_contain_any_whitespaces(String actual) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertContainsWhitespaces(someInfo(), actual))
                                                   .withMessage(shouldContainOneOrMoreWhitespaces(actual).create());
  }
}
