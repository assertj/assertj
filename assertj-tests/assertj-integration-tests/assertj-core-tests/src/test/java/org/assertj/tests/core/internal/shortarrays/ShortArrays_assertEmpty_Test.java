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
package org.assertj.tests.core.internal.shortarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.testkit.ShortArrays.arrayOf;
import static org.assertj.tests.core.testkit.ShortArrays.emptyArray;
import static org.assertj.tests.core.testkit.TestData.someInfo;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ShortArrays;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link ShortArrays#assertEmpty(AssertionInfo, short[])}</code>.
 * 
 * @author Alex Ruiz
 */
class ShortArrays_assertEmpty_Test {

  private short[] actual = arrayOf(6, 8, 10);
  private final ShortArrays arrays = ShortArrays.instance();

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertEmpty(someInfo(), null))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_is_not_empty() {
    AssertionInfo info = someInfo();
    short[] actual = { 6, 8 };

    var error = expectAssertionError(() -> arrays.assertEmpty(info, actual));

    then(error).hasMessage(shouldBeEmpty(actual).create());
  }

  @Test
  void should_pass_if_actual_is_empty() {
    arrays.assertEmpty(someInfo(), emptyArray());
  }
}
