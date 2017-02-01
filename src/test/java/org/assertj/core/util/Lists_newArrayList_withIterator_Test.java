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

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

/**
 * Tests for <code>{@link Lists#newArrayList(Iterator)}</code>.
 * 
 * @author Christian Rösch
 */
public class Lists_newArrayList_withIterator_Test {
  @Test
  public void should_return_List_containing_all_elements_in_iterator() {
    String[] expected = { "One", "Two" };
    Iterator<String> elements = asList(expected).iterator();
    ArrayList<String> list = Lists.newArrayList(elements);
    assertThat(list).containsExactly(expected);
  }

  @Test
  public void should_return_null_if_iterator_is_null() {
    Iterator<?> elements = null;
    assertThat(Lists.newArrayList(elements)).isNull();
  }

  @Test
  public void should_return_empty_List_if_iterator_is_empty() {
    Iterator<String> elements = new ArrayList<String>().iterator();
    ArrayList<String> list = Lists.newArrayList(elements);
    assertThat(list).isEmpty();
  }
}
