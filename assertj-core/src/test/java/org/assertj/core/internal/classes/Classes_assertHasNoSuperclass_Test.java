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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveNoSuperclass.shouldHaveNoSuperclass;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.stream.Stream;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Classes assertHasNoSuperclass")
class Classes_assertHasNoSuperclass_Test extends ClassesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasNoSuperclass(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_a_superclass() {
    // GIVEN
    Class<?> actual = Integer.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasNoSuperclass(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldHaveNoSuperclass(actual).create());
  }

  @ParameterizedTest
  @MethodSource("nullSuperclassTypes")
  void should_pass_if_actual_has_no_superclass(Class<?> actual) {
    // WHEN/THEN
    classes.assertHasNoSuperclass(someInfo(), actual);
  }

  private static Stream<Class<?>> nullSuperclassTypes() {
    return Stream.of(Object.class,
                     Cloneable.class, // any interface
                     Boolean.TYPE,
                     Byte.TYPE,
                     Character.TYPE,
                     Double.TYPE,
                     Float.TYPE,
                     Integer.TYPE,
                     Long.TYPE,
                     Short.TYPE,
                     Void.TYPE);
  }

}
