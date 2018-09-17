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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.shortarrays;

import org.assertj.core.internal.ShortArraysBaseTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveSizeLessThan.shouldHaveSizeLessThan;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class ShortArrays_assertHasSizeLessThan_Test extends ShortArraysBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSizeLessThan(someInfo(), null, 6))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_fail_if_size_of_actual_is_not_less_than_boundary() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSizeLessThan(someInfo(), actual, 1))
                                                   .withMessage(shouldHaveSizeLessThan(actual, actual.length, 1).create());
  }

  @Test
  public void should_pass_if_size_of_actual_is_less_than_boundary() {
    arrays.assertHasSizeLessThan(someInfo(), actual, 4);
  }
}
