/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.internal;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link StandardComparisonStrategy#duplicatesFrom(Iterable)}.<br>
 * 
 * @author Joel Costigliola
 */
class StandardComparisonStrategy_duplicatesFrom_Test extends AbstractTest_StandardComparisonStrategy {

  @Test
  void should_return_existing_duplicates_in_order() {
    // GIVEN
    List<String> list = list("Merry", "Frodo", null, null, "Merry", "Sam", "Frodo");
    // WHEN
    @SuppressWarnings("unchecked")
    Iterable<Object> duplicates = (Iterable<Object>) standardComparisonStrategy.duplicatesFrom(list);
    // THEN
    then(duplicates).containsExactly(null, "Merry", "Frodo");
  }

  @Test
  void should_return_existing_duplicates_array() {
    // GIVEN
    List<String[]> list = list(array("Merry"), array("Frodo"), new String[] { null }, new String[] { null },
                               array("Merry"), array("Sam"), array("Frodo"));
    // WHEN
    @SuppressWarnings("unchecked")
    Iterable<String[]> duplicates = (Iterable<String[]>) standardComparisonStrategy.duplicatesFrom(list);
    // THEN
    then(duplicates).containsExactly(new String[] { null }, array("Merry"), array("Frodo"));
  }

  @Test
  void should_not_return_any_duplicates() {
    // GIVEN
    Iterable<String> hobbits = list("Frodo", "Sam", "Gandalf");
    // WHEN
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(hobbits);
    // THEN
    then(duplicates).isEmpty();
  }

  @Test
  void should_not_return_any_duplicates_if_collection_is_empty() {
    // GIVEN
    Iterable<String> emptyIterable = list();
    // WHEN
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(emptyIterable);
    // THEN
    then(duplicates).isEmpty();
  }

  @Test
  void should_not_return_any_duplicates_if_collection_is_null() {
    // WHEN
    Iterable<?> duplicates = standardComparisonStrategy.duplicatesFrom(null);
    // THEN
    then(duplicates).isEmpty();
  }

}
