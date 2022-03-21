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
import static org.assertj.core.error.ShouldHaveFields.shouldHaveDeclaredFields;
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoDeclaredFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertHasDeclaredFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 * 
 * @author William Delanoue
 */
class Classes_assertHasDeclaredFields_Test extends ClassesBaseTest {

  @BeforeEach
  void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  void should_pass_if_class_has_expected_declared_fields() {
    classes.assertHasDeclaredFields(someInfo(), actual, "publicField", "protectedField", "privateField");
  }

  @Test
  void should_pass_if_class_has_no_declared_fields_and_none_are_expected() {
    classes.assertHasDeclaredFields(someInfo(), NoField.class);
  }

  @Test
  void should_fail_if_actual_is_null() {
    actual = null;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasDeclaredFields(someInfo(), actual))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_fail_if_fields_are_missing() {
    String[] expected = new String[] { "missingField", "publicField" };
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasDeclaredFields(someInfo(), actual,
                                                                                                     expected))
                                                   .withMessage(format(shouldHaveDeclaredFields(actual,
                                                                                                newLinkedHashSet(expected),
                                                                                                newLinkedHashSet("missingField")).create()));
  }

  @Test
  void should_fail_if_no_declared_fields_are_expected_and_class_has_some() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasDeclaredFields(someInfo(),
                                                                                                     actual))
                                                   .withMessage(format(shouldHaveNoDeclaredFields(actual,
                                                                                                  newLinkedHashSet("publicField",
                                                                                                                   "publicField2",
                                                                                                                   "protectedField",
                                                                                                                   "privateField")).create()));
  }

}
