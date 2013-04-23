/*
 * Created on Jun 17, 2010
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 * 
 * Copyright @2010-2012 the original author or authors.
 */
package org.assertj.core.util;

import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

/**
 * Tests for <code>{@link Collections#nonNullElementsIn(Collection)}</code>.
 * 
 * @author Joel Costigliola
 * @author Alex Ruiz
 */
public class Collections_nonNullElementsIn_Test {
  @Test
  public void should_return_empty_List_if_given_Collection_is_null() {
    Collection<?> c = null;
    assertTrue(Collections.nonNullElementsIn(c).isEmpty());
  }

  @Test
  public void should_return_empty_List_if_given_Collection_has_only_null_elements() {
    Collection<String> c = new ArrayList<String>();
    c.add(null);
    assertTrue(Collections.nonNullElementsIn(c).isEmpty());
  }

  @Test
  public void should_return_empty_List_if_given_Collection_is_empty() {
    Collection<String> c = new ArrayList<String>();
    assertTrue(Collections.nonNullElementsIn(c).isEmpty());
  }

  @Test
  public void should_return_a_list_without_null_elements() {
    List<String> c = newArrayList("Frodo", null, "Sam", null);
    List<String> nonNull = Collections.nonNullElementsIn(c);
    assertArrayEquals(new String[] { "Frodo", "Sam" }, nonNull.toArray());
  }
}
