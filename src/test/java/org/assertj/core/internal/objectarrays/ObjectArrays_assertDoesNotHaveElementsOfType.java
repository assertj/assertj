/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.objectarrays;

import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.Test;

import static org.assertj.core.error.ShouldNotHaveElementsOfType.shouldNotHaveElementsOfType;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;

public class ObjectArrays_assertDoesNotHaveElementsOfType extends ObjectArraysBaseTest {

  private static final Object[] array = { 6, 7.0, 8L };

  @Test
  public void should_pass_if_actual_does_not_have_elements_of_the_not_expected_type() {
    arrays.assertDoesNotHaveElementsOfType(someInfo(), array, Float.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    thrown.expectAssertionError(actualIsNull());
    arrays.assertDoesNotHaveElementsOfType(someInfo(), null, Integer.class);
  }

  @Test
  public void should_throw_exception_if_expected_type_is_null() {
    thrown.expectNullPointerException();
    arrays.assertDoesNotHaveElementsOfType(someInfo(), array, null);
  }

  @Test
  public void should_fail_if_one_element_in_actual_does_belong_to_the_not_expected_type() {
    thrown.expectAssertionError(shouldNotHaveElementsOfType(array, Long.class, Long.class).create());
    arrays.assertDoesNotHaveElementsOfType(someInfo(), array, Long.class);
  }
}
