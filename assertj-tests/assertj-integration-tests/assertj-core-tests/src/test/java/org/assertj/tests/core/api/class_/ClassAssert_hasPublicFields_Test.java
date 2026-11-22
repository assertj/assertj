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
import static org.assertj.core.error.ShouldHaveFields.shouldHaveFields;
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoPublicFields;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import org.junit.jupiter.api.Test;

/**
 * @author William Delanoue
 */
class ClassAssert_hasPublicFields_Test {

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasPublicFields());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_class_has_expected_public_fields() {
    assertThat(WithFields.class).hasPublicFields("publicField")
                                .hasPublicFields("publicField", "publicField2");
  }

  @Test
  void should_pass_if_class_has_no_public_fields_and_none_are_expected() {
    assertThat(NoField.class).hasPublicFields();
  }

  @Test
  void should_fail_if_expected_fields_are_protected_or_private() {
    // GIVEN
    String[] expected = array("publicField", "protectedField", "privateField");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasPublicFields(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveFields(WithFields.class, newLinkedHashSet(expected),
                                                     newLinkedHashSet("protectedField", "privateField")).create());
  }

  @Test
  void should_fail_if_actual_does_not_have_all_expected_fields() {
    // GIVEN
    String[] expected = array("missingField", "publicField");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasPublicFields(expected));
    // THEN
    then(assertionError).hasMessage(shouldHaveFields(WithFields.class, newLinkedHashSet(expected),
                                                     newLinkedHashSet("missingField")).create());
  }

  @Test
  void should_fail_if_no_public_fields_are_expected_and_class_has_some() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasPublicFields());
    // THEN
    then(assertionError).hasMessage(shouldHaveNoPublicFields(WithFields.class,
                                                             newLinkedHashSet("publicField", "publicField2")).create());
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
