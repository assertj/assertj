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

import static org.assertj.protobuf.api.Assertions.assertThat;

import org.assertj.tests.protobuf.TestProtos.TestMessage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.protobuf.api.MessageAssert#ignoringFields(String...)}.
 */
class MessageAssert_ignoringFields_Test {

  @Test
  void should_pass_when_ignoring_different_field() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder()
                                    .setFoo("bar")
                                    .setBar(42)
                                    .build();
    TestMessage expected = TestMessage.newBuilder()
                                      .setFoo("bar")
                                      .setBar(99)
                                      .build();
    // WHEN/THEN
    assertThat(actual).ignoringFields("bar").isEqualTo(expected);
  }

  @Test
  void should_pass_when_ignoring_multiple_fields() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder()
                                    .setFoo("bar")
                                    .setBar(42)
                                    .build();
    TestMessage expected = TestMessage.newBuilder()
                                      .setFoo("baz")
                                      .setBar(99)
                                      .build();
    // WHEN/THEN
    assertThat(actual).ignoringFields("foo", "bar").isEqualTo(expected);
  }
}
