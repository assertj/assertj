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
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.error.ShouldHaveOnlyElementsOfType.shouldHaveOnlyElementsOfType;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectArrayAssert#hasOnlyElementsOfType(Class)}.
 */
public class ObjectArrays_assertHasOnlyElementsOfType_Test extends ObjectArraysBaseTest {

  private static final Object[] arrayOfNumbers = { 6, 7.0, 8L };

  @Test
  public void should_pass_if_actual_has_only_elements_of_the_expected_type() {
    arrays.assertHasOnlyElementsOfType(someInfo(), arrayOfNumbers, Number.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    // GIVEN
    Object[] array = null;
    // GIVEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasOnlyElementsOfType(someInfo(), array, String.class));
    // THEN
    assertThat(error).hasMessage(actualIsNull());
  }

  @Test
  public void should_throw_exception_if_expected_type_is_null() {
    assertThatNullPointerException().isThrownBy(() -> arrays.assertHasOnlyElementsOfType(someInfo(), arrayOfNumbers, null));
  }

  @Test
  public void should_fail_if_one_element_in_actual_does_not_belong_to_the_expected_type() {
    // GIVEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasOnlyElementsOfType(someInfo(), arrayOfNumbers, Long.class));
    // THEN
    assertThat(error).hasMessage(shouldHaveOnlyElementsOfType(arrayOfNumbers, Long.class, Integer.class).create());
  }

  @Test
  public void should_throw_assertion_error_and_not_null_pointer_exception_on_null_elements() {
    // GIVEN
    Object[] array = array(null, "notNull");
    // GIVEN
    AssertionError error = expectAssertionError(() -> arrays.assertHasOnlyElementsOfType(someInfo(), array, String.class));
    // THEN
    assertThat(error).hasMessage(shouldHaveOnlyElementsOfType(array, String.class, null).create());
  }

}