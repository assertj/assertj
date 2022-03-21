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
package org.assertj.core.internal.objectarrays;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldNotHaveAnyElementsOfTypes.shouldNotHaveAnyElementsOfTypes;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.internal.ObjectArraysBaseTest;
import org.junit.jupiter.api.Test;

class ObjectArrays_assertDoesNotHaveAnyElementsOfTypes_Test extends ObjectArraysBaseTest {

  private static final Object[] array = { 6, 7.0, 8L };

  @Test
  void should_pass_if_actual_does_not_have_any_elements_of_the_unexpected_types() {
    arrays.assertDoesNotHaveAnyElementsOfTypes(someInfo(), array, array(Float.class, BigDecimal.class));
  }

  @Test
  void should_fail_if_actual_is_null() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> arrays.assertDoesNotHaveAnyElementsOfTypes(someInfo(), null,
                                                                                                                Integer.class))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_one_element_is_one_of_the_unexpected_types() {
    // GIVEN
    Map<Class<?>, List<Object>> nonMatchingElementsByType = new LinkedHashMap<>();
    nonMatchingElementsByType.put(Long.class, newArrayList(8L));
    Class<?>[] unexpectedTypes = { Long.class };

    // THEN
    String message = shouldNotHaveAnyElementsOfTypes(array, unexpectedTypes, nonMatchingElementsByType).create();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      // WHEN;
      arrays.assertDoesNotHaveAnyElementsOfTypes(someInfo(), array, Long.class);
    }).withMessage(message);
  }

  @Test
  void should_fail_if_one_element_type_is_a_subclass_one_of_the_unexpected_types() {
    // GIVEN
    Map<Class<?>, List<Object>> nonMatchingElementsByType = new LinkedHashMap<>();
    nonMatchingElementsByType.put(Number.class, newArrayList(6, 7.0, 8L));
    Class<?>[] unexpectedTypes = { Number.class };

    // THEN
    String message = shouldNotHaveAnyElementsOfTypes(array, unexpectedTypes, nonMatchingElementsByType).create();
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> {
      // WHEN;
      arrays.assertDoesNotHaveAnyElementsOfTypes(someInfo(), array, Number.class);
    }).withMessage(message);
  }
}
