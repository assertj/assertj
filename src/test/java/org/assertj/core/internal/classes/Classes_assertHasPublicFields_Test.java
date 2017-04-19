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

import static org.assertj.core.error.ShouldHaveFields.shouldHaveFields;
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoPublicFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertHasPublicFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 * 
 * @author William Delanoue
 */
public class Classes_assertHasPublicFields_Test extends ClassesBaseTest {

  @Before
  public void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasPublicFields(someInfo(), actual);
  }

  @Test
  public void should_pass_if_class_has_expected_public_fields() {
    classes.assertHasPublicFields(someInfo(), actual, "publicField");
    classes.assertHasPublicFields(someInfo(), actual, "publicField", "publicField2");
  }

  @Test
  public void should_pass_if_class_has_no_public_fields_and_none_are_expected() {
    classes.assertHasPublicFields(someInfo(), NoField.class);
  }

  @Test
  public void should_fail_if_expected_fields_are_protected_or_private() {
    String[] expected = array("publicField", "protectedField", "privateField");
    thrown.expectAssertionError(shouldHaveFields(actual,
                                                 newLinkedHashSet(expected),
                                                 newLinkedHashSet("protectedField", "privateField")));
    classes.assertHasPublicFields(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_actual_does_not_have_all_expected_fields() {
    String[] expected = array("missingField", "publicField");
    thrown.expectAssertionError(shouldHaveFields(actual,
                                                 newLinkedHashSet(expected),
                                                 newLinkedHashSet("missingField")));
    classes.assertHasPublicFields(someInfo(), actual, expected);
  }

  @Test
  public void should_fail_if_no_public_fields_are_expected_and_class_has_some() {
    thrown.expectAssertionError(shouldHaveNoPublicFields(actual, newLinkedHashSet("publicField", "publicField2")));
    classes.assertHasPublicFields(someInfo(), actual);
  }

}
