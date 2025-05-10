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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.api.recursive.comparison.fields;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.tests.core.testkit.AlwaysDifferentComparator.alwaysDifferent;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_STRING;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.ALWAYS_EQUALS_TIMESTAMP;
import static org.assertj.tests.core.testkit.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.tests.core.testkit.BiPredicates.DOUBLE_EQUALS;
import static org.assertj.tests.core.testkit.BiPredicates.STRING_EQUALS;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;

import org.assertj.core.api.recursive.comparison.FieldComparators;
import org.assertj.core.groups.Tuple;
import org.assertj.tests.core.testkit.AlwaysDifferentComparator;
import org.assertj.tests.core.testkit.AlwaysEqualComparator;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_fluent_API_Test extends WithComparingFieldsIntrospectionStrategyBaseTest {

  private final static Object ACTUAL = "";

  @Test
  void usingRecursiveComparison_should_set_a_default_RecursiveComparisonConfiguration() {
    // WHEN
    var configuration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                          .getRecursiveComparisonConfiguration();
    // THEN
    then(configuration.isInStrictTypeCheckingMode()).isFalse();
    List<Entry<Class<?>, Comparator<?>>> defaultComparators = defaultTypeComparators().comparatorByTypes().collect(toList());
    then(configuration.getTypeComparators().comparatorByTypes()).containsExactlyElementsOf(defaultComparators);
    then(configuration.comparatorByFields()).isEmpty();
    then(configuration.getIgnoreAllActualNullFields()).isFalse();
    then(configuration.getIgnoredFields()).isEmpty();
    then(configuration.getIgnoredTypes()).isEmpty();
    then(configuration.getIgnoredFieldsRegexes()).isEmpty();
    then(configuration.getIgnoredOverriddenEqualsForFields()).isEmpty();
    then(configuration.getIgnoredOverriddenEqualsForTypes()).isEmpty();
    then(configuration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes()).isEmpty();
    then(configuration.hasCustomComparators()).isTrue();
  }

  @Test
  void should_allow_to_enable_strict_mode_comparison() {
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .withStrictTypeChecking()
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.isInStrictTypeCheckingMode()).isTrue();
  }

  @Test
  void should_allow_to_use_its_own_RecursiveComparisonConfiguration() {
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration).isSameAs(recursiveComparisonConfiguration);
  }

  @Test
  void should_allow_to_ignore_all_actual_null_fields() {
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringActualNullFields()
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoreAllActualNullFields()).isTrue();
  }

  @Test
  void should_allow_to_ignore_all_actual_empty_optional_fields() {
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringActualEmptyOptionalFields()
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoreAllActualEmptyOptionalFields()).isTrue();
  }

  @Test
  void should_allow_to_ignore_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringFields(field1, field2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredFields()).containsExactly(field1, field2);
  }

  @Test
  void should_allow_to_ignore_fields_matching_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringFieldsMatchingRegexes(regex1, regex2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredFieldsRegexes()).extracting(Pattern::pattern)
                                                        .containsExactly(regex1, regex2);
  }

  @Test
  void should_allow_to_ignore_fields_of_the_given_types() {
    // GIVEN
    Class<?> type1 = UUID.class;
    Class<?> type2 = String.class;
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringFieldsOfTypes(type1, type2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredTypes()).containsExactly(type1, type2);
  }

  @Test
  void should_allow_to_ignore_fields_whose_type_matched_the_given_regexes() {
    // GIVEN
    String regex1 = "foo.*";
    String regex2 = ".*bar";
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringFieldsOfTypesMatchingRegexes(regex1, regex2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredTypesRegexes()).extracting(Pattern::toString).containsExactly(regex1, regex2);
  }

  @Test
  void should_allow_to_ignore_overridden_equals_for_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringOverriddenEqualsForFields(field1, field2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredOverriddenEqualsForFields()).containsExactly(field1, field2);
  }

  @Test
  void should_allow_to_ignore_overridden_equals_by_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringOverriddenEqualsForFieldsMatchingRegexes(regex1,
                                                                                                   regex2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                                   .containsExactly(regex1, regex2);
  }

  @Test
  void should_allow_to_ignore_overridden_equals_for_types() {
    // GIVEN
    Class<String> type1 = String.class;
    Class<Date> type2 = Date.class;
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringOverriddenEqualsForTypes(type1, type2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredOverriddenEqualsForTypes()).containsExactly(type1, type2);
  }

  @Test
  void should_allow_to_ignore_collection_order() {
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringCollectionOrder()
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoreCollectionOrder()).isTrue();
  }

  @Test
  void should_allow_to_ignore_array_order() {
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringArrayOrder()
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.shouldIgnoreArrayOrder()).isTrue();
  }

  @Test
  void should_allow_to_ignore_collection_order_in_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringCollectionOrderInFields(field1, field2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredCollectionOrderInFields()).containsExactly(field1, field2);
  }

  @Test
  void should_allow_to_ignore_collection_order_in_fields_matching_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .ignoringCollectionOrderInFieldsMatchingRegexes(regex1,
                                                                                                 regex2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getIgnoredCollectionOrderInFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                                 .containsExactly(regex1, regex2);
  }

  @Test
  void should_allow_to_register_field_comparators() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    String field3 = "bar";
    String field4 = "baz";
    AlwaysEqualComparator<?> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<?> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .withComparatorForFields(alwaysEqualComparator, field1, field3)
                                                 .withComparatorForFields(alwaysDifferentComparator, field2)
                                                 .withEqualsForFields((o1, o2) -> true, field4)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.comparatorByFields()).hasSize(4)
                                                   .contains(entry(field3, alwaysEqualComparator),
                                                             entry(field1, alwaysEqualComparator),
                                                             entry(field2, alwaysDifferentComparator));
    then(currentConfiguration.comparatorByFields()).anyMatch(entry -> entry.getKey().equals(field4) && entry.getValue() != null);
  }

  @Test
  void should_allow_to_register_comparators_by_regex_matching_fields() {
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .withEqualsForFieldsMatchingRegexes(STRING_EQUALS, ".*a", ".*b")
                                                 .withEqualsForFieldsMatchingRegexes(DOUBLE_EQUALS, ".*d", ".*e")
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    FieldComparators fieldComparators = currentConfiguration.getFieldComparators();
    then(fieldComparators.hasRegexFieldComparators()).isTrue();
    then(fieldComparators.comparatorByRegexFields()).allMatch(entry -> entry.getValue() != null);
    then(fieldComparators.comparatorByRegexFields()).anyMatch(entry -> entry.getKey().toString().equals("[.*a, .*b]"));
    then(fieldComparators.comparatorByRegexFields()).anyMatch(entry -> entry.getKey().toString().equals("[.*d, .*e]"));
  }

  @Test
  void should_allow_to_register_type_comparators() {
    // GIVEN
    Class<String> type1 = String.class;
    Class<Timestamp> type2 = Timestamp.class;
    Class<Tuple> type3 = Tuple.class;
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .withComparatorForType(ALWAYS_EQUALS_STRING, type1)
                                                 .withComparatorForType(ALWAYS_EQUALS_TIMESTAMP, type2)
                                                 .withEqualsForType((o1, o2) -> true, type3)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getTypeComparators().comparatorByTypes()).contains(entry(type1, ALWAYS_EQUALS_STRING),
                                                                                 entry(type2, ALWAYS_EQUALS_TIMESTAMP));
    then(currentConfiguration.getTypeComparators().comparatorByTypes()).anyMatch(entry -> entry.getKey().equals(type3)
                                                                                          && entry.getValue() != null);
  }

  @Test
  void should_allow_to_override_field_comparator() {
    // GIVEN
    String field1 = "foo.bar";
    String field2 = "foo.baz";
    AlwaysEqualComparator<?> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<?> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .withComparatorForFields(alwaysEqualComparator, field1)
                                                 .withComparatorForFields(alwaysDifferentComparator, field1)
                                                 .withComparatorForFields(alwaysEqualComparator, field2)
                                                 .withEqualsForFields((o1, o2) -> false, field2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getComparatorForField(field1)).isSameAs(alwaysDifferentComparator);
    then(currentConfiguration.getComparatorForField(field2)).isNotSameAs(alwaysEqualComparator);
  }

  @Test
  void should_allow_to_override_type_comparator() {
    // GIVEN
    Class<?> type1 = String.class;
    Class<?> type2 = Tuple.class;
    AlwaysEqualComparator<Object> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<Object> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    var currentConfiguration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                 .withComparatorForType(alwaysEqualComparator, type1)
                                                 .withComparatorForType(alwaysDifferentComparator, type1)
                                                 .withComparatorForType(alwaysEqualComparator, type2)
                                                 .withEqualsForType((o1, o2) -> false, type2)
                                                 .getRecursiveComparisonConfiguration();
    // THEN
    then(currentConfiguration.getComparatorForType(type1)).isSameAs(alwaysDifferentComparator);
    then(currentConfiguration.getComparatorForType(type2)).isNotSameAs(alwaysEqualComparator);
  }

}
