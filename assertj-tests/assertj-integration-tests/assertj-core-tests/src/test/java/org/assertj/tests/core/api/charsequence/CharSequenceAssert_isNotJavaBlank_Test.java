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
package org.assertj.tests.core.api.charsequence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBeBlank.shouldNotBeBlank;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author Filip Hrisafov
 */
@SuppressWarnings("deprecation")
class CharSequenceAssert_isNotJavaBlank_Test {

  @ParameterizedTest
  @NullSource
  @ValueSource(strings = {
      "",
      "a",
      " bc ",
      "\u00A0", // non-breaking space
      "\u2007", // non-breaking space
      "\u202F"  // non-breaking space
  })
  void should_pass_if_actual_is_not_blank(String actual) {
    // WHEN/THEN
    assertThat(actual).isNotJavaBlank();
  }

  @ParameterizedTest
  @ValueSource(strings = {
      " ",
      "\t", // tab
      "\n", // line feed
      "\r", // carriage return
      " \n\r  "
  })
  void should_fail_if_actual_is_blank(String actual) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotJavaBlank());
    // THEN
    then(assertionError).hasMessage(shouldNotBeBlank(actual).create());
  }

}
