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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.api.charsequence;

import org.assertj.core.api.CharSequenceAssert;
import org.assertj.core.api.CharSequenceAssertBaseTest;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldNotBeEqualNormalizingWhitespace.shouldNotBeEqualNormalizingWhitespace;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.verify;

/**
 * Tests for <code>{@link org.assertj.core.api.CharSequenceAssert#isNotEqualToNormalizingWhitespace(CharSequence)}</code>.
 *
 * @author Dan Corder
 */
class CharSequenceAssert_isNotEqualToNormalizingWhitespace_Test extends CharSequenceAssertBaseTest {

  @Override
  protected CharSequenceAssert invoke_api_method() {
    return assertions.isNotEqualToNormalizingWhitespace(" my foo bar ");
  }

  @Override
  protected void verify_internal_effects() {
    verify(strings).assertNotEqualsNormalizingWhitespace(getInfo(assertions), getActual(assertions), " my foo bar ");
  }

  @ParameterizedTest
  @MethodSource("notEqualToNormalizingWhiteSpaceGenerator")
  void should_fail_if_actual_is_equal_normalizing_breaking_spaces(String actual, String expected) {
    assertThatExceptionOfType(AssertionError.class)
                                                   .isThrownBy(() -> assertThat(actual).isNotEqualToNormalizingWhitespace(expected))
                                                   .withMessage(shouldNotBeEqualNormalizingWhitespace(actual,
                                                                                                      expected).create());
  }

  public static Stream<Arguments> notEqualToNormalizingWhiteSpaceGenerator() {
    return NON_BREAKING_SPACES.stream()
                              .map(nonBreakingSpace -> arguments("my" + nonBreakingSpace
                                                                 + "foo bar", "my foo bar"));
  }
}
