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
package org.assertj.tests.core.api.class_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveEnclosingClass.shouldHaveEnclosingClass;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ClassAssert_hasEnclosingClass_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasEnclosingClass(Object.class));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_null_class_is_given() {
    // GIVEN
    Class<?> actual = Nested.class;
    Class<?> enclosingClass = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> assertThat(actual).hasEnclosingClass(enclosingClass));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class).hasMessage(shouldNotBeNull("enclosingClass").create());
  }

  @ParameterizedTest
  @MethodSource("enclosedClasses")
  void should_pass_if_actual_has_given_class_as_direct_enclosing_class(Class<?> actual) {
    // WHEN/THEN
    assertThat(actual).hasEnclosingClass(ClassAssert_hasEnclosingClass_Test.class);
  }

  private static Stream<Class<?>> enclosedClasses() {
    return Stream.of(Nested.class,
                     Inner.class,
                     localClass(),
                     anonymousClass());
  }

  @ParameterizedTest
  @MethodSource("noEnclosingClassTypes")
  void should_fail_if_actual_has_no_enclosing_class(Class<?> actual) {
    // GIVEN
    Class<?> enclosingClass = Object.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasEnclosingClass(enclosingClass));
    // THEN
    then(assertionError).hasMessage(shouldHaveEnclosingClass(actual, enclosingClass).create());
  }

  private static Stream<Class<?>> noEnclosingClassTypes() {
    return Stream.of(ClassAssert_hasEnclosingClass_Test.class, // any top-level class
                     Nested[].class, // array classes have no enclosing class
                     Integer.TYPE,
                     Void.TYPE);
  }

  @Test
  void should_fail_if_actual_has_not_given_class_as_enclosing_class() {
    // GIVEN
    Class<?> actual = Nested.class;
    Class<?> enclosingClass = Object.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasEnclosingClass(enclosingClass));
    // THEN
    then(assertionError).hasMessage(shouldHaveEnclosingClass(actual, enclosingClass).create());
  }

  @Test
  void should_fail_if_actual_has_given_class_as_indirect_enclosing_class() {
    // GIVEN
    Class<?> actual = Nested.DeeplyNested.class;
    Class<?> enclosingClass = ClassAssert_hasEnclosingClass_Test.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasEnclosingClass(enclosingClass));
    // THEN
    then(assertionError).hasMessage(shouldHaveEnclosingClass(actual, enclosingClass).create());
  }

  private static Class<?> localClass() {
    class Local {
    }
    return Local.class;
  }

  private static Class<?> anonymousClass() {
    return new Object() {}.getClass();
  }

  private static class Nested {

    private static class DeeplyNested {
    }
  }

  private class Inner {
  }

}
