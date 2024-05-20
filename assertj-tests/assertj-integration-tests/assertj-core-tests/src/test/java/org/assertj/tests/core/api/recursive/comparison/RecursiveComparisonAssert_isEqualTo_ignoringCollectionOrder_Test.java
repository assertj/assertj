/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.api.recursive.comparison.RecursiveComparisonAssert_isEqualTo_ignoringCollectionOrder_Test.Type.FIRST;
import static org.assertj.tests.core.api.recursive.comparison.RecursiveComparisonAssert_isEqualTo_ignoringCollectionOrder_Test.Type.SECOND;
import static org.assertj.tests.core.api.recursive.data.FriendlyPerson.friend;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.tests.core.api.recursive.data.FriendlyPerson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_ignoringCollectionOrder_Test
    extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{0}: actual={1} / expected={2}")
  @MethodSource("should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_source")
  @SuppressWarnings("unused")
  void should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored(String description,
                                                                                   Object actual,
                                                                                   Object expected) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringCollectionOrder()
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_source() {
    FriendlyPerson friendlyPerson1 = friend("Sherlock Holmes");
    friendlyPerson1.friends.add(friend("Dr. John Watson"));
    friendlyPerson1.friends.add(friend("Molly Hooper"));

    FriendlyPerson friendlyPerson2 = friend("Sherlock Holmes");
    friendlyPerson2.friends.add(friend("Molly Hooper"));
    friendlyPerson2.friends.add(friend("Dr. John Watson"));

    FriendlyPerson friendlyPerson3 = friend("Sherlock Holmes");
    FriendlyPerson friendlyPerson4 = friend("Dr. John Watson");
    friendlyPerson4.friends.add(friend("D.I. Greg Lestrade"));
    friendlyPerson4.friends.add(friend("Mrs. Hudson"));
    friendlyPerson3.friends.add(friendlyPerson4);
    friendlyPerson3.friends.add(friend("Molly Hooper"));

    FriendlyPerson friendlyPerson5 = friend("Sherlock Holmes");
    FriendlyPerson friendlyPerson6 = friend("Dr. John Watson");
    friendlyPerson6.friends.add(friend("Mrs. Hudson"));
    friendlyPerson6.friends.add(friend("D.I. Greg Lestrade"));
    friendlyPerson5.friends.add(friendlyPerson6);
    friendlyPerson5.friends.add(friend("Molly Hooper"));

    return Stream.of(arguments("same data except friends property collection order",
                               friendlyPerson1, friendlyPerson2),
                     arguments("same data except friends property order in subfield collection",
                               friendlyPerson3, friendlyPerson5));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_collection_order_is_ignored() {
    // GIVEN
    FriendlyPerson actual = friend("Sherlock Holmes");
    actual.home.address.number = 1;
    actual.friends.add(friend("Dr. John Watson"));
    actual.friends.add(friend("Molly Hooper"));

    FriendlyPerson expected = friend("Sherlock Holmes");
    expected.home.address.number = 2;
    expected.friends.add(friend("Molly Hooper"));
    expected.friends.add(friend("Dr. John Watson"));

    recursiveComparisonConfiguration.ignoreCollectionOrder(true);

    // WHEN/THEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("home.address.number"), 1, 2));
    compareRecursivelyFailsWithDifferences(actual, expected, comparisonDifference);
  }

  @ParameterizedTest(name = "{0}: actual={1} / expected={2} / ignore collection order in fields={3}")
  @MethodSource("should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_specified_fields_source")
  @SuppressWarnings("unused")
  void should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_specified_fields(String description,
                                                                                                       Object actual,
                                                                                                       Object expected,
                                                                                                       String[] fieldsToIgnoreCollectionOrder) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringCollectionOrderInFields(fieldsToIgnoreCollectionOrder)
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_specified_fields_source() {
    FriendlyPerson friendlyPerson1 = friend("Sherlock Holmes");
    friendlyPerson1.friends.add(friend("Dr. John Watson"));
    friendlyPerson1.friends.add(friend("Molly Hooper"));

    FriendlyPerson friendlyPerson2 = friend("Sherlock Holmes");
    friendlyPerson2.friends.add(friend("Molly Hooper"));
    friendlyPerson2.friends.add(friend("Dr. John Watson"));

    FriendlyPerson friendlyPerson3 = friend("Sherlock Holmes");
    FriendlyPerson friendlyPerson4 = friend("Dr. John Watson");
    friendlyPerson4.friends.add(friend("D.I. Greg Lestrade"));
    friendlyPerson4.friends.add(friend("Mrs. Hudson"));
    friendlyPerson3.friends.add(friendlyPerson4);
    friendlyPerson3.friends.add(friend("Molly Hooper"));

    FriendlyPerson friendlyPerson5 = friend("Sherlock Holmes");
    FriendlyPerson friendlyPerson6 = friend("Dr. John Watson");
    friendlyPerson6.friends.add(friend("Mrs. Hudson"));
    friendlyPerson6.friends.add(friend("D.I. Greg Lestrade"));
    friendlyPerson5.friends.add(friendlyPerson6);
    friendlyPerson5.friends.add(friend("Molly Hooper"));

    return Stream.of(arguments("same data except friends property collection order",
                               friendlyPerson1, friendlyPerson2, array("friends")),
                     arguments("same data except friends property order in subfield collection",
                               friendlyPerson3, friendlyPerson5, array("friends.friends")));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_collection_order_is_ignored_in_some_fields() {
    // GIVEN
    FriendlyPerson actual = friend("Sherlock Holmes");
    actual.friends.add(friend("Molly Hooper"));
    FriendlyPerson actualFriend = friend("Dr. John Watson");
    actualFriend.friends.add(friend("D.I. Greg Lestrade"));
    actualFriend.friends.add(friend("Mrs. Hudson"));
    actual.friends.add(actualFriend);

    FriendlyPerson expected = friend("Sherlock Holmes");
    expected.friends.add(friend("Molly Hooper"));
    FriendlyPerson expectedFriend = friend("Dr. John Watson");
    expectedFriend.friends.add(friend("Mrs. Hudson"));
    expectedFriend.friends.add(friend("D.I. Greg Lestrade"));
    expected.friends.add(expectedFriend);

    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("friends");

    // WHEN/THEN
    ComparisonDifference friendsDifference = diff("friends", actual.friends, expected.friends,
                                                  format("The following expected elements were not matched in the actual ArrayList:%n"
                                                         + "  [Person [dateOfBirth=null, name=Dr. John Watson, phone=null, home=Home [address=Address [number=1]]]]"));
    compareRecursivelyFailsWithDifferences(actual, expected, friendsDifference);
  }

  @ParameterizedTest(name = "{0}: actual={1} / expected={2} / ignore collection order in fields matching regexes={3}")
  @MethodSource("should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_fields_matching_specified_regexes_source")
  @SuppressWarnings("unused")
  void should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_fields_matching_specified_regexes(String description,
                                                                                                                        Object actual,
                                                                                                                        Object expected,
                                                                                                                        String[] regexes) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringCollectionOrderInFieldsMatchingRegexes(regexes)
                      .isEqualTo(expected);
  }

  private static Stream<Arguments> should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_fields_matching_specified_regexes_source() {
    FriendlyPerson friendlyPerson1 = friend("Sherlock Holmes");
    friendlyPerson1.friends.add(friend("Dr. John Watson"));
    friendlyPerson1.friends.add(friend("Molly Hooper"));

    FriendlyPerson friendlyPerson2 = friend("Sherlock Holmes");
    friendlyPerson2.friends.add(friend("Molly Hooper"));
    friendlyPerson2.friends.add(friend("Dr. John Watson"));

    FriendlyPerson friendlyPerson3 = friend("Sherlock Holmes");
    FriendlyPerson friendlyPerson4 = friend("Dr. John Watson");
    friendlyPerson4.friends.add(friend("D.I. Greg Lestrade"));
    friendlyPerson4.friends.add(friend("Mrs. Hudson"));
    friendlyPerson3.friends.add(friendlyPerson4);
    friendlyPerson3.friends.add(friend("Molly Hooper"));

    FriendlyPerson friendlyPerson5 = friend("Sherlock Holmes");
    FriendlyPerson friendlyPerson6 = friend("Dr. John Watson");
    friendlyPerson6.friends.add(friend("Mrs. Hudson"));
    friendlyPerson6.friends.add(friend("D.I. Greg Lestrade"));
    friendlyPerson5.friends.add(friendlyPerson6);
    friendlyPerson5.friends.add(friend("Molly Hooper"));

    return Stream.of(arguments("same data except friends property collection order",
                               friendlyPerson1, friendlyPerson2, array("friend.")),
                     arguments("same data except friends property order in subfield collection",
                               friendlyPerson3, friendlyPerson5, array("friends\\..*")),
                     arguments("should not stack overflow with regexes",
                               friendlyPerson3, friendlyPerson5, array("friends[\\D]+")));
  }

  @Test
  void should_fail_when_actual_differs_from_expected_even_when_collection_order_is_ignored_in_fields_matching_some_regexes() {
    // GIVEN
    FriendlyPerson actual = friend("Sherlock Holmes");
    FriendlyPerson actualFriend = friend("Dr. John Watson");
    actualFriend.friends.add(friend("D.I. Greg Lestrade"));
    actualFriend.friends.add(friend("Mrs. Hudson"));
    actual.friends.add(actualFriend);
    actual.friends.add(friend("Molly Hooper"));

    FriendlyPerson expected = friend("Sherlock Holmes");
    expected.friends.add(friend("Molly Hooper"));
    FriendlyPerson expectedFriend = friend("Dr. John Watson");
    expectedFriend.friends.add(friend("Mrs. Hudson"));
    expectedFriend.friends.add(friend("D.I. Greg Lestrade"));
    expected.friends.add(expectedFriend);

    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes("friend.");

    // WHEN/THEN
    ComparisonDifference friendsDifference = diff("friends", actual.friends, expected.friends,
                                                  format("The following expected elements were not matched in the actual ArrayList:%n"
                                                         + "  [Person [dateOfBirth=null, name=Dr. John Watson, phone=null, home=Home [address=Address [number=1]]]]"));
    compareRecursivelyFailsWithDifferences(actual, expected, friendsDifference);
  }

  @Test
  void should_fix_1854() {
    // Original Lists
    List<Integer> listA = list(1, 2);
    List<Integer> listB = list(1, 2);

    // --------------------------------------------------------------------------------------------------------------
    // Base test case - compare against exact copies of the original lists
    List<Integer> listACopy = list(1, 2);
    List<Integer> listBCopy = list(1, 2);
    // The lists themselves are equal to each other.
    assertThat(listA).usingRecursiveComparison()
                     .ignoringCollectionOrder()
                     .isEqualTo(listACopy);
    assertThat(listB).usingRecursiveComparison()
                     .ignoringCollectionOrder()
                     .isEqualTo(listBCopy);
    // Also, nested lists are still considered equal (regardless of the order of the top-level list)
    assertThat(list(listA, listB)).usingRecursiveComparison()
                                  .ignoringCollectionOrder()
                                  .isEqualTo(list(listACopy, listBCopy))
                                  .isEqualTo(list(listBCopy, listACopy));

    // --------------------------------------------------------------------------------------------------------------
    // Reversed test case - compare against reversed copies of the original lists
    List<Integer> listAReverse = list(2, 1);
    List<Integer> listBReverse = list(2, 1);
    // The lists themselves are still equal to each other. So far so good.
    assertThat(listA).usingRecursiveComparison()
                     .ignoringCollectionOrder()
                     .isEqualTo(listAReverse);
    assertThat(listB).usingRecursiveComparison()
                     .ignoringCollectionOrder()
                     .isEqualTo(listBReverse);
    // Also, comparing a list with one reversed and one copy works!
    assertThat(list(listA, listB)).usingRecursiveComparison()
                                  .ignoringCollectionOrder()
                                  .isEqualTo(list(listACopy, listBReverse))
                                  .isEqualTo(list(listAReverse, listBCopy));

    // <<<<<<<<<<<<<<<<<<<<<<<< HERE IS THE PROBLEM >>>>>>>>>>>>>>>>>>>>>>>>>>>>
    // Comparing the original lists against two reversed lists fails!
    assertThat(list(listA, listB)).usingRecursiveComparison()
                                  .ignoringCollectionOrder()
                                  .isEqualTo(list(listAReverse, listBReverse))
                                  .isEqualTo(list(listBReverse, listAReverse));

    // --------------------------------------------------------------------------------------------------------------
    // Additional test case - compare against reversed copies of lists with different core elements
    List<Integer> listC = list(3, 4);
    List<Integer> listCReverse = list(4, 3);
    // The lists themselves are equal to each other.
    assertThat(listC).usingRecursiveComparison()
                     .ignoringCollectionOrder()
                     .isEqualTo(listCReverse);

    // Interestingly, both of these assertions work fine!
    assertThat(list(listA, listC)).usingRecursiveComparison()
                                  .ignoringCollectionOrder()
                                  .isEqualTo(list(listAReverse, listCReverse))
                                  .isEqualTo(list(listCReverse, listAReverse));
  }

  /**
   * This test shows that we can't track all visited values, only the one with potential cycles.
   * <p>
   * Let's run it step by step with tracking all visited values:<br>
   * list(listA, listB) vs list(listAReverse, listBReverse) means trying to find<br>
   * - listA in list(listAReverse, listBReverse) and then listB
   * <p>
   * After comparing possible pairs (listA element, listAReverse element) we conclude that listA matches listAReverse<br>
   * - here are the pairs (1, 2), (1, 1), (2, 2), we add them to the visited ones<br>
   * <p>
   * We now try to find listB in list(listBReverse) - listAReverse must not be taken into account as it had already been matched<br>
   * - we would like to try (1, 2), (1, 1), (2, 2) but they have already been visited, so we skip them.<br>
   * at this point, we know listB won't be found because (1, 1), (2, 2) won't be considered.<br>
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
   * If listA and listB contained non wrapped basic types then == is enough to differentiate them.
   */
  @Test
  void should_fix_1854_minimal_test() {
    // GIVEN
    List<Integer> listA = list(1, 2);
    List<Integer> listB = list(1, 2);
    // Reversed lists
    List<Integer> listAReverse = list(2, 1);
    List<Integer> listBReverse = list(2, 1);
    // WHEN - THEN
    assertThat(list(listA, listB)).usingRecursiveComparison()
                                  .ignoringCollectionOrder()
                                  .isEqualTo(list(listAReverse, listBReverse));

  }

  @Test
  void should_fix_1854_with_non_wrapped_basic_types() {
    // GIVEN
    FriendlyPerson p1 = friend("Sherlock Holmes");
    FriendlyPerson p2 = friend("Watson");
    FriendlyPerson p3 = friend("Sherlock Holmes");
    FriendlyPerson p4 = friend("Watson");
    List<FriendlyPerson> listA = list(p1, p2);
    List<FriendlyPerson> listB = list(p1, p2);
    // Reversed lists
    List<FriendlyPerson> listAReverse = list(p4, p3);
    List<FriendlyPerson> listBReverse = list(p4, p3);
    // WHEN - THEN
    assertThat(list(listA, listB)).usingRecursiveComparison()
                                  .ignoringCollectionOrder()
                                  .isEqualTo(list(listAReverse, listBReverse));

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
      return String.format("Person [name=%s, type=%s]", name, type);
    }

  }

  @Test
  public void should_not_remove_already_visited_enum_dual_values_as_they_cant_produce_cycles() {
    // GIVEN
    List<PersonWithEnum> persons = list(new PersonWithEnum("name-1", FIRST),
                                        new PersonWithEnum("name-2", FIRST),
                                        new PersonWithEnum("name-2", SECOND));

    // WHEN/THEN
    assertThat(persons).usingRecursiveComparison()
                       .ignoringCollectionOrder()
                       .isEqualTo(list(new PersonWithEnum("name-2", SECOND),
                                       new PersonWithEnum("name-2", FIRST),
                                       new PersonWithEnum("name-1", FIRST)));
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
      return String.format("Person [name=%s, type=%s]", name, type);
    }

  }

  @Test
  public void should_not_remove_already_visited_int_dual_values_as_they_cant_produce_cycles() {
    // GIVEN
    List<PersonWithInt> persons = list(new PersonWithInt("name-1", 1),
                                       new PersonWithInt("name-2", 1),
                                       new PersonWithInt("name-2", 2));
    // WHEN/THEN
    then(persons).usingRecursiveComparison()
                 .ignoringCollectionOrder()
                 .isEqualTo(list(new PersonWithInt("name-2", 2),
                                 new PersonWithInt("name-2", 1),
                                 new PersonWithInt("name-1", 1)));
  }

  // https://github.com/assertj/assertj/issues/2954

  static class DataStore {
    List<Data> field1 = new ArrayList<>();
    List<Data> field2 = new ArrayList<>();

    @Override
    public String toString() {
      return format("DataStore[field1=%s, field2=%s]", this.field1, this.field2);
    }
  }

  static class Data {
    final String text;

    Data(String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return super.toString().replace("org.assertj.core.api.recursive.comparison.RecursiveComparisonAssert_isEqualTo_ignoringCollectionOrder_Test$",
                                      "");
    }
  }

  @Test
  void evaluating_visited_dual_values_should_check_location() {
    // GIVEN
    Data d1 = new Data("111");
    Data d2 = new Data("222");
    DataStore dataStore1 = createDataStore(d1, d2);
    DataStore dataStore2 = createDataStore(d2, d1);
    // WHEN/THEN
    then(dataStore1).usingRecursiveComparison()
                    .withEqualsForType((data1, data2) -> data1.text.equals(data2.text), Data.class)
                    .ignoringCollectionOrder()
                    .isEqualTo(dataStore2);
  }

  private static DataStore createDataStore(Data d1, Data d2) {
    DataStore dataStore = new DataStore();
    dataStore.field1.addAll(list(d1, d2));
    dataStore.field2.addAll(list(d1, d2));
    return dataStore;
  }

  // related to https://github.com/assertj/assertj/issues/3598
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

    List<Outer> listA = list(o1A, o2A, o3A);
    List<Outer> listB = list(o2B, o1B, o3B);
    List<Outer> listACopy = list(o1A, o2A, o3A);
    List<Outer> listBCopy = list(o2B, o1B, o3B);

    // WHEN/THEN
    then(list(listA, listACopy)).usingRecursiveComparison()
                                .ignoringCollectionOrder()
                                .isEqualTo(list(listB, listBCopy));
  }

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

}
