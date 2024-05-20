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

import static java.util.UUID.randomUUID;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.Lists.list;
import static org.assertj.tests.core.api.recursive.comparison.DualValueUtil.dualValueWithPath;
import static org.assertj.tests.core.api.recursive.comparison.DualValueUtil.randomPath;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.assertj.core.api.recursive.comparison.DualValue;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.assertj.tests.core.api.recursive.data.Person;
import org.assertj.tests.core.testkit.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecursiveComparisonConfiguration_shouldNotEvaluate_Test {

  private RecursiveComparisonConfiguration recursiveComparisonConfiguration;

  @BeforeEach
  void setup() {
    recursiveComparisonConfiguration = new RecursiveComparisonConfiguration();
  }

  @Test
  void should_evaluate_all_fields_when_compared_types_are_specified_as_a_value_not_to_compare_could_have_a_field_to_compare() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFieldsOfTypes(Person.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue("ceo", new Employee()));
    // THEN
    then(ignored).isFalse();
  }

  @ParameterizedTest(name = "{0} should be not be evaluated")
  @MethodSource
  void should_not_evaluate_actual_null_fields(DualValue dualValue) {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualNullFields(true);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).as("%s should not be evaluated", dualValue).isTrue();
  }

  private static Stream<Arguments> should_not_evaluate_actual_null_fields() {
    return Stream.of(arguments(dualValue(null, "John")),
                     arguments(dualValue(null, 123)),
                     arguments(dualValue(null, null)),
                     arguments(dualValue(null, new Date())));
  }

  @ParameterizedTest(name = "{0} should not be evaluated")
  @MethodSource
  void should_not_evaluate_actual_optional_empty_fields(DualValue dualValue) {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllActualEmptyOptionalFields(true);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).as("%s should not be evaluated", dualValue).isTrue();
  }

  private static Stream<Arguments> should_not_evaluate_actual_optional_empty_fields() {
    return Stream.of(arguments(dualValue(Optional.empty(), "John")),
                     arguments(dualValue(Optional.empty(), Optional.of("John"))),
                     arguments(dualValue(OptionalInt.empty(), OptionalInt.of(123))),
                     arguments(dualValue(OptionalLong.empty(), OptionalLong.of(123L))),
                     arguments(dualValue(OptionalDouble.empty(), OptionalDouble.of(123.0))));
  }

  @ParameterizedTest(name = "{0} should not be evaluated")
  @MethodSource
  void should_not_evaluate_expected_null_fields(DualValue dualValue) {
    // GIVEN
    recursiveComparisonConfiguration.setIgnoreAllExpectedNullFields(true);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).as("%s should not be evaluated", dualValue).isTrue();
  }

  private static Stream<Arguments> should_not_evaluate_expected_null_fields() {
    return Stream.of(arguments(dualValue("John", null)),
                     arguments(dualValue(123, null)),
                     arguments(dualValue(null, null)),
                     arguments(dualValue(new Date(), null)));
  }

  @ParameterizedTest(name = "{0} should be ignored with these ignored fields {1}")
  @MethodSource
  void should_not_evaluate_specified_fields(DualValue dualValue, List<String> ignoredFields) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFields(ignoredFields.toArray(new String[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).as("%s should be ignored with these ignored fields %s", dualValue, ignoredFields).isTrue();
  }

  private static Stream<Arguments> should_not_evaluate_specified_fields() {
    return Stream.of(arguments(dualValueWithPath("name"), list("name")),
                     arguments(dualValueWithPath("name"), list("foo", "name", "foo")),
                     arguments(dualValueWithPath("name", "first"), list("name.first")),
                     arguments(dualValueWithPath("name", "[2]", "first"), list("name.first")),
                     arguments(dualValueWithPath("[0]", "first"), list("first")),
                     arguments(dualValueWithPath("[1]", "first", "second"), list("first.second")),
                     arguments(dualValueWithPath("father", "name", "first"), list("father", "name.first", "father.name.first")));
  }

  @Test
  void ignoring_fields_with_regex_does_not_replace_previous_regexes() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("foo");
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes("bar", "baz");
    // THEN
    then(recursiveComparisonConfiguration.getIgnoredFieldsRegexes()).extracting(Pattern::pattern)
                                                                    .containsExactlyInAnyOrder("foo", "bar", "baz");
  }

  @ParameterizedTest(name = "{0} should be ignored with these regexes {1}")
  @MethodSource
  void should_not_evaluate_fields_matching_given_regexes(DualValue dualValue, List<String> regexes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(regexes.toArray(new String[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).as("%s should be ignored with these regexes %s", dualValue, regexes).isTrue();
  }

  private static Stream<Arguments> should_not_evaluate_fields_matching_given_regexes() {
    return Stream.of(arguments(dualValueWithPath("name"), list(".*name")),
                     arguments(dualValueWithPath("name"), list("foo", "n.m.", "foo")),
                     arguments(dualValueWithPath("name", "first"), list("name\\.first")),
                     arguments(dualValueWithPath("name", "first"), list(".*first")),
                     arguments(dualValueWithPath("name", "first"), list("name.*")),
                     arguments(dualValueWithPath("name", "[2]", "first"), list("name\\.first")),
                     arguments(dualValueWithPath("[0]", "first"), list("fir.*")),
                     arguments(dualValueWithPath("[1]", "first", "second"), list("f..st\\..*nd")),
                     arguments(dualValueWithPath("father", "name", "first"),
                               list("father", "name.first", "father\\.name\\.first")));
  }

  @ParameterizedTest(name = "{0} should not be evaluated")
  @MethodSource
  void should_not_evaluate_fields(DualValue dualValue) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsMatchingRegexes(".*name");
    recursiveComparisonConfiguration.ignoreFields("number");
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(String.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).as("%s should not be evaluated", dualValue).isTrue();
  }

  private static Stream<Arguments> should_not_evaluate_fields() {
    return Stream.of(arguments(dualValueWithPath("name")),
                     arguments(dualValueWithPath("number")),
                     arguments(dualValueWithPath("surname")),
                     arguments(dualValueWithPath("first", "name")),
                     arguments(new DualValue(randomPath(), "actual", "expected")));
  }

  @Test
  void ignoring_fields_for_types_does_not_replace_previous_ignored_types() {
    // WHEN
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(UUID.class);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(ZonedDateTime.class, String.class);
    // THEN
    then(recursiveComparisonConfiguration.getIgnoredTypes()).containsExactlyInAnyOrder(UUID.class, ZonedDateTime.class,
                                                                                       String.class);
  }

  @ParameterizedTest(name = "{0} should be ignored with these ignored types {1}")
  @MethodSource
  void should_not_evaluate_fields_of_specified_types(DualValue dualValue, List<Class<?>> ignoredTypes) {
    // GIVEN
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(ignoredTypes.toArray(new Class<?>[0]));
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).as("%s should be ignored with these ignored types %s", dualValue, ignoredTypes)
                 .isTrue();
  }

  private static Stream<Arguments> should_not_evaluate_fields_of_specified_types() {
    return Stream.of(arguments(new DualValue(randomPath(), "actual", "expected"), list(String.class)),
                     arguments(new DualValue(randomPath(), randomUUID(), randomUUID()), list(String.class, UUID.class)));
  }

  @Test
  void should_evaluate_field_if_its_type_is_not_ignored() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), "actual", "expected");
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(UUID.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isFalse();
  }

  @Test
  void should_be_able_to_ignore_boolean() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), true, false);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(boolean.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @Test
  void should_be_able_to_ignore_byte() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), (byte) 0, (byte) 1);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(byte.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @Test
  void should_be_able_to_ignore_char() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), 'a', 'b');
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(char.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @Test
  void should_be_able_to_ignore_short() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), (short) 123, (short) 123);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(short.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @Test
  void should_be_able_to_ignore_int() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), 123, 123);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(int.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @Test
  void should_be_able_to_ignore_float() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), 123.0f, 123.0f);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(float.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @Test
  void should_be_able_to_ignore_double() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), 123.0, 123.0);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(double.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @ParameterizedTest(name = "{0} should be ignored by specifying to ignore {1}")
  @MethodSource
  void should_be_able_to_ignore_primitive_field_by_specifying_their_wrapper_type(Object fieldValue, Class<?> wrapperType) {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), fieldValue, fieldValue);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(wrapperType);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  private static Stream<Arguments> should_be_able_to_ignore_primitive_field_by_specifying_their_wrapper_type() {
    return Stream.of(arguments(false, Boolean.class),
                     arguments((byte) 0, Byte.class),
                     arguments('b', Character.class),
                     arguments(123, Integer.class),
                     arguments(123.0f, Float.class),
                     arguments(123.0, Double.class),
                     arguments((short) 123, Short.class));
  }

  @Test
  void should_return_false_if_the_field_type_is_subtype_of_an_ignored_type() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), Double.MAX_VALUE, "expected");
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(Number.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isFalse();
  }

  @Test
  void should_not_ignore_actual_null_fields_for_specified_types_if_strictTypeChecking_is_disabled() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), null, "expected");
    recursiveComparisonConfiguration.strictTypeChecking(false);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(String.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isFalse();
  }

  @Test
  void should_not_evaluate_actual_null_fields_for_specified_types_if_strictTypeChecking_is_enabled_and_expected_is_not_null() {
    // GIVEN
    DualValue dualValue = new DualValue(randomPath(), null, "expected");
    recursiveComparisonConfiguration.strictTypeChecking(true);
    recursiveComparisonConfiguration.ignoreFieldsOfTypes(String.class);
    // WHEN
    boolean ignored = recursiveComparisonConfiguration.shouldNotEvaluate(dualValue);
    // THEN
    then(ignored).isTrue();
  }

  @Test
  void should_treat_empty_compared_fields_as_not_restricting_comparison() {
    // GIVEN
    recursiveComparisonConfiguration.compareOnlyFields();
    // WHEN
    boolean shouldBeCompared = !recursiveComparisonConfiguration.shouldNotEvaluate(dualValueWithPath("name"));
    // THEN
    then(shouldBeCompared).isTrue();
  }

  static DualValue dualValue(Object value1, Object value2) {
    return new DualValue(randomPath(), value1, value2);
  }

}
