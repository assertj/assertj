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
import static org.assertj.core.api.recursive.comparison.DualValueUtil.dualKeyWithPath;
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

public class RecursiveComparisonConfiguration_shouldIgnoreCollectionOrder_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @ParameterizedTest(name = "{0} collection order should be ignored")
  @MethodSource("should_ignore_collection_order_source")
  public void should_ignore_collection_order(DualValue dualKey) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrder(true);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualKey);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored", dualKey).isTrue();
  }

  private static Stream<DualValue> should_ignore_collection_order_source() {
    return Stream.of(dualKeyWithPath("name"),
                     dualKeyWithPath("name", "first"));
  }

  @Test
  public void should_register_ignore_collection_order_in_fields_without_duplicates() {
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
  public void should_ignore_collection_order_in_specified_fields(DualValue dualKey, String[] ignoredFields) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields(ignoredFields);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualKey);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored with these fields %s", dualKey, ignoredFields)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_specified_fields_source() {
    return Stream.of(arguments(dualKeyWithPath("name"), array("name")),
                     arguments(dualKeyWithPath("name"), array("foo", "name", "foo")),
                     arguments(dualKeyWithPath("name", "first"), array("name.first")),
                     arguments(dualKeyWithPath("father", "name", "first"), array("father", "name.first", "father.name.first")));
  }

  @Test
  public void should_register_ignore_collection_order_in_fields_matching_regexes_without_replacing_previous() {
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
  public void should_ignore_collection_order_in_fields_matching_specified_regexes(DualValue dualKey, String[] regexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(regexes);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualKey);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored with these regexes %s", dualKey, regexes)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_fields_matching_specified_regexes_source() {
    return Stream.of(arguments(dualKeyWithPath("name"), array(".*name")),
                     arguments(dualKeyWithPath("name"), array("foo", "n.m.", "foo")),
                     arguments(dualKeyWithPath("name", "first"), array("name\\.first")),
                     arguments(dualKeyWithPath("name", "first"), array(".*first")),
                     arguments(dualKeyWithPath("name", "first"), array("name.*")),
                     arguments(dualKeyWithPath("father", "name", "first"),
                               array("father", "name.first", "father\\.name\\.first")));
  }

  @ParameterizedTest(name = "{0} collection order should be ignored")
  @MethodSource("should_ignore_collection_order_in_fields_source")
  public void should_ignore_collection_order_in_fields(DualValue dualKey) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes(".*name");
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("number");
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnoreCollectionOrder(dualKey);
    // THEN
    assertThat(ignored).as("%s collection order should be ignored", dualKey)
                       .isTrue();
  }

  private static Stream<Arguments> should_ignore_collection_order_in_fields_source() {
    return Stream.of(arguments(dualKeyWithPath("name")),
                     arguments(dualKeyWithPath("number")),
                     arguments(dualKeyWithPath("surname")),
                     arguments(dualKeyWithPath("first", "name")));
  }

}