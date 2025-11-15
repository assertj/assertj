/*
 * Copyright 2012-2025 the original author or authors.
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

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveAtLeastOneElementOfType.shouldHaveAtLeastOneElementOfType;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertHasAtLeastOneElementOfType_Test extends ObjectArraysBaseTest {

  private static final Object[] array = { 6, "Hello" };

  @Test
  void should_pass_if_actual_has_one_element_of_the_expected_type() {
    arrays.assertHasAtLeastOneElementOfType(INFO, array, Integer.class);
    arrays.assertHasAtLeastOneElementOfType(INFO, array, String.class);
    arrays.assertHasAtLeastOneElementOfType(INFO, array, Object.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasAtLeastOneElementOfType(INFO, null, Integer.class));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_exception_if_expected_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertHasAtLeastOneElementOfType(INFO, array, null));
  }

  @Test
  void should_fail_if_no_elements_in_actual_belongs_to_the_expected_type() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasAtLeastOneElementOfType(INFO, array, Float.class));
    // THEN
    then(error).hasMessage(shouldHaveAtLeastOneElementOfType(array, Float.class).create());
  }

}
