package org.assertj.core.util.diff;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteDeltaTest {

  @Test
  public void testGetType() throws Exception {
    // given
    Chunk<String> chunk = new Chunk<>(1, new ArrayList<>());
    Delta<String> delta = new DeleteDelta<>(chunk, chunk);

    // when
    Delta.TYPE type = delta.getType();

    // then
    assertThat(type).isEqualTo(Delta.TYPE.DELETE);
  }

  @Test
  public void testToString() throws Exception {
    // given
    Chunk<String> chunk1 = new Chunk<>(1, Arrays.asList("line1", "line2"));
    Chunk<String> chunk2 = new Chunk<>(2, new ArrayList<>());
    Delta<String> delta = new DeleteDelta<>(chunk1, chunk2);

    // when
    String desc = delta.toString();

    // then
    assertThat(desc).isEqualTo("[DeleteDelta, position: 1, lines: [line1, line2]]");
  }
}