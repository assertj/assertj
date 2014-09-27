package org.assertj.core.internal;

import java.util.Comparator;

/**
 * Compare Object field by field
 */
public class FieldByFieldComparator implements Comparator<Object> {

  private static final int NOT_EQUAL = -1;

  @Override
  public int compare(Object actual, Object other) {
	if (actual == null && other == null) return 0;
	if (actual == null || other == null) return NOT_EQUAL;
	// value returned is not relevant for ordering if objects are not equal.
	return areEqual(actual, other) ? 0 : NOT_EQUAL;
  }

  protected boolean areEqual(Object actual, Object other) {
	return Objects.instance().areEqualToIgnoringGivenFields(actual, other);
  }

  @Override
  public String toString() {
	return "field by field comparator on all fields";
  }

}