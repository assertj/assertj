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

import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

// immutable
class IterableDiff {

  private final ComparisonStrategy comparisonStrategy;

  List<Object> unexpected;
  List<Object> missing;

  <T> IterableDiff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
    this.unexpected = unexpectedElements(actual, expected);
    this.missing = missingElements(actual, expected);
  }

  static <T> IterableDiff diff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    return new IterableDiff(actual, expected, comparisonStrategy);
  }

  boolean differencesFound() {
    return !unexpected.isEmpty() || !missing.isEmpty();
  }

  private <T> List<Object> missingElements(Iterable<T> actual, Iterable<T> expected) {
    // return the elements in expected that are not in actual
    return subtract(actual, expected);
  }

  private <T> List<Object> unexpectedElements(Iterable<T> actual, Iterable<T> expected) {
    // return the elements in actual that are not in expected
    return subtract(expected, actual);
  }

  /**
   * Returns the list of elements in the second iterable that are not in the first  
   * @param first the list we want to look for missing elements
   * @param second the list of expected elements
   * @return list of elements from expected missing in source
   */
  private <T> List<Object> subtract(Iterable<T> first, Iterable<T> second) {
    List<Object> missingInFirst = new ArrayList<>();
    // use a copy to deal correctly with potential duplicates
    List<T> copyOfFirst = newArrayList(first);
    for (Object elementInSecond : second) {
      if (!iterableContains(copyOfFirst, elementInSecond)) {
        missingInFirst.add(elementInSecond);
      } else {
        // remove the element otherwise a duplicate would be still found in the case if there was only one in actual
        iterablesRemoveFirst(copyOfFirst, elementInSecond);
      }
    }
    return unmodifiableList(missingInFirst);
  }

  private boolean iterableContains(Iterable<?> actual, Object value) {
    return comparisonStrategy.iterableContains(actual, value);
  }

  private void iterablesRemoveFirst(Iterable<?> actual, Object value) {
    comparisonStrategy.iterablesRemoveFirst(actual, value);
  }
}
