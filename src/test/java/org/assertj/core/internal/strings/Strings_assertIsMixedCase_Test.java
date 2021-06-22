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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeMixedCase.shouldBeMixedCase;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link org.assertj.core.internal.Strings#assertMixedCase(org.assertj.core.api.AssertionInfo, CharSequence)} </code>.
 *
 * @author Andrey Kuzmin
 */
@DisplayName("Strings assertIsMixedCase")
class Strings_assertIsMixedCase_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_mixed_case() {
    strings.assertMixedCase(someInfo(), "anExampleOfCamelCaseString");
  }

  @Test()
  void should_pass_if_actual_is_mixed_case_with_numbers_and_special_symbols() {
    strings.assertMixedCase(someInfo(), "anEx4mpl3OfC4m3lC4s3Str1ng!");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertMixedCase(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_single_lowercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), "p"))
      .withMessage(shouldBeMixedCase("p").create());
  }

  @Test
  void should_fail_if_actual_is_lowercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), "please be quiet"))
      .withMessage(shouldBeMixedCase("please be quiet").create());
  }

  @Test
  void should_pass_if_actual_is_whitespace() {
    strings.assertMixedCase(someInfo(), " ");
  }

  @Test
  void should_pass_if_actual_is_has_no_letters() {
    strings.assertMixedCase(someInfo(), "@$#24^");
  }

  @Test
  void should_fail_if_actual_is_single_uppercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), "P"))
      .withMessage(shouldBeMixedCase("P").create());
  }

  @Test
  void should_fail_if_actual_is_uppercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), "I AM GROOT!"))
                                                   .withMessage(shouldBeMixedCase("I AM GROOT!").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), null))
      .withMessage(shouldNotBeNull().create());
  }

}
