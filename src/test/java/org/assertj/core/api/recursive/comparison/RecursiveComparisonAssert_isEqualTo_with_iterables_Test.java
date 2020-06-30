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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static com.google.common.collect.Sets.newHashSet;
import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.Author.authorsTreeSet;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Maps.newHashMap;
import static org.assertj.core.util.Sets.newLinkedHashSet;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.assertj.core.internal.objects.data.PersonDto;
import org.assertj.core.test.Person;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_with_iterables_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest
    implements PersonData {

  @ParameterizedTest(name = "actual {0} / expected {1}")
  @MethodSource("container_values")
  public void should_fail_as_Person_overridden_equals_should_be_honored(Object actual, Object expected,
                                                                        ComparisonDifference difference) {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
  }

  static Stream<Arguments> container_values() {
    // sheldon type is Person which overrides equals!
    Iterable<Person> actualAsIterable = newHashSet(sheldon);
    Iterable<PersonDto> expectAsIterable = newHashSet(sheldonDto);
    Person[] actualAsArray = array(sheldon);
    PersonDto[] expectedAsArray = array(sheldonDto);
    Optional<Person> actualAsOptional = Optional.of(sheldon);
    Optional<PersonDto> expectedAsOptional = Optional.of(sheldonDto);
    Map<String, PersonDto> expectedAsMap = newHashMap("sheldon", sheldonDto);
    Map<String, Person> actualAsMap = newHashMap("sheldon", sheldon);
    return Stream.of(Arguments.of(actualAsIterable, expectAsIterable, diff("", actualAsIterable, expectAsIterable)),
                     Arguments.of(actualAsArray, expectedAsArray, diff("", sheldon, sheldonDto)),
                     Arguments.of(actualAsOptional, expectedAsOptional, diff("value", sheldon, sheldonDto)),
                     Arguments.of(actualAsMap, expectedAsMap, diff("", sheldon, sheldonDto)));
  }

  @ParameterizedTest(name = "author 1 {0} / author 2 {1}")
  @MethodSource("matchingCollections")
  public void should_pass_when_comparing_same_collection_fields(Collection<Author> authors1, Collection<Author> authors2) {
    // GIVEN
    WithCollection<Author> actual = new WithCollection<>(authors1);
    WithCollection<Author> expected = new WithCollection<>(authors2);
    // THEN
    assertThat(actual).usingRecursiveComparison()
                      .isEqualTo(expected);
  }

  static Stream<Arguments> matchingCollections() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Collection<Author> empty = emptyList();
    return Stream.of(Arguments.of(list(pratchett), list(pratchett)),
                     Arguments.of(list(pratchett, georgeMartin), list(pratchett, georgeMartin)),
                     Arguments.of(list(pratchett, null), list(pratchett, null)),
                     Arguments.of(empty, empty),
                     Arguments.of(authorsTreeSet(pratchett), authorsTreeSet(pratchett)),
                     Arguments.of(authorsTreeSet(pratchett, georgeMartin), authorsTreeSet(pratchett, georgeMartin)),
                     Arguments.of(authorsTreeSet(pratchett, null), authorsTreeSet(pratchett, null)),
                     Arguments.of(authorsTreeSet(), authorsTreeSet()),
                     // ordered vs ordered is ok as long as the ordered elements match
                     Arguments.of(list(pratchett), authorsTreeSet(pratchett)),
                     Arguments.of(list(pratchett), newLinkedHashSet(pratchett)),
                     Arguments.of(list(georgeMartin, pratchett), authorsTreeSet(pratchett, georgeMartin)),
                     Arguments.of(list(pratchett, georgeMartin), newLinkedHashSet(pratchett, georgeMartin)),
                     Arguments.of(newLinkedHashSet(pratchett), list(pratchett)),
                     Arguments.of(newLinkedHashSet(pratchett), authorsTreeSet(pratchett)),
                     Arguments.of(newLinkedHashSet(pratchett, georgeMartin), list(pratchett, georgeMartin)),
                     Arguments.of(newLinkedHashSet(georgeMartin, pratchett), authorsTreeSet(pratchett, georgeMartin)),
                     Arguments.of(authorsTreeSet(pratchett), list(pratchett)),
                     Arguments.of(authorsTreeSet(pratchett), newLinkedHashSet(pratchett)),
                     Arguments.of(authorsTreeSet(pratchett, georgeMartin), list(georgeMartin, pratchett)),
                     Arguments.of(authorsTreeSet(pratchett, georgeMartin), newLinkedHashSet(georgeMartin, pratchett)),
                     Arguments.of(authorsTreeSet(pratchett, null), authorsTreeSet(pratchett, null)),
                     // actual ordered vs expected unordered can be compared but not the other way around
                     Arguments.of(list(pratchett), newHashSet(pratchett)),
                     Arguments.of(newLinkedHashSet(pratchett), newHashSet(pratchett)),
                     Arguments.of(authorsTreeSet(pratchett), newHashSet(pratchett)),
                     Arguments.of(list(pratchett, georgeMartin), newHashSet(pratchett, georgeMartin)),
                     Arguments.of(newLinkedHashSet(georgeMartin, pratchett), newHashSet(pratchett, georgeMartin)),
                     Arguments.of(authorsTreeSet(georgeMartin, pratchett), newHashSet(pratchett, georgeMartin)));
  }

  @ParameterizedTest(name = "authors 1 {0} / authors 2 {1} / path {2} / value 1 {3} / value 2 {4}")
  @MethodSource("differentCollections")
  public void should_fail_when_comparing_different_collection_fields(Collection<Author> authors1, Collection<Author> authors2,
                                                                     String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithCollection<Author> actual = new WithCollection<>(authors1);
    WithCollection<Author> expected = new WithCollection<>(authors2);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
  }

  static Stream<Arguments> differentCollections() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    Set<Author> pratchettHashSet = newHashSet(pratchett);
    List<Author> pratchettList = list(pratchett);
    return Stream.of(Arguments.of(pratchettList, list(georgeMartin), "group.name", "Terry Pratchett", "George Martin", null),
                     Arguments.of(list(pratchett, georgeMartin), pratchettList, "group",
                                  list(pratchett, georgeMartin), pratchettList,
                                  "actual and expected values are collections of different size, actual size=2 when expected size=1"),
                     Arguments.of(pratchettList, list(none), "group", pratchett, null, null),
                     Arguments.of(list(none), pratchettList, "group", null, pratchett, null),
                     // actual non ordered vs expected ordered collections
                     Arguments.of(pratchettHashSet, pratchettList, "group", pratchettHashSet, pratchettList,
                                  "expected field is an ordered collection but actual field is not (java.util.HashSet), ordered collections are: [java.util.List, java.util.SortedSet, java.util.LinkedHashSet]"),
                     Arguments.of(authorsTreeSet(pratchett), authorsTreeSet(georgeMartin), "group.name",
                                  "Terry Pratchett", "George Martin", null),
                     Arguments.of(newHashSet(pratchett, georgeMartin), pratchettHashSet, "group",
                                  newHashSet(pratchett, georgeMartin), pratchettHashSet,
                                  "actual and expected values are collections of different size, actual size=2 when expected size=1"),
                     // hashSet diff is at the collection level, not the element as in ordered collection where we can show the
                     // pair of different elements, this is why actual and expected are set and not element values.
                     Arguments.of(pratchettHashSet, newHashSet(none), "group",
                                  pratchettHashSet, newHashSet(none), null),
                     Arguments.of(newHashSet(none), pratchettHashSet, "group",
                                  newHashSet(none), pratchettHashSet, null),
                     Arguments.of(pratchettHashSet, newHashSet(georgeMartin), "group",
                                  pratchettHashSet, newHashSet(georgeMartin), null),
                     Arguments.of(authorsTreeSet(pratchett, georgeMartin), authorsTreeSet(pratchett), "group",
                                  authorsTreeSet(pratchett, georgeMartin), authorsTreeSet(pratchett),
                                  "actual and expected values are collections of different size, actual size=2 when expected size=1"),
                     Arguments.of(authorsTreeSet(pratchett), authorsTreeSet(none), "group", pratchett, null, null),
                     Arguments.of(authorsTreeSet(none), authorsTreeSet(pratchett), "group", null, pratchett, null));
  }

  @ParameterizedTest(name = "authors {0} / object {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource("iterableWithNonIterables")
  public void should_fail_when_comparing_iterable_to_non_iterable(Object actualFieldValue, Collection<Author> expectedFieldValue,
                                                                  String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithObject actual = new WithObject(actualFieldValue);
    WithCollection<Author> expected = new WithCollection<>(expectedFieldValue);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
  }

  static Stream<Arguments> iterableWithNonIterables() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    // We need to use the actual iterable and the expected list otherwise
    // verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall fails as actualIterable and expectedList description includes
    // their instance reference (e.g. @123ff3f) to differentiate their otherwise similar description.
    Author[] array = array(pratchett, georgeMartin);
    List<Author> orderedCollection = list(pratchett, georgeMartin);
    Set<Author> nonOrderedCollection = newHashSet(orderedCollection);
    return Stream.of(Arguments.of(pratchett, list(pratchett), "group", pratchett, list(pratchett),
                                  "expected field is an ordered collection but actual field is not (org.assertj.core.api.recursive.comparison.Author), ordered collections are: [java.util.List, java.util.SortedSet, java.util.LinkedHashSet]"),
                     Arguments.of(array, orderedCollection, "group", array, orderedCollection,
                                  "expected field is an ordered collection but actual field is not (org.assertj.core.api.recursive.comparison.Author[]), ordered collections are: [java.util.List, java.util.SortedSet, java.util.LinkedHashSet]"),
                     Arguments.of(array, nonOrderedCollection, "group", array, nonOrderedCollection,
                                  "expected field is an iterable but actual field is not (org.assertj.core.api.recursive.comparison.Author[])"),
                     Arguments.of(pratchett, nonOrderedCollection, "group", pratchett, nonOrderedCollection,
                                  "expected field is an iterable but actual field is not (org.assertj.core.api.recursive.comparison.Author)"));
  }

  public static class WithCollection<E> {
    public Collection<E> group;

    public WithCollection(Collection<E> collection) {
      this.group = collection;
    }

    @Override
    public String toString() {
      return format("WithCollection group=%s", group);
    }

  }

}
