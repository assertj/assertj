package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;

public class FieldLocation_from_Test {

  @Test
  public void should_build_fieldLocations_from_given_strings() {
    // GIVEN
    String[] locations = { "foo", "bar", "foo.bar" };
    // WHEN
    List<FieldLocation> fieldLocations = FieldLocation.from(locations);
    // THEN
    assertThat(fieldLocations).containsExactly(new FieldLocation("foo"),
                                               new FieldLocation("bar"),
                                               new FieldLocation("foo.bar"));
  }

}
