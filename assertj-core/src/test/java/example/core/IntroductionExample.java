/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

// tag::import[]
// entry point for all assertThat methods and utility methods (e.g. entry)
import static org.assertj.core.api.Assertions.*;
// end::import[]

import org.junit.jupiter.api.Test;

import example.data.TolkienCharacter;

// not run by the build (class name does not match *Test)
class IntroductionExample extends AbstractAssertionsExamples {

  @Test
  void a_few_examples() {
    // tag::user_guide[]
    // basic assertions
    assertThat(frodo.getName()).isEqualTo("Frodo");
    assertThat(frodo).isNotEqualTo(sauron);

    // chaining string specific assertions
    assertThat(frodo.getName()).startsWith("Fro")
                               .endsWith("do")
                               .isEqualToIgnoringCase("frodo");

    // collection specific assertions (there are plenty more)
    // in the examples below fellowshipOfTheRing is a List<TolkienCharacter>
    assertThat(fellowshipOfTheRing).hasSize(9)
                                   .contains(frodo, sam)
                                   .doesNotContain(sauron);

    // as() is used to describe the test and will be shown before the error message
    assertThat(frodo.getAge()).as("check %s's age", frodo.getName()).isEqualTo(33);

    // exception assertion, standard style ...
    assertThatThrownBy(() -> { throw new Exception("boom!"); }).hasMessage("boom!");
    // ... or BDD style
    Throwable thrown = catchThrowable(() -> { throw new Exception("boom!"); });
    assertThat(thrown).hasMessageContaining("boom");

    // using the 'extracting' feature to check fellowshipOfTheRing character's names
    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName)
                                   .doesNotContain("Sauron", "Elrond");

    // extracting multiple values at once grouped in tuples
    assertThat(fellowshipOfTheRing).extracting("name", "age", "race.name")
                                   .contains(tuple("Boromir", 37, "Man"),
                                             tuple("Sam", 38, "Hobbit"),
                                             tuple("Legolas", 1000, "Elf"));

    // filtering a collection before asserting
    assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
                                   .containsOnly(aragorn, frodo, legolas, boromir);

    // combining filtering and extraction (yes we can)
    assertThat(fellowshipOfTheRing).filteredOn(character -> character.getName().contains("o"))
                                   .containsOnly(aragorn, frodo, legolas, boromir)
                                   .extracting(character -> character.getRace().getName())
                                   .contains("Hobbit", "Elf", "Man");

    // and many more assertions: iterable, stream, array, map, dates, path, file, numbers, predicate, optional ...
    // end::user_guide[]
  }
}
// @formatter:on
