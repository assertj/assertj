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

import static java.lang.reflect.Modifier.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class ClassAssert_hasPublicMethods_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasPublicMethods());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_has_the_expected_public_methods() {
    assertThat(MethodsClass.class).hasPublicMethods("publicMethod", "toString");
  }

  @Test
  void should_fail_if_no_methods_are_expected_but_public_methods_are_available() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasPublicMethods());
    // THEN
    then(assertionError).hasMessage(shouldNotHaveMethods(MethodsClass.class, Modifier.toString(PUBLIC), false,
                                                         newTreeSet("publicMethod", "wait", "equals", "toString", "hashCode",
                                                                    "getClass", "notify", "notifyAll")).create());
  }

  @Test
  void should_fail_if_methods_are_protected_or_private() {
    // GIVEN
    String[] expected = array("publicMethod", "protectedMethod", "privateMethod");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasPublicMethods(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(MethodsClass.class, false, newTreeSet(expected),
                                                      newTreeSet("protectedMethod", "privateMethod")).create());
  }

  @Test
  void should_fail_if_expected_public_methods_are_missing() {
    // GIVEN
    String[] expected = array("missingMethod", "publicMethod");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasPublicMethods(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(MethodsClass.class, false, newTreeSet(expected),
                                                      newTreeSet("missingMethod")).create());
  }

  @SuppressWarnings("unused")
  private static class MethodsClass {
    public void publicMethod() {}

    protected void protectedMethod() {}

    private void privateMethod() {}
  }

}
