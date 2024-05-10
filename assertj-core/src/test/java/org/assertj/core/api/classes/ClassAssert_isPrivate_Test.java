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
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePrivate;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ClassAssert_isPrivate_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPrivate());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_actual_is_not_private() {
    // GIVEN
    Class<?> actual = String.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isPrivate());
    // THEN
    then(assertionError).hasMessage(shouldBePrivate(actual).create());
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_pass_if_actual_is_private(Class<?> actual) {
    // WHEN/THEN
    assertThat(actual).isPrivate();
  }

  static Stream<Class<?>> parameters() throws ClassNotFoundException {
    return Stream.of(PrivateClass.class,
                     Class.forName(ClassAssert_isPrivate_Test.class.getName() + "$PrivateClass"));
  }

  private static class PrivateClass {
  }

}
