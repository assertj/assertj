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
