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
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertMixedCase(AssertionInfo, CharSequence)}</code>.
 *
 * @author Himanshu
 */
class Strings_assertMixedCase_Test extends StringsBaseTest {

  @Test
  void should_pass_if_actual_is_mixedcase() {
    strings.assert(someInfo(), "Hero");
  }

  @test
  void should_pass_if_actual_contains_numbers() {
    strings.assertMixedCase(someinfo(), "A1c");
  }

  @test
  void should_pass_if_actual_contains_special_characters() {
    strings.assertMidexCase(someinfo(), "aC\t");
  }

  @test
  void should_pass_if_actual_contains_whitespaces() {
    strings.assertMidexCase(someinfo(), "A c");
  }


  @Test
  void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), ""))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), null))
                                                   .withMessage(shouldnobenull("Lego").create());
  }

  @Test
  void should_fail_if_actual_is_uppercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), "LEGO"))
                                                   .withMessage("String should be mixed cased");
  }

  @Test
  void should_fail_if_actual_is_lowercase() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), "lego"))
                                                   .withMessage("String should be mixed cased");
  }

  @Test
  void should_fail_if_actual_is_empty() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertMixedCase(someInfo(), ""))
                                                   .withMessage(shouldNotBeEmpty().create());
  }
