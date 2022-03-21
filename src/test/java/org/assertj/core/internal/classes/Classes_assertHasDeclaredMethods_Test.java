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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newTreeSet;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertHasDeclaredMethods(AssertionInfo, Class, String...)}</code>
 */
class Classes_assertHasDeclaredMethods_Test extends ClassesBaseTest {

  @BeforeEach
  void setupActual() {
    AnotherMethodsClass m = new AnotherMethodsClass();
    Strings.isNullOrEmpty(m.string); // causes a synthetic method in AnotherMethodsClass
    actual = AnotherMethodsClass.class;
  }

  @SuppressWarnings("unused")
  private static final class AnotherMethodsClass {
    private String string;

    public void publicMethod() {}

    protected void protectedMethod() {}

    private void privateMethod() {}
  }

  @Test
  void should_pass_if_actual_has_the_expected_declared_methods() {
    classes.assertHasDeclaredMethods(someInfo(), actual, "publicMethod", "protectedMethod", "privateMethod");
  }

  @Test
  void should_pass_if_actual_has_no_declared_methods_and_no_expected_methods_are_given() {
    actual = Jedi.class;
    classes.assertHasDeclaredMethods(someInfo(), actual);
  }

  @Test
  void should_fail_if_actual_is_null() {
    actual = null;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasDeclaredMethods(someInfo(), actual))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_actual_has_some_declared_methods_and_no_expected_methods_are_given() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasDeclaredMethods(someInfo(),
                                                                                                      actual))
                                                   .withMessage(format(shouldNotHaveMethods(actual, true,
                                                                                            newTreeSet("publicMethod",
                                                                                                       "privateMethod",
                                                                                                       "protectedMethod")).create()));
  }

  @Test
  void should_fail_if_actual_does_not_have_the_expected_declared_methods() {
    String[] expected = new String[] { "missingMethod", "publicMethod" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasDeclaredMethods(someInfo(),
                                                                                                      actual, expected))
                                                   .withMessage(format(shouldHaveMethods(actual, true,
                                                                                         newTreeSet(expected),
                                                                                         newTreeSet("missingMethod")).create()));
  }
}
