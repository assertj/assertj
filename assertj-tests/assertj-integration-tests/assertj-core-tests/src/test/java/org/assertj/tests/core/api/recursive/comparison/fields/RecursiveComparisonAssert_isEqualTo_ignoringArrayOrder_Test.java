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
package org.assertj.tests.core.api.recursive.comparison.fields;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.registerFormatterForType;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.tests.core.api.recursive.comparison.fields.RecursiveComparisonAssert_isEqualTo_ignoringArrayOrder_Test.FriendlyPerson.friend;
import static org.assertj.tests.core.api.recursive.comparison.fields.RecursiveComparisonAssert_isEqualTo_ignoringArrayOrder_Test.Type.FIRST;
import static org.assertj.tests.core.api.recursive.comparison.fields.RecursiveComparisonAssert_isEqualTo_ignoringArrayOrder_Test.Type.SECOND;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Objects;
import java.util.stream.Stream;

import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.WithObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_ignoringArrayOrder_Test extends WithComparingFieldsIntrospectionStrategyBaseTest {

  static class FriendlyPerson extends Person {
    public FriendlyPerson[] friends;

    public FriendlyPerson(String name, FriendlyPerson... friends) {
      super(name);
      this.friends = friends;
    }

    public static FriendlyPerson friend(String name) {
      return new FriendlyPerson(name);
    }

  }

  @ParameterizedTest(name = "{0}: actual={1} / expected={2}")
  @MethodSource
  @SuppressWarnings("unused")
  void should_pass_for_objects_with_the_same_data_when_array_order_is_ignored(String description,
                                                                              Object actual,
                                                                              Object expected) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringArrayOrder()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_for_objects_with_the_same_data_when_array_order_is_ignored() {
    var sherlock1 = new FriendlyPerson("Sherlock Holmes", friend("Dr. John Watson"), friend("Molly Hooper"));
    var sherlock1Clone = new FriendlyPerson("Sherlock Holmes", friend("Dr. John Watson"), friend("Molly Hooper"));
    var sherlock2 = new FriendlyPerson("Sherlock Holmes", friend("Molly Hooper"), friend("Dr. John Watson"));

    FriendlyPerson watson1 = new FriendlyPerson("Dr. John Watson", friend("D.I. Greg Lestrade"), friend("Mrs. Hudson"));
    FriendlyPerson watson2 = new FriendlyPerson("Dr. John Watson", friend("Mrs. Hudson"), friend("D.I. Greg Lestrade"));
    FriendlyPerson sherlock3 = new FriendlyPerson("Sherlock Holmes", watson1, friend("Molly Hooper"));
    FriendlyPerson sherlock4 = new FriendlyPerson("Sherlock Holmes", friend("Molly Hooper"), watson2);

    return Stream.of(arguments("same data", sherlock1, sherlock1Clone),
                     arguments("same data except friends order", sherlock1, sherlock2),
                     arguments("same data except friends order in subfield", sherlock3, sherlock4));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_collection_order_is_ignored() {
    // GIVEN
    var actual = new FriendlyPerson("Sherlock Holmes", friend("Dr. John Watson"), friend("Molly Hooper"));
    actual.home.address.number = 1;
    var expected = new FriendlyPerson("Sherlock Holmes", friend("Molly Hooper"), friend("Dr. John Watson"));
    expected.home.address.number = 2;
    // WHEN
    recursiveComparisonConfiguration.ignoreArrayOrder(true);
    // THEN
    compareRecursivelyFailsWithDifferences(actual, expected, javaTypeDiff("home.address.number", 1, 2));
  }

  /**
   * This test shows that we can't track all visited values, only the one with potential cycles.
   * <p>
   * Let's run it step by step with tracking all visited values:<br>
   * array(arrayA, arrayB) vs array(arrayAReverse, arrayBReverse) means trying to find<br>
   * - arrayA in array(arrayAReverse, arrayBReverse) and then arrayB
   * <p>
   * After comparing possible pairs (arrayA element, arrayAReverse element) we conclude that arrayA matches arrayAReverse<br>
   * - here are the pairs (1, 2), (1, 1), (2, 2), we add them to the visited ones<br>
   * <p>
   * We now try to find arrayB in array(arrayBReverse) - arrayAReverse must not be taken into account as it had already been matched<br>
   * - we would like to try (1, 2), (1, 1), (2, 2) but they have already been visited, so we skip them.<br>
   * at this point, we know arrayB won't be found because (1, 1), (2, 2) won't be considered.<br>
   * We compare dual values but not the location since to track cycles we want to find the same objects at different locations
   * <p>
   * Comparing dualValues actual and expected with == does not solve the issue because Java does not always create different objects
   * for primitive wrapping the same basic value, i.e. {@code new Integer(1) == new Integer(1)}.
   * <p>
   * The solution is to avoid adding all pairs to visited values. <br>
   * Visited values are here to track cycles, a pair of wrapped primitive types can't cycle back to itself, we thus can and must ignore them.
   * <p>
   * For good measure we don't track pair that include any java.lang values.
   * <p>
   * If arrayA and arrayB contained non wrapped basic types then == is enough to differentiate them.
   */
  @Test
  void should_fix_1854_minimal_test() {
    // GIVEN
    Integer[] arrayA = array(1, 2);
    Integer[] arrayB = array(1, 2);
    WithObject actual = new WithObject(array(arrayA, arrayB));
    WithObject actualClone = new WithObject(array(arrayA, arrayB));
    // Reversed arrays
    Integer[] reversedArrayA = array(2, 1);
    Integer[] reversedArrayB = array(2, 1);
    WithObject expected = new WithObject(array(reversedArrayA, reversedArrayB));
    // WHEN - THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .ignoringArrayOrder()
                .isEqualTo(expected)
                .isEqualTo(actualClone);
  }

  @Test
  void should_fix_1854_with_non_wrapped_basic_types() {
    // GIVEN
    FriendlyPerson p1 = friend("Sherlock Holmes");
    FriendlyPerson p2 = friend("Watson");
    FriendlyPerson p3 = friend("Sherlock Holmes");
    FriendlyPerson p4 = friend("Watson");
    FriendlyPerson[] arrayA = array(p1, p2);
    FriendlyPerson[] arrayB = array(p1, p2);
    WithObject actual = new WithObject(array(arrayA, arrayB));
    // Reversed lists
    FriendlyPerson[] reversedArrayA = array(p4, p3);
    FriendlyPerson[] reversedArrayB = array(p4, p3);
    WithObject expected = new WithObject(array(reversedArrayA, reversedArrayB));
    // WHEN - THEN
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringArrayOrder()
                      .isEqualTo(expected);
  }

  enum Type {
    FIRST, SECOND,
  }

  static class PersonWithEnum {
    String name;
    Type type;

    PersonWithEnum(String name, Type type) {
      this.name = name;
      this.type = type;
    }

    @Override
    public String toString() {
      return "Person [name=%s, type=%s]".formatted(name, type);
    }

  }

  static class PersonWithInt {
    String name;
    int type;

    PersonWithInt(String name, int type) {
      this.name = name;
      this.type = type;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PersonWithInt that = (PersonWithInt) o;
      return type == that.type && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name, type);
    }

    @Override
    public String toString() {
      return "Person [name=%s, type=%s]".formatted(name, type);
    }

  }

  @ParameterizedTest(name = "{0}: actual={1} / expected={2}")
  @MethodSource
  @SuppressWarnings("unused")
  public void should_not_remove_already_visited_dual_values_that_cant_produce_cycles(String description,
                                                                                     WithObject actual,
                                                                                     WithObject expected) {
    assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                      .ignoringArrayOrder()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_not_remove_already_visited_dual_values_that_cant_produce_cycles() {
    WithObject actualWithEnum = new WithObject(array(new PersonWithEnum("name-1", FIRST),
                                                     new PersonWithEnum("name-2", FIRST),
                                                     new PersonWithEnum("name-2", SECOND)));
    WithObject expectedWithEnum = new WithObject(array(new PersonWithEnum("name-2", SECOND),
                                                       new PersonWithEnum("name-2", FIRST),
                                                       new PersonWithEnum("name-1", FIRST)));
    WithObject actualWithInt = new WithObject(array(new PersonWithInt("name-1", 1),
                                                    new PersonWithInt("name-2", 1),
                                                    new PersonWithInt("name-2", 2)));
    WithObject expectedWithInt = new WithObject(array(new PersonWithInt("name-2", 2),
                                                      new PersonWithInt("name-2", 1),
                                                      new PersonWithInt("name-1", 1)));
    return Stream.of(arguments("with enums", actualWithEnum, expectedWithEnum),
                     arguments("with ints", actualWithInt, expectedWithInt));
  }

  // https://github.com/assertj/assertj/issues/2954 but for arrays

  static class DataStore {
    Data[] field1;
    Data[] field2;

    @Override
    public String toString() {
      return "DataStore[field1=%s, field2=%s]".formatted(field1, field2);
    }
  }

  record Data(String text) {
  }

  private static DataStore createDataStore(Data d1, Data d2) {
    DataStore dataStore = new DataStore();
    dataStore.field1 = array(d1, d2);
    dataStore.field2 = array(d1, d2);
    return dataStore;
  }

  @Test
  void evaluating_visited_dual_values_should_check_location() {
    // GIVEN
    Data d1 = new Data("111");
    Data d2 = new Data("222");
    DataStore dataStore1 = createDataStore(d1, d2);
    DataStore dataStore2 = createDataStore(d2, d1);
    // WHEN/THEN
    then(dataStore1).usingRecursiveComparison(recursiveComparisonConfiguration)
                    .withEqualsForType((data1, data2) -> data1.text.equals(data2.text), Data.class)
                    .ignoringArrayOrder()
                    .isEqualTo(dataStore2);
  }

  // related to https://github.com/assertj/assertj/issues/3598 but for arrays

  static class Outer {
    public Inner inner;

    public Outer(Inner inner) {
      this.inner = inner;
    }

    public String toString() {
      return "O" + inner;
    }
  }

  static class Inner {
    public int val;

    public Inner(int val) {
      this.val = val;
    }

    public String toString() {
      return "I" + val;
    }
  }

  @Test
  public void should_fix_3598() {
    // GIVEN
    Inner i1 = new Inner(1);
    Inner i2 = new Inner(2);
    Inner i3 = new Inner(3);

    Outer o1A = new Outer(i1);
    Outer o2A = new Outer(i2);
    Outer o3A = new Outer(i3);
    Outer o1B = new Outer(i1);
    Outer o2B = new Outer(i2);
    Outer o3B = new Outer(i3);

    Outer[] arrayA = array(o1A, o2A, o3A);
    Outer[] arrayB = array(o2B, o1B, o3B);
    Outer[] arrayACopy = array(o1A, o2A, o3A);
    Outer[] arrayBCopy = array(o2B, o1B, o3B);

    WithObject actual = new WithObject(array(arrayA, arrayACopy));
    WithObject expected = new WithObject(array(arrayB, arrayBCopy));

    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .ignoringArrayOrder()
                .isEqualTo(expected);
  }

  record Item(String name, int quantity) {
  }

  @Test
  void should_honor_representation_in_unmatched_elements_when_comparing_iterables_ignoring_order() {
    // GIVEN
    WithObject actual = new WithObject(array(new Item("Pants", 3), new Item("Loafers", 1)));
    WithObject expected = new WithObject(array(new Item("Shoes", 2), new Item("Pants", 3)));
    registerFormatterForType(Item.class, item -> "Item(%s, %d)".formatted(item.name(), item.quantity()));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .ignoringArrayOrder()
                                                                      .isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining(format("The following expected elements were not matched in the actual ArrayList:%n"
                                                     + "  [Item(Shoes, 2)]"));
  }
}
