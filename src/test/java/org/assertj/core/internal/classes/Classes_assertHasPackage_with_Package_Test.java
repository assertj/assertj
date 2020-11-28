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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHavePackage.shouldHavePackage;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.Collection;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Classes assertHasPackage(Package)")
class Classes_assertHasPackage_with_Package_Test extends ClassesBaseTest {

  @Test
  void should_pass_if_actual_declares_given_package() {
    // GIVEN
    Class<?> actual = Object.class;
    Package aPackage = actual.getPackage();
    // WHEN/THEN
    classes.assertHasPackage(someInfo(), actual, aPackage);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    Package aPackage = Object.class.getPackage();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(), actual, aPackage));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_fail_if_aPackage_is_null() {
    // GIVEN
    Class<?> actual = Object.class;
    Package aPackage = null;
    // WHEN
    Throwable thrown = catchThrowable(() -> classes.assertHasPackage(someInfo(), actual, aPackage));
    // THEN
    then(thrown).isInstanceOf(NullPointerException.class)
                .hasMessage(shouldNotBeNull("aPackage").create());
  }

  @Test
  void should_fail_if_package_does_not_match() {
    // GIVEN
    Class<?> actual = Object.class;
    Package aPackage = Collection.class.getPackage();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasPackage(someInfo(), actual, aPackage));
    // THEN
    then(assertionError).hasMessage(shouldHavePackage(actual, aPackage).create());
  }

}
