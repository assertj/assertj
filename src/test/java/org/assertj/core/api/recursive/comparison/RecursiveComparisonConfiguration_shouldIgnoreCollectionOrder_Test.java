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
 * Copyright 2012-2022 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Arrays.array;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.FieldLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonConfiguration_shouldIgnoreCollectionOrder_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @ParameterizedTest(name = "{0} collection order should be ignored")
  @MethodSource
  void should_ignore_collection_order(FieldLocation fieldLocation) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrder(true);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(fieldLocation);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored", fieldLocation).isTrue();
  }

  private static Stream<FieldLocation> should_ignore_collection_order() {
    return Stream.of(fieldLocation("name"),
                     fieldLocation("name", "first"));
  }

  private static FieldLocation fieldLocation(String... pathElements) {
    return new FieldLocation(list(pathElements));
  }

  @Test
  void should_register_ignore_collection_order_in_fields_without_duplicates() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("foo", "bar", "foo.bar", "bar");
    // WHEN
    Set<String> fields = recursiveComparisonConfiguration.getIgnoredCollectionOrderInFields();
    // THEN
    assertThat(fields).containsExactly("foo", "bar", "foo.bar");
  }

  @ParameterizedTest(name = "{0} collection order should be ignored with these fields {1}")
  @MethodSource
  void should_ignore_collection_order_in_specified_fields(FieldLocation fieldLocation, String[] ignoredFields) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields(ignoredFields);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(fieldLocation);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored with these fields %s", fieldLocation, ignoredFields)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_specified_fields() {
    return Stream.of(arguments(fieldLocation("name"), array("name")),
                     arguments(fieldLocation("name"), array("foo", "name", "foo")),
                     arguments(fieldLocation("name", "first"), array("name.first")),
                     arguments(fieldLocation("father", "name", "first"), array("father", "name.first", "father.name.first")));
  }

  @Test
  void should_register_ignore_collection_order_in_fields_matching_regexes_without_replacing_previous() {
    // WHEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes("foo");
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes("bar", "baz");
    // THEN
    List<Pattern> regexes = recursiveComparisonConfiguration.getIgnoredCollectionOrderInFieldsMatchingRegexes();
    assertThat(regexes).extracting(Pattern::pattern)
                       .containsExactlyInAnyOrder("foo", "bar", "baz");
  }

  @ParameterizedTest(name = "{0} collection order should be ignored with these regexes {1}")
  @MethodSource
  void should_ignore_collection_order_in_fields_matching_specified_regexes(FieldLocation fieldLocation, String[] regexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(regexes);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(fieldLocation);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored with these regexes %s", fieldLocation, regexes)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_fields_matching_specified_regexes() {
    return Stream.of(arguments(fieldLocation("name"), array(".*name")),
                     arguments(fieldLocation("name"), array("foo", "n.m.", "foo")),
                     arguments(fieldLocation("name", "first"), array("name\\.first")),
                     arguments(fieldLocation("name", "first"), array(".*first")),
                     arguments(fieldLocation("name", "first"), array("name.*")),
                     arguments(fieldLocation("father", "name", "first"), array("father", "name.first", "father\\.name\\.first")));
  }

  @ParameterizedTest(name = "{0} collection order should be ignored")
  @MethodSource
  void should_ignore_collection_order_in_fields(FieldLocation fieldLocation) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(".*name");
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("number");
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(fieldLocation);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored", fieldLocation)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_fields() {
    return Stream.of(arguments(fieldLocation("name")),
                     arguments(fieldLocation("number")),
                     arguments(fieldLocation("surname")),
                     arguments(fieldLocation("first", "name")));
  }

}
