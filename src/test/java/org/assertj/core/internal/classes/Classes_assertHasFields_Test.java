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
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.junit.Before;
import org.junit.Test;

import org.assertj.core.internal.ClassesBaseTest;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertHasFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 * 
 * @author William Delanoue
 */
public class Classes_assertHasFields_Test extends ClassesBaseTest {

  @Before
  public void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasFields(someInfo(), actual);
  }

  @Test
  public void should_pass_if_no_fields_are_expected() {
    classes.assertHasFields(someInfo(), actual);
  }

  @Test
  public void should_pass_if_fields_are_public() {
    classes.assertHasFields(someInfo(), actual, "publicField");
  }

  @Test()
  public void should_fail_if_fields_are_protected_or_private() {
    String[] expected = new String[] { "publicField", "protectedField", "privateField" };
    thrown.expectAssertionError(shouldHaveFields(actual,
                                                 newLinkedHashSet(expected),
                                                 newLinkedHashSet("protectedField", "privateField")));
    classes.assertHasFields(someInfo(), actual, expected);
  }

  @Test()
  public void should_fail_if_fields_are_missing() {
    String[] expected = new String[] { "missingField", "publicField" };
    thrown.expectAssertionError(shouldHaveFields(actual,
                                                 newLinkedHashSet(expected),
                                                 newLinkedHashSet("missingField")));
    classes.assertHasFields(someInfo(), actual, expected);
  }
}
