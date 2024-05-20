/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.assertj.core.api.recursive.comparison.FieldLocation;
import org.junit.jupiter.api.Test;

import com.google.common.base.Stopwatch;

import nl.jqno.equalsverifier.EqualsVerifier;

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
    FieldLocation fieldLocation1 = new FieldLocation(list("a"));
    FieldLocation fieldLocation2 = new FieldLocation(list("a.b"));
    FieldLocation fieldLocation3 = new FieldLocation(list("aaa"));
    FieldLocation fieldLocation4 = new FieldLocation(list("z"));
    List<FieldLocation> fieldLocations = list(fieldLocation2, fieldLocation1, fieldLocation3, fieldLocation4);
    Collections.shuffle(fieldLocations);
    // WHEN
    Collections.sort(fieldLocations);
    // THEN
    then(fieldLocations).containsExactly(fieldLocation1, fieldLocation2, fieldLocation3, fieldLocation4);
  }

  @Test
  void toString_should_succeed() {
    // GIVEN
    FieldLocation underTest = new FieldLocation(list("location"));
    // WHEN
    String result = underTest.toString();
    // THEN
    then(result).isEqualTo("<location>");
  }

  @Test
  void should_build_from_string_simple_path() {
    // WHEN
    FieldLocation underTest = new FieldLocation("name");
    // THEN
    then(underTest.getDecomposedPath()).isEqualTo(list("name"));
  }

  @Test
  void should_build_from_string_nested_path() {
    // WHEN
    FieldLocation underTest = new FieldLocation("name.first.second");
    // THEN
    then(underTest.getDecomposedPath()).isEqualTo(list("name", "first", "second"));
  }

  @Test
  void should_build_from_long_nested_path_in_reasonable_time() {
    // WHEN
    Stopwatch stopwatch = Stopwatch.createStarted();
    FieldLocation underTest = new FieldLocation("1.2.3.4.5.6.7.8.9.10");
    // THEN
    then(stopwatch.elapsed()).isLessThan(Duration.ofSeconds(10));
    then(underTest.getDecomposedPath()).isEqualTo(list("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
  }
}
