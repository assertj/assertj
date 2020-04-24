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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.guava.internal.rangesets;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.guava.error.ShouldHaveSize.shouldHaveSize;

import org.assertj.core.error.ErrorMessageFactory;
import org.assertj.guava.internal.RangeSetsBaseTest;
import org.junit.jupiter.api.Test;

class RangeSets_assertHasSize_Test extends RangeSetsBaseTest {

  @Test
  void should_pass_if_expected_size_is_correct() {
    rangeSets.assertHasSize(someInfo(), actual, actual.asRanges().size());
  }

  @Test
  void should_fail_if_expected_size_is_incorrect() {
    ErrorMessageFactory expectedMessage = shouldHaveSize(actual, actual.asRanges().size(), 5);

    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertHasSize(someInfo(), actual, 5))
                                                   .withMessage(expectedMessage.create());
  }

  @Test
  void should_throw_error_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> rangeSets.assertHasSize(someInfo(), null, 3))
                                                   .withMessage(actualIsNull());
  }
}
