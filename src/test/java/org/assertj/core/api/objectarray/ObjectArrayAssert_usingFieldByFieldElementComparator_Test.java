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
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.core.api.objectarray;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.assertj.core.util.Arrays.array;

import java.util.Objects;

import org.assertj.core.api.ObjectArrayAssert;
import org.assertj.core.api.ObjectArrayAssertBaseTest;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.ObjectArrayElementComparisonStrategy;
import org.assertj.core.internal.ObjectArrays;
import org.junit.Before;
import org.junit.Test;

public class ObjectArrayAssert_usingFieldByFieldElementComparator_Test extends ObjectArrayAssertBaseTest {

  private ObjectArrays arraysBefore;

  @Before
  public void before() {
    arraysBefore = getArrays(assertions);
  }

  @Override
  protected ObjectArrayAssert<Object> invoke_api_method() {
    return assertions.usingFieldByFieldElementComparator();
  }

  @Override
  protected void verify_internal_effects() {
    assertThat(arraysBefore).isNotSameAs(getArrays(assertions));
    assertThat(getArrays(assertions).getComparisonStrategy() instanceof ComparatorBasedComparisonStrategy).isTrue();
    assertThat(getObjects(assertions).getComparisonStrategy() instanceof ObjectArrayElementComparisonStrategy).isTrue();
  }

  @Test
  public void successful_isEqualTo_assertion_using_field_by_field_element_comparator() {
    Foo[] array1 = array(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 1));
    assertThat(array1).usingFieldByFieldElementComparator().isEqualTo(array2);
  }

  @Test
  public void successful_isIn_assertion_using_field_by_field_element_comparator() {
    Foo[] array1 = array(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 1));
    assertThat(array1).usingFieldByFieldElementComparator().isIn(array2, array2);
  }

  @Test
  public void successful_isEqualTo_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    Animal[] array1 = array(new Bird("White"), new Snake(15));
    Animal[] array2 = array(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator().isEqualTo(array2);
  }

  @Test
  public void successful_contains_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    Animal[] array1 = array(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator()
                     .contains(new Snake(15), new Bird("White"))
                     .contains(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator()
                     .containsOnly(new Snake(15), new Bird("White"))
                     .containsOnly(new Bird("White"), new Snake(15));
  }

  @Test
  public void successful_isIn_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    Animal[] array1 = array(new Bird("White"), new Snake(15));
    Animal[] array2 = array(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator().isIn(array2, array2);
  }

  @Test
  public void successful_containsExactly_assertion_using_field_by_field_element_comparator_with_heterogeneous_array() {
    Animal[] array1 = array(new Bird("White"), new Snake(15));
    assertThat(array1).usingFieldByFieldElementComparator().containsExactly(new Bird("White"), new Snake(15));
  }

  @Test
  public void successful_containsOnly_assertion_using_field_by_field_element_comparator_with_unordered_array() {
    Person goodObiwan = new Person("Obi-Wan", "Kenobi", "good man");
    Person badObiwan = new Person("Obi-Wan", "Kenobi", "bad man");

    Person[] list = array(goodObiwan, badObiwan);
    assertThat(list).usingFieldByFieldElementComparator().containsOnly(badObiwan, goodObiwan);
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

  @Test
  public void failed_isEqualTo_assertion_using_field_by_field_element_comparator() {
    Foo[] array1 = array(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 2));
    try {
      assertThat(array1).usingFieldByFieldElementComparator().isEqualTo(array2);
    } catch (AssertionError e) {
      // @format:off
      assertThat(e).hasMessage(format("%nExpecting:%n" +
                                      " <[Foo(id=id, bar=1)]>%n" +
                                      "to be equal to:%n" +
                                      " <[Foo(id=id, bar=2)]>%n" +
                                      "when comparing elements using 'field by field comparator on all fields' but was not."));
      // @format:on
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void failed_isIn_assertion_using_field_by_field_element_comparator() {
    Foo[] array1 = array(new Foo("id", 1));
    Foo[] array2 = array(new Foo("id", 2));
    try {
      assertThat(array1).usingFieldByFieldElementComparator().isIn(array2, array2);
    } catch (AssertionError e) {
      assertThat(e).hasMessage(String.format("%nExpecting:%n" +
                                             " <[Foo(id=id, bar=1)]>%n" +
                                             "to be in:%n" +
                                             " <[[Foo(id=id, bar=2)], [Foo(id=id, bar=2)]]>%n" +
                                             "when comparing elements using 'field by field comparator on all fields'"));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
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
  }
}

