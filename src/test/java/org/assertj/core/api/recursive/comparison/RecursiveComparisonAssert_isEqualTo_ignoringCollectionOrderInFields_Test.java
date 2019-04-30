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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
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

public class RecursiveComparisonAssert_isEqualTo_ignoringCollectionOrderInFields_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @ParameterizedTest(name = "{0}: actual={1} / expected={2} / ignore collection order in fields={3}")
  @MethodSource("should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_specified_fields_source")
  @SuppressWarnings("unused")
  public void should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_specified_fields(String description,
                                                                                                              Object actual,
                                                                                                              Object expected,
                                                                                                              List<String> fieldsToIgnoreCollectionOrder) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringCollectionOrderInFields(arrayOf(fieldsToIgnoreCollectionOrder))
                      .isEqualTo(expected);
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_specified_fields_source() {
    FriendlyPerson friendlyPerson1 = new FriendlyPerson("Sherlock Holmes");
    friendlyPerson1.friends.add(new FriendlyPerson("Dr. John Watson"));
    friendlyPerson1.friends.add(new FriendlyPerson("Molly Hooper"));

    FriendlyPerson friendlyPerson2 = new FriendlyPerson("Sherlock Holmes");
    friendlyPerson2.friends.add(new FriendlyPerson("Molly Hooper"));
    friendlyPerson2.friends.add(new FriendlyPerson("Dr. John Watson"));

    FriendlyPerson friendlyPerson3 = new FriendlyPerson("Sherlock Holmes");
    FriendlyPerson friendlyPerson4 = new FriendlyPerson("Dr. John Watson");
    friendlyPerson4.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    friendlyPerson4.friends.add(new FriendlyPerson("Mrs. Hudson"));
    friendlyPerson3.friends.add(friendlyPerson4);
    friendlyPerson3.friends.add(new FriendlyPerson("Molly Hooper"));

    FriendlyPerson friendlyPerson5 = new FriendlyPerson("Sherlock Holmes");
    FriendlyPerson friendlyPerson6 = new FriendlyPerson("Dr. John Watson");
    friendlyPerson6.friends.add(new FriendlyPerson("Mrs. Hudson"));
    friendlyPerson6.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    friendlyPerson5.friends.add(friendlyPerson6);
    friendlyPerson5.friends.add(new FriendlyPerson("Molly Hooper"));

    return Stream.of(arguments("same data except friends property collection order",
                               friendlyPerson1, friendlyPerson2, list("friends")),
                     arguments("same data except friends property order in subfield collection",
                               friendlyPerson3, friendlyPerson5, list("friends.friends")));
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_even_when_collection_order_is_ignored_in_some_fields() {
    // GIVEN
    FriendlyPerson actual = new FriendlyPerson("Sherlock Holmes");
    FriendlyPerson actualFriend = new FriendlyPerson("Dr. John Watson");
    actualFriend.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    actualFriend.friends.add(new FriendlyPerson("Mrs. Hudson"));
    actual.friends.add(actualFriend);
    actual.friends.add(new FriendlyPerson("Molly Hooper"));

    FriendlyPerson expected = new FriendlyPerson("Sherlock Holmes");
    expected.friends.add(new FriendlyPerson("Molly Hooper"));
    FriendlyPerson expectedFriend = new FriendlyPerson("Dr. John Watson");
    expectedFriend.friends.add(new FriendlyPerson("Mrs. Hudson"));
    expectedFriend.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    expected.friends.add(expectedFriend);

    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("friends");

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference friendsDifference = diff("friends", actual.friends, expected.friends);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, friendsDifference);
  }

  @ParameterizedTest(name = "{0}: actual={1} / expected={2} / ignore collection order in fields matching regexes={3}")
  @MethodSource("should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_fields_matching_specified_regexes_source")
  @SuppressWarnings("unused")
  public void should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_fields_matching_specified_regexes(String description,
                                                                                                                               Object actual,
                                                                                                                               Object expected,
                                                                                                                               List<String> regexes) {
    assertThat(actual).usingRecursiveComparison()
                      .ignoringCollectionOrderInFieldsMatchingRegexes(arrayOf(regexes))
                      .isEqualTo(expected);
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> should_pass_for_objects_with_the_same_data_when_collection_order_is_ignored_in_fields_matching_specified_regexes_source() {
    FriendlyPerson friendlyPerson1 = new FriendlyPerson("Sherlock Holmes");
    friendlyPerson1.friends.add(new FriendlyPerson("Dr. John Watson"));
    friendlyPerson1.friends.add(new FriendlyPerson("Molly Hooper"));

    FriendlyPerson friendlyPerson2 = new FriendlyPerson("Sherlock Holmes");
    friendlyPerson2.friends.add(new FriendlyPerson("Molly Hooper"));
    friendlyPerson2.friends.add(new FriendlyPerson("Dr. John Watson"));

    FriendlyPerson friendlyPerson3 = new FriendlyPerson("Sherlock Holmes");
    FriendlyPerson friendlyPerson4 = new FriendlyPerson("Dr. John Watson");
    friendlyPerson4.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    friendlyPerson4.friends.add(new FriendlyPerson("Mrs. Hudson"));
    friendlyPerson3.friends.add(friendlyPerson4);
    friendlyPerson3.friends.add(new FriendlyPerson("Molly Hooper"));

    FriendlyPerson friendlyPerson5 = new FriendlyPerson("Sherlock Holmes");
    FriendlyPerson friendlyPerson6 = new FriendlyPerson("Dr. John Watson");
    friendlyPerson6.friends.add(new FriendlyPerson("Mrs. Hudson"));
    friendlyPerson6.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    friendlyPerson5.friends.add(friendlyPerson6);
    friendlyPerson5.friends.add(new FriendlyPerson("Molly Hooper"));

    return Stream.of(arguments("same data except friends property collection order",
                               friendlyPerson1, friendlyPerson2, list("friend.")),
                     arguments("same data except friends property order in subfield collection",
                               friendlyPerson3, friendlyPerson5, list("friends\\..*")),
                     arguments("should not stack overflow with regexes",
                               friendlyPerson3, friendlyPerson5, list("friends[\\D]+")));
  }

  @Test
  public void should_fail_when_actual_differs_from_expected_even_when_collection_order_is_ignored_in_fields_matching_some_regexes() {
    // GIVEN
    FriendlyPerson actual = new FriendlyPerson("Sherlock Holmes");
    FriendlyPerson actualFriend = new FriendlyPerson("Dr. John Watson");
    actualFriend.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    actualFriend.friends.add(new FriendlyPerson("Mrs. Hudson"));
    actual.friends.add(actualFriend);
    actual.friends.add(new FriendlyPerson("Molly Hooper"));

    FriendlyPerson expected = new FriendlyPerson("Sherlock Holmes");
    expected.friends.add(new FriendlyPerson("Molly Hooper"));
    FriendlyPerson expectedFriend = new FriendlyPerson("Dr. John Watson");
    expectedFriend.friends.add(new FriendlyPerson("Mrs. Hudson"));
    expectedFriend.friends.add(new FriendlyPerson("D.I. Greg Lestrade"));
    expected.friends.add(expectedFriend);

    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes("friend.");

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference friendsDifference = diff("friends", actual.friends, expected.friends);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, friendsDifference);
  }

  private static String[] arrayOf(List<String> list) {
    return list.toArray(new String[0]);
  }

}
