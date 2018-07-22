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
import static org.assertj.core.error.ShouldBeBlank.shouldBeBlank;
import static org.assertj.core.test.TestData.someInfo;

import java.util.stream.Stream;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Strings_assertBlank_Test extends StringsBaseTest {

  public static Stream<Arguments> blank() {
    return Stream.of(Arguments.of((String) null),
                     Arguments.of(""),
                     Arguments.of(" "),
                     Arguments.of("\u005Ct"), // tab
                     Arguments.of("\u005Cn"), // line feed
                     Arguments.of("\u005Cr"), // carriage return
                     Arguments.of(" \u005Cn\u005Cr  "));
  }

  @ParameterizedTest
  @MethodSource("blank")
  public void should_pass_if_string_is_blank(String actual) {
    strings.assertBlank(someInfo(), actual);
  }

  public static Stream<Arguments> notBlank() {
    return Stream.of(Arguments.of("a"),
                     Arguments.of(" bc "),
                     Arguments.of("\u00A0"), // non-breaking space
                     Arguments.of("\u2007"), // non-breaking space
                     Arguments.of("\u202F")); // non-breaking space);
  }

  @ParameterizedTest
  @MethodSource("notBlank")
  public void should_fail_if_string_is_not_blank(String actual) {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertBlank(someInfo(), actual))
                                                   .withMessage(shouldBeBlank(actual).create());
  }
}
