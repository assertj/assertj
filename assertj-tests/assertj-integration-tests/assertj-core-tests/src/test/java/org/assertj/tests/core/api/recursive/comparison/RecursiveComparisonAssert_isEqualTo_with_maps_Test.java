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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison;

import static com.google.common.collect.ImmutableSortedMap.of;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.presentation.StandardRepresentation.registerFormatterForType;
import static org.assertj.tests.core.testkit.Maps.mapOf;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import com.google.common.collect.ImmutableMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;
import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonAssert_isEqualTo_with_maps_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  // TODO should_fail_when_comparing_actual_unordered_with_expected_ordered_map
  @Test
  void should_fail_when_comparing_actual_unsorted_with_expected_sorted_map() {
    WithMap<Long, Boolean> actual = new WithMap<>(new LinkedHashMap<>());
    actual.map.put(1L, true);
    actual.map.put(2L, false);
    WithMap<Long, Boolean> expected = new WithMap<>(new TreeMap<>());
    expected.map.put(2L, false);
    expected.map.put(1L, true);

    // WHEN/THEN
    ComparisonDifference mapDifference = diff("map", actual.map, expected.map,
                                              "expected field is a sorted map but actual field is not (java.util.LinkedHashMap)");
    compareRecursivelyFailsWithDifferences(actual, expected, mapDifference);
  }

  @ParameterizedTest(name = "author 1 {0} / author 2 {1}")
  @MethodSource("sameMaps")
  void should_pass_when_comparing_same_map_fields(Map<String, Author> authors1, Map<String, Author> authors2) {
    // GIVEN
    WithMap<String, Author> actual = new WithMap<>(authors1);
    WithMap<String, Author> expected = new WithMap<>(authors2);
    // THEN
    then(actual).usingRecursiveComparison()
                .isEqualTo(expected);
  }

  static Stream<Arguments> sameMaps() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    Map<String, Author> empty = newHashMap();
    SortedMap<String, Author> martinAndPratchettSorted = of(pratchett.name, pratchett, georgeMartin.name, georgeMartin);
    Map<String, Author> singletonPratchettMap = singletonMap(pratchett.name, pratchett);
    LinkedHashMap<String, Author> pratchettAndMartin = mapOf(entry(pratchett.name, pratchett),
                                                             entry(georgeMartin.name, georgeMartin));
    LinkedHashMap<String, Author> martinAndPratchett = mapOf(entry(georgeMartin.name, georgeMartin),
                                                             entry(pratchett.name, pratchett));
    return Stream.of(arguments(singletonPratchettMap, singletonPratchettMap),
                     arguments(pratchettAndMartin, pratchettAndMartin),
                     arguments(martinAndPratchett, pratchettAndMartin),
                     arguments(pratchettAndMartin, martinAndPratchett),
                     arguments(martinAndPratchettSorted, martinAndPratchettSorted),
                     arguments(martinAndPratchettSorted, martinAndPratchett),
                     arguments(singletonMap(pratchett.name, none), singletonMap(pratchett.name, none)),
                     arguments(mapOf(entry(pratchett.name, pratchett)), singletonPratchettMap),
                     arguments(empty, empty));
  }

  @ParameterizedTest(name = "authors 1 {0} / authors 2 {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource("differentMaps")
  void should_fail_when_comparing_different_map_fields(Map<String, Author> authors1, Map<String, Author> authors2,
                                                       String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithMap<String, Author> actual = new WithMap<>(authors1);
    WithMap<String, Author> expected = new WithMap<>(authors2);
    // WHEN/THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  static Stream<Arguments> differentMaps() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    Map<String, Author> empty = newHashMap();
    SortedMap<String, Author> sortedMartinAndPratchett = of(pratchett.name, pratchett, georgeMartin.name, georgeMartin);
    SortedMap<String, Author> sortedPratchettMap = of(pratchett.name, pratchett);
    Map<String, Author> nonSortedPratchettAndMartin = mapOf(entry(pratchett.name, pratchett),
                                                            entry(georgeMartin.name, georgeMartin));
    Map<String, Author> singletonPratchettMap = singletonMap(pratchett.name, pratchett);
    Map<String, Author> singletonGeorgeMartinMap = singletonMap(georgeMartin.name, georgeMartin);
    return Stream.of(arguments(singletonPratchettMap, singletonGeorgeMartinMap, "map",
                               singletonPratchettMap, singletonGeorgeMartinMap,
                               "The following keys were not found in the actual map value:%n  [\"George Martin\"]".formatted()),
                     arguments(nonSortedPratchettAndMartin, singletonPratchettMap, "map",
                               nonSortedPratchettAndMartin, singletonPratchettMap,
                               "actual and expected values are maps of different size, actual size=2 when expected size=1"),
                     arguments(sortedMartinAndPratchett, sortedPratchettMap, "map",
                               sortedMartinAndPratchett, sortedPratchettMap,
                               "actual and expected values are sorted maps of different size, actual size=2 when expected size=1"),
                     arguments(nonSortedPratchettAndMartin, sortedMartinAndPratchett, "map",
                               nonSortedPratchettAndMartin, sortedMartinAndPratchett,
                               "expected field is a sorted map but actual field is not (java.util.LinkedHashMap)"),
                     arguments(singletonMap(pratchett.name, none), singletonPratchettMap, "map.Terry Pratchett",
                               none, pratchett, null),
                     arguments(singletonPratchettMap, singletonMap(georgeMartin.name, pratchett), "map",
                               singletonPratchettMap, singletonMap(georgeMartin.name, pratchett),
                               "The following keys were not found in the actual map value:%n  [\"George Martin\"]".formatted()),
                     arguments(singletonPratchettMap, empty, "map",
                               singletonPratchettMap, empty,
                               "actual and expected values are maps of different size, actual size=1 when expected size=0"));
  }

  @ParameterizedTest(name = "authors {0} / object {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource
  void should_fail_when_comparing_map_to_non_map(Object actualFieldValue, Map<String, Author> expectedFieldValue,
                                                 String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithObject actual = new WithObject(actualFieldValue);
    WithObject expected = new WithObject(expectedFieldValue);
    // WHEN/THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    compareRecursivelyFailsWithDifferences(actual, expected, difference);
  }

  @Test
  void should_report_unmatched_elements() {
    // GIVEN
    Map<String, String> actual = ImmutableMap.of("a", "a", "b", "b", "e", "e");
    Map<String, String> expected = ImmutableMap.of("a", "a", "c", "c", "d", "d");
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).usingRecursiveComparison().isEqualTo(expected));
    // THEN
    then(assertionError).hasMessageContaining("The following keys were not found in the actual map value:%n  [\"c\", \"d\"]".formatted());
  }

  static Stream<Arguments> should_fail_when_comparing_map_to_non_map() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    Map<String, Author> mapOfTwoAuthors = mapOf(entry(pratchett.name, pratchett), entry(georgeMartin.name, georgeMartin));
    return Stream.of(arguments(pratchett, mapOfTwoAuthors, "group", pratchett, mapOfTwoAuthors,
                               "expected field is a map but actual field is not (org.assertj.tests.core.api.recursive.comparison.Author)"),
                     arguments(none, mapOfTwoAuthors, "group", none, mapOfTwoAuthors, null));
  }

  record Item(String name, int quantity) {
  }

  @Test
  void should_honor_representation_in_unmatched_elements_when_comparing_unordered_iterables() {
    // GIVEN
    Map<String, Item> expectedItems = mapOf(entry("Shoes", new Item("Shoes", 2)), entry("Pants", new Item("Pants", 3)));
    Map<String, Item> actualItems = mapOf(entry("Pants", new Item("Pants", 3)), entry("Hat", new Item("Hat", 1)));
    registerFormatterForType(Item.class, item -> "Item(%s, %d)".formatted(item.name(), item.quantity()));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actualItems).usingRecursiveComparison()
                                                                                      .isEqualTo(expectedItems));
    // THEN
    then(assertionError).hasMessageContaining(format("The following keys were not found in the actual map value:%n" +
                                                     "  [\"Shoes\"]"));
  }

}
