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
package org.assertj.core.internal.maps;

import static org.assertj.core.error.ShouldHaveSizeLessThanOrEqualTo.shouldHaveSizeLessThanOrEqualTo;
import static org.assertj.core.util.AssertionsUtil.assertThatAssertionErrorIsThrownBy;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.MapsBaseTest;
import org.junit.jupiter.api.Test;

class Maps_assertHasSizeLessThanOrEqualTo_Test extends MapsBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    assertThatAssertionErrorIsThrownBy(() -> maps.assertHasSizeLessThanOrEqualTo(INFO, null, 6)).withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_less_than_or_equal_to_boundary() {
    assertThatAssertionErrorIsThrownBy(() -> maps.assertHasSizeLessThanOrEqualTo(INFO, actual,
                                                                                 1)).withMessage(shouldHaveSizeLessThanOrEqualTo(actual,
                                                                                                                                 actual.size(),
                                                                                                                                 1).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_less_than_boundary() {
    maps.assertHasSizeLessThanOrEqualTo(INFO, actual, 4);
  }

  @Test
  void should_pass_if_size_of_actual_is_equal_to_boundary() {
    maps.assertHasSizeLessThanOrEqualTo(INFO, actual, actual.size());
  }
}
