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
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.core.groups.Tuple;
import org.assertj.tests.core.testkit.AlwaysDifferentComparator;
import org.assertj.tests.core.testkit.AlwaysEqualComparator;
import org.junit.jupiter.api.Test;

class RecursiveComparisonAssert_fluent_API_Test {

  private final static Object ACTUAL = "";

  @Test
  void usingRecursiveComparison_should_set_a_default_RecursiveComparisonConfiguration() {
    // WHEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                                          .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(recursiveComparisonConfiguration.isInStrictTypeCheckingMode()).isFalse();
    List<Entry<Class<?>, Comparator<?>>> defaultComparators = defaultTypeComparators().comparatorByTypes().collect(toList());
    assertThat(recursiveComparisonConfiguration.getTypeComparators()
                                               .comparatorByTypes()).containsExactlyElementsOf(defaultComparators);
    assertThat(recursiveComparisonConfiguration.comparatorByFields()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoreAllActualNullFields()).isFalse();
    assertThat(recursiveComparisonConfiguration.getIgnoredFields()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredTypes()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredFieldsRegexes()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForFields()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForTypes()).isEmpty();
    assertThat(recursiveComparisonConfiguration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes()).isEmpty();
    assertThat(recursiveComparisonConfiguration.hasCustomComparators()).isTrue();
  }

  @Test
  void should_allow_to_enable_strict_mode_comparison() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                       .withStrictTypeChecking()
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.isInStrictTypeCheckingMode()).isTrue();
  }

  @Test
  void should_allow_to_use_its_own_RecursiveComparisonConfiguration() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration).isSameAs(recursiveComparisonConfiguration);
  }

  @Test
  void should_allow_to_ignore_all_actual_null_fields() {
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringActualNullFields()
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoreAllActualNullFields()).isTrue();
  }

  @Test
  void should_allow_to_ignore_all_actual_empty_optional_fields() {
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringActualEmptyOptionalFields()
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoreAllActualEmptyOptionalFields()).isTrue();
  }

  @Test
  void should_allow_to_ignore_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringFields(field1, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredFields()).containsExactly(field1, field2);
  }

  @Test
  void should_allow_to_ignore_fields_matching_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringFieldsMatchingRegexes(regex1, regex2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredFieldsRegexes()).extracting(Pattern::pattern)
                                                       .containsExactly(regex1, regex2);
  }

  @Test
  void should_allow_to_ignore_fields_of_the_given_types() {
    // GIVEN
    Class<?> type1 = UUID.class;
    Class<?> type2 = String.class;
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringFieldsOfTypes(type1, type2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredTypes()).containsExactly(type1, type2);
  }

  @Test
  void should_allow_to_ignore_fields_whose_type_matched_the_given_regexes() {
    // GIVEN
    String regex1 = "foo.*";
    String regex2 = ".*bar";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringFieldsOfTypesMatchingRegexes(regex1, regex2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    then(configuration.getIgnoredTypesRegexes()).extracting(Pattern::toString).containsExactly(regex1, regex2);
  }

  @Test
  void should_allow_to_ignore_overridden_equals_for_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringOverriddenEqualsForFields(field1, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredOverriddenEqualsForFields()).containsExactly(field1, field2);
  }

  @Test
  void should_allow_to_ignore_overridden_equals_by_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringOverriddenEqualsForFieldsMatchingRegexes(regex1,
                                                                                                                         regex2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                                  .containsExactly(regex1, regex2);
  }

  @Test
  void should_allow_to_ignore_overridden_equals_for_types() {
    // GIVEN
    Class<String> type1 = String.class;
    Class<Date> type2 = Date.class;
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringOverriddenEqualsForTypes(type1, type2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredOverriddenEqualsForTypes()).containsExactly(type1, type2);
  }

  @Test
  void should_allow_to_ignore_collection_order() {
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringCollectionOrder()
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoreCollectionOrder()).isTrue();
  }

  @Test
  void should_allow_to_ignore_collection_order_in_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringCollectionOrderInFields(field1, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredCollectionOrderInFields()).containsExactly(field1, field2);
  }

  @Test
  void should_allow_to_ignore_collection_order_in_fields_matching_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringCollectionOrderInFieldsMatchingRegexes(regex1,
                                                                                                                       regex2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredCollectionOrderInFieldsMatchingRegexes()).extracting(Pattern::pattern)
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
    // @format:off
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForFields(alwaysEqualComparator, field1, field3)
                                                                       .withComparatorForFields(alwaysDifferentComparator, field2)
                                                                       .withEqualsForFields((o1, o2) -> true, field4)
                                                                       .getRecursiveComparisonConfiguration();
    // @format:on
    // THEN
    assertThat(configuration.comparatorByFields()).hasSize(4)
                                                  .contains(entry(field3, alwaysEqualComparator),
                                                            entry(field1, alwaysEqualComparator),
                                                            entry(field2, alwaysDifferentComparator));
    assertThat(configuration.comparatorByFields()).anyMatch(entry -> entry.getKey().equals(field4) && entry.getValue() != null);
  }

  @Test
  void should_allow_to_register_comparators_by_regex_matching_fields() {
    // WHEN
    // @format:off
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withEqualsForFieldsMatchingRegexes(STRING_EQUALS, ".*a", ".*b")
                                                                       .withEqualsForFieldsMatchingRegexes(DOUBLE_EQUALS, ".*d", ".*e")
                                                                       .getRecursiveComparisonConfiguration();
    // @format:on
    // THEN
    FieldComparators fieldComparators = configuration.getFieldComparators();
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
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForType(ALWAYS_EQUALS_STRING, type1)
                                                                       .withComparatorForType(ALWAYS_EQUALS_TIMESTAMP, type2)
                                                                       .withEqualsForType((o1, o2) -> true, type3)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    then(configuration.getTypeComparators().comparatorByTypes()).contains(entry(type1, ALWAYS_EQUALS_STRING),
                                                                          entry(type2, ALWAYS_EQUALS_TIMESTAMP));
    then(configuration.getTypeComparators().comparatorByTypes()).anyMatch(entry -> entry.getKey().equals(type3)
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
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForFields(alwaysEqualComparator, field1)
                                                                       .withComparatorForFields(alwaysDifferentComparator, field1)
                                                                       .withComparatorForFields(alwaysEqualComparator, field2)
                                                                       .withEqualsForFields((o1, o2) -> false, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getComparatorForField(field1)).isSameAs(alwaysDifferentComparator);
    assertThat(configuration.getComparatorForField(field2)).isNotSameAs(alwaysEqualComparator);
  }

  @Test
  void should_allow_to_override_type_comparator() {
    // GIVEN
    Class<?> type1 = String.class;
    Class<?> type2 = Tuple.class;
    AlwaysEqualComparator<Object> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<Object> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForType(alwaysEqualComparator, type1)
                                                                       .withComparatorForType(alwaysDifferentComparator, type1)
                                                                       .withComparatorForType(alwaysEqualComparator, type2)
                                                                       .withEqualsForType((o1, o2) -> false, type2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getComparatorForType(type1)).isSameAs(alwaysDifferentComparator);
    assertThat(configuration.getComparatorForType(type2)).isNotSameAs(alwaysEqualComparator);
  }

}
