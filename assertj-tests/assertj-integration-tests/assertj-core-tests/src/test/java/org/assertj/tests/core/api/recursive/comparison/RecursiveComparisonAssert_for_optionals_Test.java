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

import java.util.Optional;

import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDto;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_for_optionals_Test implements PersonData {

  // verify we don't need to cast actual to an Object as before when only Object assertions provided usingRecursiveComparison()
  @Test
  void should_be_directly_usable_with_maps() {
    // GIVEN
    Optional<Person> person = Optional.of(new Person("Sheldon"));
    Optional<PersonDto> personDto = Optional.of(new PersonDto("Sheldon"));
    // WHEN/THEN
    then(person).usingRecursiveComparison()
                .isEqualTo(personDto);
  }

}
