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
package org.assertj.core.util.diff.myers;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.diff.myers.MyersDiff.MAX_EDIT_DISTANCE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.assertj.core.util.diff.ChangeDelta;
import org.assertj.core.util.diff.Chunk;
import org.assertj.core.util.diff.Patch;
import org.junit.jupiter.api.Test;

class MyersDiff_diff_Test {

  @Test
  void should_report_a_single_change_between_common_prefix_and_suffix_when_edit_distance_exceeds_limit() {
    // GIVEN
    List<String> originalMiddle = lines("original", MAX_EDIT_DISTANCE / 2 + 2);
    List<String> revisedMiddle = lines("revised", MAX_EDIT_DISTANCE / 2 + 2);
    List<String> original = surround(originalMiddle);
    List<String> revised = surround(revisedMiddle);
    // WHEN
    Patch<String> patch = new MyersDiff<String>().diff(original, revised);
    // THEN
    then(patch.getDeltas()).singleElement()
                           .isInstanceOf(ChangeDelta.class)
                           .returns(new Chunk<>(1, originalMiddle), delta -> delta.getOriginal())
                           .returns(new Chunk<>(1, revisedMiddle), delta -> delta.getRevised());
  }

  private static List<String> lines(String prefix, int count) {
    List<String> lines = new ArrayList<>(IntStream.range(0, count).mapToObj(i -> prefix + i).toList());
    lines.set(count / 2, "shared line");
    return lines;
  }

  private static List<String> surround(List<String> middle) {
    List<String> lines = new ArrayList<>();
    lines.add("common prefix");
    lines.addAll(middle);
    lines.add("common suffix");
    return lines;
  }

}
