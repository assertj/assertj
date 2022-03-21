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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithNamesOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorsByTypeOf;
import static org.assertj.core.data.TolkienCharacter.Race.DWARF;
import static org.assertj.core.data.TolkienCharacter.Race.ELF;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.data.TolkienCharacter.Race.MAIA;
import static org.assertj.core.data.TolkienCharacter.Race.MAN;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_TIMESTAMP;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.util.CaseInsensitiveStringComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IterableAssert_flatExtracting_with_multiple_extractors_Test {

  private final List<TolkienCharacter> fellowshipOfTheRing = new ArrayList<>();

  @SuppressWarnings("deprecation")
  private static final Extractor<TolkienCharacter, String> name = TolkienCharacter::getName;
  private static final Function<TolkienCharacter, Integer> age = TolkienCharacter::getAge;
  private static final ThrowingExtractor<TolkienCharacter, String, Exception> nameThrowingExtractor = TolkienCharacter::getName;
  private static final ThrowingExtractor<TolkienCharacter, Integer, Exception> ageThrowingExtractor = TolkienCharacter::getAge;

  @BeforeEach
  void setUp() {
    fellowshipOfTheRing.add(TolkienCharacter.of("Frodo", 33, HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Sam", 38, HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Gandalf", 2020, MAIA));
    fellowshipOfTheRing.add(TolkienCharacter.of("Legolas", 1000, ELF));
    fellowshipOfTheRing.add(TolkienCharacter.of("Pippin", 28, HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Gimli", 139, DWARF));
    fellowshipOfTheRing.add(TolkienCharacter.of("Aragorn", 87, MAN));
    fellowshipOfTheRing.add(TolkienCharacter.of("Boromir", 37, MAN));
  }

  @Test
  void should_allow_assertions_on_multiple_extracted_values_flattened_in_a_single_list() {
    assertThat(fellowshipOfTheRing).flatExtracting("age", "name")
                                   .as("extract ages and names")
                                   .containsSequence(33, "Frodo", 38, "Sam");

    assertThat(fellowshipOfTheRing).flatExtracting(age, name)
                                   .as("extract ages and names")
                                   .contains(33, "Frodo", 38, "Sam");
  }

  @Test
  void should_throw_assertion_error_if_actual_is_null() {
    // GIVEN
    List<TolkienCharacter> hobbits = null;
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(hobbits).flatExtracting(age, name));
    // THEN
    then(assertionError).hasMessageContaining("Expecting actual not to be null");
  }

  @Test
  void should_throw_IllegalArgumentException_when_no_fields_or_properties_are_specified() {
    // GIVEN
    String[] fields = new String[0];
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(fellowshipOfTheRing).flatExtracting(fields));
  }

  @Test
  void should_throw_IllegalArgumentException_when_null_fields_or_properties_vararg() {
    // GIVEN
    String[] fields = null;
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(fellowshipOfTheRing).flatExtracting(fields));
  }

  @Test
  void should_throw_IllegalArgumentException_when_extracting_from_null() {
    // GIVEN
    fellowshipOfTheRing.add(null);
    // WHEN/THEN
    assertThatIllegalArgumentException().isThrownBy(() -> assertThat(fellowshipOfTheRing).flatExtracting("age", "name"));
  }

  @Test
  void should_throw_IllegalArgumentException_when_extracting_from_null_extractors() {
    // GIVEN
    fellowshipOfTheRing.add(null);
    // WHEN/THEN
    assertThatNullPointerException().isThrownBy(() -> assertThat(fellowshipOfTheRing).flatExtracting(age, name));
  }

  @Test
  void should_allow_assertions_on_multiple_extracted_values_using_throwing_extractor() {
    assertThat(fellowshipOfTheRing).flatExtracting(input -> {
      if (input.getAge() < 20) throw new Exception("age < 20");
      return input.getName();
    }, input2 -> {
      if (input2.getAge() < 20) throw new Exception("age < 20");
      return input2.getAge();
    }).contains(33, "Frodo", 38, "Sam");
  }

  @SuppressWarnings("deprecation")
  @Test
  void flatExtracting_with_multiple_extractors_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    // @format:off
    AbstractListAssert<?, ?, ?, ?> assertion
      = assertThat(fellowshipOfTheRing).as("test description")
      .withFailMessage("error message")
      .withRepresentation(UNICODE_REPRESENTATION)
      .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING, "foo")
      .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP, Timestamp.class)
      .usingComparatorForType(CaseInsensitiveStringComparator.instance, String.class)
      .flatExtracting(age, name)
      .contains(33, "frodo", 38, "SAM");
    // @format:on
    // THEN
    then(assertion.descriptionText()).isEqualTo("test description");
    then(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    then(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    then(comparatorsByTypeOf(assertion).getComparatorForType(String.class))
                                                                           .isSameAs(CaseInsensitiveStringComparator.instance);
    then(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class))
                                                                                               .isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    then(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }

  @SuppressWarnings("deprecation")
  @Test
  void flatExtracting_with_multiple_ThrowingExtractors_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    // @format:off
    AbstractListAssert<?, ?, ?, ?> assertion
      = assertThat(fellowshipOfTheRing).as("test description")
      .withFailMessage("error message")
      .withRepresentation(UNICODE_REPRESENTATION)
      .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING, "foo")
      .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP, Timestamp.class)
      .usingComparatorForType(CaseInsensitiveStringComparator.instance, String.class)
      .flatExtracting(ageThrowingExtractor, nameThrowingExtractor)
      .contains(33, "frodo", 38, "SAM");
    // @format:on
    // THEN
    then(assertion.descriptionText()).isEqualTo("test description");
    then(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    then(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    then(comparatorsByTypeOf(assertion).getComparatorForType(String.class))
                                                                           .isSameAs(CaseInsensitiveStringComparator.instance);
    then(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class))
                                                                                               .isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    then(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }
}
