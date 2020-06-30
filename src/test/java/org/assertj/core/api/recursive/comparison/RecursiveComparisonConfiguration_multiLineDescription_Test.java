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

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.presentation.StandardRepresentation.STANDARD_REPRESENTATION;
import static org.assertj.core.test.AlwaysDifferentComparator.alwaysDifferent;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TUPLE;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.UUID;

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
  public void should_show_that_actual_null_fields_are_ignored() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- all actual null fields were ignored in the comparison%n"));
  }

  @Test
  public void should_show_that_actual_empty_optional_fields_are_ignored() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualEmptyOptionalFields(true);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains("- all actual empty optional fields were ignored in the comparison (including Optional, OptionalInt, OptionalLong and OptionalDouble)");
  }

  @Test
  public void should_show_that_expected_null_fields_are_ignored() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllExpectedNullFields(true);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- all expected null fields were ignored in the comparison%n"));
  }

  @Test
  public void should_show_that_some_given_fields_are_ignored() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- the following fields were ignored in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_the_regexes_used_to_ignore_fields() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("foo", "bar", "foo.bar");
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- the fields matching the following regexes were ignored in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_that_some_given_types_are_ignored() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(UUID.class, ZonedDateTime.class);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- the following types were ignored in the comparison: java.util.UUID, java.time.ZonedDateTime%n"));
  }

  @Test
  public void should_show_the_ignored_all_overridden_equals_methods_flag() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreAllOverriddenEquals();
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains("- no overridden equals methods were used in the comparison (except for java types)");
  }

  @Test
  public void should_show_the_ignored_all_overridden_equals_methods_flag_and_additional_ones() {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("foo", "bar", "foo.bar");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes(".*oo", ".*ar");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(String.class, Multimap.class);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format("- overridden equals methods were used in the comparison except for:%n" +
                                                     "  - the following fields: foo, bar, foo.bar%n" +
                                                     "  - the following types: java.lang.String, com.google.common.collect.Multimap%n" +
                                                     "  - the types matching the following regexes: .*oo, .*ar%n"));
    // @format:on
  }

  @Test
  public void should_show_the_ignored_overridden_equals_methods_regexes() {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes("foo", "bar", "foo.bar");
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format("- overridden equals methods were used in the comparison except for:%n" +
                                                     "  - the types matching the following regexes: foo, bar, foo.bar%n"));
    // @format:on
  }

  @Test
  public void should_show_the_ignored_overridden_equals_methods_types() {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(String.class, Multimap.class);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format("- overridden equals methods were used in the comparison except for:%n" +
                                                     "  - the following types: java.lang.String, com.google.common.collect.Multimap%n"));
    // @format:on
  }

  @Test
  public void should_not_show_specific_ignored_overridden_equals_methods_when_all_are_ignored() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreAllOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(String.class, Multimap.class);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains("- no overridden equals methods were used in the comparison (except for java types)")
                                    .doesNotContain("java.lang.String", "com.google.common.collect.Multimap");
  }

  @Test
  public void should_show_the_ignored_overridden_equals_methods_fields() {
    // GIVEN
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("foo", "baz", "foo.baz");
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).contains(format(
               "- overridden equals methods were used in the comparison except for:%n" +
               "  - the following fields: foo, baz, foo.baz%n"));
    // @format:on
  }

  @Test
  public void should_show_all_overridden_equals_methods_are_ignored_by_default() {
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains("- no overridden equals methods were used in the comparison (except for java types)");
  }

  @Test
  public void should_show_the_ignored_collection_order() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrder(true);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- collection order was ignored in all fields in the comparison%n"));
  }

  @Test
  public void should_show_the_ignored_collection_order_in_fields() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("foo", "bar", "foo.bar");
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- collection order was ignored in the following fields in the comparison: foo, bar, foo.bar%n"));
  }

  @Test
  public void should_show_the_ignored_collection_order_in_fields_matching_regexes() {
    // GIVEN
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes("f.*", "ba.", "foo.*");
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- collection order was ignored in the fields matching the following regexes in the comparison: f.*, ba., foo.*%n"));
  }

  @Test
  public void should_show_the_registered_comparator_by_types_and_the_default_ones() {
    // GIVEN
    recursiveComparisonConfiguration.registerComparatorForType(new AbsValueComparator<>(), Integer.class);
    recursiveComparisonConfiguration.registerComparatorForType(AlwaysEqualComparator.ALWAY_EQUALS_TUPLE, Tuple.class);
    // WHEN
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
    // GIVEN
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAY_EQUALS_TUPLE, "foo");
    recursiveComparisonConfiguration.registerComparatorForFields(alwaysDifferent(), "bar");
    recursiveComparisonConfiguration.registerComparatorForFields(new PercentageComparator(), "height");
    // WHEN
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
    // GIVEN
    recursiveComparisonConfiguration.strictTypeChecking(true);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- actual and expected objects and their fields were considered different when of incompatible types (i.e. expected type does not extend actual's type) even if all their fields match, for example a Person instance will never match a PersonDto (call strictTypeChecking(false) to change that behavior).%n"));
  }

  @Test
  public void should_show_when_lenient_type_checking_is_used() {
    // GIVEN
    recursiveComparisonConfiguration.strictTypeChecking(false);
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    assertThat(multiLineDescription).contains(format("- actual and expected objects and their fields were compared field by field recursively even if they were not of the same type, this allows for example to compare a Person to a PersonDto (call strictTypeChecking(true) to change that behavior).%n"));
  }

  @Test
  public void should_show_a_complete_multiline_description() {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    recursiveComparisonConfiguration.setIgnoreAllActualEmptyOptionalFields(true);
    recursiveComparisonConfiguration.setIgnoreAllExpectedNullFields(true);
    recursiveComparisonConfiguration.ignoreFields("foo", "bar", "foo.bar");
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("f.*", ".ba.", "..b%sr..");
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(UUID.class, ZonedDateTime.class);
    recursiveComparisonConfiguration.useOverriddenEquals();
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFieldsMatchingRegexes(".*oo", ".ar", "oo.ba");
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForTypes(String.class, Multimap.class);
    recursiveComparisonConfiguration.ignoreOverriddenEqualsForFields("foo", "baz", "foo.baz");
    recursiveComparisonConfiguration.ignoreCollectionOrder(true);
    recursiveComparisonConfiguration.ignoreCollectionOrderInFields("foo", "bar", "foo.bar");
    recursiveComparisonConfiguration.ignoreCollectionOrderInFieldsMatchingRegexes("f.*", "ba.", "foo.*");
    recursiveComparisonConfiguration.registerComparatorForType(new AbsValueComparator<>(), Integer.class);
    recursiveComparisonConfiguration.registerComparatorForType(AlwaysEqualComparator.ALWAY_EQUALS_TUPLE, Tuple.class);
    recursiveComparisonConfiguration.registerComparatorForFields(ALWAY_EQUALS_TUPLE, "foo");
    recursiveComparisonConfiguration.registerComparatorForFields(alwaysDifferent(), "bar.baz");
    // WHEN
    String multiLineDescription = recursiveComparisonConfiguration.multiLineDescription(STANDARD_REPRESENTATION);
    // THEN
    // @format:off
    assertThat(multiLineDescription).isEqualTo(format(
               "- all actual null fields were ignored in the comparison%n" +
               "- all actual empty optional fields were ignored in the comparison (including Optional, OptionalInt, OptionalLong and OptionalDouble)%n" +
               "- all expected null fields were ignored in the comparison%n" +
               "- the following fields were ignored in the comparison: foo, bar, foo.bar%n" +
               "- the fields matching the following regexes were ignored in the comparison: f.*, .ba., ..b%%sr..%n"+
               "- the following types were ignored in the comparison: java.util.UUID, java.time.ZonedDateTime%n" +
               "- overridden equals methods were used in the comparison except for:%n" +
               "  - the following fields: foo, baz, foo.baz%n" +
               "  - the following types: java.lang.String, com.google.common.collect.Multimap%n" +
               "  - the types matching the following regexes: .*oo, .ar, oo.ba%n" +
               "- collection order was ignored in all fields in the comparison%n" +
               "- collection order was ignored in the following fields in the comparison: foo, bar, foo.bar%n" +
               "- collection order was ignored in the fields matching the following regexes in the comparison: f.*, ba., foo.*%n" +
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
