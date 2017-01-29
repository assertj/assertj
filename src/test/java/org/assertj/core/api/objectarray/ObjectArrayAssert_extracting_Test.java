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
package org.assertj.core.api.objectarray;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.data.TolkienCharacter.Race.DWARF;
import static org.assertj.core.data.TolkienCharacter.Race.ELF;
import static org.assertj.core.data.TolkienCharacter.Race.HOBBIT;
import static org.assertj.core.data.TolkienCharacter.Race.MAIA;
import static org.assertj.core.data.TolkienCharacter.Race.MAN;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.util.Arrays.array;

import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.AbstractIterableAssert;
import org.assertj.core.api.iterable.Extractor;
import org.assertj.core.data.TolkienCharacter;
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
  private Employee[] employees;
  private TolkienCharacter[] fellowshipOfTheRing;

  @Rule
  public ExpectedException thrown = none();

  @Before
  public void setUpOnce() {
    yoda = new Employee(1L, new Name("Yoda"), 800);
    luke = new Employee(2L, new Name("Luke", "Skywalker"), 26);
    employees = array(yoda, luke);
    fellowshipOfTheRing = new TolkienCharacter[8];
    fellowshipOfTheRing[0] = TolkienCharacter.of("Frodo", 33, HOBBIT);
    fellowshipOfTheRing[1] = TolkienCharacter.of("Sam", 38, HOBBIT);
    fellowshipOfTheRing[2] = TolkienCharacter.of("Gandalf", 2020, MAIA);
    fellowshipOfTheRing[3] = TolkienCharacter.of("Legolas", 1000, ELF);
    fellowshipOfTheRing[4] = TolkienCharacter.of("Pippin", 28, HOBBIT);
    fellowshipOfTheRing[5] = TolkienCharacter.of("Gimli", 139, DWARF);
    fellowshipOfTheRing[6] = TolkienCharacter.of("Aragorn", 87, MAN);
    fellowshipOfTheRing[7] = TolkienCharacter.of("Boromir", 37, MAN);
  };

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable() {
    assertThat(employees).extracting("age").containsOnly(800, 26);
  }

  @Test
  public void should_allow_assertions_on_property_values_extracted_from_given_iterable_with_extracted_type_defined()
       {
    assertThat(employees).extracting("name", Name.class).containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
  }

  @Test
  public void should_allow_assertions_on_field_values_extracted_from_given_iterable() {
    // basic types
    assertThat(employees).extracting("id").containsOnly(1L, 2L);
    // object
    assertThat(employees).extracting("name").containsOnly(new Name("Yoda"), new Name("Luke", "Skywalker"));
    // nested property
    assertThat(employees).extracting("name.first").containsOnly("Yoda", "Luke");
  }

  @Test
  public void should_throw_error_if_no_property_nor_field_with_given_name_can_be_extracted() {
    thrown.expect(IntrospectionError.class);
    assertThat(employees).extracting("unknown");
  }

  @Test
  public void should_allow_assertions_on_multiple_extracted_values_from_given_iterable() {
    assertThat(employees).extracting("name.first", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                             tuple("Luke", 26, 2L));
  }

  @Test
  public void should_throw_error_if_one_property_or_field_can_not_be_extracted() {
    thrown.expect(IntrospectionError.class);
    assertThat(employees).extracting("unknown", "age", "id").containsOnly(tuple("Yoda", 800, 1L),
                                                                          tuple("Luke", 26, 2L));
  }

  @Test
  public void should_allow_assertions_on_extractor_assertions_extracted_from_given_array() {
    assertThat(employees).extracting(new Extractor<Employee, String>() {
      @Override
      public String extract(Employee input) {
        return input.getName().getFirst();
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

  @Test //https://github.com/joel-costigliola/assertj-core/issues/880
  public void should_be_able_to_extract_values_returned_from_default_methods_from_given_iterable_elements() {
    List<Person> people = Arrays.asList(new Person());

    assertThat(people).extracting("name").containsOnly("John Doe");
  }

  public static class Person implements DefaultName {
  }

  public static interface DefaultName {
    default String getName() {
      return "John Doe";
    }
  }
}
