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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.protobuf.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.protobuf.testkit.AssertionErrors.expectAssertionError;
import static org.assertj.protobuf.api.Assertions.assertThat;

import org.assertj.tests.protobuf.TestProtos.RepeatedFieldMessage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.protobuf.api.MessageAssert#ignoringRepeatedFieldOrder()}.
 */
class MessageAssert_ignoringRepeatedFieldOrder_Test {

  @Test
  void should_pass_when_repeated_fields_match_in_different_order() {
    // GIVEN
    RepeatedFieldMessage actual = RepeatedFieldMessage.newBuilder()
                                                      .addItems("a")
                                                      .addItems("b")
                                                      .addItems("c")
                                                      .build();
    RepeatedFieldMessage expected = RepeatedFieldMessage.newBuilder()
                                                        .addItems("c")
                                                        .addItems("a")
                                                        .addItems("b")
                                                        .build();
    // WHEN/THEN
    assertThat(actual).ignoringRepeatedFieldOrder().isEqualTo(expected);
  }

  @Test
  void should_fail_when_repeated_fields_have_different_values() {
    // GIVEN
    RepeatedFieldMessage actual = RepeatedFieldMessage.newBuilder()
                                                      .addItems("a")
                                                      .addItems("b")
                                                      .build();
    RepeatedFieldMessage expected = RepeatedFieldMessage.newBuilder()
                                                        .addItems("a")
                                                        .addItems("c")
                                                        .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).ignoringRepeatedFieldOrder()
                                                                        .isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("items");
  }

  @Test
  void should_fail_when_repeated_fields_have_different_sizes() {
    // GIVEN
    RepeatedFieldMessage actual = RepeatedFieldMessage.newBuilder()
                                                      .addItems("a")
                                                      .addItems("b")
                                                      .build();
    RepeatedFieldMessage expected = RepeatedFieldMessage.newBuilder()
                                                        .addItems("a")
                                                        .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).ignoringRepeatedFieldOrder()
                                                                        .isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("size");
  }

  @Test
  void should_fail_without_ignoring_order() {
    // GIVEN
    RepeatedFieldMessage actual = RepeatedFieldMessage.newBuilder()
                                                      .addItems("a")
                                                      .addItems("b")
                                                      .build();
    RepeatedFieldMessage expected = RepeatedFieldMessage.newBuilder()
                                                        .addItems("b")
                                                        .addItems("a")
                                                        .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("items");
  }

  @Test
  void should_pass_with_ignoring_order() {
    // GIVEN
    RepeatedFieldMessage actual = RepeatedFieldMessage.newBuilder()
                                                      .addItems("a")
                                                      .addItems("b")
                                                      .build();
    RepeatedFieldMessage expected = RepeatedFieldMessage.newBuilder()
                                                        .addItems("b")
                                                        .addItems("a")
                                                        .build();
    // WHEN/THEN
    assertThat(actual).ignoringRepeatedFieldOrder().isEqualTo(expected);
  }
}
