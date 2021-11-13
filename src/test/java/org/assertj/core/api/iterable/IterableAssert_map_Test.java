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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.iterable;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.GroupAssertTestHelper.*;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Lists.newArrayList;

/**
 * Tests for <code>{@link AbstractIterableAssert#map(Function)}</code> and
 * <code>{@link AbstractIterableAssert#map(ThrowingExtractor)}</code>.
 *
 * @author Joel Costigliola
 * @author Mateusz Haligowski
 * @author Trang Nguyen
 *
 */
public class IterableAssert_map_Test {
  private Employee yoda;
  private Employee luke;
  private Iterable<Employee> jedis;

  @BeforeEach
  void setUp() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    jedis = newArrayList(yoda, luke);
  }

  @Test
  void should_throw_a_proper_assertion_error_if_any_element_is_null_when_mapping_using_a_function(){
    // GIVEN
    yoda.setName(null);
    // WHEN/THEN
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(()->assertThat(jedis).map(firstNameFunction))
      .withMessage(actualIsNull());
  }

  @Test
  void should_throw_a_proper_assertion_error_if_any_element_is_null_when_mapping_using_throwing_extractor(){
    // GIVEN
    yoda.setName(null);
    // WHEN/THEN
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(()->assertThat(jedis)
        .map(throwingFirstNameExtractor))
      .withMessage(actualIsNull());
  }

  @Test
  void should_throw_a_proper_assertion_error_if_any_mapped_values_is_null_from_given_iterable_by_using_functions(){
    // GIVEN
    yoda.setName(null);
    // WHEN/THEN
    assertThatExceptionOfType(AssertionError.class)
      .isThrownBy(()->assertThat(jedis).map(firstNameFunction,lastNameFunction))
      .withMessage(actualIsNull());
  }

}
