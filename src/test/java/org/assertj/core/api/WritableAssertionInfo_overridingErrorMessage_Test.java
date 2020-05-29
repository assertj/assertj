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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenIllegalStateException;

import org.junit.jupiter.api.Test;

public class WritableAssertionInfo_overridingErrorMessage_Test {

  @Test
  public void should_return_the_overriding_error_message_set_with_a_supplier() {
    // GIVEN
    String message = "my first message";
    WritableAssertionInfo info = new WritableAssertionInfo();
    // WHEN
    info.overridingErrorMessage(() -> message);
    // THEN
    then(info.overridingErrorMessage()).isEqualTo(message);
  }

  @Test
  public void should_return_the_overriding_error_message_set_with_a_string() {
    // GIVEN
    String message = "my first message";
    WritableAssertionInfo info = new WritableAssertionInfo();
    // WHEN
    info.overridingErrorMessage(message);
    // THEN
    then(info.overridingErrorMessage()).isEqualTo(message);
  }

  @Test
  public void should_not_allow_overriding_error_message_with_a_supplier_and_then_a_string() {
    // GIVEN
    WritableAssertionInfo info = new WritableAssertionInfo();
    info.overridingErrorMessage(() -> "my first message");
    // WHEN/THEN
    thenIllegalStateException().isThrownBy(() -> info.overridingErrorMessage("my second message"))
                               .withMessage("An error message has already been set with overridingErrorMessage(Supplier<String> supplier)");
  }

  @Test
  public void should_not_allow_overriding_error_message_with_a_string_and_then_a_supplier() {
    // GIVEN
    WritableAssertionInfo info = new WritableAssertionInfo();
    info.overridingErrorMessage("my first message");
    // WHEN/THEN
    thenIllegalStateException().isThrownBy(() -> info.overridingErrorMessage(() -> "my second message"))
                               .withMessage("An error message has already been set with overridingErrorMessage(String newErrorMessage)");
  }

}
