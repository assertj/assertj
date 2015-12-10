/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.util.diff;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class DeleteDeltaTest {

  @Test
  public void testGetType() throws Exception {
    // given
    Chunk<String> chunk = new Chunk<>(1, new ArrayList<String>());
    Delta<String> delta = new DeleteDelta<>(chunk, chunk);

    // when
    Delta.TYPE type = delta.getType();

    // then
    assertThat(type).isEqualTo(Delta.TYPE.DELETE);
  }

  @Test
  public void testToString() throws Exception {
    // given
    Chunk<String> chunk1 = new Chunk<>(0, Arrays.asList("line1", "line2"));
    Chunk<String> chunk2 = new Chunk<>(1, new ArrayList<String>());
    Delta<String> delta = new DeleteDelta<>(chunk1, chunk2);

    // when
    String desc = delta.toString();

    // then
    assertThat(desc).isEqualTo(format("%n"
                                      + "Missing content at line 1:%n"
                                      + "  [\"line1\",%n"
                                      + "   \"line2\"]%n"));
  }
}