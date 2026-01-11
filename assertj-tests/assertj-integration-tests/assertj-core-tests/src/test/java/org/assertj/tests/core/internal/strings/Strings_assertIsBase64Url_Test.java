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
import static org.assertj.core.error.ShouldBeBase64Url.shouldBeBase64Url;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.tests.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

class Strings_assertIsBase64Url_Test extends StringsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    String actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertIsBase64Url(INFO, actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_invalid_Base64Url_characters() {
    // GIVEN
    String actual = "inv@lid";
    // WHEN
    var assertionError = expectAssertionError(() -> strings.assertIsBase64Url(INFO, actual));
    // THEN
    then(assertionError).hasMessage(shouldBeBase64Url(actual).create());
  }

  @Test
  void should_succeeds_if_actual_is_Base64Url_encoded() {
    strings.assertIsBase64Url(INFO, "QXNzZXJ0Sisr");
  }
}
