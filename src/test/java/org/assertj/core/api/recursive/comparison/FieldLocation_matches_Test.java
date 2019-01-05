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
 * Copyright 2012-2018 the original author or authors.
 */
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
