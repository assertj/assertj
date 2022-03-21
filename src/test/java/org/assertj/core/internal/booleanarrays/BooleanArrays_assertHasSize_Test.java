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
package org.assertj.core.internal.booleanarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveSize.shouldHaveSize;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.BooleanArrays;
import org.assertj.core.internal.BooleanArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link BooleanArrays#assertHasSize(AssertionInfo, boolean[], int)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class BooleanArrays_assertHasSize_Test extends BooleanArraysBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSize(someInfo(), null, 6))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_equal_to_expected_size() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertHasSize(someInfo(), actual, 6))
                                                   .withMessage(shouldHaveSize(actual, actual.length, 6).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_equal_to_expected_size() {
    arrays.assertHasSize(someInfo(), actual, 2);
  }
}
