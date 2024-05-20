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
package org.assertj.core.internal.classes;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.testkit.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.junit.jupiter.api.condition.JRE.JAVA_18;

import java.util.SortedSet;
import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.JRE;

class Classes_assertHasMethods_Test extends ClassesBaseTest {

  @BeforeEach
  void setupActual() {
    AnotherMethodsClass m = new AnotherMethodsClass();
    Strings.isNullOrEmpty(m.string); // causes a synthetic method in AnotherMethodsClass
    actual = AnotherMethodsClass.class;
  }

  private static class AnotherMethodsClass extends MethodsClass {
    private String string;
  }

  @Test
  void should_pass_if_actual_has_expected_accessible_public_methods() {
    classes.assertHasMethods(someInfo(), actual, "publicMethod", "protectedMethod", "privateMethod");
  }

  @Test
  void should_fail_if_no_methods_are_expected_and_methods_are_available() {
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> classes.assertHasMethods(someInfo(), actual));
    // THEN
    then(assertionError).hasMessage(shouldNotHaveMethods(actual, false, actualMethods()).create());
  }

  private static SortedSet<String> actualMethods() {
    SortedSet<String> actualMethods = newTreeSet("publicMethod",
                                                 "protectedMethod",
                                                 "privateMethod",
                                                 "finalize",
                                                 "wait",
                                                 "equals",
                                                 "toString",
                                                 "hashCode",
                                                 "getClass",
                                                 "clone",
                                                 "notify",
                                                 "notifyAll");

    if (JRE.currentVersion().compareTo(JAVA_18) > 0) actualMethods.add("wait0");

    return actualMethods;
  }

  @Test
  void should_fail_if_actual_is_null() {
    actual = null;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasMethods(someInfo(), actual))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_methods_are_inherited() {
    String[] expected = array("notify", "notifyAll");
    classes.assertHasMethods(someInfo(), actual, expected);
  }

  @Test
  void should_fail_if_expected_methods_are_missing() {
    String[] expected = array("missingMethod", "publicMethod");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasMethods(someInfo(), actual,
                                                                                              expected))
                                                   .withMessage(format(shouldHaveMethods(actual, false,
                                                                                         newTreeSet(expected),
                                                                                         newTreeSet("missingMethod")).create()));
  }

  @Test
  void should_pass_with_directly_inherited_default_method() {
    // GIVEN
    Class<?> actual = ClassWithDirectlyInheritedDefaultMethod.class;
    String[] expected = { "method" };
    // WHEN/THEN
    classes.assertHasMethods(someInfo(), actual, expected);
  }

  private static class ClassWithDirectlyInheritedDefaultMethod implements InterfaceWithDefaultMethod {
  }

  private interface InterfaceWithDefaultMethod {

    @SuppressWarnings("unused")
    default void method() {}

  }

  @Test
  void should_pass_with_indirectly_inherited_default_method() {
    // GIVEN
    Class<?> actual = ClassWithIndirectlyInheritedDefaultMethod.class;
    String[] expected = { "method" };
    // WHEN/THEN
    classes.assertHasMethods(someInfo(), actual, expected);
  }

  private static class ClassWithIndirectlyInheritedDefaultMethod implements InterfaceInheritingDefaultMethod {
  }

  private interface InterfaceInheritingDefaultMethod extends InterfaceWithDefaultMethod {
  }

}
