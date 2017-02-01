/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class IterableAssert_flatExtracting_with_multiple_extractors_Test {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private final List<TolkienCharacter> fellowshipOfTheRing = new ArrayList<>();

  @Before
  public void setUp() {
    fellowshipOfTheRing.add(TolkienCharacter.of("Frodo", 33, TolkienCharacter.Race.HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Sam", 38, TolkienCharacter.Race.HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Gandalf", 2020, TolkienCharacter.Race.MAIA));
    fellowshipOfTheRing.add(TolkienCharacter.of("Legolas", 1000, TolkienCharacter.Race.ELF));
    fellowshipOfTheRing.add(TolkienCharacter.of("Pippin", 28, TolkienCharacter.Race.HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Gimli", 139, TolkienCharacter.Race.DWARF));
    fellowshipOfTheRing.add(TolkienCharacter.of("Aragorn", 87, TolkienCharacter.Race.MAN));
    fellowshipOfTheRing.add(TolkienCharacter.of("Boromir", 37, TolkienCharacter.Race.MAN));
  }

  @Test
  public void should_allow_assertions_on_multiple_extracted_values_flattened_in_a_single_list() {
    assertThat(fellowshipOfTheRing).flatExtracting("age", "name")
                                   .as("extract ages and names")
                                   .containsSequence(33, "Frodo", 38, "Sam");
  }

  @Test
  public void should_throw_IllegalArgumentException_when_no_fields_or_properties_are_specified() {
    thrown.expect(IllegalArgumentException.class);
    assertThat(fellowshipOfTheRing).flatExtracting();
  }

  @Test
  public void should_throw_IllegalArgumentException_when_null_fields_or_properties_vararg() {
    thrown.expect(IllegalArgumentException.class);
    String[] fields = null;
    assertThat(fellowshipOfTheRing).flatExtracting(fields);
  }
  
  @Test
  public void should_throw_IllegalArgumentException_when_extracting_from_null() {
    thrown.expect(IllegalArgumentException.class);
    fellowshipOfTheRing.add(null);
    assertThat(fellowshipOfTheRing).flatExtracting("age", "name");
  }
}
