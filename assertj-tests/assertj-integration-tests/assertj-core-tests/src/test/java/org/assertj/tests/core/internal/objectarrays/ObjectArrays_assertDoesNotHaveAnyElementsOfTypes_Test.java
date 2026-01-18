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
import static org.assertj.core.error.ShouldNotHaveAnyElementsOfTypes.shouldNotHaveAnyElementsOfTypes;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ObjectArrays_assertDoesNotHaveAnyElementsOfTypes_Test extends ObjectArraysBaseTest {

  private static final Object[] array = { 6, 7.0, 8L };

  @Test
  void should_pass_if_actual_does_not_have_any_elements_of_the_unexpected_types() {
    arrays.assertDoesNotHaveAnyElementsOfTypes(INFO, array, array(Float.class, BigDecimal.class));
  }

  @Test
  void should_fail_if_actual_is_null() {
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotHaveAnyElementsOfTypes(INFO, null, Integer.class));
    // THEN
    then(error).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_one_element_is_one_of_the_unexpected_types() {
    // GIVEN
    Map<Class<?>, List<Object>> nonMatchingElementsByType = new LinkedHashMap<>();
    nonMatchingElementsByType.put(Long.class, newArrayList(8L));
    Class<?>[] unexpectedTypes = { Long.class };
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotHaveAnyElementsOfTypes(INFO, array, Long.class));
    // THEN
    then(error).hasMessage(shouldNotHaveAnyElementsOfTypes(array, unexpectedTypes, nonMatchingElementsByType).create());
  }

  @Test
  void should_fail_if_one_element_type_is_a_subclass_one_of_the_unexpected_types() {
    // GIVEN
    Map<Class<?>, List<Object>> nonMatchingElementsByType = new LinkedHashMap<>();
    nonMatchingElementsByType.put(Number.class, newArrayList(6, 7.0, 8L));
    Class<?>[] unexpectedTypes = { Number.class };
    // WHEN
    var error = expectAssertionError(() -> arrays.assertDoesNotHaveAnyElementsOfTypes(INFO, array, Number.class));
    // THEN
    then(error).hasMessage(shouldNotHaveAnyElementsOfTypes(array, unexpectedTypes, nonMatchingElementsByType).create());
  }
}
