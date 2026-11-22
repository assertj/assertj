/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.class_;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.ShouldHaveFields.shouldHaveDeclaredFields;
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoDeclaredFields;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author William Delanoue
 */
class ClassAssert_hasDeclaredFields_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasDeclaredFields());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_class_has_expected_declared_fields() {
    assertThat(WithFields.class).hasDeclaredFields("publicField", "protectedField", "privateField");
  }

  @Test
  void should_pass_if_class_has_no_declared_fields_and_none_are_expected() {
    assertThat(NoField.class).hasDeclaredFields();
  }

  @Test
  void should_fail_if_fields_are_missing() {
    // GIVEN
    String[] expected = array("missingField", "publicField");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasDeclaredFields(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveDeclaredFields(WithFields.class, newLinkedHashSet(expected),
                                                             newLinkedHashSet("missingField")).create());
  }

  @Test
  void should_fail_if_no_declared_fields_are_expected_and_class_has_some() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasDeclaredFields());
    // THEN
    then(assertionError).hasMessage(shouldHaveNoDeclaredFields(WithFields.class,
                                                               newLinkedHashSet("publicField", "publicField2", "protectedField",
                                                                                "privateField")).create());
  }

  @SuppressWarnings("unused")
  private static class WithFields {
    public String publicField;
    public String publicField2;
    protected String protectedField;
    private String privateField;
  }

  private static class NoField {
  }

}
