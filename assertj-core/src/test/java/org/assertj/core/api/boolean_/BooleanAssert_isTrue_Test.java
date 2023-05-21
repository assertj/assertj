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
package org.assertj.core.api.boolean_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.description.EmptyTextDescription.emptyDescription;
import static org.assertj.core.error.ShouldBeTrue.shouldBeTrue;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class BooleanAssert_isTrue_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Boolean actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> then(actual).isTrue());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_primitive_boolean_is_true() {
    // GIVEN
    boolean actual = true;
    // WHEN/THEN
    then(actual).isTrue();
  }

  @Test
  void should_pass_if_Boolean_is_true() {
    // GIVEN
    Boolean actual = true;
    // WHEN/THEN
    then(actual).isTrue();
  }

  @Test
  void should_fail_if_primitive_boolean_is_false() {
    // GIVEN
    boolean actual = false;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isTrue());
    // THEN
    then(assertionError).hasMessage(shouldBeTrue(actual).create(emptyDescription(), STANDARD_REPRESENTATION));
  }

  @Test
  void should_fail_if_Boolean_is_false() {
    // GIVEN
    Boolean actual = false;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).isTrue());
    // THEN
    then(assertionError).hasMessage(shouldBeTrue(actual).create(emptyDescription(), STANDARD_REPRESENTATION));
  }

}
