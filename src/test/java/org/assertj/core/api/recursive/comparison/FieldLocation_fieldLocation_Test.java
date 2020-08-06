package org.assertj.core.api.recursive.comparison;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class FieldLocation_fieldLocation_Test {

  private static final String OTHER_VALUE = "other_value";
  private static final String FIELD_LOCATION_VALUE = "test_value_field_location";

  @Test
  void should_fieldLocation_same() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation).isEqualTo(FieldLocation.fieldLocation(FIELD_LOCATION_VALUE));
  }

  @Test
  void should_fieldLocation_diff() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation).isNotEqualTo(FieldLocation.fieldLocation(OTHER_VALUE));
  }
}
