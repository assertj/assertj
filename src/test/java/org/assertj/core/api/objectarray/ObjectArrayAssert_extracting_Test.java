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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithNamesOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorForElementFieldsWithTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.comparatorsByTypeOf;
import static org.assertj.core.api.GroupAssertTestHelper.firstNameFunction;
import static org.assertj.core.api.GroupAssertTestHelper.lastNameFunction;
import static org.assertj.core.api.GroupAssertTestHelper.throwingFirstNameExtractor;
import static org.assertj.core.data.TolkienCharacter.Race.DWARF;
import static org.assertj.core.data.TolkienCharacter.Race.ELF;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.data.TolkienCharacter.Race.MAIA;
import static org.assertj.core.data.TolkienCharacter.Race.MAN;
import static org.assertj.core.extractor.Extractors.byName;
import static org.assertj.core.presentation.UnicodeRepresentation.UNICODE_REPRESENTATION;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TIMESTAMP;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TUPLE;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Arrays.array;

import java.sql.Timestamp;
import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.api.iterable.ThrowingExtractor;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.groups.Tuple;
import org.assertj.core.test.Employee;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Tests for <code>{@link AbstractIterableAssert#extracting(String)}</code>.
 * 
 * @author Joel Costigliola
 * @author Mateusz Haligowski
 */
public class ObjectArrayAssert_extracting_Test {

  private Employee yoda;
  private Employee luke;
  private Employee[] jedis;
  private TolkienCharacter[] fellowshipOfTheRing;

  @Rule
  public ExpectedException thrown = none();

  @Before
  public void setUpOnce() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    jedis = array(yoda, luke);
    fellowshipOfTheRing = new TolkienCharacter[8];
    fellowshipOfTheRing[0] = TolkienCharacter.of("Frodo", 33, HOBBIT);
    fellowshipOfTheRing[1] = TolkienCharacter.of("Sam", 38, HOBBIT);
    fellowshipOfTheRing[2] = TolkienCharacter.of("Gandalf", 2020, MAIA);
    fellowshipOfTheRing[3] = TolkienCharacter.of("Legolas", 1000, ELF);
    fellowshipOfTheRing[4] = TolkienCharacter.of("Pippin", 28, HOBBIT);
    fellowshipOfTheRing[5] = TolkienCharacter.of("Gimli", 139, DWARF);
    fellowshipOfTheRing[6] = TolkienCharacter.of("Aragorn", 87, MAN);
    fellowshipOfTheRing[7] = TolkienCharacter.of("Boromir", 37, MAN);
  }

  private static final ThrowingExtractor<Employee, Object, Exception> THROWING_EXTRACTOR = employee -> {
    if (employee.getAge() < 20) throw new Exception("age < 20");
    return employee.getName().getFirst();
  };

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable() {
    assertThat(jedis).extracting("age").containsOnly(800, 26);
  }

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable_with_extracted_type_defined() {
    assertThat(jedis).extracting("name", Name.class).containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_allow_assertions_on_field_values_extracted_from_given_iterable() {
    // basic types
    assertThat(jedis).extracting("id").containsOnly(1L, 2L);
    // object
    assertThat(jedis).extracting("name").containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
    // nested property
    assertThat(jedis).extracting("name.first").containsOnly("Yoda", "Luke");
  }

  @Test
  public void should_throw_error_if_no_property_nor_field_with_given_name_can_be_extracted() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> assertThat(jedis).extracting("unknown"));
  }

  @Test
  public void should_allow_assertions_on_multiple_extracted_values_from_given_iterable() {
    assertThat(jedis).extracting("name.first", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                         tuple("Luke", 26, 2L));
  }

  @Test
  public void should_throw_error_if_one_property_or_field_can_not_be_extracted() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> {
      assertThat(jedis).extracting("unknown", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                        tuple("Luke", 26, 2L));
    });
  }

  @Test
  public void should_allow_assertions_on_extractor_assertions_extracted_from_given_array_compatibility() {
    assertThat(jedis).extracting(input -> input.getName().getFirst()).containsOnly("Yoda", "Luke");
  }

  @Test
  public void should_allow_assertions_on_extractor_assertions_extracted_from_given_array_compatibility_RuntimeException() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(jedis).extracting(input -> {
      if (input.getAge() > 100) throw new RuntimeException("age > 100");
      return input.getName().getFirst();
    }));
  }

  @Test
  public void should_allow_assertions_on_extractor_assertions_extracted_from_given_array() {
    assertThat(jedis).extracting(input -> input.getName().getFirst()).containsOnly("Yoda", "Luke");
  }

  @Test
  public void should_rethrow_throwing_extractor_checked_exception_as_a_runtime_exception() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(jedis).extracting(employee -> {
      if (employee.getAge() > 100) throw new Exception("age > 100");
      return employee.getName().getFirst();
    })).withMessage("java.lang.Exception: age > 100");
  }

  @Test
  public void should_let_throwing_extractor_runtime_exception_bubble_up() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(jedis).extracting(employee -> {
      if (employee.getAge() > 100) throw new RuntimeException("age > 100");
      return employee.getName().getFirst();
    })).withMessage("age > 100");
  }

  @Test
  public void should_allow_extracting_with_throwing_extractor() {
    assertThat(jedis).extracting(THROWING_EXTRACTOR).containsOnly("Yoda", "Luke");
  }

  @Test
  public void should_allow_extracting_with_anonymous_class_throwing_extractor() {
    assertThat(jedis).extracting(new ThrowingExtractor<Employee, Object, Exception>() {
      @Override
      public Object extractThrows(Employee employee) throws Exception {
        if (employee.getAge() < 20) throw new Exception("age < 20");
        return employee.getName().getFirst();
      }
    }).containsOnly("Yoda", "Luke");
  }

  @Test
  public void should_allow_assertions_on_two_extracted_values_from_given_iterable_by_using_a_function() {

    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
                                               TolkienCharacter::getAge)
                                   .containsOnly(tuple("Frodo", 33),
                                                 tuple("Sam", 38),
                                                 tuple("Gandalf", 2020),
                                                 tuple("Legolas", 1000),
                                                 tuple("Pippin", 28),
                                                 tuple("Gimli", 139),
                                                 tuple("Aragorn", 87),
                                                 tuple("Boromir", 37));
  }

  @Test
  public void should_allow_assertions_on_three_extracted_values_from_given_iterable_by_using_a_function() {

    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
                                               TolkienCharacter::getAge,
                                               TolkienCharacter::getRace)
                                   .containsOnly(tuple("Frodo", 33, HOBBIT),
                                                 tuple("Sam", 38, HOBBIT),
                                                 tuple("Gandalf", 2020, MAIA),
                                                 tuple("Legolas", 1000, ELF),
                                                 tuple("Pippin", 28, HOBBIT),
                                                 tuple("Gimli", 139, DWARF),
                                                 tuple("Aragorn", 87, MAN),
                                                 tuple("Boromir", 37, MAN));
  }

  @Test
  public void should_allow_assertions_on_four_extracted_values_from_given_iterable_by_using_a_function() {

    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
                                               TolkienCharacter::getAge,
                                               TolkienCharacter::getRace,
                                               character -> character.name)
                                   .containsOnly(tuple("Frodo", 33, HOBBIT, "Frodo"),
                                                 tuple("Sam", 38, HOBBIT, "Sam"),
                                                 tuple("Gandalf", 2020, MAIA, "Gandalf"),
                                                 tuple("Legolas", 1000, ELF, "Legolas"),
                                                 tuple("Pippin", 28, HOBBIT, "Pippin"),
                                                 tuple("Gimli", 139, DWARF, "Gimli"),
                                                 tuple("Aragorn", 87, MAN, "Aragorn"),
                                                 tuple("Boromir", 37, MAN, "Boromir"));
  }

  @Test
  public void should_allow_assertions_on_five_extracted_values_from_given_iterable_by_using_a_function() {

    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
                                               TolkienCharacter::getAge,
                                               TolkienCharacter::getRace,
                                               character -> character.name,
                                               character -> character.age)
                                   .containsOnly(tuple("Frodo", 33, HOBBIT, "Frodo", 33),
                                                 tuple("Sam", 38, HOBBIT, "Sam", 38),
                                                 tuple("Gandalf", 2020, MAIA, "Gandalf", 2020),
                                                 tuple("Legolas", 1000, ELF, "Legolas", 1000),
                                                 tuple("Pippin", 28, HOBBIT, "Pippin", 28),
                                                 tuple("Gimli", 139, DWARF, "Gimli", 139),
                                                 tuple("Aragorn", 87, MAN, "Aragorn", 87),
                                                 tuple("Boromir", 37, MAN, "Boromir", 37));
  }

  @Test
  public void should_allow_assertions_on_more_than_five_extracted_values_from_given_iterable_by_using_a_function() {

    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
                                               TolkienCharacter::getAge,
                                               TolkienCharacter::getRace,
                                               character -> character.name,
                                               character -> character.age,
                                               character -> character.race)
                                   .containsOnly(tuple("Frodo", 33, HOBBIT, "Frodo", 33, HOBBIT),
                                                 tuple("Sam", 38, HOBBIT, "Sam", 38, HOBBIT),
                                                 tuple("Gandalf", 2020, MAIA, "Gandalf", 2020, MAIA),
                                                 tuple("Legolas", 1000, ELF, "Legolas", 1000, ELF),
                                                 tuple("Pippin", 28, HOBBIT, "Pippin", 28, HOBBIT),
                                                 tuple("Gimli", 139, DWARF, "Gimli", 139, DWARF),
                                                 tuple("Aragorn", 87, MAN, "Aragorn", 87, MAN),
                                                 tuple("Boromir", 37, MAN, "Boromir", 37, MAN));
  }

  // https://github.com/joel-costigliola/assertj-core/issues/880
  @Test
  public void should_be_able_to_extract_values_returned_from_default_methods_from_given_iterable_elements() {
    List<Person> people = asList(new Person());

    assertThat(people).extracting("name").containsOnly("John Doe");
  }

  public static class Person implements DefaultName {
  }

  public static interface DefaultName {
    default String getName() {
      return "John Doe";
    }
  }

  @Test
  public void should_use_property_field_names_as_description_when_extracting_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extracting("name.first").isEmpty()).withMessageContaining("[Extracted: name.first]");
  }

  @Test
  public void should_use_property_field_names_as_description_when_extracting_typed_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extracting("name.first", String.class).isEmpty()).withMessageContaining("[Extracted: name.first]");
  }

  @Test
  public void should_use_property_field_names_as_description_when_extracting_tuples_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extracting("name.first", "name.last").isEmpty()).withMessageContaining("[Extracted: name.first, name.last]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_typed_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees first name").extracting("name.first", String.class).isEmpty()).withMessageContaining("[check employees first name]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_tuples_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees name").extracting("name.first", "name.last").isEmpty()).withMessageContaining("[check employees name]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees first name").extracting("name.first").isEmpty()).withMessageContaining("[check employees first name]");
  }

  @Test
  public void should_keep_existing_description_if_set_when_extracting_using_extractor() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees first name").extracting(input -> input.getName().getFirst()).isEmpty()).withMessageContaining("[check employees first name]");
  }

  public void should_keep_existing_description_if_set_when_extracting_using_throwing_extractor() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("expected exception").extracting(THROWING_EXTRACTOR).isEmpty()).withMessageContaining("[expected exception]");
  }

  @Test
  public void extracting_by_several_functions_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_TUPLE, Tuple.class)
                                                                .extracting(firstNameFunction, lastNameFunction)
                                                                .contains(tuple("YODA", null), tuple("Luke", "Skywalker"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(Tuple.class)).isSameAs(ALWAY_EQUALS_TUPLE);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_name_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting("name.first")
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_strongly_typed_name_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting("name.first", String.class)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_multiple_names_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_TUPLE, Tuple.class)
                                                                .extracting("name.first", "name.last")
                                                                .contains(tuple("YODA", null), tuple("Luke", "Skywalker"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(Tuple.class)).isSameAs(ALWAY_EQUALS_TUPLE);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_single_extractor_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting(byName("name.first"))
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

  @Test
  public void extracting_by_throwing_extractor_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAY_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .usingComparatorForType(ALWAY_EQUALS_STRING, String.class)
                                                                .extracting(throwingFirstNameExtractor)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).get(String.class)).isSameAs(ALWAY_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).get(Timestamp.class)).isSameAs(ALWAY_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAY_EQUALS_STRING);
  }

}
