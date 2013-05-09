/*
 * Created on Sep 23, 2006
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
 * Copyright @2006-2011 the original author or authors.
 */
package org.assertj.core.internal;

import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Iterables.isNullOrEmpty;
import static org.assertj.core.util.Iterables.sizeOf;
import static org.assertj.core.util.Lists.newArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Tests for {@link StandardComparisonStrategy#duplicatesFrom(java.util.Collection)}.<br>
 * 
 * @author Joel Costigliola
 */
public class StandardComparisonStrategy_duplicatesFrom_Test extends AbstractTest_StandardComparisonStrategy {

  @Test
  public void should_return_existing_duplicates() {
    List<String> list = newArrayList("Merry", "Frodo", null, null, "Merry", "Sam", "Frodo");
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(list);
    
    assertEquals(3, sizeOf(duplicates));
    assertTrue(standardComparisonStrategy.iterableContains(duplicates, "Frodo"));
    assertTrue(standardComparisonStrategy.iterableContains(duplicates, "Merry"));
    assertTrue(standardComparisonStrategy.iterableContains(duplicates, null));
  }

  @Test
  public void should_return_existing_duplicates_array() {
    List<String[]> list = newArrayList(array("Merry"), array("Frodo"), new String[] { null }, new String[] { null },
                                       array("Merry"), array("Sam"), array("Frodo"));
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(list);

    assertTrue("must contains null", standardComparisonStrategy.iterableContains(duplicates, new String[] { null }));
    assertTrue("must contains Frodo", standardComparisonStrategy.iterableContains(duplicates, array("Frodo")));
    assertTrue("must contains Merry", standardComparisonStrategy.iterableContains(duplicates, array("Merry")));
    assertEquals(3, sizeOf(duplicates));
  }

  @Test
  public void should_not_return_any_duplicates() {
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(newArrayList("Frodo", "Sam", "Gandalf"));
    assertTrue(isNullOrEmpty(duplicates));
  }

  @Test
  public void should_not_return_any_duplicates_if_collection_is_empty() {
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(new ArrayList<String>());
    assertTrue(isNullOrEmpty(duplicates));
  }

  @Test
  public void should_not_return_any_duplicates_if_collection_is_null() {
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(null);
    assertTrue(isNullOrEmpty(duplicates));
  }

}
