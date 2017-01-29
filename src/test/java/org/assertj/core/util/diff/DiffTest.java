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

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class DiffTest {

  @Test
  public void testDiff_Insert() {
    Patch<String> patch = DiffUtils.diff(newArrayList("hhh"), newArrayList("hhh", "jjj", "kkk"));

    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas.size()).isEqualTo(1);
    Delta<String> delta = deltas.get(0);
    assertThat(delta).isInstanceOf(InsertDelta.class);
    assertThat(delta.getOriginal()).isEqualTo(new Chunk<>(1, emptyList()));
    assertThat(delta.getRevised()).isEqualTo(new Chunk<>(1, newArrayList("jjj", "kkk")));
  }

  @Test
  public void testDiff_Delete() {
    Patch<String> patch = DiffUtils.diff(newArrayList("ddd", "fff", "ggg"), newArrayList("ggg"));

    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas.size()).isEqualTo(1);
    Delta<String> delta = deltas.get(0);
    assertThat(delta).isInstanceOf(DeleteDelta.class);
    assertThat(delta.getOriginal()).isEqualTo(new Chunk<>(0, newArrayList("ddd", "fff")));
    assertThat(delta.getRevised()).isEqualTo(new Chunk<>(0, emptyList()));
  }

  @Test
  public void testDiff_Change() {
    List<String> changeTest_from = newArrayList("aaa", "bbb", "ccc");
    List<String> changeTest_to = newArrayList("aaa", "zzz", "ccc");

    Patch<String> patch = DiffUtils.diff(changeTest_from, changeTest_to);

    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas.size()).isEqualTo(1);
    Delta<String> delta = deltas.get(0);
    assertThat(delta).isInstanceOf(ChangeDelta.class);
    assertThat(delta.getOriginal()).isEqualTo(new Chunk<>(1, newArrayList("bbb")));
    assertThat(delta.getRevised()).isEqualTo(new Chunk<>(1, newArrayList("zzz")));
  }

  @Test
  public void testDiff_EmptyList() {
    Patch<Object> patch = DiffUtils.diff(emptyList(), emptyList());

    assertThat(patch.getDeltas().size()).isEqualTo(0);
  }

  @Test
  public void testDiff_EmptyListWithNonEmpty() {
    List<String> emptyList = Collections.emptyList();
    Patch<String> patch = DiffUtils.diff(emptyList, newArrayList("aaa"));

    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas.size()).isEqualTo(1);
    assertThat(deltas.get(0)).isInstanceOf(InsertDelta.class);
  }
}
