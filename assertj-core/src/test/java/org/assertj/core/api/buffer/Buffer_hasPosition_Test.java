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
package org.assertj.core.api.buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldHavePosition.shouldHavePosition;

import java.nio.ByteBuffer;
import org.assertj.core.api.AbstractBufferAssert;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractBufferAssert#hasPosition(int)}</code>.
 * @author Jean de Leeuw
 */
public class Buffer_hasPosition_Test {

  @Test
  public void should_pass_when_expected_position_matches() {
    assertThat(ByteBuffer.allocate(10)).hasPosition(0);
  }

  @Test
  public void should_fail_when_expected_position_mismatches() {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    assertThatThrownBy(() -> assertThat(buffer).hasPosition(1))
                                                               .isInstanceOf(AssertionError.class)
                                                               .hasMessage(shouldHavePosition(1, 0, buffer).create());
  }
}
