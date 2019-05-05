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

import static com.google.common.collect.ImmutableSortedMap.of;
import static com.google.common.collect.Maps.newHashMap;
import static java.lang.String.format;
import static java.util.Collections.singletonMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.test.Maps.mapOf;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.assertj.core.api.RecursiveComparisonAssert_isEqualTo_BaseTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonAssert_isEqualTo_with_maps_Test extends RecursiveComparisonAssert_isEqualTo_BaseTest {

  @Test
  public void should_fail_when_comparing_actual_unsorted_with_expected_sorted_map() {
    WithMap<Long, Boolean> actual = new WithMap<>(new LinkedHashMap<>());
    actual.group.put(1L, true);
    actual.group.put(2L, false);
    WithMap<Long, Boolean> expected = new WithMap<>(new TreeMap<>());
    expected.group.put(2L, false);
    expected.group.put(1L, true);

    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);

    // THEN
    ComparisonDifference mapDifference = diff("group", actual.group, expected.group,
                                              "expected field is a sorted map but actual field is not (java.util.LinkedHashMap)");
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, mapDifference);
  }

  @ParameterizedTest(name = "author 1 {0} / author 2 {1}")
  @MethodSource("sameMaps")
  public void should_pass_when_comparing_same_map_fields(Map<String, Author> authors1, Map<String, Author> authors2) {
    // GIVEN
    WithMap<String, Author> actual = new WithMap<>(authors1);
    WithMap<String, Author> expected = new WithMap<>(authors2);
    // THEN
    assertThat(actual).usingRecursiveComparison()
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
    return Stream.of(Arguments.of(singletonPratchettMap, singletonPratchettMap),
                     Arguments.of(pratchettAndMartin, pratchettAndMartin),
                     Arguments.of(martinAndPratchett, pratchettAndMartin),
                     Arguments.of(pratchettAndMartin, martinAndPratchett),
                     Arguments.of(martinAndPratchettSorted, martinAndPratchettSorted),
                     Arguments.of(martinAndPratchettSorted, martinAndPratchett),
                     Arguments.of(singletonMap(pratchett.name, none), singletonMap(pratchett.name, none)),
                     Arguments.of(mapOf(entry(pratchett.name, pratchett)), singletonPratchettMap),
                     Arguments.of(empty, empty));
  }

  @ParameterizedTest(name = "authors 1 {0} / authors 2 {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource("differentMaps")
  public void should_fail_when_comparing_different_map_fields(Map<String, Author> authors1, Map<String, Author> authors2,
                                                              String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithMap<String, Author> actual = new WithMap<>(authors1);
    WithMap<String, Author> expected = new WithMap<>(authors2);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
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
    return Stream.of(Arguments.of(singletonPratchettMap, singletonGeorgeMartinMap, "group",
                                  singletonPratchettMap, singletonGeorgeMartinMap, null),
                     Arguments.of(nonSortedPratchettAndMartin, singletonPratchettMap, "group",
                                  nonSortedPratchettAndMartin, singletonPratchettMap,
                                  "actual and expected values are maps of different size, actual size=2 when expected size=1"),
                     Arguments.of(sortedMartinAndPratchett, sortedPratchettMap, "group",
                                  sortedMartinAndPratchett, sortedPratchettMap,
                                  "actual and expected values are sorted maps of different size, actual size=2 when expected size=1"),
                     Arguments.of(nonSortedPratchettAndMartin, sortedMartinAndPratchett, "group",
                                  nonSortedPratchettAndMartin, sortedMartinAndPratchett,
                                  "expected field is a sorted map but actual field is not (java.util.LinkedHashMap)"),
                     Arguments.of(singletonMap(pratchett.name, none), singletonPratchettMap, "group",
                                  none, pratchett, null),
                     Arguments.of(singletonPratchettMap, singletonMap(georgeMartin.name, pratchett), "group",
                                  singletonPratchettMap, singletonMap(georgeMartin.name, pratchett), null),
                     Arguments.of(singletonPratchettMap, empty, "group",
                                  singletonPratchettMap, empty,
                                  "actual and expected values are maps of different size, actual size=1 when expected size=0"));
  }

  @ParameterizedTest(name = "authors {0} / object {1} / path {2} / value 1 {3}/ value 2 {4}")
  @MethodSource("mapWithNonMaps")
  public void should_fail_when_comparing_map_to_non_map(Object actualFieldValue, Map<String, Author> expectedFieldValue,
                                                        String path, Object value1, Object value2, String desc) {
    // GIVEN
    WithObject actual = new WithObject(actualFieldValue);
    WithMap<String, Author> expected = new WithMap<>(expectedFieldValue);
    // WHEN
    compareRecursivelyFailsAsExpected(actual, expected);
    // THEN
    ComparisonDifference difference = desc == null ? diff(path, value1, value2) : diff(path, value1, value2, desc);
    verifyShouldBeEqualByComparingFieldByFieldRecursivelyCall(actual, expected, difference);
  }

  static Stream<Arguments> mapWithNonMaps() {
    Author pratchett = new Author("Terry Pratchett");
    Author georgeMartin = new Author("George Martin");
    Author none = null;
    Map<String, Author> mapOfTwoAuthors = mapOf(entry(pratchett.name, pratchett), entry(georgeMartin.name, georgeMartin));
    return Stream.of(Arguments.of(pratchett, mapOfTwoAuthors, "group", pratchett, mapOfTwoAuthors,
                                  "expected field is a map but actual field is not (org.assertj.core.api.recursive.comparison.Author)"),
                     Arguments.of(none, mapOfTwoAuthors, "group", none, mapOfTwoAuthors, null));
  }

  public static class WithMap<K, V> {
    public Map<K, V> group;

    public WithMap(Map<K, V> map) {
      this.group = map;
    }

    @Override
    public String toString() {
      return format("WithMap group=r%s", group);
    }

  }

}
