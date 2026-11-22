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
import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.assertj.tests.core.testkit.Maps.mapOf;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.lang.reflect.Modifier;
import java.util.Map;

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
  void should_fail_if_methods_are_protected_private_or_package_private() {
    // GIVEN
    String[] expected = array("publicMethod", "protectedMethod", "privateMethod", "packagePrivateMethod");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasPublicMethods(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(MethodsClass.class, false, newTreeSet(expected), Modifier.toString(PUBLIC),
                                                      mapOf(entry("packagePrivateMethod", "package-private"),
                                                            entry("privateMethod", "private"),
                                                            entry("protectedMethod", "protected"))).create());
  }

  @Test
  void should_fail_if_inherited_method_is_not_public() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(SubMethodsClass.class).hasPublicMethods("staticProtectedMethod"));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(SubMethodsClass.class, false, newTreeSet("staticProtectedMethod"),
                                                      Modifier.toString(PUBLIC),
                                                      mapOf(entry("staticProtectedMethod", "protected static"))).create());
  }

  @Test
  void should_report_both_missing_and_non_public_methods() {
    // GIVEN
    String[] expected = array("missingMethod", "privateMethod", "publicMethod");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasPublicMethods(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(MethodsClass.class, false, newTreeSet(expected), Modifier.toString(PUBLIC),
                                                      newTreeSet("missingMethod"),
                                                      mapOf(entry("privateMethod", "private"))).create());
  }

  @Test
  void should_pass_if_one_of_the_overloaded_methods_is_public() {
    assertThat(OverloadedMethodsClass.class).hasPublicMethods("overloaded");
  }

  @Test
  void should_pass_if_public_method_is_inherited() {
    assertThat(SubMethodsClass.class).hasPublicMethods("publicMethod");
  }

  @Test
  void should_fail_if_expected_public_methods_are_missing() {
    // GIVEN
    String[] expected = array("missingMethod", "publicMethod");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(MethodsClass.class).hasPublicMethods(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(MethodsClass.class, false, newTreeSet(expected), Modifier.toString(PUBLIC),
                                                      newTreeSet("missingMethod"), Map.of()).create());
  }

  @SuppressWarnings("unused")
  private static class MethodsClass {
    public void publicMethod() {}

    protected void protectedMethod() {}

    private void privateMethod() {}

    void packagePrivateMethod() {}

    protected static void staticProtectedMethod() {}
  }

  private static class SubMethodsClass extends MethodsClass {
  }

  @SuppressWarnings("unused")
  private static class OverloadedMethodsClass {
    private void overloaded(String s) {}

    public void overloaded() {}
  }

}
