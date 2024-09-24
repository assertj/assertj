package org.assertj.core.api.buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldHaveCapacity.shouldHaveCapacity;

import java.nio.ByteBuffer;
import org.assertj.core.api.AbstractBufferAssert;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractBufferAssert#hasCapacity(int)}</code>.
 * @author Jean de Leeuw
 */
public class Buffer_hasCapacity_Test {

  @Test
  public void should_pass_when_expected_capacity_matches() {
    assertThat(ByteBuffer.allocate(10)).hasCapacity(10);
  }

  @Test
  public void should_fail_when_expected_capacity_mismatches() {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    assertThatThrownBy(() -> assertThat(buffer).hasCapacity(11))
      .isInstanceOf(AssertionError.class)
        .hasMessage(shouldHaveCapacity(11, 10, buffer).create());
  }
}
