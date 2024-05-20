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
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.RecursiveComparator;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.api.recursive.data.Human;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparator_compare_Test {

  @ParameterizedTest(name = "{2}: actual={0} / expected={1}")
  @MethodSource("recursivelyEqualObjects")
  void should_return_zero_for_objects_equal_recursively(Object actual, Object expected) {
    // GIVEN
    RecursiveComparator recursiveComparator = new RecursiveComparator(new RecursiveComparisonConfiguration());
    // WHEN
    int compareResult = recursiveComparator.compare(actual, expected);
    // THEN
    then(compareResult).isZero();
  }

  private static Stream<Arguments> recursivelyEqualObjects() {
    Person person1 = new Person("John");
    person1.home.address.number = 1;
    Person person2 = new Person("John");
    person2.home.address.number = 1;

    Person person3 = new Person("John");
    person3.home.address.number = 1;
    Human person4 = new Human();
    person4.name = "John";
    person4.home.address.number = 1;

    return Stream.of(arguments(null, null, "null instances"),
                     arguments(person1, person1, "same objects"),
                     arguments(person1, person2, "same data, same type"),
                     arguments(person2, person1, "same data, same type reversed"),
                     arguments(person3, person4, "same data, different type"),
                     arguments(person4, person3, "same data, different type"));
  }

  @Test
  void should_return_non_zero_for_objects_different_recursively() {
    // GIVEN
    RecursiveComparator recursiveComparator = new RecursiveComparator(new RecursiveComparisonConfiguration());
    Person actual = new Person("jack");
    Person other = new Person("john");
    // WHEN
    int compareResult1 = recursiveComparator.compare(actual, other);
    int compareResult2 = recursiveComparator.compare(other, actual);
    // THEN
    then(compareResult1).isNotZero();
    then(compareResult2).isNotZero();
  }

  @Test
  void should_return_non_zero_when_one_object_is_null_and_the_other_is_not() {
    // GIVEN
    RecursiveComparator recursiveComparator = new RecursiveComparator(new RecursiveComparisonConfiguration());
    Person actual = new Person("jack");
    // WHEN
    int compareResult1 = recursiveComparator.compare(actual, null);
    int compareResult2 = recursiveComparator.compare(null, actual);
    // THEN
    then(compareResult1).isNotZero();
    then(compareResult2).isNotZero();
  }

}
