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

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;

import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.test.AlwaysEqualComparator;
import org.junit.jupiter.api.Test;

public class RecursiveComparisonConfiguration_builder_Test {

  @Test
  public void should_set_ignoreAllActualNullFields() {
    // GIVEN
    boolean value = RandomUtils.nextBoolean();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoreAllActualNullFields(value)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoreAllActualNullFields()).isEqualTo(value);
  }

  @Test
  public void should_set_ignoreAllActualEmptyOptionalFields() {
    // GIVEN
    boolean value = RandomUtils.nextBoolean();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoreAllActualEmptyOptionalFields(value)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoreAllActualEmptyOptionalFields()).isEqualTo(value);
  }

  @Test
  public void should_set_ignoreAllExpectedNullFields() {
    // GIVEN
    boolean value = RandomUtils.nextBoolean();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoreAllExpectedNullFields(value)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoreAllExpectedNullFields()).isEqualTo(value);
  }

  @Test
  public void should_set_ignoreAllOverriddenEquals() {
    // GIVEN
    boolean value = RandomUtils.nextBoolean();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoreAllOverriddenEquals(value)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoreAllOverriddenEquals()).isEqualTo(value);
  }

  @Test
  public void should_set_ignoreCollectionOrder() {
    // GIVEN
    boolean value = RandomUtils.nextBoolean();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoreCollectionOrder(value)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoreCollectionOrder()).isEqualTo(value);
  }

  @Test
  public void should_set_ignoreCollectionOrderInFields() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredCollectionOrderInFields(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredCollectionOrderInFields()).extracting(FieldLocation::getFieldPath)
                                                           .containsExactly(values);
  }

  @Test
  public void should_set_ignoreCollectionOrderInFieldsMatchingRegexes() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredCollectionOrderInFieldsMatchingRegexes(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredCollectionOrderInFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                          .containsExactly(values);
  }

  @Test
  public void should_set_ignoredFields() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredFields(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredFields()).extracting(FieldLocation::getFieldPath)
                                          .containsExactly(values);
  }

  @Test
  public void should_set_ignoredFieldsRegexes() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredFieldsMatchingRegexes(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredFieldsRegexes()).extracting(Pattern::pattern)
                                                 .containsExactly(values);
  }

  @Test
  public void should_set_ignoredOverriddenEqualsForFields() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredOverriddenEqualsForFields(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredOverriddenEqualsForFields()).extracting(FieldLocation::getFieldPath)
                                                             .containsExactly(values);
  }

  @Test
  public void should_set_ignoredOverriddenEqualsForTypes() {
    // GIVEN
    Class<?>[] values = { String.class, Long.class, int.class };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredOverriddenEqualsForTypes(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredOverriddenEqualsForTypes()).containsExactly(String.class, Long.class, int.class);
  }

  @Test
  public void should_set_ignoredOverriddenEqualsRegexes() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredOverriddenEqualsForFieldsMatchingRegexes(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                            .containsExactly(values);
  }

  @Test
  public void should_set_strictTypeCheckingMode() {
    // GIVEN
    boolean value = RandomUtils.nextBoolean();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withStrictTypeChecking(value)
                                                                                     .build();
    // THEN
    then(configuration.isInStrictTypeCheckingMode()).isEqualTo(value);
  }

  @Test
  public void should_set_ignoredTypes() {
    // GIVEN
    Class<?>[] values = { String.class, Long.class, Object.class, int.class };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredFieldsOfTypes(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredTypes()).containsExactly(String.class, Long.class, Object.class, Integer.class);
  }

  @Test
  public void should_set_comparatorForField() {
    // GIVEN
    String fooLocation = "foo";
    String barLocation = "foo.bar";
    AlwaysEqualComparator<?> alwaysEqualComparator = alwaysEqual();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withComparatorForFields(alwaysEqualComparator,
                                                                                                              fooLocation,
                                                                                                              barLocation)
                                                                                     .build();
    // THEN
    then(configuration.hasComparatorForField(fooLocation)).isTrue();
    then(configuration.getComparatorForField(fooLocation)).isSameAs(alwaysEqualComparator);
    then(configuration.hasComparatorForField(barLocation)).isTrue();
    then(configuration.getComparatorForField(barLocation)).isSameAs(alwaysEqualComparator);
  }

  @Test
  public void should_set_comparatorForType() {
    // GIVEN
    AlwaysEqualComparator<String> alwaysEqualComparator = alwaysEqual();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withComparatorForType(alwaysEqualComparator,
                                                                                                            String.class)
                                                                                     .build();
    // THEN
    then(configuration.hasComparatorForType(String.class)).isTrue();
    then(configuration.getComparatorForType(String.class)).isSameAs(alwaysEqualComparator);
  }
}
