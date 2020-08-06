package org.assertj.core.api.recursive.comparison;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

class FieldLocation_equals_Test {

  private static final String EMPTY__VALUE = "";
  private static final String FIELD_LOCATION_VALUE = "test_value";
  private static final String FIELD_LOCATION_OTHER_VALUE = "other_test_value";

  @Test
  void should_fieldLocation_equals_itself() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation.equals(fieldLocation)).isEqualTo(true);
  }

  @Test
  void should_empty_fieldLocation_not_equals_null() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(EMPTY__VALUE);

    // WHEN & THEN
    then(fieldLocation.equals(null)).isEqualTo(false);
  }

  @Test
  void should_fieldLocation_not_equals_empty() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation.equals(null)).isEqualTo(false);
  }

  @Test
  void should_string_not_equals_fieldLocation() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation.equals(EMPTY__VALUE)).isEqualTo(false);
  }

  @Test
  void should_fieldLocation_equals_fieldLocation_with_same_value() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);
    FieldLocation other = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);

    // WHEN & THEN
    then(fieldLocation.equals(other)).isEqualTo(true);
  }

  @Test
  void should_fieldLocation_equals_fieldLocation_with_empty_value() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(EMPTY__VALUE);
    FieldLocation other = FieldLocation.fieldLocation(EMPTY__VALUE);

    // WHEN & THEN
    then(fieldLocation.equals(other)).isEqualTo(true);
  }

  @Test
  void should_fieldLocation_not_equals_fieldLocation_with_diff_value() {
    // GIVEN
    FieldLocation fieldLocation = FieldLocation.fieldLocation(FIELD_LOCATION_VALUE);
    FieldLocation other = FieldLocation.fieldLocation(FIELD_LOCATION_OTHER_VALUE);

    // WHEN & THEN
    then(fieldLocation.equals(other)).isEqualTo(false);
  }
}
