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
package org.assertj.tests.core.internal.objectarrays;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeNullOrEmpty.shouldBeNullOrEmpty;
import static org.assertj.tests.core.testkit.ObjectArrays.emptyArray;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
class ObjectArrays_assertNullOrEmpty_Test extends ObjectArraysBaseTest {

  @Test
  void should_fail_if_array_is_not_null_and_is_not_empty() {
    // GIVEN
    Integer[] actual = new Integer[] { 5, 8 };
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertNullOrEmpty(INFO, actual));
    // THEN
    then(error).hasMessage(shouldBeNullOrEmpty(actual).create());
  }

  @Test
  void should_pass_if_array_is_null() {
    arrays.assertNullOrEmpty(INFO, null);
  }

  @Test
  void should_pass_if_array_is_empty() {
    arrays.assertNullOrEmpty(INFO, emptyArray());
  }
}
