package org.assertj.core.internal;

import static org.assertj.core.util.Arrays.isNullOrEmpty;
import static org.assertj.core.util.Strings.isNullOrEmpty;

import org.assertj.core.presentation.StandardRepresentation;
import org.assertj.core.util.VisibleForTesting;

public class OnFieldsComparator extends FieldByFieldComparator {

  private final static StandardRepresentation REPRESENTATION = new StandardRepresentation();

  private String[] fields;

  public OnFieldsComparator(String... fields) {
	if (isNullOrEmpty(fields)) throw new IllegalArgumentException("No fields specified");
	for (String field : fields) {
	  if (isNullOrEmpty(field) || isNullOrEmpty(field.trim()))
		throw new IllegalArgumentException("Null/blank fields are invalid, fields were "
		                                   + REPRESENTATION.toStringOf(fields));
	}
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
	if (fields.length == 1) return "single field comparator on field " + REPRESENTATION.toStringOf(fields[0]);
	return "field by field comparator on fields " + REPRESENTATION.toStringOf(fields);
  }

}
