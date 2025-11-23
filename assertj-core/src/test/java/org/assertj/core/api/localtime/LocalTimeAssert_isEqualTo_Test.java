/*
 * Copyright 2012-2025 the original author or authors.
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
package org.assertj.core.api.localtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.core.api.LocalTimeAssert#isEqualTo(String)} with invalid time strings.
 */
class LocalTimeAssert_isEqualTo_Test {

  @Test
  void should_fail_with_assertion_error_when_comparing_to_invalid_string() {
    // GIVEN
    LocalTime now = LocalTime.now();
    String invalidString = "not a LocalTime";

    // WHEN/THEN
    // FIX: With the fallback mechanism (issue #4021), parsing failures now result in AssertionError
    // because the invalid string is compared as a plain Object (not a LocalTime)
    assertThatExceptionOfType(AssertionError.class)
                                                   .isThrownBy(() -> assertThat(now).isEqualTo(invalidString))
                                                   .withMessageContaining(now.toString())
                                                   .withMessageContaining("not a LocalTime");
  }
}
