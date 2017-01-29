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
package org.assertj.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Tests for <code>{@link Lists#newArrayList()}</code>.
 * 
 * @author Christian Rösch
 */
public class Lists_newArrayList_Test {
  @Test
  public void should_return_empty_mutable_List() {
    ArrayList<String> list = Lists.newArrayList();
    assertThat(list).isEmpty();
    list.add("abc");
    assertThat(list).containsExactly("abc");
  }

  @Test
  public void should_return_new_List() {
    ArrayList<String> list1 = Lists.newArrayList();
    ArrayList<String> list2 = Lists.newArrayList();
    assertThat(list2).isNotSameAs(list1);

    // be sure they have nothing in common
    list1.add("abc");
    assertThat(list2).isEmpty();
  }

  @Test
  public void should_return_empty_List() {
    ArrayList<String> list = Lists.newArrayList();
    assertThat(list).isEmpty();
  }

}
