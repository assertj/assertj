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
 * Copyright 2012-2023 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.RecursiveComparisonAssert_isEqualTo_ignoringCollectionOrder_Test.Type.FIRST;
import static org.assertj.core.api.recursive.comparison.RecursiveComparisonAssert_isEqualTo_ignoringCollectionOrder_Test.Type.SECOND;
import static org.assertj.core.internal.objects.data.FriendlyPerson.friend;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.FriendlyPerson;
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

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("home.address.number"), 1, 2));
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, comparisonDifference);
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

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference friendsDifference = diff("friends", actual.friends, expected.friends,
                                                  format("The following expected elements were not matched in the actual ArrayList:%n"
                                                         + "  [Person [dateOfBirth=null, name=Dr. John Watson, phone=null, home=Home [address=Address [number=1]]]]"));
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, friendsDifference);
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

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference friendsDifference = diff("friends", actual.friends, expected.friends,
                                                  format("The following expected elements were not matched in the actual ArrayList:%n"
                                                         + "  [Person [dateOfBirth=null, name=Dr. John Watson, phone=null, home=Home [address=Address [number=1]]]]"));
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, friendsDifference);
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
   * - we would like to try (1, 2), (1, 1), (2, 2) but they have already been visited so we skip them<br>
   * at this point, we know listB won't be found because (1, 1), (2, 2) won't be considered.
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
    assertThat(persons).usingRecursiveComparison()
                       .ignoringCollectionOrder()
                       .isEqualTo(list(new PersonWithInt("name-2", 2),
                                       new PersonWithInt("name-2", 1),
                                       new PersonWithInt("name-1", 1)));
  }

}
