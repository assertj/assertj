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
import static org.assertj.core.api.Assertions.catchNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldBeAssignableTo.shouldBeAssignableTo;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * @author Vikram Nithyanandam
 * @author Jessica Hamilton
 */
class ClassAssert_isAssignableTo_Test {

  @Test
  void should_fail_if_other_is_null() {
    // GIVEN
    Class<?> actual = ArrayList.class;
    Class<?> other = null;
    // WHEN
    NullPointerException exception = catchNullPointerException(() -> assertThat(actual).isAssignableTo(other));
    // THEN
    then(exception).hasMessage(shouldNotBeNull("other").create());
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    Class<?> other = List.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAssignableTo(other));
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @ParameterizedTest
  @CsvSource({
      "java.util.List, java.util.ArrayList",
      "int, java.lang.Object"
  })
  void should_fail_if_actual_is_not_assignable_to_other(Class<?> actual, Class<?> other) {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isAssignableTo(other));
    // THEN
    then(assertionError).hasMessage(shouldBeAssignableTo(actual, other).create());
  }

  @ParameterizedTest
  @CsvSource({
      "java.util.ArrayList, java.util.List",
      "int, int"
  })
  void should_pass_if_actual_is_assignable_to_other(Class<?> actual, Class<?> other) {
    // WHEN/THEN
    assertThat(actual).isAssignableTo(other);
  }

}
