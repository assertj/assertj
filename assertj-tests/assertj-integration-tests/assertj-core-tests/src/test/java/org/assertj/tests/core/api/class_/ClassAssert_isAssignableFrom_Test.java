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
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAssignableFrom.shouldBeAssignableFrom;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author William Delanoue
 */
class ClassAssert_isAssignableFrom_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAssignableFrom(Jedi.class));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_given_classes_are_empty() {
    // WHEN
    var exception = catchIllegalArgumentException(() -> assertThat(Jedi.class).isAssignableFrom());
    // THEN
    then(exception).hasMessage("Expecting at least one Class to be specified");
  }

  @Test
  void should_fail_if_one_of_the_given_classes_is_null() {
    // WHEN
    var exception = catchNullPointerException(() -> assertThat(Jedi.class).isAssignableFrom(HumanJedi.class, null));
    // THEN
    then(exception).hasMessage("The class to compare actual with should not be null");
  }

  @Test
  void should_fail_if_actual_is_not_assignable_from_all_given_classes() {
    // GIVEN
    Class<?> actual = HumanJedi.class;
    Class<?>[] others = { HumanJedi.class, Jedi.class };
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).isAssignableFrom(others));
    // THEN
    then(assertionError).hasMessage(shouldBeAssignableFrom(actual, newLinkedHashSet(others),
                                                           newLinkedHashSet(Jedi.class)).create());
  }

  @Test
  void should_pass_if_actual_is_assignable_from_given_classes() {
    assertThat(Jedi.class).isAssignableFrom(Jedi.class, HumanJedi.class);
  }

  @Test
  void should_pass_if_actual_is_a_primitive_assignable_from_itself() {
    assertThat(int.class).isAssignableFrom(int.class);
  }

  private static class Jedi {
  }

  private static class HumanJedi extends Jedi {
  }

}
