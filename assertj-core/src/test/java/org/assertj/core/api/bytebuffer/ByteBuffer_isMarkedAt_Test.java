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
 * Copyright 2012-2024 the original author or authors.
 */
package org.assertj.core.api.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldBeMarked.shouldBeMarked;
import static org.assertj.core.error.ShouldBeMarkedAt.shouldBeMarkedAt;

import java.nio.ByteBuffer;
import org.assertj.core.api.AbstractByteBufferAssert;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractByteBufferAssert#isMarkedAt(int)}</code>.
 * @author Jean de Leeuw
 */
public class ByteBuffer_isMarkedAt_Test {

  @Test
  public void should_pass_when_expected_marked_position_matches() {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    buffer.mark();
    assertThat(buffer).isMarkedAt(0);
  }

  @Test
  public void should_fail_when_not_marked() {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    assertThatThrownBy(() -> assertThat(buffer).isMarkedAt(0))
                                                              .isInstanceOf(AssertionError.class)
                                                              .hasMessage(shouldBeMarked(buffer).create());
  }

  @Test
  public void should_fail_when_expected_marked_position_mismatches() {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    buffer.mark();
    assertThatThrownBy(() -> assertThat(buffer).isMarkedAt(1))
                                                              .isInstanceOf(AssertionError.class)
                                                              .hasMessage(shouldBeMarkedAt(1, 0, buffer).create());
  }
}
