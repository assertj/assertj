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
package org.assertj.core.internal;

import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.IterableUtil.isNullOrEmpty;
import static org.assertj.core.util.IterableUtil.sizeOf;
import static org.assertj.core.util.Lists.newArrayList;

import static org.assertj.core.api.Assertions.assertThat;

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
    
    assertThat(sizeOf(duplicates)).isEqualTo(3);
    assertThat(standardComparisonStrategy.iterableContains(duplicates, "Frodo")).isTrue();
    assertThat(standardComparisonStrategy.iterableContains(duplicates, "Merry")).isTrue();
    assertThat(standardComparisonStrategy.iterableContains(duplicates, null)).isTrue();
  }

  @Test
  public void should_return_existing_duplicates_array() {
    List<String[]> list = newArrayList(array("Merry"), array("Frodo"), new String[] { null }, new String[] { null },
                                       array("Merry"), array("Sam"), array("Frodo"));
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(list);

    assertThat(standardComparisonStrategy.iterableContains(duplicates, new String[] { null })).as("must contains null").isTrue();
    assertThat(standardComparisonStrategy.iterableContains(duplicates, array("Frodo"))).as("must contains Frodo").isTrue();
    assertThat(standardComparisonStrategy.iterableContains(duplicates, array("Merry"))).as("must contains Merry").isTrue();
    assertThat(sizeOf(duplicates)).isEqualTo(3);
  }

  @Test
  public void should_not_return_any_duplicates() {
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(newArrayList("Frodo", "Sam", "Gandalf"));
    assertThat(isNullOrEmpty(duplicates)).isTrue();
  }

  @Test
  public void should_not_return_any_duplicates_if_collection_is_empty() {
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(new ArrayList<String>());
    assertThat(isNullOrEmpty(duplicates)).isTrue();
  }

  @Test
  public void should_not_return_any_duplicates_if_collection_is_null() {
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(null);
    assertThat(isNullOrEmpty(duplicates)).isTrue();
  }

}
