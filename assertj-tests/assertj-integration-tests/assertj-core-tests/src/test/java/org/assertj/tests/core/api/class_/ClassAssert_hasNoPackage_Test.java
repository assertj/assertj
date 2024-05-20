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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveNoPackage.shouldHaveNoPackage;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class ClassAssert_hasNoPackage_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasNoPackage());
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_a_package() {
    // GIVEN
    Class<?> actual = Object.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).hasNoPackage());
    // THEN
    then(assertionError).hasMessage(shouldHaveNoPackage(actual).create());
  }

  @ParameterizedTest
  @MethodSource("nullPackageTypes")
  void should_pass_if_actual_has_no_package(Class<?> actual) {
    // WHEN/THEN
    assertThat(actual).hasNoPackage();
  }

  private static Stream<Class<?>> nullPackageTypes() {
    return Stream.of(int[].class, // any array
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
