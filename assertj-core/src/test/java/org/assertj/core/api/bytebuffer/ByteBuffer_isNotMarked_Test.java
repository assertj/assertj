package org.assertj.core.api.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldBeMarked.shouldBeMarked;
import static org.assertj.core.error.ShouldBeMarked.shouldNotBeMarked;

import java.nio.ByteBuffer;
import org.assertj.core.api.AbstractByteBufferAssert;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractByteBufferAssert#isNotMarked()}</code>.
 * @author Jean de Leeuw
 */
public class ByteBuffer_isNotMarked_Test {

  @Test
  public void should_pass_when_not_marked() {
    assertThat(ByteBuffer.allocate(10)).isNotMarked();
  }

  @Test
  public void should_fail_when_marked() {
    ByteBuffer buffer = ByteBuffer.allocate(10).mark();
    assertThatThrownBy(() -> assertThat(buffer).isNotMarked())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeMarked(buffer).create());
  }
}
