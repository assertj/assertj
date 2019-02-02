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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.recursive.comparison.FieldLocation.fielLocation;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.test.AlwaysDifferentComparator.alwaysDifferent;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TUPLE;
import static org.assertj.core.util.Lists.list;

import java.util.Comparator;

import org.assertj.core.groups.Tuple;
import org.assertj.core.test.AlwaysEqualComparator;
import org.assertj.core.util.AbsValueComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.common.collect.Multimap;

public class RecursiveComparisonConfiguration_multiLineDescription_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  public void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  public void should_show_that_null_fields_are_ignored() {
    // WHEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- all actual null fields were ignored in the comparison%n"));
  }

  @Test
  public void should_show_that_some_given_fields_are_ignored() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- the following fields were ignored in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_the_regexes_used_to_ignore_fields() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- the fields matching the following regexes were ignored in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_the_ignored_overridden_equals_methods_regexes() {
    // WHEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes("foo", "bar", "foo.bar");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format(
               "- overridden equals methods were used in the comparison, except for:%n" +
               "  - the types matching the following regexes: foo, bar, foo.bar%n"));
    // @format:on
  }

  @Test
  public void should_show_the_ignored_overridden_equals_methods_types() {
    // WHEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(String.class, Multimap.class);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format(
               "- overridden equals methods were used in the comparison, except for:%n" +
               "  - the following types: java.lang.String, com.google.common.collect.Multimap%n"));
    // @format:on
  }

  @Test
  public void should_show_the_ignored_overridden_equals_methods_fields() {
    // WHEN
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("foo", "baz", "foo.baz");
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format(
               "- overridden equals methods were used in the comparison, except for:%n" +
               "  - the following fields: foo, baz, foo.baz%n"));
    // @format:on
  }

  @Test
  public void should_show_the_registered_comparator_by_types_and_the_default_ones() {
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForType(new AbsValueComparator<>(), Integer.class);
    recursiveComparisonConfiguration.registerComparatorForType(AlwaysEqualComparator.ALWAY_EQUALS_TUPLE, Tuple.class);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format(
               "- these types were compared with the following comparators:%n" +
               "  - java.lang.Double -> DoubleComparator[precision=1.0E-15]%n" +
               "  - java.lang.Float -> FloatComparator[precision=1.0E-6]%n" +
               "  - java.lang.Integer -> AbsValueComparator%n" +
               "  - org.assertj.core.groups.Tuple -> AlwaysEqualComparator%n"));
    // @format:on
  }

  @Test
  public void should_show_the_registered_comparator_for_specific_fields_alphabetically() {
    // WHEN
    recursiveComparisonConfiguration.registerComparatorForField(ALWAY_EQUALS_TUPLE, fielLocation("foo"));
    recursiveComparisonConfiguration.registerComparatorForField(alwaysDifferent(), fielLocation("bar"));
    recursiveComparisonConfiguration.registerComparatorForField(new PercentageComparator(), fielLocation("height"));
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format("- these fields were compared with the following comparators:%n" +
                                                     "  - bar -> AlwaysDifferentComparator%n" +
                                                     "  - foo -> AlwaysEqualComparator%n" +
                                                     "  - height -> %%s %% %%%% %%d%n"));
    // @format:on
  }

  @Test
  public void should_show_when_strict_type_checking_is_used() {
    // WHEN
    recursiveComparisonConfiguration.strictTypeChecking(true);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- actual and expected objects and their fields were considered different when of incompatible types (i.e. expected type does not extend actual's type) even if all their fields match, for example a Person instance will never match a PersonDto (call strictTypeChecking(false) to change that behavior).%n"));
  }

  @Test
  public void should_show_when_lenient_type_checking_is_used() {
    // WHEN
    recursiveComparisonConfiguration.strictTypeChecking(false);
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).%n"));
  }

  @Test
  public void should_show_a_complete_multiline_description() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("f.*", ".ba.", "..b%sr..");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes(".*oo", ".ar", "oo.ba");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(String.class, Multimap.class);
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("foo", "baz", "foo.baz");
    recursiveComparisonConfiguration.registerComparatorForType(new AbsValueComparator<>(), Integer.class);
    recursiveComparisonConfiguration.registerComparatorForType(AlwaysEqualComparator.ALWAY_EQUALS_TUPLE, Tuple.class);
    recursiveComparisonConfiguration.registerComparatorForField(ALWAY_EQUALS_TUPLE, fielLocation("foo"));
    recursiveComparisonConfiguration.registerComparatorForField(alwaysDifferent(), fielLocation("bar.baz"));
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).isEqualTo(format(
               "- all actual null fields were ignored in the comparison%n" +
               "- the following fields were ignored in the comparison: foo, bar, foo.bar%n" +
               "- the fields matching the following regexes were ignored in the comparison: f.*, .ba., ..b%%sr..%n"+
               "- overridden equals methods were used in the comparison, except for:%n" +
               "  - the following fields: foo, baz, foo.baz%n" +
               "  - the following types: java.lang.String, com.google.common.collect.Multimap%n" +
               "  - the types matching the following regexes: .*oo, .ar, oo.ba%n" +
               "- these types were compared with the following comparators:%n" +
               "  - java.lang.Double -> DoubleComparator[precision=1.0E-15]%n" +
               "  - java.lang.Float -> FloatComparator[precision=1.0E-6]%n" +
               "  - java.lang.Integer -> AbsValueComparator%n" +
               "  - org.assertj.core.groups.Tuple -> AlwaysEqualComparator%n" +
               "- these fields were compared with the following comparators:%n" +
               "  - bar.baz -> AlwaysDifferentComparator%n" +
               "  - foo -> AlwaysEqualComparator%n" +
               "- field comparators take precedence over type comparators.%n"+
               "- actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).%n"));
    // @format:on
  }

  @Test
  public void should_build_multiline_description_containing_percent() {
    // GIVEN
    ComparisonDifference com = new ComparisonDifference(list("a", "b"), "foo%", "%bar%%", "%additional %information%");

    // THEN
    assertThat(com.multiLineDescription()).isEqualTo(format("field/property 'a.b' differ:%n" +
                                                            "- actual value   : \"foo%%\"%n" +
                                                            "- expected value : \"%%bar%%%%\"%n" +
                                                            "%%additional %%information%%"));
  }

  // just to test the description does not fail when given a comparator with various String.format reserved flags
  private class PercentageComparator implements Comparator<Double> {

    @Override
    public int compare(Double o1, Double o2) {
      return 0;
    }

    @Override
    public String toString() {
      return "%s % %% %d";
    }

  }
}
