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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.internal.classes;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ClassModifierShouldBe.shouldBePackagePrivate;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

@DisplayName("Classes assertIsPackagePrivate")
class Classes_assertIsPackagePrivate_Test extends ClassesBaseTest {

  @Test
  public void should_fail_if_actual_is_null() {
    //GIVEN
    Class clazz = null;
    //WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(clazz).isPackagePrivate());
    //THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_actual_is_a_package_private_class() {
    //GIVEN
    Class clazz = PackagePrivateClass.class;
    //THEN
    then(clazz).isPackagePrivate();
  }

  @Test
  public void should_fail_if_actual_is_not_a_package_private_class() {
    //GIVEN
    Class clazz = Object.class;
    //WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(clazz).isPackagePrivate());
    //THEN
    then(assertionError).hasMessage(shouldBePackagePrivate(Object.class).create());
  }
}

class PackagePrivateClass {

}
