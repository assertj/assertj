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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSizeGreaterThan.shouldHaveSizeGreaterThan;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertHasSizeGreaterThan_Test extends ObjectArraysBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertHasSizeGreaterThan(INFO, null, 6));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_size_of_actual_is_not_greater_than_boundary() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertHasSizeGreaterThan(INFO, actual, 6));
    // THEN
    then(error).hasMessage(shouldHaveSizeGreaterThan(actual, actual.length, 6).create());
  }

  @Test
  void should_pass_if_size_of_actual_is_greater_than_boundary() {
    arrays.assertHasSizeGreaterThan(INFO, actual, 1);
  }
}
