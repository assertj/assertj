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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.classes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldNotBePrimitive.shouldNotBePrimitive;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.api.ClassAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for <code>{@link ClassAssert#isNotPrimitive()}</code>.
 *
 * @author Manuel Gutierrez
 */
class ClassAssert_isNotPrimitive_Test {

  @Test
  void should_pass_if_actual_is_an_object() {
    // GIVEN
    Class<?> actual = Object.class;
    // WHEN/THEN
    then(actual).isNotPrimitive();
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isNotPrimitive());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @ParameterizedTest
  @ValueSource(classes = { byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class,
      char.class })
  void should_fail_if_actual_is_a_primitive_type(Class<?> primitiveClass) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(primitiveClass).isNotPrimitive());
    // THEN
    then(assertionError).hasMessage(shouldNotBePrimitive(primitiveClass).create());
  }
}
