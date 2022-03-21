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
import static org.assertj.core.error.ShouldHaveFields.shouldHaveFields;
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoPublicFields;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.FailureMessages.actualIsNull;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import org.assertj.core.internal.ClassesBaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for
 * <code>{@link org.assertj.core.internal.Classes#assertHasPublicFields(org.assertj.core.api.AssertionInfo, Class, String...)}</code>
 * .
 * 
 * @author William Delanoue
 */
class Classes_assertHasPublicFields_Test extends ClassesBaseTest {

  @BeforeEach
  void setupActual() {
    actual = AnnotatedClass.class;
  }

  @Test
  void should_fail_if_actual_is_null() {
    actual = null;
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasPublicFields(someInfo(), actual))
                                                   .withMessage(actualIsNull());
  }

  @Test
  void should_pass_if_class_has_expected_public_fields() {
    classes.assertHasPublicFields(someInfo(), actual, "publicField");
    classes.assertHasPublicFields(someInfo(), actual, "publicField", "publicField2");
  }

  @Test
  void should_pass_if_class_has_no_public_fields_and_none_are_expected() {
    classes.assertHasPublicFields(someInfo(), NoField.class);
  }

  @Test
  void should_fail_if_expected_fields_are_protected_or_private() {
    String[] expected = array("publicField", "protectedField", "privateField");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasPublicFields(someInfo(), actual,
                                                                                                   expected))
                                                   .withMessage(format(shouldHaveFields(actual,
                                                                                        newLinkedHashSet(expected),
                                                                                        newLinkedHashSet("protectedField",
                                                                                                         "privateField")).create()));
  }

  @Test
  void should_fail_if_actual_does_not_have_all_expected_fields() {
    String[] expected = array("missingField", "publicField");
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasPublicFields(someInfo(), actual,
                                                                                                   expected))
                                                   .withMessage(format(shouldHaveFields(actual,
                                                                                        newLinkedHashSet(expected),
                                                                                        newLinkedHashSet("missingField")).create()));
  }

  @Test
  void should_fail_if_no_public_fields_are_expected_and_class_has_some() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> classes.assertHasPublicFields(someInfo(), actual))
                                                   .withMessage(shouldHaveNoPublicFields(actual,
                                                                                         newLinkedHashSet("publicField",
                                                                                                          "publicField2")).create());
  }

}
