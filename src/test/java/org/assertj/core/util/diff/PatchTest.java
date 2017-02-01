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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;

import org.junit.Test;

public class PatchTest {

  @Test
  public void testPatch_Insert() {
    List<String> insertTest_from = newArrayList("hhh");
    List<String> insertTest_to = newArrayList("hhh", "jjj", "kkk", "lll");

    Patch<String> patch = DiffUtils.diff(insertTest_from, insertTest_to);
    assertThat(DiffUtils.patch(insertTest_from, patch)).isEqualTo(insertTest_to);
  }

  @Test
  public void testPatch_Delete() {
    List<String> deleteTest_from = newArrayList("ddd", "fff", "ggg", "hhh");
    List<String> deleteTest_to = newArrayList("ggg");

    Patch<String> patch = DiffUtils.diff(deleteTest_from, deleteTest_to);
    assertThat(DiffUtils.patch(deleteTest_from, patch)).isEqualTo(deleteTest_to);
  }

  @Test
  public void testPatch_Change() {
    List<String> changeTest_from = newArrayList("aaa", "bbb", "ccc", "ddd");
    List<String> changeTest_to = newArrayList("aaa", "bxb", "cxc", "ddd");

    Patch<String> patch = DiffUtils.diff(changeTest_from, changeTest_to);
    assertThat(DiffUtils.patch(changeTest_from, patch)).isEqualTo(changeTest_to);
  }
}
