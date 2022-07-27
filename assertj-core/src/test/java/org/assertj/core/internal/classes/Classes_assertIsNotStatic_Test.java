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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ClassModifierShouldBe.shouldNotBeStatic;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.Test;

class Classes_assertIsNotStatic_Test extends ClassesBaseTest {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsNotStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_pass_if_actual_is_not_a_static_class() {
    // GIVEN
    Class<?> actual = Math.class;
    // WHEN/THEN
    classes.assertIsNotStatic(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_an_interface() {
    // GIVEN
    Class<?> actual = Classes_assertIsStatic_Test.NestedInterface.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsNotStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeStatic(actual).create());
  }

  @Test
  void should_fail_if_actual_is_not_a_static_class() {
    // GIVEN
    Class<?> actual = Classes_assertIsStatic_Test.StaticNestedClass.class;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertIsNotStatic(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldNotBeStatic(actual).create());
  }

}
