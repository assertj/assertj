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

import static org.assertj.core.error.ShouldOnlyHaveFields.shouldOnlyHaveDeclaredFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.LinkedHashSet;

import org.assertj.core.internal.ClassesBaseTest;
import org.assertj.core.util.Sets;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code
 * >{@link org.assertj.core.internal.Classes#assertHasOnlyDeclaredFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 *
 * @author Filip Hrisafov
 */
public class Classes_assertHasOnlyDeclaredFields_Test extends ClassesBaseTest {

  private static final LinkedHashSet<String> EMPTY_STRING_SET = Sets.<String> newLinkedHashSet();

  @Before
  public void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasOnlyDeclaredFields(someInfo(), actual);
  }

  @Test
  public void should_fail_if_no_fields_are_expected() {
    thrown.expectAssertionError(shouldOnlyHaveDeclaredFields(actual,
                                                                EMPTY_STRING_SET,
                                                                EMPTY_STRING_SET,
                                                                newLinkedHashSet("publicField", "public2Field",
                                                                                 "protectedField", "privateField")));
    classes.assertHasOnlyDeclaredFields(someInfo(), actual);
  }

  @Test
  public void should_fail_if_not_all_fields_are_expected() {
    thrown.expectAssertionError(shouldOnlyHaveDeclaredFields(actual,
                                                                newLinkedHashSet("publicField", "protectedField",
                                                                                 "privateField"),
                                                                EMPTY_STRING_SET,
                                                                newLinkedHashSet("public2Field")));
    classes.assertHasOnlyDeclaredFields(someInfo(), actual, "publicField", "protectedField", "privateField");
  }

  @Test
  public void should_fail_if_fields_are_missing() {
    String[] expected = array("missingField", "publicField", "public2Field", "protectedField", "privateField");
    thrown.expectAssertionError(shouldOnlyHaveDeclaredFields(actual,
                                                                newLinkedHashSet(expected),
                                                                newLinkedHashSet("missingField"),
                                                                EMPTY_STRING_SET));
    classes.assertHasOnlyDeclaredFields(someInfo(), actual, expected);
  }

  @Test
  public void should_pass_if_all_fields_are_expected() {
    classes.assertHasOnlyDeclaredFields(someInfo(), actual, "publicField", "public2Field", "protectedField",
                                           "privateField");
  }

  @Test
  public void should_pass_if_public_all_fields_are_expected_whatever_the_order_is() {
    classes.assertHasOnlyDeclaredFields(someInfo(), actual, "protectedField", "privateField", "public2Field",
                                           "publicField");
  }

  @Test()
  public void should_fail_if_fields_are_not_expected_and_not_found() {
    String[] expected = array("publicField", "public2Field", "missing", "privateField");
    thrown.expectAssertionError(shouldOnlyHaveDeclaredFields(actual,
                                                                newLinkedHashSet(expected),
                                                                newLinkedHashSet("missing"),
                                                                newLinkedHashSet("protectedField")));
    classes.assertHasOnlyDeclaredFields(someInfo(), actual, expected);
  }
}
