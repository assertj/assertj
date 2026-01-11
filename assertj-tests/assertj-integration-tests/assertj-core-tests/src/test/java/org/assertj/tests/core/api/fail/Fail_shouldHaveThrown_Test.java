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
package org.assertj.tests.core.api.fail;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.Fail.shouldHaveThrown;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class Fail_shouldHaveThrown_Test {

  @Test
  void should_include_message_built_with_given_exception_name() {
    // WHEN
    var assertionError = expectAssertionError(() -> shouldHaveThrown(NullPointerException.class));
    // THEN
    then(assertionError).hasMessage("NullPointerException should have been thrown");
  }

  @Test
  void should_include_message_built_with_given_throwable_name() {
    // WHEN
    var assertionError = expectAssertionError(() -> shouldHaveThrown(OutOfMemoryError.class));
    // THEN
    then(assertionError).hasMessage("OutOfMemoryError should have been thrown");
  }
}
