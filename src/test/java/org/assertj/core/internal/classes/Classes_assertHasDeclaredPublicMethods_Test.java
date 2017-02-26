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
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newTreeSet;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.Map;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertHasDeclaredPublicMethods(AssertionInfo, Class, String...)} </code>
 */
public class Classes_assertHasDeclaredPublicMethods_Test extends ClassesBaseTest {

  @Before
  public void setupActual() {
    actual = MethodsClass.class;
  }

  @Test
  public void should_pass_if_actual_has_the_expected_declared_public_methods() {
    classes.assertHasDeclaredPublicMethods(someInfo(), actual, "publicMethod");
  }

  @Test
  public void should_pass_if_actual_has_no_declared_public_methods_and_no_expected_methods_are_given() {
    actual = Jedi.class;
    classes.assertHasDeclaredPublicMethods(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasDeclaredPublicMethods(someInfo(), actual);
  }

  @Test
  public void should_fail_if_actual_has_some_declared_public_methods_and_no_expected_methods_are_given() {
    thrown.expectAssertionError(shouldNotHaveMethods(actual, Modifier.toString(Modifier.PUBLIC), true,
                                                     newTreeSet("publicMethod")));
    classes.assertHasDeclaredPublicMethods(someInfo(), actual);
  }

  @Test()
  public void should_fail_if_the_only_methods_are_protected_or_private() {
    String[] expected = new String[] { "publicMethod", "protectedMethod", "privateMethod" };
    Map<String, String> nonMatching = new LinkedHashMap<>();
    nonMatching.put("protectedMethod", "protected");
    nonMatching.put("privateMethod", "private");

    thrown.expectAssertionError(shouldHaveMethods(actual, true,
                                                  newTreeSet(expected),
                                                  Modifier.toString(Modifier.PUBLIC),
                                                  nonMatching));

    classes.assertHasDeclaredPublicMethods(someInfo(), actual, expected);
  }

  @Test()
  public void should_fail_if_actual_does_not_have_the_expected_declared_public_methods() {
    String[] expected = new String[] { "missingMethod", "publicMethod" };
    thrown.expectAssertionError(shouldHaveMethods(actual, true,
                                                  newTreeSet(expected),
                                                  newTreeSet("missingMethod")));
    classes.assertHasDeclaredPublicMethods(someInfo(), actual, expected);
  }
}
