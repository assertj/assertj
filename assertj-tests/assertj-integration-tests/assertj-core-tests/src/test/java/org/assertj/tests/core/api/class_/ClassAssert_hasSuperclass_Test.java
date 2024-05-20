/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.class_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveSuperclass.shouldHaveSuperclass;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ClassAssert_hasSuperclass_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasSuperclass(Object.class));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_null_class_is_given() {
    // GIVEN
    Class<?> actual = Integer.class;
    Class<?> superclass = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasSuperclass(superclass));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(shouldNotBeNull("superclass").create());
  }

  @Test
  void should_pass_if_actual_has_given_class_as_direct_superclass() {
    // GIVEN
    Class<?> actual = Integer.class;
    Class<?> superclass = Number.class;
    // WHEN/THEN
    assertThat(actual).hasSuperclass(superclass);
  }

  @ParameterizedTest
  @ValueSource(classes = { Object.class, Comparable.class, String.class })
  void should_fail_if_actual_has_not_given_class_as_direct_superclass(Class<?> superclass) {
    // GIVEN
    Class<?> actual = Integer.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasSuperclass(superclass));
    // THEN
    then(assertionError).hasMessage(shouldHaveSuperclass(actual, superclass).create());
  }

  @Test
  void should_pass_if_actual_is_an_array_class_and_object_class_is_given() {
    // GIVEN
    Class<?> actual = Integer[].class;
    // WHEN/THEN
    assertThat(actual).hasSuperclass(Object.class);
  }

}
