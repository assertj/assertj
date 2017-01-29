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

import static org.assertj.core.util.Lists.newArrayList;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

/**
 * Tests for <code>{@link IterableUtil#isNullOrEmpty(Iterable)}</code>.
 * 
 * @author Alex Ruiz
 */
public class IterableUtil_isNullOrEmpty_Test {
  @Test
  public void should_return_true_if_Collection_is_empty() {
    Iterable<String> c = new ArrayList<>();
    assertThat(IterableUtil.isNullOrEmpty(c)).isTrue();
  }

  @Test
  public void should_return_false_if_Collection_has_elements() {
    Iterable<String> c = newArrayList("Frodo");
    assertThat(IterableUtil.isNullOrEmpty(c)).isFalse();
  }

  @Test
  public void should_return_true_if_Iterable_is_empty() {
    Iterable<String> i = new StringIterable();
    assertThat(IterableUtil.isNullOrEmpty(i)).isTrue();
  }

  @Test
  public void should_return_true_if_Iterable_is_null() {
    assertThat(IterableUtil.isNullOrEmpty(null)).isTrue();
  }

  @Test
  public void should_return_false_if_Iterable_has_elements() {
    Iterable<String> i = new StringIterable("Frodo");
    assertThat(IterableUtil.isNullOrEmpty(i)).isFalse();
  }

  private static class StringIterable implements Iterable<String> {
    private final List<String> elements;

    StringIterable(String...elements) {
      this.elements = Lists.newArrayList(elements);
    }

    @Override
    public Iterator<String> iterator() {
      return elements.iterator();
    }
  }
}
