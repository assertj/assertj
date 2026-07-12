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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.protobuf.api.Assertions.assertThat;
import static org.assertj.tests.protobuf.testkit.AssertionErrors.expectAssertionError;

import org.assertj.tests.protobuf.TestProtos.MapFieldMessage;
import org.assertj.tests.protobuf.TestProtos.NestedMessage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.protobuf.api.MessageAssert#isEqualTo(Object)} with map fields.
 */
class MessageAssert_isEqualTo_with_map_fields_Test {

  @Test
  void should_pass_when_maps_have_the_same_entries_whatever_their_order() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder()
                                            .putLabels("env", "prod")
                                            .putLabels("team", "backend")
                                            .putLabels("region", "us")
                                            .build();
    MapFieldMessage expected = MapFieldMessage.newBuilder()
                                              .putLabels("region", "us")
                                              .putLabels("env", "prod")
                                              .putLabels("team", "backend")
                                              .build();
    // WHEN/THEN
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_pass_when_maps_have_the_same_entries_whatever_their_order_without_ignoring_repeated_field_order() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder().putScores(1, 100).putScores(2, 200).build();
    MapFieldMessage expected = MapFieldMessage.newBuilder().putScores(2, 200).putScores(1, 100).build();
    // WHEN/THEN
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_fail_when_a_map_value_differs() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder().putLabels("env", "dev").build();
    MapFieldMessage expected = MapFieldMessage.newBuilder().putLabels("env", "prod").build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("Map field <labels[env]>: expected <prod> but was <dev>");
  }

  @Test
  void should_fail_when_an_expected_key_is_missing() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder().putLabels("env", "prod").build();
    MapFieldMessage expected = MapFieldMessage.newBuilder()
                                              .putLabels("env", "prod")
                                              .putLabels("team", "backend")
                                              .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("Map field <labels>: expected to contain key <team> but it was missing");
  }

  @Test
  void should_fail_when_actual_has_an_unexpected_key() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder()
                                            .putLabels("env", "prod")
                                            .putLabels("team", "backend")
                                            .build();
    MapFieldMessage expected = MapFieldMessage.newBuilder().putLabels("env", "prod").build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("Map field <labels>: did not expect key <team> but it was set to <backend>");
  }

  @Test
  void should_compare_message_values_recursively() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder()
                                            .putNestedByKey("first",
                                                            NestedMessage.newBuilder().setValue("a").setCount(1).build())
                                            .build();
    MapFieldMessage expected = MapFieldMessage.newBuilder()
                                              .putNestedByKey("first",
                                                              NestedMessage.newBuilder().setValue("a").setCount(2).build())
                                              .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("nested_by_key[first].count")
               .hasMessageContaining("expected <2> but was <1>");
  }

  @Test
  void should_pass_when_expected_declares_a_subset_of_keys_and_comparing_expected_fields_only() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder()
                                            .putLabels("env", "prod")
                                            .putLabels("team", "backend")
                                            .build();
    MapFieldMessage expected = MapFieldMessage.newBuilder().putLabels("env", "prod").build();
    // WHEN/THEN
    assertThat(actual).comparingExpectedFieldsOnly().isEqualTo(expected);
  }

  @Test
  void should_fail_when_an_expected_key_is_missing_and_comparing_expected_fields_only() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder().putLabels("team", "backend").build();
    MapFieldMessage expected = MapFieldMessage.newBuilder().putLabels("env", "prod").build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).comparingExpectedFieldsOnly()
                                                                        .isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("Map field <labels>: expected to contain key <env> but it was missing");
  }

  @Test
  void should_pass_when_ignoring_a_map_field() {
    // GIVEN
    MapFieldMessage actual = MapFieldMessage.newBuilder().putLabels("env", "prod").build();
    MapFieldMessage expected = MapFieldMessage.newBuilder().putLabels("env", "dev").build();
    // WHEN/THEN
    assertThat(actual).ignoringFields("labels").isEqualTo(expected);
  }
}
