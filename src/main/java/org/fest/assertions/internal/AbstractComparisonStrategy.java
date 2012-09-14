/*
 * Created on Sep 17, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.internal;

import static java.lang.reflect.Array.getLength;
import static org.fest.util.Iterables.isNullOrEmpty;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

/**
 * Base implementation of {@link ComparisonStrategy} contract.
 * 
 * @author Joel Costigliola
 */
public abstract class AbstractComparisonStrategy implements ComparisonStrategy {

  @Override
  public Iterable<?> duplicatesFrom(Iterable<?> iterable) {
    Set<Object> duplicates = new HashSet<Object>();
    if (isNullOrEmpty(iterable)) {
      return duplicates;
    }
    Set<Object> noDuplicates = new HashSet<Object>();
    for (Object element : iterable) {
      if (iterableContains(noDuplicates, element)) {
        duplicates.add(element);
        continue;
      }
      noDuplicates.add(element);
    }
    return duplicates;
  }

  @Override
  public boolean arrayContains(Object array, Object value) {
    for (int i = 0; i < getLength(array); i++) {
      Object element = Array.get(array, i);
      if (areEqual(element, value)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean isLessThan(Object actual, Object other) {
    if (areEqual(actual, other)) {
      return false;
    }
    return !isGreaterThan(actual, other);
  }

  @Override
  public boolean isLessThanOrEqualTo(Object actual, Object other) {
    if (areEqual(actual, other)) {
      return true;
    }
    return isLessThan(actual, other);
  }

  @Override
  public boolean isGreaterThanOrEqualTo(Object actual, Object other) {
    if (areEqual(actual, other)) {
      return true;
    }
    return isGreaterThan(actual, other);
  }

}
