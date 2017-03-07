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

import static org.assertj.core.api.AbstractObjectAssert.defaultTypeComparators;
import static org.assertj.core.internal.ComparatorBasedComparisonStrategy.NOT_EQUAL;
import static org.assertj.core.internal.DeepDifference.determineDifferences;

import java.util.Comparator;
import java.util.Map;

import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Compares objects field/property by field/property recursively.
 */
public class RecursiveFieldByFieldComparator implements Comparator<Object> {

  private final Map<String, Comparator<?>> comparatorByPropertyOrField;
  private final TypeComparators comparatorByType;

  public RecursiveFieldByFieldComparator(Map<String, Comparator<?>> comparatorByPropertyOrField,
                                         TypeComparators comparatorByType) {
    this.comparatorByPropertyOrField = comparatorByPropertyOrField;
    this.comparatorByType = isNullOrEmpty(comparatorByType) ? defaultTypeComparators() : comparatorByType;
  }

  private boolean isNullOrEmpty(TypeComparators comparatorByType) {
    return comparatorByType == null || comparatorByType.isEmpty();
  }

  @Override
  public int compare(Object actual, Object other) {
    if (actual == null && other == null) return 0;
    if (actual == null || other == null) return NOT_EQUAL;
    // value returned is not relevant for ordering if objects are not equal.
    return areEqual(actual, other) ? 0 : NOT_EQUAL;
  }

  protected boolean areEqual(Object actual, Object other) {
    try {
      return determineDifferences(actual, other, comparatorByPropertyOrField, comparatorByType).isEmpty();
    } catch (IntrospectionError e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return "recursive field/property by field/property comparator on all fields/properties";
  }
}
