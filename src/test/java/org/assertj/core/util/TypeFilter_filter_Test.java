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
 * Copyright 2012-2014 the original author or authors.
 */
package org.assertj.core.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests for {@link TypeFilter#filter(java.util.Collection)}.
 * 
 * @author Yvonne Wang
 */
public class TypeFilter_filter_Test {
  @Test
  public void should_filter_Collection() {
    List<Object> original = new ArrayList<>();
    original.add(1);
    original.add("Frodo");
    original.add(5);
    List<String> filtered = new TypeFilter<>(String.class).filter(original);
    assertEquals(1, filtered.size());
    assertEquals("Frodo", filtered.get(0));
  }
}
