/*
 * Created on Sep 23, 2006
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2006-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static org.fest.util.Lists.newArrayList;

import static org.junit.Assert.*;

import java.util.List;

import org.fest.assertions.internal.ComparatorBasedComparisonStrategy;
import org.junit.Test;

/**
 * Tests for {@link ComparatorBasedComparisonStrategy#iterableRemoves(Iterable, Object)}.
 * 
 * @author Joel Costigliola
 */
public class ComparatorBasedComparisonStrategy_iterableRemove_Test extends AbstractTest_ComparatorBasedComparisonStrategy {

  @Test
  public void should_remove_value_from_collections_since_it_matches_one_collections_element_according_to_given_comparator() {
    List<String> hobbits = newArrayList("Merry", "Frodo", null, "Merry", "Sam");
    assertTrue(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "SAM"));
    caseInsensitiveComparisonStrategy.iterableRemoves(hobbits, "Sam");
    assertFalse(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "SAM"));
    caseInsensitiveComparisonStrategy.iterableRemoves(hobbits, null);
    assertFalse(caseInsensitiveComparisonStrategy.iterableContains(hobbits, null));
  }

  @Test
  public void should_not_remove_value_from_collections_since_it_does_not_match_any_collections_elements_according_to_given_comparator() {
    List<String> hobbits = newArrayList("Merry", "Frodo", null, "Merry", "Sam");
    assertTrue(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "SAM"));
    caseInsensitiveComparisonStrategy.iterableRemoves(hobbits, "SAM ");
    assertTrue(caseInsensitiveComparisonStrategy.iterableContains(hobbits, "SAM"));
  }

  @Test
  public void should_not_fail_if_collections_is_empty_or_null() {
    List<String> hobbits = newArrayList();
    caseInsensitiveComparisonStrategy.iterableRemoves(hobbits, "SAM");
    caseInsensitiveComparisonStrategy.iterableRemoves(null, "SAM ");
  }

}
