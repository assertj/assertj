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

import org.assertj.core.api.AbstractBufferAssert;
import org.junit.jupiter.api.Test;

import java.nio.Buffer;
import java.nio.ByteBuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldBeEmpty.shouldBeEmpty;

/**
 * Tests for <code>{@link AbstractBufferAssert#isEmpty()}</code>.
 * @author Jean de Leeuw
 */
public class Buffer_isEmpty_Test {

  @Test
  public void should_pass_when_empty() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    Buffer buffer = byteBuffer;
    buffer.flip();
    assertThat(buffer).isEmpty();
  }

  @Test
  public void should_fail_when_not_empty() {
    ByteBuffer byteBuffer = ByteBuffer.allocate(10);
    byteBuffer.put((byte) 10);
    Buffer buffer = byteBuffer;
    buffer.flip();
    assertThatThrownBy(() -> assertThat(buffer).isEmpty())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldBeEmpty(buffer).create());
  }
}
