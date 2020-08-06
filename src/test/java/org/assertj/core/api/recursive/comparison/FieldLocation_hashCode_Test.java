package org.assertj.core.api.recursive.comparison;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class FieldLocation_hashCode_Test {

  private static final String FIELD_LOCATION_VALUE = "test_value";

  @Test
  void should_fieldLocation_hashCode_equal() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation.hashCode()).isEqualTo(fieldLocation.hashCode());
    then(fieldLocation.equals(fieldLocation)).isEqualTo(true);
  }

  @Test
  void should_fieldLocation_hashCode_same_value_equals() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);
    FieldLocation other = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation.hashCode()).isEqualTo(other.hashCode());
    then(fieldLocation.equals(other)).isEqualTo(true);
  }

  @Test
  void should_fieldLocation_hashCode_diff_value_not_equals() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);
    FieldLocation other = FieldLocation.fieldLocation("");

    // WHEN & THEN
    then(fieldLocation.hashCode()).isNotEqualTo(other.hashCode());
    then(fieldLocation.equals(other)).isEqualTo(false);
  }
}
