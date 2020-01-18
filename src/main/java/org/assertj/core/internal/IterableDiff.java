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
 * Copyright 2012-2020 the original author or authors.
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
    // return the elements in actual that are not in expected: actual - expected
    this.unexpected = subtract(actual, expected);
    // return the elements in expected that are not in actual: expected - actual
    this.missing = subtract(expected, actual);
  }

  static <T> IterableDiff diff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    return new IterableDiff(actual, expected, comparisonStrategy);
  }

  boolean differencesFound() {
    return !unexpected.isEmpty() || !missing.isEmpty();
  }

  /**
   * Returns the list of elements in the first iterable that are not in the second, i.e. first - second
   *
   * @param <T> the element type
   * @param first the list we want to subtract from
   * @param second the list to subtract
   * @return the list of elements in the first iterable that are not in the second, i.e. first - second
   */
  private <T> List<Object> subtract(Iterable<T> first, Iterable<T> second) {
    List<Object> missingInFirst = new ArrayList<>();
    // use a copy to deal correctly with potential duplicates
    List<T> copyOfSecond = newArrayList(second);
    for (Object elementInFirst : first) {
      if (iterableContains(copyOfSecond, elementInFirst)) {
        // remove the element otherwise a duplicate would be found in the case if there is one in actual
        iterablesRemoveFirst(copyOfSecond, elementInFirst);
      } else {
        missingInFirst.add(elementInFirst);
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
