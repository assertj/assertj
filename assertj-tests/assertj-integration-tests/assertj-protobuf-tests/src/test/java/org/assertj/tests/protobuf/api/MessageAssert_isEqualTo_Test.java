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

import org.assertj.tests.protobuf.TestProtos.TestMessage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.protobuf.api.MessageAssert#isEqualTo(Object)}.
 */
class MessageAssert_isEqualTo_Test {

  @Test
  void should_pass_when_messages_are_equal() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder()
                                    .setFoo("bar")
                                    .setBar(42)
                                    .build();
    TestMessage expected = TestMessage.newBuilder()
                                      .setFoo("bar")
                                      .setBar(42)
                                      .build();
    // WHEN/THEN
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void should_pass_when_comparing_same_instance() {
    // GIVEN
    TestMessage message = TestMessage.newBuilder()
                                     .setFoo("bar")
                                     .build();
    // WHEN/THEN
    assertThat(message).isEqualTo(message);
  }

  @Test
  void should_fail_when_messages_are_different() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder()
                                    .setFoo("bar")
                                    .build();
    TestMessage expected = TestMessage.newBuilder()
                                      .setFoo("baz")
                                      .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("foo")
               .hasMessageContaining("bar")
               .hasMessageContaining("baz");
  }

  @Test
  void should_fail_when_actual_has_extra_field() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder()
                                    .setFoo("bar")
                                    .setBar(42)
                                    .build();
    TestMessage expected = TestMessage.newBuilder()
                                      .setFoo("bar")
                                      .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("bar");
  }

  @Test
  void should_fail_when_expected_has_extra_field() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder()
                                    .setFoo("bar")
                                    .build();
    TestMessage expected = TestMessage.newBuilder()
                                      .setFoo("bar")
                                      .setBar(42)
                                      .build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("bar");
  }
}
