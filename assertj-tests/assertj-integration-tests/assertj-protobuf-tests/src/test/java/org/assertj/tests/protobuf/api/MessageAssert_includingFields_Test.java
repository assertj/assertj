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
package org.assertj.tests.protobuf.api;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.protobuf.api.Assertions.assertThat;
import static org.assertj.tests.protobuf.testkit.AssertionErrors.expectAssertionError;

import org.assertj.tests.protobuf.TestProtos.MapFieldMessage;
import org.assertj.tests.protobuf.TestProtos.NestedMessage;
import org.assertj.tests.protobuf.TestProtos.TestMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class MessageAssert_includingFields_Test {

  @Test
  void should_fail_when_included_boolean_differs_from_default() {
    // GIVEN
    var actual = TestMessage.newBuilder().setPaid(true).setBar(3).build();
    var expected = TestMessage.getDefaultInstance();
    // WHEN/THEN
    assertThat(actual).comparingExpectedFieldsOnly().isEqualTo(expected);
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly()
                                                             .includingFields("paid").isEqualTo(expected));
    then(error).hasMessageContaining("Field <paid>: expected <false> but was <true>")
               .hasMessageNotContaining("Field <bar>");
  }

  @Test
  void should_pass_when_included_default_matches_and_other_fields_differ() {
    // GIVEN
    var actual = TestMessage.newBuilder().setBar(3).build();
    // WHEN/THEN
    assertThat(actual).includingFields("paid").comparingExpectedFieldsOnly().isEqualTo(TestMessage.getDefaultInstance());
  }

  @Test
  void should_fail_when_included_numeric_and_string_defaults_differ() {
    // GIVEN
    var actual = TestMessage.newBuilder().setBar(3).setFoo("other").build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly()
                                                             .includingFields("bar").includingFields("foo", "bar")
                                                             .isEqualTo(TestMessage.getDefaultInstance()));
    // THEN
    then(error).hasMessageContaining("Field <bar>: expected <0> but was <3>")
               .hasMessageContaining("Field <foo>: expected <> but was <other>");
  }

  @Test
  void should_fail_when_an_expected_field_outside_included_fields_differs() {
    // GIVEN
    var actual = TestMessage.newBuilder().setFoo("actual").build();
    var expected = TestMessage.newBuilder().setFoo("expected").build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly()
                                                             .includingFields("paid").isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("Field <foo>");
  }

  @Test
  void should_keep_full_comparison_unless_partial_comparison_is_requested() {
    // GIVEN
    var actual = TestMessage.newBuilder().setBar(3).build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).includingFields("paid")
                                                             .isEqualTo(TestMessage.getDefaultInstance()));
    // THEN
    then(error).hasMessageContaining("Field <bar>");
  }

  @Test
  void should_fail_when_nested_included_default_differs() {
    // GIVEN
    var actual = TestMessage.newBuilder().setNested(NestedMessage.newBuilder().setCount(3)).build();
    var expected = TestMessage.newBuilder().setNested(NestedMessage.getDefaultInstance()).build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly()
                                                             .includingFields("nested.count").isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("Field <nested.count>: expected <0> but was <3>");
  }

  @Test
  void should_compare_parent_presence_for_nested_paths() {
    // GIVEN
    var actual = TestMessage.newBuilder().setNested(NestedMessage.getDefaultInstance()).build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly()
                                                             .includingFields("nested.count")
                                                             .isEqualTo(TestMessage.getDefaultInstance()));
    // THEN
    then(error).hasMessageContaining("Field <nested>: expected <null>");
  }

  @Test
  void should_pass_when_both_parent_messages_are_absent() {
    // GIVEN
    var actual = TestMessage.newBuilder().setBar(3).build();
    // WHEN/THEN
    assertThat(actual).comparingExpectedFieldsOnly().includingFields("nested.count")
                      .isEqualTo(TestMessage.getDefaultInstance());
  }

  @Test
  void should_keep_message_children_partial_when_only_parent_is_included() {
    // GIVEN
    var actual = TestMessage.newBuilder().setNested(NestedMessage.newBuilder().setCount(3)).build();
    var expected = TestMessage.newBuilder().setNested(NestedMessage.getDefaultInstance()).build();
    // WHEN/THEN
    assertThat(actual).comparingExpectedFieldsOnly().includingFields("nested").isEqualTo(expected);
  }

  @Test
  void should_give_ignored_fields_precedence_regardless_of_call_order() {
    // GIVEN
    var actual = TestMessage.newBuilder().setPaid(true).build();
    var expected = TestMessage.getDefaultInstance();
    // WHEN/THEN
    assertThat(actual).comparingExpectedFieldsOnly().includingFields("paid").ignoringFields("paid").isEqualTo(expected);
    assertThat(actual).comparingExpectedFieldsOnly().ignoringFields("paid").includingFields("paid").isEqualTo(expected);
  }

  @Test
  void should_ignore_included_descendants_when_parent_is_ignored() {
    // GIVEN
    var actual = TestMessage.newBuilder().setNested(NestedMessage.newBuilder().setCount(3)).build();
    // WHEN/THEN
    assertThat(actual).comparingExpectedFieldsOnly().includingFields("nested.count").ignoringFields("nested")
                      .isEqualTo(TestMessage.getDefaultInstance());
  }

  @Test
  void should_preserve_explicit_presence_for_default_values() {
    // GIVEN
    var actual = TestMessage.newBuilder().setOptionalPaid(false).build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly()
                                                             .includingFields("optional_paid")
                                                             .isEqualTo(TestMessage.getDefaultInstance()));
    // THEN
    then(error).hasMessageContaining("Field <optional_paid>: expected <null> but was <false>");
  }

  @Test
  void should_compare_empty_repeated_fields_when_included() {
    // GIVEN
    var actual = TestMessage.newBuilder().addNumbers(1).build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly().includingFields("numbers")
                                                             .isEqualTo(TestMessage.getDefaultInstance()));
    // THEN
    then(error).hasMessageContaining("Field <numbers>");
  }

  @Test
  void should_compare_empty_maps_when_included() {
    // GIVEN
    var actual = MapFieldMessage.newBuilder().putLabels("env", "prod").build();
    // WHEN
    var error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly().includingFields("labels")
                                                             .isEqualTo(MapFieldMessage.getDefaultInstance()));
    // THEN
    then(error).hasMessageContaining("Field <labels>");
  }

  @ParameterizedTest
  @ValueSource(strings = { "", "missing", "nested.missing", "nested.", ".paid", "nested..count", "foo.value", "numbers.value" })
  void should_reject_invalid_paths(String path) {
    // GIVEN
    var assertion = assertThat(TestMessage.getDefaultInstance());
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> assertion.includingFields(path)).withMessageContaining(path);
  }

  @Test
  void should_reject_paths_into_map_entries() {
    // GIVEN
    var assertion = assertThat(MapFieldMessage.getDefaultInstance());
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> assertion.includingFields("nested_by_key.count"));
  }

  @Test
  void should_reject_null_paths() {
    // GIVEN
    var assertion = assertThat(TestMessage.getDefaultInstance());
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertion.includingFields((String[]) null));
    assertThatNullPointerException().isThrownBy(() -> assertion.includingFields((String) null));
  }

  @Test
  void should_reject_null_actual() {
    // WHEN/THEN
    expectAssertionError(() -> assertThat((TestMessage) null).includingFields("paid"));
  }

  @Test
  void should_not_add_fields_when_validation_fails() {
    // GIVEN
    var actual = TestMessage.newBuilder().setPaid(true).build();
    var assertion = assertThat(actual).comparingExpectedFieldsOnly();
    // WHEN
    assertThatIllegalArgumentException().isThrownBy(() -> assertion.includingFields("paid", "missing"));
    // THEN
    assertion.isEqualTo(TestMessage.getDefaultInstance());
  }

  @Test
  void should_return_same_assertion_and_allow_empty_paths() {
    // GIVEN
    var assertion = assertThat(TestMessage.getDefaultInstance());
    // WHEN/THEN
    then(assertion.includingFields()).isSameAs(assertion);
    then(assertion.includingFields("paid")).isSameAs(assertion);
  }
}
