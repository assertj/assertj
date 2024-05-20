/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeMixedCase.shouldBeMixedCase;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Andrey Kuzmin
 */
class Strings_assertIsMixedCase_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertMixedCase(someInfo(), null));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "I AM GROOT!",
      "P",
  })
  void should_fail_if_actual_is_uppercase(CharSequence actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertMixedCase(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeMixedCase(actual).create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "please be quiet",
      "p",
  })
  void should_fail_if_actual_is_lowercase(CharSequence actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> strings.assertMixedCase(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeMixedCase(actual).create());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "",
      " ",
      "anExampleOfCamelCaseString",
      "anEx4mpl3OfC4m3lC4s3Str1ng!", // with numbers and special characters
      "@$#24^", // only numbers and special characters
  })
  void should_pass_if_actual_is_mixed_case(CharSequence actual) {
    // WHEN/THEN
    strings.assertMixedCase(someInfo(), actual);
  }

}
