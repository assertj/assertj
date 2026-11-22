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
import static org.junit.jupiter.api.condition.JRE.JAVA_18;

import java.util.SortedSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.JRE;

class ClassAssert_hasMethods_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasMethods());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_actual_has_expected_methods_whatever_their_visibility() {
    assertThat(SubMethodsClass.class).hasMethods("publicMethod", "protectedMethod", "privateMethod");
  }

  @Test
  void should_pass_if_methods_are_inherited() {
    assertThat(SubMethodsClass.class).hasMethods("notify", "notifyAll");
  }

  @Test
  void should_pass_with_directly_inherited_default_method() {
    assertThat(ClassWithDirectlyInheritedDefaultMethod.class).hasMethods("method");
  }

  @Test
  void should_pass_with_indirectly_inherited_default_method() {
    assertThat(ClassWithIndirectlyInheritedDefaultMethod.class).hasMethods("method");
  }

  @Test
  void should_fail_if_no_methods_are_expected_and_methods_are_available() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(SubMethodsClass.class).hasMethods());
    // THEN
    then(assertionError).hasMessage(shouldNotHaveMethods(SubMethodsClass.class, false, actualMethods()).create());
  }

  @Test
  void should_fail_if_expected_methods_are_missing() {
    // GIVEN
    String[] expected = array("missingMethod", "publicMethod");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(SubMethodsClass.class).hasMethods(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveMethods(SubMethodsClass.class, false, newTreeSet(expected),
                                                      newTreeSet("missingMethod")).create());
  }

  private static SortedSet<String> actualMethods() {
    SortedSet<String> actualMethods = newTreeSet("publicMethod", "protectedMethod", "privateMethod", "finalize", "wait",
                                                 "equals", "toString", "hashCode", "getClass", "clone", "notify", "notifyAll");
    if (JRE.currentVersion().compareTo(JAVA_18) > 0) actualMethods.add("wait0");
    return actualMethods;
  }

  @SuppressWarnings("unused")
  private static class MethodsClass {
    public void publicMethod() {}

    protected void protectedMethod() {}

    private void privateMethod() {}
  }

  private static class SubMethodsClass extends MethodsClass {
  }

  private interface InterfaceWithDefaultMethod {
    @SuppressWarnings("unused")
    default void method() {}
  }

  private interface InterfaceInheritingDefaultMethod extends InterfaceWithDefaultMethod {
  }

  private static class ClassWithDirectlyInheritedDefaultMethod implements InterfaceWithDefaultMethod {
  }

  private static class ClassWithIndirectlyInheritedDefaultMethod implements InterfaceInheritingDefaultMethod {
  }

}
