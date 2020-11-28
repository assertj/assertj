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

import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.Maps.mapOf;

import java.util.Map;

import org.assertj.core.internal.objects.data.PersonDto;
import org.assertj.core.test.Person;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_for_maps_Test {

  @Test
  // verify we don't need to cast actual to an Object as before when only Object assertions provided usingRecursiveComparison()
  void should_be_directly_usable_with_maps() {
    // GIVEN
    Person sheldon = new Person("Sheldon");
    Person leonard = new Person("Leonard");
    Person raj = new Person("Rajesh");

    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");
    PersonDto rajDto = new PersonDto("Rajesh");

    Map<String, Person> actual = mapOf(entry(sheldon.getName(), sheldon),
                                       entry(leonard.getName(), leonard),
                                       entry(raj.getName(), raj));
    Map<String, PersonDto> expected = mapOf(entry(sheldonDto.name, sheldonDto),
                                            entry(leonardDto.name, leonardDto),
                                            entry(rajDto.name, rajDto));
    // WHEN/THEN no need to cast actual to an Object as before (since only object assertions provided usingRecursiveComparison()
    then(actual).usingRecursiveComparison()
                .ignoringAllOverriddenEquals()
                .isEqualTo(expected);
  }

}
