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
package org.assertj.core.internal.characters;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeLowerCase.shouldBeLowerCase;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Characters;
import org.assertj.core.internal.CharactersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Characters#assertLowerCase(AssertionInfo, Character)}</code>.
 * 
 * @author Yvonne Wang
 * @author Joel Costigliola
 */
class Characters_assertLowerCase_Test extends CharactersBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> characters.assertLowerCase(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_lowercase() {
    characters.assertLowerCase(someInfo(), 'a');
  }

  @Test
  void should_fail_if_actual_is_not_lowercase() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> characters.assertLowerCase(info, 'A'));

    // THEN
    then(error).hasMessage(shouldBeLowerCase('A').create());
  }

  @Test
  void should_fail_if_actual_is_null_whatever_custom_comparison_strategy_is() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> charactersWithCaseInsensitiveComparisonStrategy.assertLowerCase(someInfo(),
                                                                                                                                     null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_lowercase_whatever_custom_comparison_strategy_is() {
    charactersWithCaseInsensitiveComparisonStrategy.assertLowerCase(someInfo(), 'a');
  }

  @Test
  void should_fail_if_actual_is_not_lowercase_whatever_custom_comparison_strategy_is() {
    // GIVEN
    AssertionInfo info = someInfo();
    // WHEN
    var error = expectAssertionError(() -> charactersWithCaseInsensitiveComparisonStrategy.assertLowerCase(info, 'A'));

    // THEN
    then(error).hasMessage(shouldBeLowerCase('A').create());
  }
}
