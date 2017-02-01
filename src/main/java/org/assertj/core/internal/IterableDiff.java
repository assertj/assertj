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

import java.util.ArrayList;
import java.util.List;

class IterableDiff {
  
  private final ComparisonStrategy comparisonStrategy;

  List<Object> unexpected;
  List<Object> missing;

  static IterableDiff diff(Iterable<Object> actual, Iterable<Object> expected, ComparisonStrategy comparisonStrategy) {
    return new IterableDiff(actual, expected, comparisonStrategy);
  }
  
  IterableDiff(Iterable<Object> actual, Iterable<Object> expected, ComparisonStrategy comparisonStrategy) {
    this.comparisonStrategy = comparisonStrategy;
    this.unexpected = unmodifiableList(unexpectedElements(actual, expected));
    this.missing = unmodifiableList(missingElements(actual, expected));
  }
  
  boolean differencesFound() {
    return !unexpected.isEmpty() || !missing.isEmpty();
  }
  
  private List<Object> missingElements(Iterable<Object> actual, Iterable<Object> expected) {
    List<Object> missing = new ArrayList<>();
    for (Object element : expected) {
      if (!iterableContains(actual, element)) {
        missing.add(element);
      }
    }
    return missing;
  }

  private List<Object> unexpectedElements(Iterable<Object> actual, Iterable<Object> expected) {
    List<Object> unexpected = new ArrayList<>();
    for (Object actualElement : actual) {
      if (!iterableContains(expected, actualElement)) {
        unexpected.add(actualElement);
      }
    }
    return unexpected;
  }

  private boolean iterableContains(Iterable<?> actual, Object value) {
    return comparisonStrategy.iterableContains(actual, value);
  }
  
}