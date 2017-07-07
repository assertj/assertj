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

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.assertj.core.api.ConcreteIterableAssert;
import org.assertj.core.api.IterableAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.IterableElementComparisonStrategy;
import org.assertj.core.internal.Iterables;
import org.assertj.core.test.Jedi;
import org.junit.Before;
import org.junit.Test;

public class IterableAssert_usingFieldByFieldElementComparator_Test extends IterableAssertBaseTest {

  private Iterables iterablesBefore;

  @Before
  public void before() {
    iterablesBefore = getIterables(assertions);
  }

  @Override
  protected ConcreteIterableAssert<Object> invoke_api_method() {
    return assertions.usingFieldByFieldElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(iterablesBefore).isNotSameAs(getIterables(assertions));
    assertThat(getIterables(assertions).getComparisonStrategy()).isInstanceOf(ComparatorBasedComparisonStrategy.class);
    assertThat(getObjects(assertions).getComparisonStrategy()).isInstanceOf(IterableElementComparisonStrategy.class);
  }

  @Test
  public void successful_isEqualTo_assertion_using_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", 1));
    List<Foo> list2 = singletonList(new Foo("id", 1));
    assertThat(list1).usingFieldByFieldElementComparator().isEqualTo(list2);
  }

  @Test
  public void successful_isIn_assertion_using_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", 1));
    List<Foo> list2 = singletonList(new Foo("id", 1));
    assertThat(list1).usingFieldByFieldElementComparator().isIn(singletonList(list2));
  }

  @Test
  public void successful_isEqualTo_assertion_using_field_by_field_element_comparator_with_heterogeneous_list() {
    List<Animal> list1 = newArrayList(new Bird("White"), new Snake(15));
    List<Animal> list2 = newArrayList(new Bird("White"), new Snake(15));
    assertThat(list1).usingFieldByFieldElementComparator().isEqualTo(list2);
  }

  @Test
  public void successful_contains_assertion_using_field_by_field_element_comparator_with_heterogeneous_list() {
    List<Animal> list1 = newArrayList(new Bird("White"), new Snake(15));
    assertThat(list1).usingFieldByFieldElementComparator()
                     .contains(new Snake(15), new Bird("White"))
                     .contains(new Bird("White"), new Snake(15));
    assertThat(list1).usingFieldByFieldElementComparator()
                     .containsOnly(new Snake(15), new Bird("White"))
                     .containsOnly(new Bird("White"), new Snake(15));
  }

  @Test
  public void successful_isIn_assertion_using_field_by_field_element_comparator_with_heterogeneous_list() {
    List<Animal> list1 = newArrayList(new Bird("White"), new Snake(15));
    List<Animal> list2 = newArrayList(new Bird("White"), new Snake(15));
    assertThat(list1).usingFieldByFieldElementComparator().isIn(singletonList(list2));
  }

  @Test
  public void successful_containsExactly_assertion_using_field_by_field_element_comparator_with_heterogeneous_list() {
    List<Animal> list1 = newArrayList(new Bird("White"), new Snake(15));
    assertThat(list1).usingFieldByFieldElementComparator().containsExactly(new Bird("White"), new Snake(15));
  }

  @Test
  public void successful_containsExactlyInAnyOrder_assertion_using_field_by_field_element_comparator_with_heterogeneous_list() {
    Snake snake = new Snake(15);
    List<Animal> list1 = newArrayList(new Bird("White"), snake, snake);
    assertThat(list1).usingFieldByFieldElementComparator().containsExactlyInAnyOrder(new Snake(15), new Bird("White"),
                                                                                     new Snake(15));
  }

  @Test
  public void successful_containsExactlyInAnyOrderElementsOf_assertion_using_field_by_field_element_comparator_with_heterogeneous_list() {
    Snake snake = new Snake(15);
    List<Animal> list1 = newArrayList(new Bird("White"), snake, snake);
    assertThat(list1).usingFieldByFieldElementComparator().containsExactlyInAnyOrderElementsOf(
      newArrayList(new Snake(15), new Bird("White"), new Snake(15)));
  }

  @Test
  public void successful_containsOnly_assertion_using_field_by_field_element_comparator_with_unordered_list() {
    Person goodObiwan = new Person("Obi-Wan", "Kenobi", "good man");
    Person badObiwan = new Person("Obi-Wan", "Kenobi", "bad man");

    List<Person> list = asList(goodObiwan, badObiwan);
    assertThat(list).usingFieldByFieldElementComparator().containsOnly(badObiwan, goodObiwan);
  }

  @Test
  public void failed_isEqualTo_assertion_using_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", 1));
    List<Foo> list2 = singletonList(new Foo("id", 2));

    thrown.expectAssertionError("%nExpecting:%n" +
                                " <[Foo(id=id, bar=1)]>%n" +
                                "to be equal to:%n" +
                                " <[Foo(id=id, bar=2)]>%n" +
                                "when comparing elements using 'field/property by field/property comparator on all fields/properties' but was not.");

    assertThat(list1).usingFieldByFieldElementComparator().isEqualTo(list2);

  }

  @Test
  public void failed_isIn_assertion_using_field_by_field_element_comparator() {
    List<Foo> list1 = singletonList(new Foo("id", 1));
    List<Foo> list2 = singletonList(new Foo("id", 2));

    thrown.expectAssertionError("%nExpecting:%n" +
                                " <[Foo(id=id, bar=1)]>%n" +
                                "to be in:%n" +
                                " <[[Foo(id=id, bar=2)]]>%n" +
                                "when comparing elements using 'field/property by field/property comparator on all fields/properties'");

    assertThat(list1).usingFieldByFieldElementComparator().isIn(singletonList(list2));
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_specified_fields_of_elements_when_using_field_by_field_element_comparator() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(singletonList(actual)).usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "name")
                                     .usingFieldByFieldElementComparator()
                                     .contains(other);
  }

  @Test
  public void comparators_for_element_field_names_should_have_precedence_over_comparators_for_element_field_types_when_using_field_by_field_element_comparator() {
    Comparator<String> comparator = new Comparator<String>() {
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    };
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "green");

    assertThat(singletonList(actual)).usingComparatorForElementFieldsWithNames(ALWAY_EQUALS_STRING, "name")
                                     .usingComparatorForElementFieldsWithType(comparator, String.class)
                                     .usingFieldByFieldElementComparator()
                                     .contains(other);
  }

  @Test
  public void should_be_able_to_use_a_comparator_for_element_fields_with_specified_type_when_using_field_by_field_element_comparator() {
    Jedi actual = new Jedi("Yoda", "green");
    Jedi other = new Jedi("Luke", "blue");

    assertThat(singletonList(actual)).usingComparatorForElementFieldsWithType(ALWAY_EQUALS_STRING, String.class)
                                     .usingFieldByFieldElementComparator()
                                     .contains(other);
  }

  public static class Foo {
    public final String id;
    public final int bar;

    public Foo(final String id, final int bar) {
      this.id = id;
      this.bar = bar;
    }

    @Override
    public String toString() {
      return "Foo(id=" + id + ", bar=" + bar + ")";
    }

  }

  private static class Animal {
    private final String name;

    private Animal(String name) {
      this.name = name;
    }

    @SuppressWarnings("unused")
    public String getName() {
      return name;
    }
  }

  private static class Bird extends Animal {
    private final String color;

    private Bird(String color) {
      super("Bird");
      this.color = color;
    }

    @SuppressWarnings("unused")
    public String getColor() {
      return color;
    }

    @Override
    public String toString() {
      return "Bird{" +
             "color='" + color + '\'' +
             '}';
    }
  }

  private static class Snake extends Animal {
    private final int length;

    private Snake(int length) {
      super("Snake");
      this.length = length;
    }

    @SuppressWarnings("unused")
    public int getLength() {
      return length;
    }

    @Override
    public String toString() {
      return "Snake{" +
             "length=" + length +
             '}';
    }
  }

  private class Person {
    private String first, last, info;

    public Person(String first, String last, String info) {
      this.first = first;
      this.last = last;
      this.info = info;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Person person = (Person) o;
      return Objects.equals(first, person.first) && Objects.equals(last, person.last);
    }

    @Override
    public int hashCode() {
      return Objects.hash(first, last);
    }

    @Override
    public String toString() {
      return String.format("Person{first='%s', last='%s', info='%s'}",
                           first, last, info);
    }
  }

}
