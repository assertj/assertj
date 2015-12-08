package org.assertj.core.util.diff;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class InsertDeltaTest {

  @Test
  public void testGetType() throws Exception {
    // given
    Chunk<String> chunk = new Chunk<>(1, new ArrayList<>());
    Delta<String> delta = new InsertDelta<>(chunk, chunk);

    // when
    Delta.TYPE type = delta.getType();

    // then
    assertThat(type).isEqualTo(Delta.TYPE.INSERT);
  }

  @Test
  public void testToString() throws Exception {
    // given
    Chunk<String> chunk1 = new Chunk<>(1, new ArrayList<>());
    Chunk<String> chunk2 = new Chunk<>(2, Arrays.asList("line1", "line2"));
    Delta<String> delta = new InsertDelta<>(chunk1, chunk2);

    // when
    String desc = delta.toString();

    // then
    assertThat(desc).isEqualTo("[InsertDelta, position: 1, lines: [line1, line2]]");
  }
}