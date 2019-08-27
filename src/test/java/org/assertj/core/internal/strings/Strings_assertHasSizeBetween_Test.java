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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.strings;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveSizeBetween.shouldHaveSizeBetween;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.Strings;
import org.assertj.core.internal.StringsBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Strings#assertHasSizeGreaterThan(AssertionInfo, CharSequence, int)}</code>.
 *
 * @author Geoffrey Arthaud
 */
public class Strings_assertHasSizeBetween_Test extends StringsBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHasSizeBetween(someInfo(), null, 2, 7))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_size_of_actual_is_less_to_min_expected_size() {
    AssertionInfo info = someInfo();
    String actual = "Han";

    String errorMessage = shouldHaveSizeBetween(actual, actual.length(), 4, 7).create();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHasSizeBetween(info, actual, 4, 7))
                                                   .withMessage(errorMessage);
  }

  @Test
  public void should_fail_if_size_of_actual_is_greater_than_max_expected_size() {
    AssertionInfo info = someInfo();
    String actual = "Han";

    String errorMessage = shouldHaveSizeBetween(actual, actual.length(), 1, 2).create();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> strings.assertHasSizeBetween(info, actual, 1, 2))
                                                   .withMessage(errorMessage);
  }

  @Test
  public void should_pass_if_size_of_actual_is_between_sizes() {
    strings.assertHasSizeBetween(someInfo(), "Han", 2, 7);
  }
}