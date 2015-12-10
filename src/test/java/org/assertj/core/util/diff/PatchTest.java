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

import junit.framework.TestCase;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

public class PatchTest extends TestCase {

  public void testPatch_Insert() {
    final List<String> insertTest_from = Arrays.asList("hhh");
    final List<String> insertTest_to = Arrays.asList("hhh", "jjj", "kkk", "lll");

    final Patch<String> patch = DiffUtils.diff(insertTest_from, insertTest_to);
    assertThat(DiffUtils.patch(insertTest_from, patch)).isEqualTo(insertTest_to);
  }

  public void testPatch_Delete() {
    final List<String> deleteTest_from = Arrays.asList("ddd", "fff", "ggg", "hhh");
    final List<String> deleteTest_to = Arrays.asList("ggg");

    final Patch<String> patch = DiffUtils.diff(deleteTest_from, deleteTest_to);
    assertThat(DiffUtils.patch(deleteTest_from, patch)).isEqualTo(deleteTest_to);
  }

  public void testPatch_Change() {
    final List<String> changeTest_from = Arrays.asList("aaa", "bbb", "ccc", "ddd");
    final List<String> changeTest_to = Arrays.asList("aaa", "bxb", "cxc", "ddd");

    final Patch<String> patch = DiffUtils.diff(changeTest_from, changeTest_to);
    assertThat(DiffUtils.patch(changeTest_from, patch)).isEqualTo(changeTest_to);
  }
}
