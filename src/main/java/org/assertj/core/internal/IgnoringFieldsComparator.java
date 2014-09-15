package org.assertj.core.internal;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.VisibleForTesting;

public class IgnoringFieldsComparator extends FieldByFieldComparator {

  private String[] fields;
  private final static StandardRepresentation REPRESENTATION = new StandardRepresentation();

  public IgnoringFieldsComparator(String... fields) {
      this.fields = fields;
  }
  
  @VisibleForTesting
  public String[] getFields() {
	return fields;
  }
  
  @Override
  protected boolean areEqual(Object actualElement, Object otherElement) {
    return Objects.instance().areEqualToIgnoringGivenFields(actualElement, otherElement, fields);
  }
  
  @Override
  public String toString() {
	return "field by field comparator on all fields but " + REPRESENTATION.toStringOf(fields);
  }
}
