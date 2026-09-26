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
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.Maps.mapOf;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

class RecursiveComparisonAssert_for_maps_Test extends WithComparingFieldsIntrospectionStrategyBaseTest {

  @Test
  // verify we don't need to cast actual to an Object as before when only Object assertions provided
  // usingRecursiveComparison(configuration)
  void should_be_directly_usable_with_maps() {
    // GIVEN
    Person sheldon = new Person("Sheldon");
    Person leonard = new Person("Leonard");
    Person raj = new Person("Rajesh");

    PersonDto sheldonDto = new PersonDto("Sheldon");
    PersonDto leonardDto = new PersonDto("Leonard");
    PersonDto rajDto = new PersonDto("Rajesh");

    Map<String, Person> actual = mapOf(entry(sheldon.name, sheldon),
                                       entry(leonard.name, leonard),
                                       entry(raj.name, raj));
    Map<String, PersonDto> expected = mapOf(entry(sheldonDto.name, sheldonDto),
                                            entry(leonardDto.name, leonardDto),
                                            entry(rajDto.name, rajDto));
    // WHEN/THEN no need to cast actual to an Object as before (since only object assertions provided
    // usingRecursiveComparison(configuration)
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .ignoringAllOverriddenEquals()
                .isEqualTo(expected);
  }

  @Test
  public void should_honor_ignored_fields() {
    // GIVEN
    Map<String, Object> mapA = ImmutableMap.of("foo", "bar", "description", "foobar", "submap",
                                               ImmutableMap.of("subFoo", "subBar", "description", "subFooBar"));
    Map<String, Object> mapB = ImmutableMap.of("foo", "bar", "description", "barfoo", "submap",
                                               ImmutableMap.of("subFoo", "subBar", "description", "subBarFoo"));
    // THEN
    then(mapA).usingRecursiveComparison(recursiveComparisonConfiguration)
              .ignoringFields("description", "submap.description")
              .isEqualTo(mapB);
    then(mapA).usingRecursiveComparison(recursiveComparisonConfiguration)
              .ignoringFieldsMatchingRegexes(".*description")
              .isEqualTo(mapB);
  }

  @Test
  public void should_honor_ignored_fields_with_sorted_maps() {
    // GIVEN
    Map<String, Object> mapA = ImmutableSortedMap.of("foo", "bar", "description", "foobar", "submap",
                                                     ImmutableSortedMap.of("subFoo", "subBar", "description", "subFooBar"));
    Map<String, Object> mapB = ImmutableSortedMap.of("foo", "bar", "description", "barfoo", "submap",
                                                     ImmutableSortedMap.of("subFoo", "subBar", "description", "subBarFoo"));
    // THEN
    then(mapA).usingRecursiveComparison(recursiveComparisonConfiguration)
              .ignoringFields("description", "submap.description")
              .isEqualTo(mapB);
    then(mapA).usingRecursiveComparison(recursiveComparisonConfiguration)
              .ignoringFieldsMatchingRegexes(".*description")
              .isEqualTo(mapB);
  }

  @Test
  public void should_report_missing_keys_as_missing_fields() {
    // GIVEN
    Map<String, Object> mapA = ImmutableSortedMap.of("foo", "bar", "desc", "foobar", "submap",
                                                     ImmutableSortedMap.of("subFoo", "subBar", "description", "subFooBar"));
    Map<String, Object> mapB = ImmutableSortedMap.of("fu", "bar", "description", "foobar", "submap",
                                                     ImmutableSortedMap.of("subFu", "subBar", "description", "subFuBar"));
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(mapA).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                    .isEqualTo(mapB));
    // THEN
    then(assertionError).hasMessageContainingAll(format("map key difference:%n"
                                                        + "- actual key  : \"foo\"%n"
                                                        + "- expected key: \"fu\""),
                                                 format("map key difference:%n"
                                                        + "- actual key  : \"desc\"%n"
                                                        + "- expected key: \"description\""),
                                                 format("map key difference:%n"
                                                        + "- actual key  : \"subFoo\"%n"
                                                        + "- expected key: \"subFu\""),
                                                 format("field/property 'submap.description' differ:%n"
                                                        + "- actual value  : \"subFooBar\"%n"
                                                        + "- expected value: \"subFuBar\"")

    );
  }

  @Test
  void should_compare_map_keys_recursively() {
    // GIVEN
    Map<Key, String> actual = Map.of(new Key("Sam", 1), "value");
    Map<KeyDto, String> expected = Map.of(new KeyDto("Sam", 1), "value");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
  }

  @Test
  void should_honor_ignored_fields_in_map_keys() {
    // GIVEN
    Map<Key, String> actual = Map.of(new Key("Sam", 1), "value");
    Map<Key, String> expected = Map.of(new Key("Sam", 2), "value");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).ignoringFields("id").isEqualTo(expected);
  }

  @Test
  void should_match_map_keys_and_values_together() {
    // GIVEN
    Map<Key, String> actual = mapOf(entry(new Key("Sam", 1), "red"), entry(new Key("Sam", 2), "blue"));
    Map<Key, String> expected = mapOf(entry(new Key("Sam", 3), "blue"), entry(new Key("Sam", 4), "red"));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).ignoringFields("id").isEqualTo(expected);
  }

  @Test
  void should_reconsider_an_earlier_map_entry_match() {
    // GIVEN
    Map<Key, String> actual = mapOf(entry(new Key("Sam", 1), "value"), entry(new Key("Sam", 2), "value"));
    Map<Key, String> expected = mapOf(entry(new Key("Sam", 3), "value"), entry(new Key("Sam", 0), "value"));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .withEqualsForType((a, b) -> b.id == 3 || Math.abs(a.id - b.id) <= 1, Key.class)
                .isEqualTo(expected);
  }

  @Test
  void should_not_reuse_a_map_entry() {
    // GIVEN
    Map<Key, String> actual = mapOf(entry(new Key("Sam", 1), "red"), entry(new Key("Sam", 2), "blue"));
    Map<Key, String> expected = mapOf(entry(new Key("Sam", 3), "red"), entry(new Key("Sam", 4), "red"));
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .ignoringFields("id").isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("blue");
  }

  @Test
  void should_honor_a_comparator_for_map_keys() {
    // GIVEN
    Map<String, Integer> actual = Map.of("name", 1);
    Map<String, Integer> expected = Map.of("NAME", 1);
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .withComparatorForType(String.CASE_INSENSITIVE_ORDER, String.class).isEqualTo(expected);
  }

  @Test
  void should_preserve_map_value_field_paths() {
    // GIVEN
    Map<String, Key> actual = Map.of("person", new Key("Sam", 1));
    Map<String, Key> expected = Map.of("person", new Key("Sam", 2));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).ignoringFields("person.id").isEqualTo(expected);
  }

  @Test
  void should_not_swap_map_keys_and_values_when_ignoring_collection_order() {
    // GIVEN
    Map<String, String> actual = Map.of("a", "b");
    Map<String, String> expected = Map.of("b", "a");
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .ignoringCollectionOrder().isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("differ");
  }

  @Test
  void should_compare_self_referencing_map_values() {
    // GIVEN
    Map<Key, Object> actual = new LinkedHashMap<>();
    actual.put(new Key("Sam", 1), actual);
    Map<KeyDto, Object> expected = new LinkedHashMap<>();
    expected.put(new KeyDto("Sam", 1), expected);
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
  }

  @Test
  void should_compare_sorted_map_keys_recursively() {
    // GIVEN
    Map<Key, String> actual = new TreeMap<>(Comparator.comparingInt(key -> key.id));
    actual.put(new Key("Sam", 1), "value");
    Map<KeyDto, String> expected = new TreeMap<>(Comparator.comparingInt(key -> key.id));
    expected.put(new KeyDto("Sam", 1), "value");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
  }

  @Test
  void should_compare_null_map_keys_and_values() {
    // GIVEN
    Map<Object, Object> actual = new LinkedHashMap<>();
    actual.put(null, new Key("Sam", 1));
    actual.put(new Key("Alex", 2), null);
    Map<Object, Object> expected = new LinkedHashMap<>();
    expected.put(new KeyDto("Alex", 2), null);
    expected.put(null, new KeyDto("Sam", 1));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
  }

  @Test
  void should_honor_strict_type_checking_for_map_keys() {
    // GIVEN
    Map<Key, String> actual = Map.of(new Key("Sam", 1), "value");
    Map<KeyDto, String> expected = Map.of(new KeyDto("Sam", 1), "value");
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .withStrictTypeChecking().isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("not found");
  }

  @Test
  void should_preserve_sorted_map_order_when_filtering_ignored_fields() {
    // GIVEN
    Map<String, String> actual = new TreeMap<>();
    actual.put("a", "first");
    actual.put("b", "second");
    actual.put("ignored", "actual");
    Map<String, String> expected = new TreeMap<>(Comparator.reverseOrder());
    expected.put("a", "first");
    expected.put("b", "second");
    expected.put("ignored", "expected");
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .ignoringFields("ignored").isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("map key difference");
  }

  @Test
  void should_report_value_differences_in_self_referencing_maps() {
    // GIVEN
    Map<Object, Object> actual = new LinkedHashMap<>();
    actual.put(new Key("Sam", 1), actual);
    actual.put("name", "actual");
    Map<Object, Object> expected = new LinkedHashMap<>();
    expected.put(new KeyDto("Sam", 1), expected);
    expected.put("name", "expected");
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("field/property 'name' differ");
  }

  @Test
  void should_not_reuse_key_comparison_for_value_at_a_different_path() {
    // GIVEN
    Key actualKey = new Key("Sam", 1);
    Key expectedKey = new Key("Sam", 2);
    Map<Key, Key> actual = Map.of(actualKey, actualKey);
    Map<Key, Key> expected = Map.of(expectedKey, expectedKey);
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .ignoringFields("id").isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("id");
  }

  @Test
  void should_honor_compared_fields_in_root_map_values() {
    // GIVEN
    Map<String, Key> actual = Map.of("person", new Key("Sam", 1));
    Map<String, Key> expected = Map.of("person", new Key("Sam", 2));
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .comparingOnlyFields("person.name").isEqualTo(expected);
  }

  @Test
  void should_not_compare_map_values_when_only_key_types_are_selected() {
    // GIVEN
    Map<Key, String> actual = Map.of(new Key("Sam", 1), "actual");
    Map<Key, String> expected = Map.of(new Key("Sam", 1), "expected");
    // WHEN/THEN
    then(new MapHolder(actual)).usingRecursiveComparison(recursiveComparisonConfiguration)
                               .comparingOnlyFieldsOfTypes(Key.class).isEqualTo(new MapHolder(expected));
  }

  @Test
  void should_report_map_key_differences_when_only_key_types_are_selected() {
    // GIVEN
    Map<Key, String> actual = Map.of(new Key("Sam", 1), "value");
    Map<Key, String> expected = Map.of(new Key("Alex", 1), "value");
    // WHEN
    var error = expectAssertionError(() -> then(new MapHolder(actual)).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                      .comparingOnlyFieldsOfTypes(Key.class)
                                                                      .isEqualTo(new MapHolder(expected)));
    // THEN
    then(error).hasMessageContaining("differ");
  }

  @Test
  void should_compare_fields_of_selected_keys_in_a_root_map() {
    // GIVEN
    Map<Key, String> actual = Map.of(new Key("Sam", 1), "value");
    Map<Key, String> expected = Map.of(new Key("Alex", 1), "value");
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .comparingOnlyFieldsOfTypes(Key.class).isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("differ");
  }

  @Test
  void should_not_compare_sorted_map_values_when_only_key_types_are_selected() {
    // GIVEN
    Map<Key, String> actual = new TreeMap<>(Comparator.comparingInt(key -> key.id));
    actual.put(new Key("Sam", 1), "actual");
    Map<Key, String> expected = new TreeMap<>(Comparator.comparingInt(key -> key.id));
    expected.put(new Key("Sam", 1), "expected");
    // WHEN/THEN
    then(new MapHolder(actual)).usingRecursiveComparison(recursiveComparisonConfiguration)
                               .comparingOnlyFieldsOfTypes(Key.class).isEqualTo(new MapHolder(expected));
  }

  @Test
  void should_not_use_overridden_equals_for_map_keys_by_default() {
    // GIVEN
    Map<IdKey, String> actual = Map.of(new IdKey("Sam", 1), "value");
    Map<IdKey, String> expected = Map.of(new IdKey("Alex", 1), "value");
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("not found");
  }

  @Test
  void should_use_overridden_equals_for_map_keys_when_requested() {
    // GIVEN
    Map<IdKey, String> actual = Map.of(new IdKey("Sam", 1), "value");
    Map<IdKey, String> expected = Map.of(new IdKey("Alex", 1), "value");
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).usingOverriddenEquals().isEqualTo(expected);
  }

  record IdKey(String name, int id) {
    @Override
    public boolean equals(Object other) {
      return other instanceof IdKey key && id == key.id;
    }

    @Override
    public int hashCode() {
      return id;
    }
  }

  @ParameterizedTest
  @CsvSource({ "false,false", "false,true", "true,false", "true,true" })
  void should_report_shared_value_differences_regardless_of_entry_order(boolean ignoredEntryFirst, boolean sorted) {
    // GIVEN
    Key actualValue = new Key("Sam", 1);
    Key expectedValue = new Key("Sam", 2);
    Comparator<String> order = ignoredEntryFirst ? Comparator.naturalOrder() : Comparator.reverseOrder();
    Map<String, Key> actual = sorted ? new TreeMap<>(order) : new LinkedHashMap<>();
    Map<String, Key> expected = sorted ? new TreeMap<>(order) : new LinkedHashMap<>();
    for (String key : ignoredEntryFirst ? new String[] { "a", "b" } : new String[] { "b", "a" }) {
      actual.put(key, actualValue);
      expected.put(key, expectedValue);
    }
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .ignoringFields("a.id").isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("b.id");
  }

  @ParameterizedTest
  @CsvSource({ "false,false,false", "false,false,true", "false,true,false", "false,true,true",
      "true,false,false", "true,false,true", "true,true,false", "true,true,true" })
  void should_not_reuse_sibling_comparisons_for_map_keys(boolean sorted, boolean comparator, boolean shared) {
    // GIVEN
    Key actualKey = new Key("Sam", 1);
    Key expectedKey = new Key("Sam", 2);
    Map<Key, String> actual = sorted ? new TreeMap<>(Comparator.comparingInt(Key::id)) : new LinkedHashMap<>();
    Map<Key, String> expected = sorted ? new TreeMap<>(Comparator.comparingInt(Key::id)) : new LinkedHashMap<>();
    actual.put(actualKey, "value");
    expected.put(expectedKey, "value");
    var actualHolder = new AliasedKeyHolder(actual, shared ? actualKey : new Key("Sam", 1));
    var expectedHolder = new AliasedKeyHolder(expected, shared ? expectedKey : new Key("Sam", 2));
    var assertion = then(actualHolder).usingRecursiveComparison(recursiveComparisonConfiguration);
    if (comparator) assertion.withEqualsForFields((a, b) -> true, "b");
    else assertion.ignoringFields("b.id");
    // WHEN
    var error = expectAssertionError(() -> assertion.isEqualTo(expectedHolder));
    // THEN
    then(error).hasMessageContaining("key");
  }

  @Test
  void should_stop_searching_for_a_complete_match_when_an_entry_cannot_be_matched() {
    // GIVEN
    int size = 30;
    Map<Integer, String> actual = new LinkedHashMap<>();
    Map<Integer, String> expected = new LinkedHashMap<>();
    for (int key = 0; key < size; key++) {
      actual.put(key, "actual");
      expected.put(key, "expected");
    }
    AtomicInteger keyComparisons = new AtomicInteger();
    // WHEN
    var error = expectAssertionError(() -> then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                       .withEqualsForType((Integer a, Integer b) -> {
                                                         keyComparisons.incrementAndGet();
                                                         return a.equals(b);
                                                       }, Integer.class)
                                                       .isEqualTo(expected));
    // THEN
    then(error).hasMessageContaining("differ");
    then(keyComparisons.get()).isLessThanOrEqualTo(3 * size);
  }

  @ParameterizedTest
  @CsvSource({ "false", "true" })
  void should_compare_deeply_nested_maps_without_overflowing_the_stack(boolean sorted) {
    // GIVEN
    Object actual = 1;
    Object expected = 1;
    for (int depth = 0; depth < 1500; depth++) {
      Map<String, Object> actualParent = sorted ? new TreeMap<>(java.util.Comparator.nullsFirst(String::compareTo))
          : new LinkedHashMap<>();
      Map<String, Object> expectedParent = sorted ? new TreeMap<>(java.util.Comparator.nullsFirst(String::compareTo))
          : new LinkedHashMap<>();
      actualParent.put(null, actual);
      expectedParent.put(null, expected);
      actual = actualParent;
      expected = expectedParent;
    }
    // WHEN/THEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration).isEqualTo(expected);
  }

  @ParameterizedTest
  @CsvSource({ "false, false", "false, true", "true, false", "true, true" })
  void should_not_repeatedly_expand_shared_nested_maps(boolean sorted, boolean sharedLeaf) {
    // GIVEN
    Object actual = 1;
    Object expected = 1;
    if (sharedLeaf) {
      Key actualKey = new Key("Sam", 1);
      Key expectedKey = new Key("Sam", 1);
      actual = new AliasedValues(actualKey, actualKey);
      expected = new AliasedValues(expectedKey, expectedKey);
    }
    for (int depth = 0; depth < 14; depth++) {
      Map<String, Object> actualParent = sorted ? new TreeMap<>() : new LinkedHashMap<>();
      Map<String, Object> expectedParent = sorted ? new TreeMap<>() : new LinkedHashMap<>();
      actualParent.put("a", actual);
      actualParent.put("b", actual);
      expectedParent.put("a", expected);
      expectedParent.put("b", expected);
      actual = actualParent;
      expected = expectedParent;
    }
    AtomicInteger leafComparisons = new AtomicInteger();
    // WHEN
    then(actual).usingRecursiveComparison(recursiveComparisonConfiguration)
                .withEqualsForType((Integer a, Integer b) -> {
                  leafComparisons.incrementAndGet();
                  return a.equals(b);
                }, Integer.class)
                .isEqualTo(expected);
    // THEN
    then(leafComparisons.get()).isLessThanOrEqualTo(28);
  }

  record AliasedValues(Key first, Key second) {
  }

  record AliasedKeyHolder(Map<Key, String> a, Key b) {
  }

  record MapHolder(Map<Key, String> map) {
  }

  record Key(String name, int id) {
  }

  record KeyDto(String name, int id) {
  }

}
