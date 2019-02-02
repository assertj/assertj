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
 * Copyright 2012-2018 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.util.Lists.list;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class RecursiveComparisonConfiguration_shouldIgnoreFields_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  public void should_register_fields_path_to_ignore_without_duplicates() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar", "bar");
    // WHEN
    Set<FieldLocation> fields = recursiveComparisonConfiguration.getIgnoredFields();
    // THEN
    assertThat(fields).containsExactlyInAnyOrder(new FieldLocation("foo"),
                                                 new FieldLocation("bar"),
                                                 new FieldLocation("foo.bar"));
  }

  @ParameterizedTest(name = "{0} should be ignored")
  @MethodSource("ignoringNullFieldsSource")
  public void should_ignore_actual_null_fields(DualKey dualKey) {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnore(dualKey);
    // THEN
    assertThat(ignored).as("%s should be ignored", dualKey).isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringNullFieldsSource() {
    return Stream.of(arguments(dualKey(null, "John")),
                     arguments(dualKey(null, 123)),
                     arguments(dualKey(null, (Object) null)),
                     arguments(dualKey(null, new Date())));

  }

  @ParameterizedTest(name = "{0} should be ignored with these ignored fields {1}")
  @MethodSource("ignoringSpecifiedFieldsSource")
  public void should_ignore_specified_fields(DualKey dualKey, List<String> ignoredFields) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFields(ignoredFields.toArray(new String[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnore(dualKey);
    // THEN
    assertThat(ignored).as("%s should be ignored with these ignored fields %s", dualKey, ignoredFields).isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringSpecifiedFieldsSource() {
    return Stream.of(arguments(dualKeyWithPath("name"), list("name")),
                     arguments(dualKeyWithPath("name"), list("foo", "name", "foo")),
                     arguments(dualKeyWithPath("name", "first"), list("name.first")),
                     arguments(dualKeyWithPath("father", "name", "first"), list("father", "name.first", "father.name.first")));

  }

  @Test
  public void ignoring_fields_with_regex_does_not_replace_previous_regexes() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("foo");
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("bar", "baz");
    // THEN
    assertThat(recursiveComparisonConfiguration.getIgnoredFieldsRegexes()).extracting(Pattern::pattern)
                                                                          .containsExactlyInAnyOrder("foo", "bar", "baz");
  }

  @ParameterizedTest(name = "{0} should be ignored with these regexes {1}")
  @MethodSource("ignoringRegexSpecifiedFieldsSource")
  public void should_ignore_fields_matching_given_regexes(DualKey dualKey, List<String> regexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(regexes.toArray(new String[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldIgnore(dualKey);
    // THEN
    assertThat(ignored).as("%s should be ignored with these regexes %s", dualKey, regexes).isTrue();
  }

  @SuppressWarnings("unused")
  private static Stream<Arguments> ignoringRegexSpecifiedFieldsSource() {
    return Stream.of(arguments(dualKeyWithPath("name"), list(".*name")),
                     arguments(dualKeyWithPath("name"), list("foo", "n.m.", "foo")),
                     arguments(dualKeyWithPath("name", "first"), list("name\\.first")),
                     arguments(dualKeyWithPath("name", "first"), list(".*first")),
                     arguments(dualKeyWithPath("name", "first"), list("name.*")),
                     arguments(dualKeyWithPath("father", "name", "first"),
                               list("father", "name.first", "father\\.name\\.first")));

  }

  private static DualKey dualKeyWithPath(String... pathElements) {
    return new DualKey(list(pathElements), new Object(), new Object());
  }

  private static DualKey dualKey(Object key1, Object key2) {
    return new DualKey(randomPath(), key1, key2);
  }

  private static List<String> randomPath() {
    return list(RandomStringUtils.random(RandomUtils.nextInt(0, 10)));
  }

}
