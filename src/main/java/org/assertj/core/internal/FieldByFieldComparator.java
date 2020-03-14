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

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.internal.ComparatorBasedComparisonStrategy.NOT_EQUAL;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.util.Strings.join;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.assertj.core.api.Assertions;
import org.assertj.core.util.introspection.IntrospectionError;

/**
 * Compares objects field/property by field/property including private fields unless
 * {@link Assertions#setAllowComparingPrivateFields(boolean)} has been called with false.
 */
public class FieldByFieldComparator implements Comparator<Object> {

  protected final Map<String, Comparator<?>> comparatorsByPropertyOrField;
  protected final TypeComparators comparatorsByType;

  public FieldByFieldComparator(Map<String, Comparator<?>> comparatorsByPropertyOrField,
                                TypeComparators typeComparators) {
    this.comparatorsByPropertyOrField = comparatorsByPropertyOrField == null
        ? new TreeMap<>()
        : comparatorsByPropertyOrField;
    this.comparatorsByType = isNullOrEmpty(typeComparators) ? defaultTypeComparators() : typeComparators;
  }

  public FieldByFieldComparator() {
    this(new TreeMap<String, Comparator<?>>(), defaultTypeComparators());
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
      return Objects.instance().areEqualToIgnoringGivenFields(actual, other, comparatorsByPropertyOrField,
                                                              comparatorsByType);
    } catch (IntrospectionError e) {
      return false;
    }
  }

  @Override
  public String toString() {
    return description() + describeUsedComparators();
  }

  protected String description() {
    return "field/property by field/property comparator on all fields/properties";
  }

  protected String describeUsedComparators() {
    if (comparatorsByPropertyOrField.isEmpty()) {
      return format("%nComparators used:%n%s", describeFieldComparatorsByType());
    }
    return format("%nComparators used:%n%s%n%s", describeFieldComparatorsByName(), describeFieldComparatorsByType());
  }

  protected String describeFieldComparatorsByType() {
    return format("- for elements fields (by type): %s", comparatorsByType);
  }

  protected String describeFieldComparatorsByName() {
    if (comparatorsByPropertyOrField.isEmpty()) {
      return "";
    }
    List<String> fieldComparatorsDescription = this.comparatorsByPropertyOrField.entrySet().stream()
                                                                                .map(FieldByFieldComparator::formatFieldComparator)
                                                                                .collect(toList());
    return format("- for elements fields (by name): {%s}", join(fieldComparatorsDescription).with(", "));
  }

  private static String formatFieldComparator(Entry<String, Comparator<?>> next) {
    return next.getKey() + " -> " + next.getValue();
  }

  private static boolean isNullOrEmpty(TypeComparators comparatorByType) {
    return comparatorByType == null || comparatorByType.isEmpty();
  }

}
