package org.assertj.core.api.bytebuffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.error.ShouldBeMarked.shouldBeMarked;
import static org.assertj.core.error.ShouldHaveLimit.shouldHaveLimit;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import org.assertj.core.api.AbstractByteBufferAssert;
import org.junit.Test;


/**
 * Tests for <code>{@link AbstractByteBufferAssert#isMarked()}</code>.
 * @author Jean de Leeuw
 */
public class ByteBuffer_isMarked_Test {

  @Test
  public void should_pass_when_marked() {
    assertThat(ByteBuffer.allocate(10).mark()).isMarked();
  }

  @Test
  public void should_fail_when_not_marked() {
    ByteBuffer buffer = ByteBuffer.allocate(10);
    assertThatThrownBy(() -> assertThat(buffer).isMarked())
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldBeMarked(buffer).create());
  }
}
