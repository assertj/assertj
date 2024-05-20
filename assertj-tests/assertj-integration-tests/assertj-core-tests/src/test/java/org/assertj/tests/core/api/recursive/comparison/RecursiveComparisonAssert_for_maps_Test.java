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
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.tests.core.testkit.Maps.mapOf;
import static org.assertj.tests.core.util.AssertionsUtil.expectAssertionError;

import java.util.Map;

import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.api.recursive.data.PersonDto;
import org.junit.jupiter.api.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;

class RecursiveComparisonAssert_for_maps_Test {

  @Test
  // verify we don't need to cast actual to an Object as before when only Object assertions provided usingRecursiveComparison()
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
    // WHEN/THEN no need to cast actual to an Object as before (since only object assertions provided usingRecursiveComparison()
    then(actual).usingRecursiveComparison()
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
    assertThat(mapA).usingRecursiveComparison()
                    .ignoringFields("description", "submap.description")
                    .isEqualTo(mapB);
    assertThat(mapA).usingRecursiveComparison()
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
    assertThat(mapA).usingRecursiveComparison()
                    .ignoringFields("description", "submap.description")
                    .isEqualTo(mapB);
    assertThat(mapA).usingRecursiveComparison()
                    .ignoringFieldsMatchingRegexes(".*description")
                    .isEqualTo(mapB);
  }

  @Test
  public void should_repor_missing_keys_as_missing_fields() {
    // GIVEN
    Map<String, Object> mapA = ImmutableSortedMap.of("foo", "bar", "desc", "foobar", "submap",
                                                     ImmutableSortedMap.of("subFoo", "subBar", "description", "subFooBar"));
    Map<String, Object> mapB = ImmutableSortedMap.of("fu", "bar", "description", "foobar", "submap",
                                                     ImmutableSortedMap.of("subFu", "subBar", "description", "subFuBar"));
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(mapA).usingRecursiveComparison()
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

}
