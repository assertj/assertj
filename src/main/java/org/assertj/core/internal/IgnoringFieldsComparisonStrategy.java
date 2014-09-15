package org.assertj.core.internal;

import org.assertj.core.util.VisibleForTesting;

public class IgnoringFieldsComparisonStrategy extends ComparatorBasedComparisonStrategy {

  private String[] fields;

  public IgnoringFieldsComparisonStrategy(String... fields) {
	super(new IgnoringFieldsComparator(fields));
	this.fields = fields;
  }

  @VisibleForTesting
  public String[] getFields() {
	return fields;
  }

  @Override
  public String asText() {
	return "when comparing elements field by field except the following fields : " + fieldsAsText() + "\n";
  }

  private String fieldsAsText() {
	return org.assertj.core.util.Strings.join(fields).with(", ");
  }

}
