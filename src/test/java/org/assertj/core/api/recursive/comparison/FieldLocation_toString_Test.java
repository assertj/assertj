package org.assertj.core.api.recursive.comparison;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class FieldLocation_toString_Test {

  private static final String FIELD_LOCATION_VALUE = "test_value_field_location";

  @Test
  void should_extract_toString() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);
    final String expected = "FieldLocation [fieldPath=" + FIELD_LOCATION_VALUE + "]";

    // WHEN & THEN
    then(fieldLocation.toString()).isEqualTo(expected);
  }
}
