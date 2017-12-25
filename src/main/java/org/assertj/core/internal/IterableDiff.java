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

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;
import static org.assertj.core.util.Lists.*;

class IterableDiff {

  private final ComparisonStrategy comparisonStrategy;

  List<Object> unexpected;
  List<Object> missing;

  <T> IterableDiff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
    this.unexpected = unmodifiableList(unexpectedElements(actual, expected));
    this.missing = unmodifiableList(missingElements(actual, expected));
  }

  static <T> IterableDiff diff(Iterable<T> actual, Iterable<T> expected, ComparisonStrategy comparisonStrategy) {
    return new IterableDiff(actual, expected, comparisonStrategy);
  }

  boolean differencesFound() {
    return !unexpected.isEmpty() || !missing.isEmpty();
  }

  private <T> List<Object> missingElements(Iterable<T> actual, Iterable<T> expected) {
    List<Object> missing = new ArrayList<>();
    List<T> copyOfActual = newArrayList(actual);
    for (Object element : expected) {
      if (!iterableContains(copyOfActual, element)) {
        missing.add(element);
      } else {
        iterablesRemoveFirst(copyOfActual, element);
      }
    }
    return missing;
  }

  private <T> List<Object> unexpectedElements(Iterable<T> actual, Iterable<T> expected) {
    List<Object> unexpected = new ArrayList<>();
    List<T> copyOfExpected = newArrayList(expected);
    for (Object actualElement : actual) {
      if (!iterableContains(copyOfExpected, actualElement)) {
        unexpected.add(actualElement);
      } else {
        iterablesRemoveFirst(copyOfExpected, actualElement);
      }
    }
    return unexpected;
  }

  private boolean iterableContains(Iterable<?> actual, Object value) {
    return comparisonStrategy.iterableContains(actual, value);
  }

  private void iterablesRemoveFirst(Iterable<?> actual, Object value) {
    comparisonStrategy.iterablesRemoveFirst(actual, value);
  }
}
