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

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.entry;
import static org.assertj.core.api.recursive.comparison.FieldLocation.fielLocation;
import static org.assertj.core.internal.TypeComparators.defaultTypeComparators;
import static org.assertj.core.test.AlwaysDifferentComparator.alwaysDifferent;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_STRING;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TIMESTAMP;
import static org.assertj.core.test.AlwaysEqualComparator.ALWAY_EQUALS_TUPLE;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.regex.Pattern;

import org.assertj.core.groups.Tuple;
import org.assertj.core.test.AlwaysDifferentComparator;
import org.assertj.core.test.AlwaysEqualComparator;
import org.junit.jupiter.api.Test;

public class RecursiveComparisonAssert_fluent_API_Test {

  private final static Object ACTUAL = "";

  @Test
  public void usingRecursiveComparison_should_set_a_default_RecursiveComparisonConfiguration() {
    // WHEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                                          .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(recursiveComparisonConfiguration.isInStrictTypeCheckingMode()).isFalse();
    List<Entry<Class<?>, Comparator<?>>> defaultComparators = defaultTypeComparators().comparatorByTypes().collect(toList());
    assertThat(recursiveComparisonConfiguration.comparatorByTypes()).containsExactlyElementsOf(defaultComparators);
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
  public void should_allow_to_enable_strict_mode_comparison() {
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
  public void should_allow_to_use_its_own_RecursiveComparisonConfiguration() {
    // GIVEN
    RecursiveComparisonConfiguration recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison(recursiveComparisonConfiguration)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration).isSameAs(recursiveComparisonConfiguration);
  }

  @Test
  public void should_allow_to_ignore_all_actual_null_fields() {
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringActualNullFields()
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoreAllActualNullFields()).isTrue();
  }

  @Test
  public void should_allow_to_ignore_all_actual_empty_optional_fields() {
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringActualEmptyOptionalFields()
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoreAllActualEmptyOptionalFields()).isTrue();
  }

  @Test
  public void should_allow_to_ignore_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringFields(field1, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredFields()).containsExactly(fielLocation(field1), fielLocation(field2));
  }

  @Test
  public void should_allow_to_ignore_fields_matching_regexes() {
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
  public void should_allow_to_ignore_fields_of_the_given_types() {
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
  public void should_allow_to_ignore_overridden_equals_for_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringOverriddenEqualsForFields(field1, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredOverriddenEqualsForFields()).containsExactly(fielLocation(field1), fielLocation(field2));
  }

  @Test
  public void should_allow_to_ignore_overridden_equals_by_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringOverriddenEqualsForFieldsMatchingRegexes(regex1, regex2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                 .containsExactly(regex1, regex2);
  }

  @Test
  public void should_allow_to_ignore_overridden_equals_for_types() {
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
  public void should_allow_to_ignore_collection_order() {
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringCollectionOrder()
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoreCollectionOrder()).isTrue();
  }

  @Test
  public void should_allow_to_ignore_collection_order_in_fields() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringCollectionOrderInFields(field1, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredCollectionOrderInFields()).containsExactly(fielLocation(field1), fielLocation(field2));
  }

  @Test
  public void should_allow_to_ignore_collection_order_in_fields_matching_regexes() {
    // GIVEN
    String regex1 = "foo";
    String regex2 = ".*foo.*";
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .ignoringCollectionOrderInFieldsMatchingRegexes(regex1, regex2)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getIgnoredCollectionOrderInFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                                .containsExactly(regex1, regex2);
  }

  @Test
  public void should_allow_to_register_field_comparators() {
    // GIVEN
    String field1 = "foo";
    String field2 = "foo.bar";
    String field3 = "bar";
    AlwaysEqualComparator<?> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<?> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    // @format:off
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForFields(alwaysEqualComparator, field1, field3)
                                                                       .withComparatorForFields(alwaysDifferentComparator, field2)
                                                                       .getRecursiveComparisonConfiguration();
    // @format:on
    // THEN
    assertThat(configuration.comparatorByFields()).containsExactly(entry(fielLocation(field3), alwaysEqualComparator),
                                                                   entry(fielLocation(field1), alwaysEqualComparator),
                                                                   entry(fielLocation(field2), alwaysDifferentComparator));
  }

  @Test
  public void should_allow_to_register_type_comparators() {
    // GIVEN
    Class<String> type1 = String.class;
    Class<Timestamp> type2 = Timestamp.class;
    Class<Tuple> type3 = Tuple.class;
    // WHEN
    // @format:off
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForType(ALWAY_EQUALS_STRING, type1)
                                                                       .withComparatorForType(ALWAY_EQUALS_TIMESTAMP, type2)
                                                                       .withComparatorForType(ALWAY_EQUALS_TUPLE, type3)
                                                                       .getRecursiveComparisonConfiguration();
    // @format:on
    // THEN
    assertThat(configuration.comparatorByTypes()).contains(entry(type1, ALWAY_EQUALS_STRING),
                                                           entry(type2, ALWAY_EQUALS_TIMESTAMP),
                                                           entry(type3, ALWAY_EQUALS_TUPLE));
  }

  @Test
  public void should_allow_to_override_field_comparator() {
    // GIVEN
    String field = "foo.bar";
    AlwaysEqualComparator<?> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<?> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForFields(alwaysEqualComparator, field)
                                                                       .withComparatorForFields(alwaysDifferentComparator, field)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getComparatorForField(field)).isSameAs(alwaysDifferentComparator);
  }

  @Test
  public void should_allow_to_override_type_comparator() {
    // GIVEN
    Class<?> type = String.class;
    AlwaysEqualComparator<Object> alwaysEqualComparator = alwaysEqual();
    AlwaysDifferentComparator<Object> alwaysDifferentComparator = alwaysDifferent();
    // WHEN
    RecursiveComparisonConfiguration configuration = assertThat(ACTUAL).usingRecursiveComparison()
                                                                       .withComparatorForType(alwaysEqualComparator, type)
                                                                       .withComparatorForType(alwaysDifferentComparator, type)
                                                                       .getRecursiveComparisonConfiguration();
    // THEN
    assertThat(configuration.getComparatorForType(type)).isSameAs(alwaysDifferentComparator);
  }

}
