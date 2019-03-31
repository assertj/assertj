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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.buffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.buffer.ShouldHaveRemainingLength.shouldHaveRemainingLength;

/**
 * Tests for <code>{@link org.assertj.core.api.AbstractBufferAssert#hasRemainingLength(int)}</code>.
 * @author Jean de Leeuw
 */
public class Buffer_hasRemainingLength_Test {

  private Buffer buffer;
  private final int capacity = 10;
  private byte[] testArray;

  @BeforeEach
  public void beforeEach() {
    testArray = "test".getBytes();
    ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
    byteBuffer.put(testArray);
    byteBuffer.flip();
    buffer = byteBuffer;
  }

  @Test
  public void should_pass_when_expected_remaining_length_matches() {
    assertThat(buffer).hasRemainingLength(capacity - testArray.length);
  }

  @Test
  public void should_fail_when_expected_remaining_length_mismatches() {
    int remainingLength = capacity - testArray.length;
    assertThatThrownBy(() -> assertThat(buffer).hasRemainingLength(remainingLength - 1))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveRemainingLength(remainingLength - 1, remainingLength, buffer).create());
  }
}
