package org.assertj.core.api.recursive.comparison;

import org.assertj.core.internal.FieldByFieldComparator;

public class RecursiveComparisonDifferenceComparator extends FieldByFieldComparator {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;
  private RecursiveComparisonDifferenceCalculator recursiveComparisonDifferenceCalculator;

  public RecursiveComparisonDifferenceComparator(RecursiveComparisonConfiguration recursiveComparisonConfiguration) {
    this.recursiveComparisonConfiguration = recursiveComparisonConfiguration;
    this.recursiveComparisonDifferenceCalculator = new RecursiveComparisonDifferenceCalculator();
  }

  @Override
  public int compare(Object actual, Object other) {
    return recursiveComparisonDifferenceCalculator
            .determineDifferences(actual, other, recursiveComparisonConfiguration)
            .isEmpty() ? 0 : -1;
  }

  @Override
  protected boolean areEqual(Object actual, Object other) {
    return compare(actual, other) == 0;
  }

}
