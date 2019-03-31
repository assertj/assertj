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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.buffer.ShouldBeFlipped.shouldBeFlipped;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import org.assertj.core.api.AbstractBufferAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link AbstractBufferAssert#isFlipped()}</code>.
 * @author Jean de Leeuw
 */
public class Buffer_isFlipped_Test {

  private Buffer buffer;

  @BeforeEach
  public void beforeEach() {
    byte[] testString = "test".getBytes();
    ByteBuffer byteBuffer = ByteBuffer.allocate(testString.length);
    byteBuffer.put(testString);
    buffer = byteBuffer;
  }

  @Test
  public void should_pass_if_buffer_is_flipped() {
    buffer.flip();
    assertThat(buffer).isFlipped();
  }

  @Test
  public void should_fail_if_buffer_is_not_flipped() {
    assertThatThrownBy(() -> assertThat(buffer).isFlipped())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldBeFlipped(buffer).create());
  }
}
