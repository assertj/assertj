package org.assertj.core.internal;


public class FieldByFieldElementComparisonStrategy extends ComparatorBasedComparisonStrategy {

  public FieldByFieldElementComparisonStrategy() {
	super(new FieldByFieldComparator());
  }

  @Override
  public String toString() {
	return "'field by field elements'";
  }
}
