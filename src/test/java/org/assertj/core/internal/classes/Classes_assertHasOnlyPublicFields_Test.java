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

import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoPublicFields;
import static org.assertj.core.error.ShouldOnlyHaveFields.shouldOnlyHaveFields;
import static org.assertj.core.test.TestData.someInfo;
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
 * >{@link org.assertj.core.internal.Classes#assertHasOnlyPublicFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 *
 * @author Filip Hrisafov
 */
public class Classes_assertHasOnlyPublicFields_Test extends ClassesBaseTest {

  private static final LinkedHashSet<String> EMPTY_STRING_SET = Sets.<String> newLinkedHashSet();

  @Before
  public void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  public void should_pass_if_class_has_all_the_expected_public_fields() {
    classes.assertHasOnlyPublicFields(someInfo(), actual, "publicField", "publicField2");
  }

  @Test
  public void should_pass_if_class_has_all_the_expected_public_fields_whatever_the_order_is() {
    classes.assertHasOnlyPublicFields(someInfo(), actual, "publicField2", "publicField");
  }

  @Test
  public void should_pass_if_class_has_no_public_fields_and_none_are_expected() {
    classes.assertHasOnlyPublicFields(someInfo(), NoField.class);
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasOnlyPublicFields(someInfo(), actual);
  }

  @Test
  public void should_fail_if_some_public_fields_are_not_present_in_the_expected_fields() {
    thrown.expectAssertionError(shouldOnlyHaveFields(actual,
                                                     newLinkedHashSet("publicField"),
                                                     EMPTY_STRING_SET,
                                                     newLinkedHashSet("publicField2")));
    classes.assertHasOnlyPublicFields(someInfo(), actual, "publicField");
  }

  @Test
  public void should_fail_if_some_public_fields_are_missing() {
    String[] expected = new String[] { "missingField", "publicField", "publicField2" };
    thrown.expectAssertionError(shouldOnlyHaveFields(actual,
                                                     newLinkedHashSet(expected),
                                                     newLinkedHashSet("missingField"),
                                                     EMPTY_STRING_SET));
    classes.assertHasOnlyPublicFields(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_fields_are_protected_or_private() {
    String[] expected = new String[] { "publicField", "publicField2", "protectedField", "privateField" };
    thrown.expectAssertionError(shouldOnlyHaveFields(actual,
                                                     newLinkedHashSet(expected),
                                                     newLinkedHashSet("protectedField", "privateField"),
                                                     EMPTY_STRING_SET));
    classes.assertHasOnlyPublicFields(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_fields_are_not_found_and_not_expected() {
    String[] expected = new String[] { "publicField", "protectedField", "privateField" };
    thrown.expectAssertionError(shouldOnlyHaveFields(actual,
                                                     newLinkedHashSet(expected),
                                                     newLinkedHashSet("protectedField", "privateField"),
                                                     newLinkedHashSet("publicField2")));
    classes.assertHasOnlyPublicFields(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_no_public_fields_are_expected_and_class_has_some() {
    thrown.expectAssertionError(shouldHaveNoPublicFields(actual, newLinkedHashSet("publicField", "publicField2")));
    classes.assertHasOnlyPublicFields(someInfo(), actual);
  }

}
