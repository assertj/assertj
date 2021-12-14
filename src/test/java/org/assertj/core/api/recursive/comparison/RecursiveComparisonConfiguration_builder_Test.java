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
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.api.recursive.comparison;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.test.AlwaysEqualComparator.alwaysEqual;
import static org.assertj.core.util.Lists.list;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.test.AlwaysEqualComparator;
import org.junit.jupiter.api.Test;

class RecursiveComparisonConfiguration_builder_Test {

  @Test
  void should_use_same_defaults_as_constructor() {
    // GIVEN
    RecursiveComparisonConfiguration configurationBuiltWithBuilder = RecursiveComparisonConfiguration.builder().build();
    RecursiveComparisonConfiguration configurationBuiltWithConstructor = new RecursiveComparisonConfiguration();
    // WHEN/THEN
    then(configurationBuiltWithBuilder).isEqualTo(configurationBuiltWithConstructor);
  }

  @Test
  void should_set_ignoreAllActualNullFields() {
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
  void should_set_ignoreAllActualEmptyOptionalFields() {
    // GIVEN
    boolean value = RandomUtils.nextBoolean();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoreAllActualEmptyOptionalFields(
                                                                                         value)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoreAllActualEmptyOptionalFields()).isEqualTo(value);
  }

  @Test
  void should_set_ignoreAllExpectedNullFields() {
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
  void should_set_ignoreAllOverriddenEquals() {
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
  void should_set_ignoreCollectionOrder() {
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
  void should_set_ignoreCollectionOrderInFields() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredCollectionOrderInFields(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredCollectionOrderInFields()).containsExactly(values);
  }

  @Test
  void should_set_ignoreCollectionOrderInFieldsMatchingRegexes() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredCollectionOrderInFieldsMatchingRegexes(
                                                                                         values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredCollectionOrderInFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                          .containsExactly(values);
  }

  @Test
  void should_set_comparedFields() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withComparedFields(values)
                                                                                     .build();
    // THEN
    then(configuration.getComparedFields()).containsExactly(values);
  }

  @Test
  void should_set_ignoredFields() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredFields(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredFields()).containsExactly(values);
  }

  @Test
  void should_set_ignoredFieldsRegexes() {
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
  void should_set_ignoredOverriddenEqualsForFields() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredOverriddenEqualsForFields(values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredOverriddenEqualsForFields()).containsExactly(values);
  }

  @Test
  void should_set_ignoredOverriddenEqualsForTypes() {
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
  void should_set_ignoredOverriddenEqualsRegexes() {
    // GIVEN
    String[] values = { "foo", "bar" };
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withIgnoredOverriddenEqualsForFieldsMatchingRegexes(
                                                                                         values)
                                                                                     .build();
    // THEN
    then(configuration.getIgnoredOverriddenEqualsForFieldsMatchingRegexes()).extracting(Pattern::pattern)
                                                                            .containsExactly(values);
  }

  @Test
  void should_set_strictTypeCheckingMode() {
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
  void should_set_ignoredTypes() {
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
  void should_set_comparatorForField() {
    // GIVEN
    String fooLocation = "foo";
    String barLocation = "foo.bar";
    AlwaysEqualComparator<?> alwaysEqualComparator = alwaysEqual();
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withComparatorForFields(
                                                                                         alwaysEqualComparator, fooLocation,
                                                                                         barLocation)
                                                                                     .build();
    // THEN
    then(configuration.hasComparatorForField(fooLocation)).isTrue();
    then(configuration.getComparatorForField(fooLocation)).isSameAs(alwaysEqualComparator);
    then(configuration.hasComparatorForField(barLocation)).isTrue();
    then(configuration.getComparatorForField(barLocation)).isSameAs(alwaysEqualComparator);
  }

  @Test
  void should_throw_NPE_if_given_comparator_for_fields_is_null() {
    // GIVEN
    Comparator<Integer> integerComparator = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> RecursiveComparisonConfiguration.builder()
                                                                               .withComparatorForFields(integerComparator,
                                                                                                        "age"));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("Expecting a non null Comparator");
  }

  @Test
  void should_throw_NPE_if_given_comparator_for_type_is_null() {
    // GIVEN
    Comparator<Integer> integerComparator = null;
    // WHEN
    Throwable throwable = catchThrowable(() -> RecursiveComparisonConfiguration.builder()
                                                                               .withComparatorForType(integerComparator,
                                                                                                      Integer.class));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("Expecting a non null Comparator");
  }

  @Test
  void should_throw_NPE_if_given_BiPredicate_for_type_is_null() {
    // GIVEN
    BiPredicate<String, String> stringEquals = null;
    // WHEN
    Throwable throwable = catchThrowable(
        () -> RecursiveComparisonConfiguration.builder().withEqualsForType(stringEquals, String.class));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("Expecting a non null BiPredicate");
  }

  @Test
  void should_throw_NPE_if_given_BiPredicate_for_fields_is_null() {
    // GIVEN
    BiPredicate<String, String> stringEquals = null;
    // WHEN
    Throwable throwable = catchThrowable(
        () -> RecursiveComparisonConfiguration.builder().withEqualsForFields(stringEquals, "id"));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class)
                   .hasMessage("Expecting a non null BiPredicate");
  }

  @Test
  void should_set_equalsForField() {
    // GIVEN
    String nameLocation = "name";
    String titleLocation = "title";
    BiPredicate<String, String> stringEquals = (String s1, String s2) -> s1.equalsIgnoreCase(s2);
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withEqualsForFields(stringEquals,
                                                                                                          nameLocation,
                                                                                                          titleLocation)
                                                                                     .build();
    // THEN
    then(configuration.hasComparatorForField(nameLocation)).isTrue();
    then(configuration.hasComparatorForField(titleLocation)).isTrue();
  }

  @Test
  void should_set_comparatorForType() {
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

  @Test
  void should_set_equalsForType() {
    // GIVEN
    BiPredicate<String, String> stringEquals = (String s1, String s2) -> s1.equalsIgnoreCase(s2);

    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withEqualsForType(stringEquals,
                                                                                                        String.class)
                                                                                     .build();
    // THEN
    then(configuration.hasComparatorForType(String.class)).isTrue();
  }

  @Test
  void should_set_message_for_field() {
    // GIVEN
    String nameLocation = "name";
    String titleLocation = "title";
    String message = "error message";
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withErrorMessageForFields(message,
                                                                                                                nameLocation,
                                                                                                                titleLocation)
                                                                                     .build();
    // THEN
    then(configuration.hasCustomMessageForField(nameLocation)).isTrue();
    then(configuration.getMessageForField(nameLocation)).isEqualTo(message);

    then(configuration.hasCustomMessageForField(titleLocation)).isTrue();
    then(configuration.getMessageForField(titleLocation)).isEqualTo(message);
  }

  @Test
  void should_set_message_with_arguments_for_field() {
    // GIVEN
    String nameLocation = "name";
    String titleLocation = "title";
    String message = "error message for %s and %s";
    List<Object> args = list("name", "title");
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withErrorMessageForFields(message, args,
                                                                                                                nameLocation,
                                                                                                                titleLocation)
                                                                                     .build();
    // THEN
    then(configuration.hasCustomMessageForField(nameLocation)).isTrue();
    then(configuration.hasCustomMessageForField(titleLocation)).isTrue();

    String expectedMessage = format(message, args.toArray());

    then(configuration.getMessageForField(nameLocation)).isEqualTo(expectedMessage);
    then(configuration.getMessageForField(titleLocation)).isEqualTo(expectedMessage);
  }

  @Test
  void should_throw_NPE_if_arguments_list_for_fields_is_null() {
    // GIVEN
    String nameLocation = "name";
    String titleLocation = "title";
    String message = "error message";
    List<Object> args = null;
    // WHEN
    Throwable throwable = catchThrowable(
        () -> RecursiveComparisonConfiguration.builder().withErrorMessageForFields(message, args, nameLocation, titleLocation));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class).hasMessage("Arguments list must not be null");
  }

  @Test
  void should_set_message_for_type() {
    // GIVEN
    String message = "error message";
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withErrorMessageForType(message,
                                                                                                              String.class)
                                                                                     .build();
    // THEN
    then(configuration.hasCustomMessageForType(String.class)).isTrue();
    then(configuration.getMessageForType(String.class)).isEqualTo(message);
  }

  @Test
  void should_set_message_with_arguments_for_type() {
    // GIVEN
    String message = "error message for %s and %s";
    List<Object> args = list("name", "title");
    // WHEN
    RecursiveComparisonConfiguration configuration = RecursiveComparisonConfiguration.builder()
                                                                                     .withErrorMessageForType(message, args,
                                                                                                              String.class)
                                                                                     .build();
    // THEN
    then(configuration.hasCustomMessageForType(String.class)).isTrue();
    then(configuration.getMessageForType(String.class)).isEqualTo(format(message, args.toArray()));
  }

  @Test
  void should_throw_NPE_if_arguments_list_for_type_is_null() {
    // GIVEN
    String message = "error message";
    List<Object> args = null;
    // WHEN
    Throwable throwable = catchThrowable(
        () -> RecursiveComparisonConfiguration.builder().withErrorMessageForType(message, args, String.class));
    // THEN
    then(throwable).isInstanceOf(NullPointerException.class).hasMessage("Arguments list must not be null");
  }
}
