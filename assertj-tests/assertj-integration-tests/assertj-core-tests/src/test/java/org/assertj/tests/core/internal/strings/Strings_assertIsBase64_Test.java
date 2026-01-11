/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.internal.strings;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeBase64.shouldBeBase64;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.tests.core.internal.StringsBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Strings assertIsBase64")
class Strings_assertIsBase64_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertIsBase64(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_invalid_Base64_characters() {
    // GIVEN
    String actual = "inv@lid";
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertIsBase64(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldBeBase64(actual).create());
  }

  @Test
  void should_succeeds_if_actual_is_Base64_encoded_with_padding() {
    // GIVEN
    String actual = "QXNzZXJ0Sg==";
    // WHEN/THEN
    strings.assertIsBase64(someInfo(), actual);
  }

  @Test
  void should_succeeds_if_actual_is_Base64_encoded_without_padding() {
    // GIVEN
    String actual = "QXNzZXJ0Sg";
    // WHEN/THEN
    strings.assertIsBase64(someInfo(), actual);
  }

}
