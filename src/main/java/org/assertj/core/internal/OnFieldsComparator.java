package org.assertj.core.internal;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.VisibleForTesting;

public class OnFieldsComparator extends FieldByFieldComparator {

  private final static StandardRepresentation REPRESENTATION = new StandardRepresentation();
  
  private String[] fields;

  public OnFieldsComparator(String... fields) {
      this.fields = fields;
  }
  
  @VisibleForTesting
  public String[] getFields() {
	return fields;
  }
  
  @Override
  protected boolean areEqual(Object actualElement, Object otherElement) {
    return Objects.instance().areEqualToComparingOnlyGivenFields(actualElement, otherElement, fields);
  }
  
  @Override
  public String toString() {
	if (fields != null && fields.length == 1) return "field " + REPRESENTATION.toStringOf(fields);
	return "field by field comparator on fields " + REPRESENTATION.toStringOf(fields);
  }
  
}
