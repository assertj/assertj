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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.DualValueUtil.dualValueWithPath;
import static org.assertj.core.util.Arrays.array;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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
  @MethodSource("should_ignore_collection_order_source")
  void should_ignore_collection_order(DualValue dualValue) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrder(true);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualValue);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored", dualValue).isTrue();
  }

  private static Stream<DualValue> should_ignore_collection_order_source() {
    return Stream.of(dualValueWithPath("name"),
                     dualValueWithPath("name", "first"));
  }

  @Test
  void should_register_ignore_collection_order_in_fields_without_duplicates() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("foo", "bar", "foo.bar", "bar");
    // WHEN
    Set<FieldLocation> fields = recursiveComparisonConfiguration.getIgnoredCollectionOrderInFields();
    // THEN
    assertThat(fields).containsExactly(new FieldLocation("foo"),
                                       new FieldLocation("bar"),
                                       new FieldLocation("foo.bar"));
  }

  @ParameterizedTest(name = "{0} collection order should be ignored with these fields {1}")
  @MethodSource("should_ignore_collection_order_in_specified_fields_source")
  void should_ignore_collection_order_in_specified_fields(DualValue dualValue, String[] ignoredFields) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields(ignoredFields);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualValue);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored with these fields %s", dualValue, ignoredFields)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_specified_fields_source() {
    return Stream.of(arguments(dualValueWithPath("name"), array("name")),
                     arguments(dualValueWithPath("name"), array("foo", "name", "foo")),
                     arguments(dualValueWithPath("name", "first"), array("name.first")),
                     arguments(dualValueWithPath("father", "name", "first"), array("father", "name.first", "father.name.first")));
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
  @MethodSource("should_ignore_collection_order_in_fields_matching_specified_regexes_source")
  void should_ignore_collection_order_in_fields_matching_specified_regexes(DualValue dualValue, String[] regexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(regexes);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualValue);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored with these regexes %s", dualValue, regexes)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_fields_matching_specified_regexes_source() {
    return Stream.of(arguments(dualValueWithPath("name"), array(".*name")),
                     arguments(dualValueWithPath("name"), array("foo", "n.m.", "foo")),
                     arguments(dualValueWithPath("name", "first"), array("name\\.first")),
                     arguments(dualValueWithPath("name", "first"), array(".*first")),
                     arguments(dualValueWithPath("name", "first"), array("name.*")),
                     arguments(dualValueWithPath("father", "name", "first"),
                               array("father", "name.first", "father\\.name\\.first")));
  }

  @ParameterizedTest(name = "{0} collection order should be ignored")
  @MethodSource("should_ignore_collection_order_in_fields_source")
  void should_ignore_collection_order_in_fields(DualValue dualValue) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(".*name");
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("number");
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualValue);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored", dualValue)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_fields_source() {
    return Stream.of(arguments(dualValueWithPath("name")),
                     arguments(dualValueWithPath("number")),
                     arguments(dualValueWithPath("surname")),
                     arguments(dualValueWithPath("first", "name")));
  }

}
