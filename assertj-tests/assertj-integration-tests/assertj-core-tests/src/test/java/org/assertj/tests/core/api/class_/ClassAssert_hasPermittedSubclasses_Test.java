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
import static org.assertj.core.error.ShouldHavePermittedSubclasses.shouldHavePermittedSubclasses;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.List;
import org.junit.jupiter.api.Test;

class ClassAssert_hasPermittedSubclasses_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasPermittedSubclasses());
    // THEN
    then(error).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_null_is_in_given_classes() {
    // WHEN
    Throwable throwable = catchThrowable(() -> assertThat(SealedClass.class).hasPermittedSubclasses(null, String.class));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("The class to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_does_not_have_permitted_subclasses() {
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(Object.class).hasPermittedSubclasses(String.class));
    // THEN
    then(error).hasMessage(shouldHavePermittedSubclasses(Object.class, array(String.class), List.of(String.class)).create());
  }

  @Test
  void should_fail_if_one_of_the_given_classes_is_not_permitted() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(SealedClass.class).hasPermittedSubclasses(NonSealedClass.class,
                                                                                                                    String.class));
    // THEN
    then(assertionError).hasMessage(shouldHavePermittedSubclasses(SealedClass.class,
                                                                  array(NonSealedClass.class, String.class),
                                                                  List.of(String.class)).create());
  }

  @Test
  void should_pass_if_actual_has_permitted_subclass() {
    // WHEN/THEN
    assertThat(SealedClass.class).hasPermittedSubclasses(NonSealedClass.class)
                                 .hasPermittedSubclasses(FinalClass.class)
                                 .hasPermittedSubclasses(NonSealedClass.class, FinalClass.class);
  }

  sealed class SealedClass permits NonSealedClass, FinalClass {
  }

  non-sealed class NonSealedClass extends SealedClass {
  }

  final class FinalClass extends SealedClass {
  }

}
