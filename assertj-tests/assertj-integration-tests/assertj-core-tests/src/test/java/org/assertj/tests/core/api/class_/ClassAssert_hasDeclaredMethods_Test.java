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
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

class ClassAssert_hasDeclaredMethods_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDeclaredMethods());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_has_the_expected_declared_methods() {
    assertThat(MethodsClass.class).hasDeclaredMethods("publicMethod", "protectedMethod", "privateMethod");
  }

  @Test
  void should_pass_if_actual_has_no_declared_methods_and_no_expected_methods_are_given() {
    assertThat(NoMethod.class).hasDeclaredMethods();
  }

  @Test
  void should_fail_if_actual_has_some_declared_methods_and_no_expected_methods_are_given() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasDeclaredMethods());
    // THEN
    then(assertionError).hasMessage(shouldNotHaveMethods(MethodsClass.class, true,
                                                         newTreeSet("publicMethod", "privateMethod", "protectedMethod"))
                                                                                                                        .create());
  }

  @Test
  void should_fail_if_actual_does_not_have_the_expected_declared_methods() {
    // GIVEN
    String[] expected = array("missingMethod", "publicMethod");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasDeclaredMethods(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(MethodsClass.class, true, newTreeSet(expected),
                                                      newTreeSet("missingMethod")).create());
  }

  @SuppressWarnings("unused")
  private static class MethodsClass {
    public void publicMethod() {}

    protected void protectedMethod() {}

    private void privateMethod() {}
  }

  private static class NoMethod {
  }

}
