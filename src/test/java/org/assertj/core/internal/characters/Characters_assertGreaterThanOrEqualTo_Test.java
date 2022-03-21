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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.characters;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.error.ShouldBeGreaterOrEqual.shouldBeGreaterOrEqual;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.mockito.Mockito.verify;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Characters;
import org.assertj.core.internal.CharactersBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Characters#assertGreaterThanOrEqualTo(AssertionInfo, Character, char)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class Characters_assertGreaterThanOrEqualTo_Test extends CharactersBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> characters.assertGreaterThanOrEqualTo(someInfo(), null, 'a'))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other() {
    characters.assertGreaterThanOrEqualTo(someInfo(), 'b', 'a');
  }

  @Test
  void should_pass_if_actual_is_equal_to_other() {
    characters.assertGreaterThanOrEqualTo(someInfo(), 'b', 'b');
  }

  @Test
  void should_fail_if_actual_is_less_than_other() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> characters.assertGreaterThanOrEqualTo(info, 'a', 'b'));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeGreaterOrEqual('a', 'b'));
  }

  @Test
  void should_fail_if_actual_is_null_according_to_custom_comparison_strategy() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> charactersWithCaseInsensitiveComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(),
                                                                                                                                                null,
                                                                                                                                                'a'))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_greater_than_other_according_to_custom_comparison_strategy() {
    charactersWithCaseInsensitiveComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), 'B', 'a');
  }

  @Test
  void should_pass_if_actual_is_equal_to_other_according_to_custom_comparison_strategy() {
    charactersWithCaseInsensitiveComparisonStrategy.assertGreaterThanOrEqualTo(someInfo(), 'B', 'b');
  }

  @Test
  void should_fail_if_actual_is_less_than_other_according_to_custom_comparison_strategy() {
    AssertionInfo info = someInfo();

    Throwable error = catchThrowable(() -> charactersWithCaseInsensitiveComparisonStrategy.assertGreaterThanOrEqualTo(info, 'a',
                                                                                                                      'B'));

    assertThat(error).isInstanceOf(AssertionError.class);
    verify(failures).failure(info, shouldBeGreaterOrEqual('a', 'B', caseInsensitiveComparisonStrategy));
  }
}
