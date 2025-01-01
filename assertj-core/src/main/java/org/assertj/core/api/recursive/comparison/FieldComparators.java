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
package org.assertj.core.api.recursive.comparison;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.data.MapEntry.entry;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * An internal holder of the comparators for fields described by their path without element index.
 * <p>
 * Examples: {@code name.first} or {@code names.first} but not {@code names[1].first} or {@code names.[1].first}
 */
public class FieldComparators extends FieldHolder<Comparator<?>> {

  protected final LinkedList<ComparatorForPatterns> comparatorByPatterns = new LinkedList<>();

  /**
   * Registers the {@code comparator} for the given {@code fieldLocation}.
   *
   * @param fieldLocation the location where to apply the comparator
   * @param comparator the comparator itself
   */
  public void registerComparator(String fieldLocation, Comparator<?> comparator) {
    super.put(fieldLocation, comparator);
  }

  /**
   * Registers the {@code comparator} for the given regexes field location.
   *
   * @param regexes the regexes field location where to apply the comparator
   * @param comparator the comparator to use for the regexes
   */
  public void registerComparatorForFieldsMatchingRegexes(String[] regexes, Comparator<?> comparator) {
    List<Pattern> patterns = Stream.of(regexes).map(Pattern::compile).collect(toList());
    comparatorByPatterns.addFirst(new ComparatorForPatterns(patterns, comparator));
  }

  /**
   * Checks, whether an any comparator is associated with the giving field location.
   *
   * @param fieldLocation the field location which association need to check
   * @return is field location contain a custom comparator
   */
  public boolean hasComparatorForField(String fieldLocation) {
    // TODO sanitize here?
    boolean hasComparatorForExactFieldLocation = super.hasEntity(fieldLocation);
    // comparator for exact location takes precedence over the one with location matched by regexes
    if (hasComparatorForExactFieldLocation) return true;
    // no comparator for exact location, check if there is a regex that matches the field location
    return comparatorByPatterns.stream()
                               .anyMatch(comparatorForPatterns -> comparatorForPatterns.hasComparatorForField(fieldLocation));
  }

  /**
   * Retrieves a custom comparator, which is associated with the giving field location. If this location does not
   * associate with any custom comparators - this method returns null.
   *
   * @param fieldLocation the field location that has to be associated with a comparator
   * @return a custom comparator or null
   */
  public Comparator<?> getComparatorForField(String fieldLocation) {
    Comparator<?> exactFieldLocationComparator = super.get(fieldLocation);
    if (exactFieldLocationComparator != null) return exactFieldLocationComparator;
    // no comparator for exact location, check if there is a regex that matches the field location
    return comparatorByPatterns.stream()
                               .map(comparatorForPatterns -> comparatorForPatterns.getComparatorForField(fieldLocation))
                               .filter(Objects::nonNull)
                               .findFirst()
                               .orElse(null);
  }

  /**
   * Returns a sequence of associated field-comparator pairs.
   *
   * @return sequence of field-comparator pairs
   */
  public Stream<Entry<String, Comparator<?>>> comparatorByFields() {
    return super.entryByField();
  }

  public Stream<Entry<List<Pattern>, Comparator<?>>> comparatorByRegexFields() {
    return comparatorByPatterns.stream().map(comparatorForPatterns -> entry(comparatorForPatterns.fieldPatterns,
                                                                            comparatorForPatterns.comparator));
  }

  @Override
  public boolean isEmpty() {
    return super.isEmpty() && comparatorByPatterns.isEmpty();
  }

  public boolean hasFieldComparators() {
    return !super.isEmpty();
  }

  public boolean hasRegexFieldComparators() {
    return !comparatorByPatterns.isEmpty();
  }
}

/**
 * Data structure holding the list of field patterns that will lead to use the given comparator.
 */
class ComparatorForPatterns {
  final List<Pattern> fieldPatterns;
  final Comparator<?> comparator;

  ComparatorForPatterns(List<Pattern> fieldPatterns, Comparator<?> comparator) {
    this.fieldPatterns = Collections.unmodifiableList(fieldPatterns);
    this.comparator = comparator;
  }

  boolean hasComparatorForField(String fieldLocation) {
    return fieldPatterns.stream().anyMatch(pattern -> pattern.matcher(fieldLocation).matches());
  }

  Comparator<?> getComparatorForField(String fieldLocation) {
    return hasComparatorForField(fieldLocation) ? comparator : null;
  }

  @Override
  public String toString() {
    return String.format("ComparatorForPatterns[patterns=%s, comparator=%s]", this.fieldPatterns, this.comparator);
  }
}
