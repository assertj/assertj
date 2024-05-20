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
package org.assertj.tests.core.api.recursive.comparison;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.recursive.comparison.FieldLocation.rootFieldLocation;
import static org.assertj.core.util.Lists.list;

import java.time.Duration;
import java.util.Date;
import java.util.Set;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDtoWithPersonNeighbour;
import org.assertj.tests.core.testkit.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.base.Stopwatch;

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
    Set<String> fields = recursiveComparisonConfiguration.getActualChildrenNodeNamesToCompare(dualValue);
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
    Set<String> fields = recursiveComparisonConfiguration.getActualChildrenNodeNamesToCompare(dualValue);
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
    Set<String> fields = recursiveComparisonConfiguration.getActualChildrenNodeNamesToCompare(dualValue);
    // THEN
    then(fields).containsExactly("name");
  }

  @Test
  void should_return_fields_in_a_reasonable_amount_of_time_for_deeply_nested_object() {
    // GIVEN
    Person p1 = new Person("Alice");
    Person p2 = new Person("Brian");
    Person p3 = new Person("Christina");
    Person p4 = new Person("David");
    Person p5 = new Person("Emily");
    Person p6 = new Person("Francisco");
    Person p7 = new Person("Gabriella");
    Person p8 = new Person("Henry");
    Person p9 = new Person("Isabelle");
    Person p10 = new Person("Jackson");
    Person p11 = new Person("Kimberly");
    Person p12 = new Person("Lucas");
    Person p13 = new Person("Melissa");
    Person p14 = new Person("Nathan");
    Person p15 = new Person("Olivia");
    Person p16 = new Person("Penelope");
    Person p17 = new Person("Quentin");
    Person p18 = new Person("Rebecca");
    Person p19 = new Person("Samuel");
    Person p20 = new Person("Tanya");
    p1.neighbour = p2;
    p2.neighbour = p3;
    p3.neighbour = p4;
    p4.neighbour = p5;
    p5.neighbour = p6;
    p6.neighbour = p7;
    p7.neighbour = p8;
    p8.neighbour = p9;
    p9.neighbour = p10;
    p10.neighbour = p11;
    p11.neighbour = p12;
    p12.neighbour = p13;
    p13.neighbour = p14;
    p14.neighbour = p15;
    p15.neighbour = p16;
    p16.neighbour = p17;
    p17.neighbour = p18;
    p18.neighbour = p19;
    p19.neighbour = p20;

    Person p1b = new Person("Anders");
    p1b.neighbour = p2;

    DualValue dualValue = new DualValue(rootFieldLocation(), p1, p2);
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(".*id");
    // WHEN
    Stopwatch stopwatch = Stopwatch.createStarted();
    Set<String> fields = recursiveComparisonConfiguration.getActualChildrenNodeNamesToCompare(dualValue);
    // THEN
    then(stopwatch.elapsed()).isLessThan(Duration.ofSeconds(10));
    then(fields).doesNotContain("id");
  }

  @Test
  void should_return_all_fields_when_some_compared_types_are_specified_as_a_value_not_to_compare_could_have_a_field_to_compare() {
    // GIVEN
    Company microsoft = new Company(new Person("Bill"));
    DualValue dualValue = new DualValue(rootFieldLocation(), microsoft, microsoft);
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(Person.class);
    recursiveComparisonConfiguration.compareOnlyFields("ceo");
    // WHEN
    Set<String> fields = recursiveComparisonConfiguration.getActualChildrenNodeNamesToCompare(dualValue);
    // THEN includes engineers in case any subfields of engineers is of a type to compare
    then(fields).containsOnly("ceo", "engineers");
  }

  static class Company {
    Person ceo;
    Employee[] engineers;

    public Company(Person ceo) {
      this.ceo = ceo;
    }
  }
}
