package org.assertj.core.internal;


public class FieldByFieldComparisonStrategy extends ComparatorBasedComparisonStrategy {

  public FieldByFieldComparisonStrategy() {
	super(new FieldByFieldComparator());
  }

  @Override
  public String asText() {
	return "when comparing elements field by field (on all field)\n";
  }

}
