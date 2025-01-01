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

import static java.lang.String.format;

import java.util.Comparator;
import java.util.List;

/**
 * {@code Comparator} comparing objects recursively as in {@link org.assertj.core.api.RecursiveComparisonAssert}.
 * <p>
 * This comparator does not enforce any ordering and returns zero if the compared objects are equals,
 * according to the recursive comparison, or a non-zero value otherwise.
 *
 * @since 3.24.0
 */
public class RecursiveComparator implements Comparator<Object> {

  private final RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private final RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator;

  /**
   * Returns a new {@code RecursiveComparator} that uses the default {@link RecursiveComparisonConfiguration}
   * when comparing objects with the recursive comparison.
   *
   * @since 3.25.0
   */
  public RecursiveComparator() {
    this(new RecursiveComparisonConfiguration());
  }

  /**
   * Returns a new {@code RecursiveComparator} that uses the given {@link RecursiveComparisonConfiguration}
   * when comparing objects with the recursive comparison.
   *
   * @param recursiveComparisonConfiguration the {@code RecursiveComparisonConfiguration} instance to be used
   */
  public RecursiveComparator(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
    this.recursiveComparisonDifferenceCalculator = new RecursiveComparisonDifferenceCalculator();
  }

  private List<ComparisonDifference> determineDifferencesWith(Object actual, Object expected) {
    return recursiveComparisonDifferenceCalculator.determineDifferences(actual, expected, recursiveComparisonConfiguration);
  }

  public String getDescription() {
    return format("RecursiveComparator a comparator based on the recursive comparison with the following configuration:%n%s",
                  recursiveComparisonConfiguration);
  }

  /**
   * Returns zero if the arguments are recursively equal to each other, or non-zero otherwise (no ordering enforced).
   *
   * @param actual the object to compare to {@code other}
   * @param other the object to compare to {@code actual}
   * @return zero if the arguments are recursively equal to each other, or non-zero otherwise.
   */
  @Override
  public int compare(Object actual, Object other) {
    if (actual == other) return 0;
    if (actual != null && other != null) return determineDifferencesWith(actual, other).size();
    // either actual or other is null but not both => can't be equal
    return -1;
  }

}
