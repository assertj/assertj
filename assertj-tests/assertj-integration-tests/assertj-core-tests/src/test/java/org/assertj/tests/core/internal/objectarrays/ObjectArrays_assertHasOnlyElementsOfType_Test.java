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

import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveOnlyElementsOfType.shouldHaveOnlyElementsOfType;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertHasOnlyElementsOfType_Test extends ObjectArraysBaseTest {

  private static final Object[] arrayOfNumbers = { 6, 7.0, 8L };

  @Test
  void should_pass_if_actual_has_only_elements_of_the_expected_type() {
    arrays.assertHasOnlyElementsOfType(INFO, arrayOfNumbers, Number.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasOnlyElementsOfType(INFO, null, String.class));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_exception_if_expected_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertHasOnlyElementsOfType(INFO, arrayOfNumbers, null));
  }

  @Test
  void should_fail_if_one_element_in_actual_does_not_belong_to_the_expected_type() {
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasOnlyElementsOfType(INFO, arrayOfNumbers, Long.class));
    // THEN
    then(error).hasMessage(shouldHaveOnlyElementsOfType(arrayOfNumbers, Long.class, Integer.class).create());
  }

  @Test
  void should_throw_assertion_error_and_not_null_pointer_exception_on_null_elements() {
    // GIVEN
    Object[] array = array(null, "notNull");
    // WHEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasOnlyElementsOfType(INFO, array, String.class));
    // THEN
    then(error).hasMessage(shouldHaveOnlyElementsOfType(array, String.class, null).create());
  }

}
