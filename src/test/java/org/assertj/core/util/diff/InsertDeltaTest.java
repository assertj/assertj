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
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.util.diff;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class InsertDeltaTest {

  private static List<String> EMPTY_LIST = Collections.emptyList();

  @Test
  public void testGetType() {
    // given
    Chunk<String> chunk = new Chunk<>(1, EMPTY_LIST);
    Delta<String> delta = new InsertDelta<>(chunk, chunk);

    // when
    Delta.TYPE type = delta.getType();

    // then
    assertThat(type).isEqualTo(Delta.TYPE.INSERT);
  }

  @Test
  public void testToString() {
    // given
    Chunk<String> chunk1 = new Chunk<>(0, EMPTY_LIST);
    Chunk<String> chunk2 = new Chunk<>(1, asList("line1", "line2"));
    Delta<String> delta = new InsertDelta<>(chunk1, chunk2);

    // when
    String desc = delta.toString();

    // then
    assertThat(desc).isEqualTo(format("Extra content at line 1:%n"
                                      + "  [\"line1\",%n"
                                      + "   \"line2\"]%n"));
  }
}