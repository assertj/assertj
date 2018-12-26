package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FieldLocation_matches_Test {

  @ParameterizedTest(name = "{0} matches {1}")
  @CsvSource(value = {
      "name, name",
      "foo.bar, foo.bar",
  })
  public void should_match_fields(String location, String matchingFieldPath) {
    // GIVEN
    FieldLocation fieldLocation = new FieldLocation(location);
    // WHEN
    boolean match = fieldLocation.matches(matchingFieldPath);
    // THEN
    assertThat(match).as("%s matches %s", fieldLocation, matchingFieldPath).isTrue();
  }

}
