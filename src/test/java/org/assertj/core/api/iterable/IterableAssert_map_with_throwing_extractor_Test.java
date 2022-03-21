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
package org.assertj.core.api.iterable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.assertj.core.util.FailureMessages.actualIsNull;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.data.TolkienCharacter.Race;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IterableAssert_map_with_throwing_extractor_Test {

  private final List<TolkienCharacter> hobbits = new ArrayList<>();
  private final ThrowingExtractor<TolkienCharacter, String, Exception> nameThrowingExtractor = TolkienCharacter::getName;
  private final ThrowingExtractor<TolkienCharacter, Race, Exception> raceThrowingExtractor = TolkienCharacter::getRace;

  @BeforeEach
  void setUp() {
    hobbits.add(TolkienCharacter.of("Frodo", 33, HOBBIT));
    hobbits.add(TolkienCharacter.of("Sam", 38, HOBBIT));
    hobbits.add(TolkienCharacter.of("Pippin", 28, HOBBIT));
  }

  @Test
  void should_allow_assertions_on_values_extracted_by_throwing_extractor() {
    then(hobbits).map(nameThrowingExtractor)
                 .containsExactly("Frodo", "Sam", "Pippin");
  }

  @Test
  void should_allow_assertions_on_tuple_values_extracted_by_given_throwing_extractors() {
    then(hobbits).map(nameThrowingExtractor, raceThrowingExtractor)
                 .containsExactly(tuple("Frodo", HOBBIT),
                                  tuple("Sam", HOBBIT),
                                  tuple("Pippin", HOBBIT));
  }

  @Test
  void should_throw_assertion_error_if_actual_is_null() {
    // GIVEN
    List<TolkienCharacter> elves = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(elves).map(nameThrowingExtractor));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

  @Test
  void should_throw_assertion_error_if_actual_is_null_when_passing_multiple_throwing_extractors() {
    // GIVEN
    List<TolkienCharacter> elves = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(elves).map(nameThrowingExtractor,
                                                                                     raceThrowingExtractor));
    // THEN
    then(assertionError).hasMessage(actualIsNull());
  }

}
