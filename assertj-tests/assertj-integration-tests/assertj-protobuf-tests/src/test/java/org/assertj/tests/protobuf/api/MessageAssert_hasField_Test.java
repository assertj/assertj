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
import static org.assertj.tests.protobuf.testkit.AssertionErrors.expectAssertionError;
import static org.assertj.protobuf.api.Assertions.assertThat;

import org.assertj.tests.protobuf.TestProtos.TestMessage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link org.assertj.protobuf.api.MessageAssert#hasField(String)}.
 */
class MessageAssert_hasField_Test {

  @Test
  void should_pass_when_field_is_set() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder()
                                    .setFoo("bar")
                                    .build();
    // WHEN/THEN
    assertThat(actual).hasField("foo");
  }

  @Test
  void should_fail_when_field_is_not_set() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder().build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasField("foo"));
    // THEN
    then(error).hasMessageContaining("foo")
               .hasMessageContaining("not set");
  }

  @Test
  void should_fail_when_field_does_not_exist() {
    // GIVEN
    TestMessage actual = TestMessage.newBuilder().build();
    // WHEN
    AssertionError error = expectAssertionError(() -> assertThat(actual).hasField("nonexistent"));
    // THEN
    then(error).hasMessageContaining("nonexistent")
               .hasMessageContaining("does not exist");
  }
}
