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
import static org.assertj.core.error.ShouldHaveNoFields.shouldHaveNoPublicFields;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.ShouldOnlyHaveFields.shouldOnlyHaveFields;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * @author Filip Hrisafov
 */
class ClassAssert_hasOnlyPublicFields_Test {

  private static final Set<String> EMPTY_STRING_SET = Set.of();

  @Test
  void should_fail_if_actual_is_null() {
    // GIVEN
    Class<?> actual = null;
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).hasOnlyPublicFields());
    // THEN
    then(assertionError).hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_pass_if_class_has_all_the_expected_public_fields() {
    assertThat(WithFields.class).hasOnlyPublicFields("publicField", "publicField2");
  }

  @Test
  void should_pass_if_class_has_all_the_expected_public_fields_whatever_the_order_is() {
    assertThat(WithFields.class).hasOnlyPublicFields("publicField2", "publicField");
  }

  @Test
  void should_pass_if_class_has_no_public_fields_and_none_are_expected() {
    assertThat(NoField.class).hasOnlyPublicFields();
  }

  @Test
  void should_fail_if_some_public_fields_are_not_present_in_the_expected_fields() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasOnlyPublicFields("publicField"));
    // THEN
    then(assertionError).hasMessage(shouldOnlyHaveFields(WithFields.class, newLinkedHashSet("publicField"), EMPTY_STRING_SET,
                                                         newLinkedHashSet("publicField2")).create());
  }

  @Test
  void should_fail_if_some_public_fields_are_missing() {
    // GIVEN
    String[] expected = array("missingField", "publicField", "publicField2");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasOnlyPublicFields(expected));
    // THEN
    then(assertionError).hasMessage(shouldOnlyHaveFields(WithFields.class, newLinkedHashSet(expected),
                                                         newLinkedHashSet("missingField"), EMPTY_STRING_SET).create());
  }

  @Test
  void should_fail_if_fields_are_protected_or_private() {
    // GIVEN
    String[] expected = array("publicField", "publicField2", "protectedField", "privateField");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasOnlyPublicFields(expected));
    // THEN
    then(assertionError).hasMessage(shouldOnlyHaveFields(WithFields.class, newLinkedHashSet(expected),
                                                         newLinkedHashSet("protectedField", "privateField"),
                                                         EMPTY_STRING_SET).create());
  }

  @Test
  void should_fail_if_fields_are_not_found_and_not_expected() {
    // GIVEN
    String[] expected = array("publicField", "protectedField", "privateField");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasOnlyPublicFields(expected));
    // THEN
    then(assertionError).hasMessage(shouldOnlyHaveFields(WithFields.class, newLinkedHashSet(expected),
                                                         newLinkedHashSet("protectedField", "privateField"),
                                                         newLinkedHashSet("publicField2")).create());
  }

  @Test
  void should_fail_if_no_public_fields_are_expected_and_class_has_some() {
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(WithFields.class).hasOnlyPublicFields());
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
