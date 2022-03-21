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

import static java.util.Comparator.comparing;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.setRemoveAssertJRelatedElementsFromStackTrace;
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
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_TIMESTAMP;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAYS_EQUALS_TUPLE;
import static org.assertj.core.util.Lists.list;

import java.sql.Timestamp;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;

import org.assertj.core.api.AbstractListAssert;
import org.assertj.core.data.TolkienCharacter;
import org.assertj.core.extractor.Extractors;
import org.assertj.core.groups.Tuple;
import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.assertj.core.util.introspection.IntrospectionError;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class IterableAssert_extracting_with_SortedSet_Test {

  private Employee yoda;
  private Employee luke;
  private SortedSet<Employee> jedis;
  private SortedSet<TolkienCharacter> fellowshipOfTheRing;

  @SuppressWarnings("deprecation")
  private static final Extractor<Employee, String> firstName = new Extractor<Employee, String>() {
    @Override
    public String extract(Employee input) {
      return input.getName().getFirst();
    }
  };

  private static final Function<Employee, Integer> age = Employee::getAge;

  private static final ThrowingExtractor<Employee, Object, Exception> throwingExtractor = new ThrowingExtractor<Employee, Object, Exception>() {
    @Override
    public Object extractThrows(Employee employee) throws Exception {
      if (employee.getAge() < 20) throw new Exception("age < 20");
      return employee.getName().getFirst();
    }
  };

  @BeforeEach
  void setUp() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    jedis = new TreeSet<>(comparing(Employee::getAge));
    jedis.add(luke);
    jedis.add(yoda);

    fellowshipOfTheRing = new TreeSet<>(comparing(TolkienCharacter::getName));
    fellowshipOfTheRing.add(TolkienCharacter.of("Frodo", 33, HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Sam", 38, HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Gandalf", 2020, MAIA));
    fellowshipOfTheRing.add(TolkienCharacter.of("Legolas", 1000, ELF));
    fellowshipOfTheRing.add(TolkienCharacter.of("Pippin", 28, HOBBIT));
    fellowshipOfTheRing.add(TolkienCharacter.of("Gimli", 139, DWARF));
    fellowshipOfTheRing.add(TolkienCharacter.of("Aragorn", 87, MAN));
    fellowshipOfTheRing.add(TolkienCharacter.of("Boromir", 37, MAN));
    setRemoveAssertJRelatedElementsFromStackTrace(false);
  }

  @Test
  void should_allow_assertions_on_property_values_extracted_from_given_iterable() {
    assertThat(jedis).extracting("age")
                     .as("extract property backed by a private field")
                     .containsOnly(800, 26);
    assertThat(jedis).extracting("adult")
                     .as("extract pure property")
                     .containsOnly(true, true);
    assertThat(jedis).extracting("name.first")
                     .as("nested property")
                     .containsOnly("Yoda", "Luke");
    assertThat(jedis).extracting("name")
                     .as("extract field that is also a property")
                     .containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
    assertThat(jedis).extracting("name", Name.class)
                     .as("extract field that is also a property but specifying the extracted type")
                     .containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  void should_allow_assertions_on_null_property_values_extracted_from_given_iterable() {
    yoda.name.setFirst(null);
    assertThat(jedis).extracting("name.first")
                     .as("not null property but null nested property")
                     .containsOnly(null, "Luke");
    yoda.setName(null);
    assertThat(jedis).extracting("name.first")
                     .as("extract nested property when top property is null")
                     .containsOnly(null, "Luke");
    assertThat(jedis).extracting("name")
                     .as("null property")
                     .containsOnly(null, new Name("Luke", "Skywalker"));
  }

  @Test
  void should_allow_assertions_on_field_values_extracted_from_given_iterable() {
    assertThat(jedis).extracting("id")
                     .as("extract field")
                     .containsOnly(1L, 2L);
    assertThat(jedis).extracting("surname")
                     .as("null field")
                     .containsNull();
    assertThat(jedis).extracting("surname.first")
                     .as("null nested field")
                     .containsNull();
    yoda.surname = new Name();
    assertThat(jedis).extracting("surname.first")
                     .as("not null field but null nested field")
                     .containsNull();
    yoda.surname = new Name("Master");
    assertThat(jedis).extracting("surname.first")
                     .as("nested field")
                     .containsOnly("Master", null);
    assertThat(jedis).extracting("surname", Name.class)
                     .as("extract field specifying the extracted type")
                     .containsOnly(new Name("Master"), null);
  }

  @Test
  void should_allow_assertions_on_property_values_extracted_from_given_iterable_with_extracted_type_defined() {
    // extract field that is also a property and check generic for comparator.
    assertThat(jedis).extracting("name", Name.class)
                     .usingElementComparator((o1, o2) -> o1.getFirst().compareTo(o2.getFirst()))
                     .containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  void should_throw_error_if_no_property_nor_field_with_given_name_can_be_extracted() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> assertThat(jedis).extracting("unknown"));
  }

  @Test
  void should_allow_assertions_on_multiple_extracted_values_from_given_iterable() {
    assertThat(jedis).extracting("name.first", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                         tuple("Luke", 26, 2L));
  }

  @Test
  void should_throw_error_if_one_property_or_field_can_not_be_extracted() {
    assertThatExceptionOfType(IntrospectionError.class).isThrownBy(() -> {
      assertThat(jedis).extracting("unknown", "age", "id")
                       .containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
    });
  }

  @Test
  void should_allow_extracting_single_values_using_extractor() {
    assertThat(jedis).extracting(firstName).containsOnly("Yoda", "Luke");
    assertThat(jedis).extracting(age).containsOnly(26, 800);
  }

  @SuppressWarnings("deprecation")
  @Test
  void should_allow_assertions_on_extractor_assertions_extracted_from_given_array_compatibility_runtimeexception() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(jedis).extracting(new Extractor<Employee, String>() {
      @Override
      public String extract(Employee input) {
        if (input.getAge() > 100) {
          throw new RuntimeException("age > 100");
        }
        return input.getName().getFirst();
      }
    }));
  }

  @Test
  void should_allow_assertions_on_extractor_assertions_extracted_from_given_array() {
    assertThat(jedis).extracting(input -> input.getName().getFirst()).containsOnly("Yoda", "Luke");
  }

  @Test
  void should_rethrow_throwing_extractor_checked_exception_as_a_runtime_exception() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(jedis).extracting(employee -> {
      if (employee.getAge() > 100) throw new Exception("age > 100");
      return employee.getName().getFirst();
    })).withMessage("java.lang.Exception: age > 100");
  }

  @Test
  void should_let_throwing_extractor_runtime_exception_bubble_up() {
    assertThatExceptionOfType(RuntimeException.class).isThrownBy(() -> assertThat(jedis).extracting(employee -> {
      if (employee.getAge() > 100) throw new RuntimeException("age > 100");
      return employee.getName().getFirst();
    })).withMessage("age > 100");
  }

  @Test
  void should_allow_extracting_with_throwing_extractor() {
    assertThat(jedis).extracting(employee -> {
      if (employee.getAge() < 20) throw new Exception("age < 20");
      return employee.getName().getFirst();
    }).containsOnly("Yoda", "Luke");
  }

  @Test
  void should_allow_extracting_with_anonymous_class_throwing_extractor() {
    assertThat(jedis).extracting(new ThrowingExtractor<Employee, Object, Exception>() {
      @Override
      public Object extractThrows(Employee employee) throws Exception {
        if (employee.getAge() < 20) throw new Exception("age < 20");
        return employee.getName().getFirst();
      }
    }).containsOnly("Yoda", "Luke");
  }

  @SuppressWarnings("deprecation")
  @Test
  void should_allow_extracting_multiple_values_using_extractor() {
    assertThat(jedis).extracting(new Extractor<Employee, Tuple>() {
      @Override
      public Tuple extract(Employee input) {
        return new Tuple(input.getName().getFirst(), input.getAge(), input.id);
      }
    }).containsOnly(tuple("Yoda", 800, 1L), tuple("Luke", 26, 2L));
  }

  @Test
  void should_allow_extracting_by_toString_method() {
    assertThat(jedis).extracting(Extractors.toStringMethod())
                     .containsOnly("Employee[id=1, name=Name[first='Yoda', last='null'], age=800]",
                                   "Employee[id=2, name=Name[first='Luke', last='Skywalker'], age=26]");
  }

  @Test
  void should_allow_assertions_by_using_function_extracted_from_given_iterable() {
    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName)
                                   .contains("Boromir", "Gandalf", "Frodo")
                                   .doesNotContain("Sauron", "Elrond");
  }

  @Test
  void should_throw_error_if_function_fails() {
    RuntimeException thrown = new RuntimeException();
    assertThatThrownBy(() -> assertThat(fellowshipOfTheRing).extracting(e -> {
      throw thrown;
    }).isEqualTo(thrown));
  }

  @Test
  void should_allow_assertions_on_two_extracted_values_from_given_iterable_by_using_a_function() {

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
  void should_allow_assertions_on_three_extracted_values_from_given_iterable_by_using_a_function() {

    assertThat(fellowshipOfTheRing).extracting(TolkienCharacter::getName,
                                               TolkienCharacter::getAge,
                                               TolkienCharacter::getRace)
                                   .containsOnly(tuple("Frodo", 33, TolkienCharacter.Race.HOBBIT),
                                                 tuple("Sam", 38, HOBBIT),
                                                 tuple("Gandalf", 2020, MAIA),
                                                 tuple("Legolas", 1000, ELF),
                                                 tuple("Pippin", 28, HOBBIT),
                                                 tuple("Gimli", 139, DWARF),
                                                 tuple("Aragorn", 87, MAN),
                                                 tuple("Boromir", 37, MAN));
  }

  @Test
  void should_allow_assertions_on_four_extracted_values_from_given_iterable_by_using_a_function() {

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
  void should_allow_assertions_on_five_extracted_values_from_given_iterable_by_using_a_function() {

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
  void should_allow_assertions_on_more_than_five_extracted_values_from_given_iterable_by_using_a_function() {

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

  @Test
  void should_use_property_field_names_as_description_when_extracting_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extracting("name.first").isEmpty())
                                                   .withMessageContaining("[Extracted: name.first]");
  }

  @Test
  void should_use_property_field_names_as_description_when_extracting_typed_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extracting("name.first", String.class)
                                                                                      .isEmpty())
                                                   .withMessageContaining("[Extracted: name.first]");
  }

  @Test
  void should_use_property_field_names_as_description_when_extracting_tuples_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).extracting("name.first",
                                                                                                  "name.last")
                                                                                      .isEmpty())
                                                   .withMessageContaining("[Extracted: name.first, name.last]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_typed_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees first name")
                                                                                      .extracting("name.first",
                                                                                                  String.class)
                                                                                      .isEmpty())
                                                   .withMessageContaining("[check employees first name]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_tuples_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees name")
                                                                                      .extracting("name.first",
                                                                                                  "name.last")
                                                                                      .isEmpty())
                                                   .withMessageContaining("[check employees name]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_simple_value_list() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees first name")
                                                                                      .extracting("name.first")
                                                                                      .isEmpty())
                                                   .withMessageContaining("[check employees first name]");
  }

  @SuppressWarnings("deprecation")
  @Test
  void should_keep_existing_description_if_set_when_extracting_using_extractor() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("check employees first name")
                                                                                      .extracting(new Extractor<Employee, String>() {
                                                                                        @Override
                                                                                        public String extract(Employee input) {
                                                                                          return input.getName()
                                                                                                      .getFirst();
                                                                                        }
                                                                                      }).isEmpty())
                                                   .withMessageContaining("[check employees first name]");
  }

  @Test
  void should_keep_existing_description_if_set_when_extracting_using_throwing_extractor() {
    assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> assertThat(jedis).as("expected exception")
                                                                                      .extracting(throwingExtractor)
                                                                                      .containsOnly("Luke"))
                                                   .withMessageContaining("[expected exception]");
  }

  @Test
  void should_extract_tuples_according_to_given_functions() {
    assertThat(jedis).extracting(firstNameFunction, lastNameFunction)
                     .contains(tuple("Yoda", null), tuple("Luke", "Skywalker"));
  }

  @Test
  void extracting_by_several_functions_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extracting(firstNameFunction, lastNameFunction)
                                                                .usingComparatorForType(ALWAYS_EQUALS_TUPLE, Tuple.class)
                                                                .contains(tuple("YODA", null), tuple("Luke", "Skywalker"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).getComparatorForType(Tuple.class)).isSameAs(ALWAYS_EQUALS_TUPLE);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class)).isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }

  @Test
  void extracting_by_name_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extracting("name.first")
                                                                .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).getComparatorForType(String.class)).isSameAs(ALWAYS_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class)).isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }

  @Test
  void extracting_by_strongly_typed_name_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extracting("name.first", String.class)
                                                                .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).getComparatorForType(String.class)).isSameAs(ALWAYS_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class)).isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }

  @Test
  void extracting_by_multiple_names_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extracting("name.first", "name.last")
                                                                .usingComparatorForType(ALWAYS_EQUALS_TUPLE, Tuple.class)
                                                                .contains(tuple("YODA", null), tuple("Luke", "Skywalker"));
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).getComparatorForType(Tuple.class)).isSameAs(ALWAYS_EQUALS_TUPLE);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class)).isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }

  @Test
  void extracting_by_single_extractor_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extracting(byName("name.first"))
                                                                .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).getComparatorForType(String.class)).isSameAs(ALWAYS_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class)).isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }

  @Test
  void extracting_by_throwing_extractor_should_keep_assertion_state() {
    // WHEN
    // not all comparators are used but we want to test that they are passed correctly after extracting
    AbstractListAssert<?, ?, ?, ?> assertion = assertThat(jedis).as("test description")
                                                                .withFailMessage("error message")
                                                                .withRepresentation(UNICODE_REPRESENTATION)
                                                                .usingComparatorForElementFieldsWithNames(ALWAYS_EQUALS_STRING,
                                                                                                          "foo")
                                                                .usingComparatorForElementFieldsWithType(ALWAYS_EQUALS_TIMESTAMP,
                                                                                                         Timestamp.class)
                                                                .extracting(throwingFirstNameExtractor)
                                                                .usingComparatorForType(ALWAYS_EQUALS_STRING, String.class)
                                                                .contains("YODA", "Luke");
    // THEN
    assertThat(assertion.descriptionText()).isEqualTo("test description");
    assertThat(assertion.info.representation()).isEqualTo(UNICODE_REPRESENTATION);
    assertThat(assertion.info.overridingErrorMessage()).isEqualTo("error message");
    assertThat(comparatorsByTypeOf(assertion).getComparatorForType(String.class)).isSameAs(ALWAYS_EQUALS_STRING);
    assertThat(comparatorForElementFieldsWithTypeOf(assertion).getComparatorForType(Timestamp.class)).isSameAs(ALWAYS_EQUALS_TIMESTAMP);
    assertThat(comparatorForElementFieldsWithNamesOf(assertion).get("foo")).isSameAs(ALWAYS_EQUALS_STRING);
  }

  @Test
  void extracting_should_not_propagate_the_sorted_set_element_comparator() {
    // GIVEN
    SortedSet<Data> sortedSet = new TreeSet<>(comparing(Data::getValue));
    sortedSet.add(new Data("1"));
    sortedSet.add(new Data("2"));
    sortedSet.add(new Data("3"));
    // THEN
    assertThat(sortedSet).extracting(Data::getValue)
                         .containsOnly("1", "2", "3");
  }

  @Test
  void extracting_should_fail_it_the_propagated_element_comparator_is_incompatible() {
    // GIVEN
    Iterable<Data> list = list(new Data("1"), new Data("2"), new Data("3"));
    // WHEN
    Throwable error = catchThrowable(() -> assertThat(list).usingElementComparator(comparing(Data::getValue))
                                                           .extracting(Data::getValue)
                                                           .containsOnly("1", "2", "3"));
    // THEN
    assertThat(error).isInstanceOf(ClassCastException.class);
  }

  private static class Data {
    private final String value;

    private Data(String value) {
      this.value = value;
    }

    private String getValue() {
      return value;
    }
  }

}
