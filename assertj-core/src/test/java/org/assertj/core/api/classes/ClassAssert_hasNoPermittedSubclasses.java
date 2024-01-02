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
package org.assertj.core.api.classes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeSealed.shouldBeSealed;
import static org.assertj.core.error.ShouldHavePermittedSubclasses.shouldHavePermittedSubclasses;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class ClassAssert_hasNoPermittedSubclasses {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasNoPermittedSubclasses());
    // THEN
    then(error).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @MethodSource("parameters")
  void should_pass_if_actual_does_not_have_permitted_subclasses(Class<?> actual) {
    assertThat(actual).hasNoPermittedSubclasses();
  }

  private static Stream<Class<?>> parameters() {
    return Stream.of(Object.class,
                     String.class,
                     List.class,
                     Object[].class,
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
