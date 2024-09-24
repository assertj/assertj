package org.assertj.core.api.buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldHaveRemaining.shouldHaveRemaining;

import java.nio.ByteBuffer;
import org.assertj.core.api.AbstractBufferAssert;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractBufferAssert#hasRemaining(int)}</code>.
 * @author Jean de Leeuw
 */
public class Buffer_hasRemaining_Test {

  @Test
  public void should_pass_when_expected_number_of_remaining_elements_matches() {
    assertThat(ByteBuffer.allocate(10)).hasRemaining(10);
  }

  @Test
  public void should_fail_when_expected_number_of_remaining_elements_mismatches() {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    assertThatThrownBy(() -> assertThat(buffer).hasRemaining(11))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldHaveRemaining(11, 10, buffer).create());
  }
}
