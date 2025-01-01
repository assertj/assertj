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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.core.internal;

import static java.util.Collections.unmodifiableList;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.ArrayList;
import java.util.List;

// immutable
/**
 * @param <T> the type of element to compare.
 */
class IterableDiff<T> {

  private final ComparisonStrategy comparisonStrategy;

  List<T> unexpected;
  List<T> missing;

  IterableDiff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
    // return the elements in actual that are not in expected: actual - expected
    this.unexpected = unexpectedActualElements(actual, expected);
    // return the elements in expected that are not in actual: expected - actual
    this.missing = missingActualElements(actual, expected);
  }

  static <T> IterableDiff<T> diff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    return new IterableDiff<>(actual, expected, comparisonStrategy);
  }

  static <T> IterableDiff<T> diff(Iterable<T> actual, Iterable<T> expected) {
    return diff(actual, expected, StandardComparisonStrategy.instance());
  }

  boolean differencesFound() {
    return !unexpected.isEmpty() || !missing.isEmpty();
  }

  /**
   * Returns the list of elements in the first iterable that are not in the second, i.e. first - second
   *
   * @param actual the list we want to subtract from
   * @param expected the list to subtract
   * @return the list of elements in the first iterable that are not in the second, i.e. first - second
   */
  private List<T> unexpectedActualElements(Iterable<T> actual, Iterable<T> expected) {
    List<T> missingInFirst = new ArrayList<>();
    // use a copy to deal correctly with potential duplicates
    List<T> copyOfExpected = newArrayList(expected);
    for (T elementInActual : actual) {
      if (isActualElementInExpected(elementInActual, copyOfExpected)) {
        // remove the element otherwise a duplicate would be found in the case if there is one in actual
        iterablesRemoveFirst(copyOfExpected, elementInActual);
      } else {
        missingInFirst.add(elementInActual);
      }
    }
    return unmodifiableList(missingInFirst);
  }

  private boolean isActualElementInExpected(T elementInActual, List<T> copyOfExpected) {
    // the order of comparisonStrategy.areEqual is important if element comparison is not symmetrical, we must compare actual to
    // expected but not expected to actual, for ex recursive comparison where:
    // - actual element is PersonDto, expected a list of Person
    // - Person has more fields than PersonDto => comparing PersonDto to Person is ok as it looks at PersonDto fields only,
    // --- the opposite always fails as the reference fields are Person fields and PersonDto does not have all of them.
    return copyOfExpected.stream().anyMatch(expectedElement -> comparisonStrategy.areEqual(elementInActual, expectedElement));
  }

  private List<T> missingActualElements(Iterable<T> actual, Iterable<T> expected) {
    List<T> missingInExpected = new ArrayList<>();
    // use a copy to deal correctly with potential duplicates
    List<T> copyOfActual = newArrayList(actual);
    for (T expectedElement : expected) {
      if (iterableContains(copyOfActual, expectedElement)) {
        // remove the element otherwise a duplicate would be found in the case if there is one in actual
        iterablesRemoveFirst(copyOfActual, expectedElement);
      } else {
        missingInExpected.add(expectedElement);
      }
    }
    return unmodifiableList(missingInExpected);
  }

  private boolean iterableContains(Iterable<?> actual, T expectedElement) {
    return comparisonStrategy.iterableContains(actual, expectedElement);
  }

  private void iterablesRemoveFirst(Iterable<?> actual, T value) {
    comparisonStrategy.iterablesRemoveFirst(actual, value);
  }
}
