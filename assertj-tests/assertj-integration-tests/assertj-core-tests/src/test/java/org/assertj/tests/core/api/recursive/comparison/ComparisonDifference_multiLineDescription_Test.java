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
import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newLinkedHashSet;
import static org.assertj.core.util.Sets.newTreeSet;
import static org.assertj.tests.core.testkit.Maps.mapOf;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.ComparisonDifference;
import org.assertj.core.api.recursive.comparison.DualValue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ComparisonDifference_multiLineDescription_Test {

  @Test
  void should_build_a_multiline_description() {
    // GIVEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("a", "b"), "foo", "bar"));
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                      "- actual value  : \"foo\"%n" +
                                                      "- expected value: \"bar\""));
  }

  @Test
  void multiline_description_should_indicate_top_level_objects_difference() {
    // GIVEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list(), "foo", "bar"));
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("Top level actual and expected objects differ:%n" +
                                                      "- actual value  : \"foo\"%n" +
                                                      "- expected value: \"bar\""));
  }

  @ParameterizedTest(name = "path {0}, index {1}")
  @CsvSource({ "[2],2", "[123],123" })
  void multiline_description_should_indicate_top_level_objects_element_difference(String path, String index) {
    // GIVEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list(path), "foo", "bar"));
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("Top level actual and expected objects element at index %s differ:%n" +
                                                      "- actual value  : \"foo\"%n" +
                                                      "- expected value: \"bar\"", index));
  }

  @ParameterizedTest(name = "path {0}, index {1}")
  @MethodSource
  void multiline_description_should_indicate_element_difference(List<String> path, String index) {
    // GIVEN
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(path, "foo", "bar"));
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).isEqualTo(format("field/property %s differ:%n" +
                                                      "- actual value  : \"foo\"%n" +
                                                      "- expected value: \"bar\"", index));
  }

  private static Stream<Arguments> multiline_description_should_indicate_element_difference() {
    return Stream.of(arguments(list("name", "[2]"), "'name[2]'"),
                     arguments(list("name", "[123]"), "'name[123]'"),
                     arguments(list("name", "[123]", "first", "[456]", "last"), "'name[123].first[456].last'"));
  }

  @Test
  void multiline_description_should_show_sets_type_difference_when_their_content_is_the_same() {
    // GIVEN
    Set<String> actual = newLinkedHashSet("bar", "foo");
    Set<String> expected = newTreeSet("bar", "foo");
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("a", "b"), actual, expected));
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).contains("field/property 'a.b' differ:")
                                    .contains("- actual value  : [\"bar\", \"foo\"] (LinkedHashSet@")
                                    .contains("- expected value: [\"bar\", \"foo\"] (TreeSet@");
  }

  @Test
  void multiline_description_should_show_maps_type_difference_when_their_content_is_the_same() {
    // GIVEN
    Map<Long, Boolean> actual = mapOf(entry(1L, true), entry(2L, false));
    Map<Long, Boolean> expected = new TreeMap<>(actual);
    ComparisonDifference comparisonDifference = new ComparisonDifference(new DualValue(list("a", "b"), actual, expected));
    // WHEN
    String multiLineDescription = comparisonDifference.multiLineDescription();
    // THEN
    assertThat(multiLineDescription).contains("field/property 'a.b' differ:")
                                    .contains("- actual value  : {1L=true, 2L=false} (LinkedHashMap@")
                                    .contains("- expected value: {1L=true, 2L=false} (TreeMap@");
  }

  @Test
  void should_build_comparison_difference_multiline_description_with_additional_information() {
    // GIVEN
    DualValue dualValue = new DualValue(list("a", "b"), "foo", "bar");
    ComparisonDifference com = new ComparisonDifference(dualValue, "additional information");
    // THEN
    assertThat(com.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                            "- actual value  : \"foo\"%n" +
                                                            "- expected value: \"bar\"%n" +
                                                            "additional information"));
  }

  @Test
  void should_build_multiline_description_containing_percent() {
    // GIVEN
    DualValue dualValue = new DualValue(list("a", "b"), "foo%", "%bar%%");
    ComparisonDifference com = new ComparisonDifference(dualValue, "%additional %information%");
    // THEN
    assertThat(com.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                            "- actual value  : \"foo%%\"%n" +
                                                            "- expected value: \"%%bar%%%%\"%n" +
                                                            "%%additional %%information%%"));
  }
}
