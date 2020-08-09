/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nl.jqno.equalsverifier.EqualsVerifier;

@SuppressWarnings("deprecation") // TODO to be removed once FieldLocation is package-private
class FieldLocation_Test {

  @Test
  void should_honor_equals_contract() {
    // WHEN/THEN
    EqualsVerifier.forClass(FieldLocation.class)
                  .verify();
  }

  @Test
  void compareTo_should_order_field_location_by_alphabetical_path() {
    // GIVEN
    FieldLocation fieldLocation1 = FieldLocation.fieldLocation("a");
    FieldLocation fieldLocation2 = FieldLocation.fieldLocation("a.b");
    FieldLocation fieldLocation3 = FieldLocation.fieldLocation("aaa");
    FieldLocation fieldLocation4 = FieldLocation.fieldLocation("z");
    List<FieldLocation> fieldLocations = list(fieldLocation2, fieldLocation1, fieldLocation3, fieldLocation4);
    Collections.shuffle(fieldLocations);
    // WHEN
    Collections.sort(fieldLocations);
    // THEN
    then(fieldLocations).containsExactly(fieldLocation1, fieldLocation2, fieldLocation3, fieldLocation4);
  }

  @Test
  void from_should_build_fieldLocations_from_given_strings() {
    // GIVEN
    String[] locations = { "foo", "bar", "foo.bar" };
    // WHEN
    List<FieldLocation> fieldLocations = FieldLocation.from(locations);
    // THEN
    then(fieldLocations).containsExactly(new FieldLocation("foo"),
                                         new FieldLocation("bar"),
                                         new FieldLocation("foo.bar"));
  }

  @ParameterizedTest(name = "{0} matches {1}")
  @CsvSource(value = {
      "name, name",
      "foo.bar, foo.bar",
  })
  void matches_should_match_fields(String location, String matchingFieldPath) {
    // GIVEN
    FieldLocation underTest = new FieldLocation(location);
    // WHEN
    boolean match = underTest.matches(matchingFieldPath);
    // THEN
    then(match).as("%s matches %s", underTest, matchingFieldPath).isTrue();
  }

  @Test
  void toString_should_succeed() {
    // GIVEN
    FieldLocation underTest = FieldLocation.fieldLocation("location");
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("FieldLocation [fieldPath=location]");
  }

}
