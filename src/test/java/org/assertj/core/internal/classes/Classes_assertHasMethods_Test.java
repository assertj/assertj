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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newTreeSet;

import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Classes_assertHasMethods_Test extends ClassesBaseTest {

  @BeforeEach
  public void setupActual() {
    AnotherMethodsClass m = new AnotherMethodsClass();
    Strings.isNullOrEmpty(m.string); // causes a synthetic method in AnotherMethodsClass
    actual = AnotherMethodsClass.class;
  }

  private static class AnotherMethodsClass extends MethodsClass {
    private String string;
  }

  @Test
  public void should_pass_if_actual_has_expected_accessible_public_methods() {
    classes.assertHasMethods(someInfo(), actual, "publicMethod", "protectedMethod", "privateMethod");
  }

  @Test
  public void should_fail_if_no_methods_are_expected_and_methods_are_available() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasMethods(someInfo(), actual))
                                                   .withMessage(format(shouldNotHaveMethods(actual, false,
                                                                                            newTreeSet("publicMethod",
                                                                                                       "protectedMethod",
                                                                                                       "privateMethod",
                                                                                                       "finalize",
                                                                                                       "wait", "equals",
                                                                                                       "toString",
                                                                                                       "hashCode",
                                                                                                       "getClass",
                                                                                                       "clone",
                                                                                                       "registerNatives",
                                                                                                       "notify",
                                                                                                       "notifyAll")).create()));
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasMethods(someInfo(), actual))
                                                   .withMessage(actualIsNull());
  }

  @Test
  public void should_pass_if_methods_are_inherited() {
    String[] expected = array("notify", "notifyAll");
    classes.assertHasMethods(someInfo(), actual, expected);
  }

  @Test()
  public void should_fail_if_expected_methods_are_missing() {
    String[] expected = array("missingMethod", "publicMethod");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasMethods(someInfo(), actual,
                                                                                              expected))
                                                   .withMessage(format(shouldHaveMethods(actual, false,
                                                                                         newTreeSet(expected),
                                                                                         newTreeSet("missingMethod")).create()));
  }
}
