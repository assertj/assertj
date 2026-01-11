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
import static org.assertj.core.api.Fail.fail;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;

import org.assertj.core.api.Fail;
import org.junit.jupiter.api.Test;

class Fail_fail_Test {

  @Test
  void should_throw_AssertionError_with_message() {
    // WHEN
    var assertionError = expectAssertionError(() -> fail("Failed :("));
    // THEN
    then(assertionError).hasMessage("Failed :(");
  }

  @Test
  void should_throw_AssertionError_with_message_from_lambda() {
    // WHEN
    var assertionError = expectAssertionError(() -> Optional.empty().orElseGet(() -> fail("Failed :(")));
    // THEN
    then(assertionError).hasMessage("Failed :(");
  }

  @Test
  void should_throw_AssertionError_with_message_formatted_with_parameters() {
    // WHEN
    var assertionError = expectAssertionError(() -> fail("Failed %s", ":("));
    // THEN
    then(assertionError).hasMessage("Failed :(");
  }

  @Test
  void should_throw_AssertionError_with_cause() {
    // GIVEN
    Throwable cause = new Throwable();
    // WHEN
    var assertionError = expectAssertionError(() -> fail("Failed :(", cause));
    // THEN
    then(assertionError).hasMessage("Failed :(")
                        .hasCause(cause);
  }

  @Test
  void should_throw_AssertionError_without_message() {
    // WHEN
    var assertionError = expectAssertionError(Fail::fail);
    // THEN
    then(assertionError).hasMessage("");
  }

  @Test
  void should_throw_AssertionError_without_message_but_with_root_cause() {
    // GIVEN
    String causeMessage = "boom!";
    // WHEN
    var assertionError = expectAssertionError(() -> fail(new Exception(causeMessage)));
    // THEN
    then(assertionError).cause().hasMessage(causeMessage);
  }
}
