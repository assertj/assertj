/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.util;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/**
 * Tests for <code>{@link Lists#newArrayList(Iterable)}</code>.
 * 
 * @author Joel Costigliola
 */
public class Lists_newArrayList_withIterable_Test {
  @Test
  public void should_return_List_containing_all_elements_in_iterable() {
    String[] expected = { "One", "Two" };
    Iterable<String> elements = asList(expected);
    ArrayList<String> list = Lists.newArrayList(elements);
    assertThat(list.toArray()).isEqualTo(expected);
  }

  @Test
  public void should_return_null_if_iterable_is_null() {
    Iterable<?> elements = null;
    assertThat(Lists.newArrayList(elements)).isNull();
  }

  @Test
  public void should_return_empty_List_if_iterable_is_empty() {
    Iterable<String> elements = new ArrayList<>();
    ArrayList<String> list = Lists.newArrayList(elements);
    assertThat(list).isEmpty();
  }

}
