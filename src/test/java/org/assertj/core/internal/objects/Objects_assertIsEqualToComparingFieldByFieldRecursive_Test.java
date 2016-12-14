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
 * Copyright 2012-2016 the original author or authors.
 */
package org.assertj.core.internal.objects;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.error.ShouldBeEqualByComparingFieldByFieldRecursively.shouldBeEqualByComparingFieldByFieldRecursive;
import static org.assertj.core.test.TestData.someInfo;
import static org.assertj.core.test.TestFailures.failBecauseExpectedAssertionErrorWasNotThrown;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.assertj.core.api.AssertionInfo;
import org.assertj.core.internal.AtPrecisionComparator;
import org.assertj.core.internal.DeepDifference.Difference;
import org.assertj.core.internal.ObjectsBaseTest;
import org.junit.Test;

public class Objects_assertIsEqualToComparingFieldByFieldRecursive_Test extends ObjectsBaseTest {

  @Test
  public void should_be_able_to_compare_objects_recursively() {
    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;

    Person other = new Person();
    other.name = "John";
    other.home.address.number = 1;

    objects.assertIsEqualToComparingFieldByFieldRecursively(someInfo(), actual, other, noFieldComparators(),
                                                            defaultTypeComparators());
  }

  @Test
  public void should_be_able_to_compare_objects_of_different_types_recursively() {
    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;

    Human other = new Human();
    other.name = "John";
    other.home.address.number = 1;

    objects.assertIsEqualToComparingFieldByFieldRecursively(someInfo(), actual, other, noFieldComparators(),
                                                            defaultTypeComparators());
  }

  @Test
  public void should_be_able_to_compare_objects_recursively_using_some_precision_for_numerical_types() {
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;

    assertThat(goliath).usingComparatorForType(new AtPrecisionComparator<Double>(0.2), Double.class)
                       .isEqualToComparingFieldByFieldRecursively(goliathTwin);
  }

  @Test
  public void should_be_able_to_compare_objects_recursively_using_given_comparator_for_specified_field() {
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;

    assertThat(goliath).usingComparatorForFields(new AtPrecisionComparator<Double>(0.2), "height")
                       .isEqualToComparingFieldByFieldRecursively(goliathTwin);
  }

  @Test
  public void should_be_able_to_compare_objects_recursively_using_given_comparator_for_specified_nested_field() {
    Giant goliath = new Giant();
    goliath.name = "Goliath";
    goliath.height = 3.0;
    goliath.home.address.number = 1;

    Giant goliathTwin = new Giant();
    goliathTwin.name = "Goliath";
    goliathTwin.height = 3.1;
    goliathTwin.home.address.number = 5;

    assertThat(goliath).usingComparatorForFields(new AtPrecisionComparator<Double>(0.2), "height")
                       .usingComparatorForFields(new AtPrecisionComparator<Integer>(10), "home.address.number")
                       .isEqualToComparingFieldByFieldRecursively(goliathTwin);
  }

  @Test
  public void should_be_able_to_compare_objects_with_cycles_recursively() {
    FriendlyPerson actual = new FriendlyPerson();
    actual.name = "John";
    actual.home.address.number = 1;

    FriendlyPerson other = new FriendlyPerson();
    other.name = "John";
    other.home.address.number = 1;

    // neighbour
    other.neighbour = actual;
    actual.neighbour = other;

    // friends
    FriendlyPerson sherlock = new FriendlyPerson();
    sherlock.name = "Sherlock";
    sherlock.home.address.number = 221;
    actual.friends.add(sherlock);
    actual.friends.add(other);
    other.friends.add(sherlock);
    other.friends.add(actual);

    objects.assertIsEqualToComparingFieldByFieldRecursively(someInfo(), actual, other, noFieldComparators(),
                                                            defaultTypeComparators());
  }

  @Test
  public void should_fail_when_fields_differ() {
    AssertionInfo info = someInfo();

    Person actual = new Person();
    actual.name = "John";

    Person other = new Person();
    other.name = "Jack";

    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                              defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info, shouldBeEqualByComparingFieldByFieldRecursive(actual, other,
                                                                                   asList(new Difference(asList("name"),
                                                                                                         "John",
                                                                                                         "Jack"))));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_fail_when_fields_of_child_objects_differ() {
    AssertionInfo info = someInfo();

    Person actual = new Person();
    actual.name = "John";
    actual.home.address.number = 1;

    Person other = new Person();
    other.name = "John";
    other.home.address.number = 2;

    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                              defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info,
                               shouldBeEqualByComparingFieldByFieldRecursive(actual, other,
                                                                             asList(new Difference(asList("home.address.number"),
                                                                                                   "1",
                                                                                                   "2"))));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  @Test
  public void should_have_error_message_with_differences_and_path_to_differences() {
    Person actual = new Person();
    actual.name = "Jack";
    actual.home.address.number = 1;

    Person other = new Person();
    other.name = "John";
    other.home.address.number = 2;

    thrown.expectAssertionError(format("Expecting:%n  <%s>%nto be equal to:%n  <%s>%n", actual, other) +
                                "when recursively comparing field by field, but found the following difference(s):%n%n" +
                                "Path to difference:  <home.address.number>%n" +
                                "- expected: <2>%n" +
                                "- actual  : <1>%n%n" +
                                "Path to difference:  <name>%n" +
                                "- expected: <John>%n" +
                                "- actual  : <Jack>");

    assertThat(actual).isEqualToComparingFieldByFieldRecursively(other);
  }

  @Test
  public void should_have_error_message_with_path_to_difference_when_difference_is_in_collection() {
    FriendlyPerson actual = new FriendlyPerson();
    FriendlyPerson friendOfActual = new FriendlyPerson();
    friendOfActual.home.address.number = 99;
    actual.friends = Arrays.asList(friendOfActual);

    FriendlyPerson other = new FriendlyPerson();
    FriendlyPerson friendOfOther = new FriendlyPerson();
    friendOfOther.home.address.number = 10;
    other.friends = Arrays.asList(friendOfOther);

    thrown.expectAssertionError(format("Expecting:%n  <%s>%nto be equal to:%n  <%s>%n", actual, other) +
                                "when recursively comparing field by field, but found the following difference(s):%n%n" +
                                "Path to difference:  <friends.home.address.number>%n" +
                                "- expected: <10>%n" +
                                "- actual  : <99>");

    assertThat(actual).isEqualToComparingFieldByFieldRecursively(other);
  }

  @Test
  public void should_not_use_equal_implemention_of_objects_to_compare() {
    AssertionInfo info = someInfo();

    EqualPerson actual = new EqualPerson();
    actual.name = "John";
    actual.home.address.number = 1;

    EqualPerson other = new EqualPerson();
    other.name = "John";
    other.home.address.number = 2;

    try {
      objects.assertIsEqualToComparingFieldByFieldRecursively(info, actual, other, noFieldComparators(),
                                                              defaultTypeComparators());
    } catch (AssertionError err) {
      verify(failures).failure(info,
                               shouldBeEqualByComparingFieldByFieldRecursive(actual, other,
                                                                             asList(new Difference(asList("home.address.number"),
                                                                                                   "1",
                                                                                                   "2"))));
      return;
    }
    failBecauseExpectedAssertionErrorWasNotThrown();
  }

  public static class Person {
    public String name;
    public Home home = new Home();
    public Person neighbour;

    public String toString() {
      return "Person [name=" + name + ", home=" + home + "]";
    }
  }

  public static class Home {
    public Address address = new Address();

    public String toString() {
      return "Home [address=" + address + "]";
    }
  }

  public static class Address {
    public int number = 1;

    public String toString() {
      return "Address [number=" + number + "]";
    }
  }

  public static class Human extends Person {
  }

  public static class Giant extends Person {
    public double height = 3.0;
  }

  public static class EqualPerson extends Person {

    public boolean equals(Object o) {
      return true;
    }
  }

  public static class FriendlyPerson extends Person {
    public List<FriendlyPerson> friends = new ArrayList<>();
  }
}
