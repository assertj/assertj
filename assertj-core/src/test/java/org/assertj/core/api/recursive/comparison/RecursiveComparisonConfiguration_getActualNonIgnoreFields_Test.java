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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.Date;
import java.util.Set;

import org.assertj.core.internal.objects.data.Person;
import org.assertj.core.internal.objects.data.PersonDtoWithPersonNeighbour;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_getActualFieldNamesToCompare_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_compute_ignored_fields() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(".*umber");
    recursiveComparisonConfiguration.ignoreFields("people.name");
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(Date.class);
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    person1.dateOfBirth = new Date(123);
    person1.neighbour = new Person("Jack");
    person1.neighbour.home.address.number = 123;
    Person person2 = new Person("John");
    person2.home.address.number = 1;
    person2.dateOfBirth = new Date(123);
    person2.neighbour = new Person("Jim");
    person2.neighbour.home.address.number = 456;
    DualValue dualValue = new DualValue(list("people"), person1, person2);
    // WHEN
    Set<String> fields = recursiveComparisonConfiguration.getActualFieldNamesToCompare(dualValue);
    // THEN
    then(fields).doesNotContain("number", "dateOfBirth", "name");
  }

  @Test
  void should_compute_ignored_fields_honoring_comparingOnly_fields() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields("dateOfBirth", "home.address");
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    person1.dateOfBirth = new Date(123);
    person1.neighbour = new Person("Jack");
    person1.neighbour.home.address.number = 123;
    Person person2 = new Person("John");
    person2.home.address.number = 1;
    person2.dateOfBirth = new Date(123);
    person2.neighbour = new Person("Jim");
    person2.neighbour.home.address.number = 456;
    DualValue dualValue = new DualValue(list(), person1, person2);
    // WHEN
    Set<String> fields = recursiveComparisonConfiguration.getActualFieldNamesToCompare(dualValue);
    // THEN
    // "home.address" is not present since getActualFieldNamesToCompare look at the direct fields
    // it registers "home" because we need to compare "home.address",
    // getActualFieldNamesToCompare will return "address" when inspecting "home"
    then(fields).containsOnly("dateOfBirth", "home");
  }

  @Test
  void should_only_return_fields_from_compareOnlyFields_list() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields("people.name");
    Person person1 = new Person("John");
    PersonDtoWithPersonNeighbour person2 = new PersonDtoWithPersonNeighbour("John");
    DualValue dualValue = new DualValue(list("people"), person2, person1);
    // WHEN
    Set<String> fields = recursiveComparisonConfiguration.getActualFieldNamesToCompare(dualValue);
    // THEN
    then(fields).containsExactly("name");
  }
}
