/*
 * Created on Jul 20,2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2011 the original author or authors.
 */
package org.assertj.core.internal.classes;

import static org.assertj.core.error.ShouldHaveFields.shouldHaveDeclaredFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.ClassesBaseTest;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertHasDeclaredFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 * 
 * @author William Delanoue
 */
public class Classes_assertHasDeclaredFields_Test extends ClassesBaseTest {

  @Before
  public void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  public void should_fail_if_actual_is_null() {
    actual = null;
    thrown.expectAssertionError(actualIsNull());
    classes.assertHasDeclaredFields(someInfo(), actual);
  }

  @Test
  public void should_pass_if_no_fields_are_expected() {
    classes.assertHasDeclaredFields(someInfo(), actual);
  }

  @Test
  public void should_pass_if_fields_are_public_protected_or_private() {
    classes.assertHasDeclaredFields(someInfo(), actual, "publicField", "protectedField", "privateField");
  }

  @Test
  public void should_fail_if_fields_are_missing() {
    AssertionInfo info = someInfo();
    String[] expected = new String[] { "missingField", "publicField" };
    try {
      classes.assertHasDeclaredFields(someInfo(), actual, expected);
    } catch (AssertionError e) {
      verify(failures).failure(info, shouldHaveDeclaredFields(actual,
                                                              newLinkedHashSet(expected),
                                                              newLinkedHashSet("missingField")));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }
}
