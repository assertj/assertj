/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.util.diff;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class DiffTest {

  @Test
  void testDiff_Insert() {
    Patch<String> patch = DiffUtils.diff(newArrayList("hhh"), newArrayList("hhh", "jjj", "kkk"));

    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas.size()).isEqualTo(1);
    Delta<String> delta = deltas.get(0);
    assertThat(delta).isInstanceOf(InsertDelta.class);
    assertThat(delta.getOriginal()).isEqualTo(new Chunk<>(1, emptyList()));
    assertThat(delta.getRevised()).isEqualTo(new Chunk<>(1, newArrayList("jjj", "kkk")));
  }

  @Test
  void testDiff_Delete() {
    Patch<String> patch = DiffUtils.diff(newArrayList("ddd", "fff", "ggg"), newArrayList("ggg"));

    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas.size()).isEqualTo(1);
    Delta<String> delta = deltas.get(0);
    assertThat(delta).isInstanceOf(DeleteDelta.class);
    assertThat(delta.getOriginal()).isEqualTo(new Chunk<>(0, newArrayList("ddd", "fff")));
    assertThat(delta.getRevised()).isEqualTo(new Chunk<>(0, emptyList()));
  }

  @Test
  void testDiff_Change() {
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
  void testDiff_EmptyList() {
    Patch<Object> patch = DiffUtils.diff(emptyList(), emptyList());

    assertThat(patch.getDeltas().size()).isEqualTo(0);
  }

  @Test
  void testDiff_EmptyListWithNonEmpty() {
    List<String> emptyList = Collections.emptyList();
    Patch<String> patch = DiffUtils.diff(emptyList, newArrayList("aaa"));

    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas.size()).isEqualTo(1);
    assertThat(deltas.get(0)).isInstanceOf(InsertDelta.class);
  }

  @Test
  void should_not_hang_and_report_single_change_when_sequences_are_completely_different() {
    // GIVEN two completely different large lists of lines, see gh-2345: the Myers exploration used to
    // allocate O(d^2) path nodes and never complete on such inputs
    int lineCount = 60_000;
    List<String> original = new ArrayList<>(lineCount);
    List<String> revised = new ArrayList<>(lineCount);
    for (int i = 0; i < lineCount; i++) {
      original.add("original-" + i);
      revised.add("revised-" + i);
    }
    // WHEN
    Patch<String> patch = DiffUtils.diff(original, revised);
    // THEN the whole content is reported as a single change
    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas).hasSize(1);
    Delta<String> delta = deltas.get(0);
    assertThat(delta).isInstanceOf(ChangeDelta.class);
    assertThat(delta.getOriginal()).isEqualTo(new Chunk<>(0, original));
    assertThat(delta.getRevised()).isEqualTo(new Chunk<>(0, revised));
  }

  @Test
  void should_report_single_change_between_common_prefix_and_suffix_when_sequences_diverge_too_much() {
    // GIVEN two large lists sharing a common prefix and suffix with a completely different middle, see gh-2345
    List<String> original = new ArrayList<>();
    List<String> revised = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      original.add("common-prefix-" + i);
      revised.add("common-prefix-" + i);
    }
    for (int i = 0; i < 60_000; i++) {
      original.add("original-" + i);
      revised.add("revised-" + i);
    }
    for (int i = 0; i < 100; i++) {
      original.add("common-suffix-" + i);
      revised.add("common-suffix-" + i);
    }
    // WHEN
    Patch<String> patch = DiffUtils.diff(original, revised);
    // THEN only the differing middle is reported as a single change
    List<Delta<String>> deltas = patch.getDeltas();
    assertThat(deltas).hasSize(1);
    Delta<String> delta = deltas.get(0);
    assertThat(delta).isInstanceOf(ChangeDelta.class);
    assertThat(delta.getOriginal().getPosition()).isEqualTo(100);
    assertThat(delta.getOriginal().getLines()).isEqualTo(original.subList(100, 60_100));
    assertThat(delta.getRevised().getLines()).isEqualTo(revised.subList(100, 60_100));
  }
}
