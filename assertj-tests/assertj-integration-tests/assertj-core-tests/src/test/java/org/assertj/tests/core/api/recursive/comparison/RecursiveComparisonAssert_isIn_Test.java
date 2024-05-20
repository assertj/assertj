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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;

import java.util.List;
import java.util.Objects;

import org.assertj.core.api.RecursiveComparisonAssert;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.recursive.comparison.RecursiveComparator;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.util.introspection.FieldSupport;
import org.assertj.tests.core.api.recursive.data.Person;
import org.junit.jupiter.api.Test;

// only check we are using the proper comparator since the implementation of isIn only switched comparator before calling
// the super call isIn method.
class RecursiveComparisonAssert_isIn_Test {

  @Test
  void should_use_recursive_comparator() {
    // GIVEN
    Person actual = new Person();
    // WHEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    RecursiveComparisonAssert<?> recursiveComparisonAssert = assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                               .isIn(actual);
    // THEN
    Object comparator = FieldSupport.extraction().fieldValue("objects.comparisonStrategy.comparator", Object.class,
                                                             recursiveComparisonAssert);
    then(comparator).isInstanceOf(RecursiveComparator.class);
    Object configuration = FieldSupport.extraction().fieldValue("recursiveComparisonConfiguration", Object.class, comparator);
    then(configuration).isSameAs(recursiveComparisonConfiguration);
  }

  // https://github.com/assertj/assertj/issues/2843

  static class PersonDto {
    private final Integer personId;

    private final List<String> personInfo;

    private final Integer age;

    public PersonDto(Integer personId, List<String> personInfo, Integer age) {
      this.personId = personId;
      this.personInfo = personInfo;
      this.age = age;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PersonDto personDto = (PersonDto) o;
      return Objects.equals(personId, personDto.personId) && Objects.equals(personInfo, personDto.personInfo)
             && Objects.equals(age, personDto.age);
    }

    @Override
    public int hashCode() {
      return Objects.hash(personId, personInfo, age);
    }
  }

  @Test
  void should_succeed_running_issue_2843_test_case() {
    // GIVEN
    PersonDto person1 = new PersonDto(10, list("John"), 25);
    PersonDto person2 = new PersonDto(20, list("Ben"), 30);
    PersonDto person3 = new PersonDto(30, list("Rick", "Sanchez"), 35);
    // Here is personInfo has the same value as in person3, but in another order
    PersonDto person4 = new PersonDto(30, list("Sanchez", "Rick"), 35);

    List<PersonDto> list1 = list(person1, person2, person3);
    List<PersonDto> list2 = list(person1, person2, person4);

    SoftAssertions softAssertions = new SoftAssertions();

    // WHEN/THEN
    list2.forEach(person -> softAssertions.assertThat(person)
                                          .usingRecursiveComparison()
                                          .ignoringCollectionOrder()
                                          .isIn(list1));
    softAssertions.assertAll();
  }

}
