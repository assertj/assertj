/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.error.ShouldHaveMethods.shouldHaveMethods;
import static org.assertj.core.error.ShouldHaveMethods.shouldNotHaveMethods;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newTreeSet;

import java.lang.reflect.Modifier;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.Before;
import org.junit.Test;

public class Classes_assertHasPublicMethods_Test extends ClassesBaseTest {

  @Before
  public void setupActual() {
    actual = MethodsClass.class;
  }

  @Test
  public void should_pass_if_actual_has_the_expected_public_methods() {
    classes.assertHasPublicMethods(someInfo(), actual, "publicMethod");
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasPublicMethods(someInfo(), actual);
  }

  @Test
  public void should_fail_if_no_methods_are_expected_but_public_methods_are_available() {
    thrown.expectAssertionError(shouldNotHaveMethods(actual, Modifier.toString(Modifier.PUBLIC), false,
                                                     newTreeSet("publicMethod", "wait", "equals", "toString",
                                                                "hashCode", "getClass", "notify", "notifyAll")));
    classes.assertHasPublicMethods(someInfo(), actual);
  }

  @Test()
  public void should_fail_if_methods_are_protected_or_private() {
    String[] expected = array("publicMethod", "protectedMethod", "privateMethod");
    thrown.expectAssertionError(shouldHaveMethods(actual, false,
                                                  newTreeSet(expected),
                                                  newTreeSet("protectedMethod", "privateMethod")));
    classes.assertHasPublicMethods(someInfo(), actual, expected);
  }

  @Test()
  public void should_fail_if_expected_public_methods_are_missing() {
    String[] expected = array("missingMethod", "publicMethod");
    thrown.expectAssertionError(shouldHaveMethods(actual, false,
                                                  newTreeSet(expected),
                                                  newTreeSet("missingMethod")));
    classes.assertHasPublicMethods(someInfo(), actual, expected);
  }
}
