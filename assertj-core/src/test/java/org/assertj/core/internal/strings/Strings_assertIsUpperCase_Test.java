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

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldBeUpperCase.shouldBeUpperCase;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * @author Marcel Overdijk
 */
class Strings_assertIsUpperCase_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_uppercase() {
    strings.assertUpperCase(someInfo(), "LEGO");
  }

  @Test
  void should_pass_if_actual_is_empty() {
    strings.assertUpperCase(someInfo(), "");
  }

  @Test
  void should_fail_if_actual_is_not_only_uppercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertUpperCase(someInfo(), "Lego"))
                                                   .withMessage(shouldBeUpperCase("Lego").create());
  }

  @Test
  void should_fail_if_actual_is_lowercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertUpperCase(someInfo(), "lego"))
                                                   .withMessage(shouldBeUpperCase("lego").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertUpperCase(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

}
