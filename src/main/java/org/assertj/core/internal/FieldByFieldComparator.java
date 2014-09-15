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
	if (actual == null || other == null) return NOT_EQUAL;
	// expecting actual and other to be arrays or iterable, compare their elements field by field
	// sizeOf(actual);
	if (areEqual(actual, other)) return 0;
	// value returned is not relevant for ordering but only means that actual and expected are not equal
	return NOT_EQUAL;
  }

  protected boolean areEqual(Object actual, Object other) {
	return Objects.instance().areEqualToIgnoringGivenFields(actual, other);
  }
  
  @Override
  public String toString() {
    return "field by field comparator on all fields";
  }

}