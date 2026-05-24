/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.core.api;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.assertj.core.api.InstanceOfAssertFactories.THROWABLE;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Verifies that navigation methods stop the chain instead of propagating {@link RuntimeException}
 * when the source {@code actual} is {@code null} under soft assertions (see assertj/assertj#4213).
 * <p>
 * The contract is:
 * <ul>
 *   <li>the navigation's internal null-guard collects one {@link AssertionError},</li>
 *   <li>the navigated assertion is returned as a dead chain so subsequent assertions no-op,</li>
 *   <li>no {@link RuntimeException} leaks out of the soft-assertion block.</li>
 * </ul>
 */
class SoftAssertions_navigations_on_null_actual_Test {

  private SoftAssertions softly;

  @BeforeEach
  void setup() {
    softly = new SoftAssertions();
  }

  @Nested
  class BigDecimalNavigations {

    @Test
    void should_not_throw_when_calling_scale_on_null_BigDecimal() {
      // GIVEN / WHEN
      softly.assertThat((BigDecimal) null).scale().isZero();
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }
  }

  @Nested
  class Extracting {

    @Test
    void should_not_throw_when_chaining_extracting_by_name_after_failed_isNotNull() {
      // GIVEN / WHEN
      softly.assertThatObject(null).isNotNull().extracting("foo").isEqualTo("bar");
      // THEN - no RuntimeException, only soft-collected AssertionErrors
      List<AssertionError> errors = softly.assertionErrorsCollected();
      then(errors).hasSize(2); // explicit isNotNull() + extracting's internal isNotNull()
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errors.get(1)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_noop_chain_after_navigation_on_null_so_subsequent_independent_chains_run() {
      // GIVEN / WHEN
      softly.assertThatObject(null).isNotNull().extracting("foo").isEqualTo("bar");
      softly.assertThat("THIS IS").isEqualTo("BUG");
      // THEN - the second chain MUST run and add its own error
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(3); // 2 from the first chain + 1 from the second
      then(errors.get(2)).hasMessageContaining("BUG");
    }

    @Test
    void should_not_throw_when_chaining_extracting_by_function_after_failed_isNotNull() {
      // WHEN
      softly.assertThatObject(null).isNotNull().extracting(Object::toString).isEqualTo("bar");
      // THEN
      List<AssertionError> errors = softly.assertionErrorsCollected();
      then(errors).hasSize(2);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errors.get(1)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_by_name_without_explicit_isNotNull() {
      // GIVEN / WHEN
      softly.assertThatObject(null).extracting("foo").isEqualTo("bar");
      // THEN - extracting's internal isNotNull collects one error; downstream is noop
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_cascade_dead_chain_through_multiple_extracting_calls() {
      // GIVEN / WHEN
      softly.assertThatObject(null)
            .extracting("foo")
            .extracting("bar")
            .isEqualTo("baz");
      // THEN - only the first extracting's internal isNotNull collects an error;
      // the second extracting is on a dead chain and must not add an error or throw.
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_chaining_extracting_with_InstanceOfAssertFactory_after_null() {
      // GIVEN / WHEN
      softly.assertThatObject(null).isNotNull().extracting("foo", STRING).startsWith("Fro");
      // THEN
      List<AssertionError> errors = softly.assertionErrorsCollected();
      then(errors).hasSize(2);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errors.get(1)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_properties_on_null() {
      // GIVEN / WHEN
      softly.assertThatObject(null).extracting("a", "b", "c").contains("x");
      // THEN - extracting's isNotNull collects one error; contains on dead chain noops.
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_functions_on_null() {
      // GIVEN / WHEN
      softly.assertThatObject(null).extracting(Object::toString, Object::hashCode).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_behave_normally_when_actual_is_not_null() {
      // GIVEN
      Person actual = new Person("Frodo");
      // WHEN
      softly.assertThat(actual).isNotNull().extracting("name").isEqualTo("Frodo");
      // THEN
      then(softly.errorsCollected()).isEmpty();
    }

  }

  @Nested
  class IterableNavigations {

    @Test
    void should_not_throw_when_calling_first_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).first().isEqualTo("foo");
      // THEN - first()'s internal isNotEmpty collects one error; downstream no-ops.
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_first_on_empty_iterable() {
      // GIVEN / WHEN
      softly.assertThat(List.of()).first().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("not to be empty");
    }

    @Test
    void should_not_throw_when_calling_first_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).first(STRING).startsWith("Fro");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_last_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).last(STRING).startsWith("Fro");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_element_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).element(0, STRING).startsWith("Fro");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_singleElement_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).singleElement(STRING).startsWith("Fro");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_last_on_empty_iterable() {
      // GIVEN / WHEN
      softly.assertThat(List.of()).last().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("not to be empty");
    }

    @Test
    void should_not_throw_when_calling_singleElement_on_empty_iterable() {
      // GIVEN / WHEN
      softly.assertThat(List.of()).singleElement().isEqualTo("foo");
      // THEN
      then(softly.assertionErrorsCollected()).hasSize(1);
    }

    @Test
    void should_not_throw_when_chaining_navigation_on_dead_iterable_chain() {
      // GIVEN / WHEN - elements() returns dead chain; chaining first() on it must not leak
      // NoSuchElementException and must not re-collect a duplicate error
      softly.assertThat((Iterable<String>) null).elements(0, 1).first().isEqualTo("x");
      // THEN - only the initial isNotEmpty error is collected
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_last_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).last().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_element_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).element(0).isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_singleElement_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).singleElement().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_elements_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).elements(0, 1).containsExactly("foo", "bar");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_by_property_name_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null).extracting("foo").contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_by_property_name_and_type_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null).extracting("foo", String.class).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_property_names_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null).extracting("foo", "bar").hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingResultOf_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null).extractingResultOf("toString").contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingResultOf_with_type_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null).extractingResultOf("toString", String.class).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_by_function_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).extracting(String::length).contains(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_functions_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).extracting(String::length, String::hashCode).hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_map_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).map(String::length).contains(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_flatExtracting_by_function_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).flatExtracting(List::of).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_flatExtracting_multiple_functions_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).flatExtracting(String::length, String::hashCode).hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_flatExtracting_by_property_name_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null).flatExtracting("foo").contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_flatExtracting_multiple_property_names_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null).flatExtracting("foo", "bar").hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_behave_normally_when_iterable_is_not_null_nor_empty() {
      // GIVEN / WHEN
      softly.assertThat(List.of("Frodo", "Sam")).first().isEqualTo("Frodo");
      softly.assertThat(List.of("Frodo", "Sam")).last().isEqualTo("Sam");
      softly.assertThat(List.of("Frodo", "Sam")).element(1).isEqualTo("Sam");
      // THEN
      then(softly.errorsCollected()).isEmpty();
    }

    @Test
    void should_skip_size_assertions_but_not_iterable_ones_when_iterable_under_test_is_null() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<Object>) null)
            .size().isGreaterThan(1)
            .returnToIterable().describedAs("isEmpty on null iterable").isEmpty();
      // THEN
      List<AssertionError> errorsCollected = softly.assertionErrorsCollected();
      then(errorsCollected.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errorsCollected.get(1)).hasMessageContaining("isEmpty on null iterable");
    }

  }

  @Nested
  class ObjectArrayNavigations {

    @Test
    void should_not_throw_when_extracting_by_property_name_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((Object[]) null).extracting("foo").contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_by_property_name_and_type_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((Object[]) null).extracting("foo", String.class).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_property_names_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((Object[]) null).extracting("foo", "bar").hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_by_function_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((String[]) null).extracting(String::length).contains(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_functions_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((String[]) null).extracting(String::length, String::hashCode).hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_flatExtracting_by_function_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((String[]) null).flatExtracting(List::of).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_flatExtracting_by_property_name_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((Object[]) null).flatExtracting("foo").contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingResultOf_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((Object[]) null).extractingResultOf("toString").contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingResultOf_with_type_on_null_array() {
      // GIVEN / WHEN
      softly.assertThat((Object[]) null).extractingResultOf("toString", String.class).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

  }

  @SuppressWarnings("OptionalAssignedToNull")
  @Nested
  class OptionalNavigations {

    @Test
    void should_not_throw_when_calling_get_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).get().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_get_on_empty_optional() {
      // GIVEN / WHEN
      softly.assertThat(Optional.<String> empty()).get().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("to contain a value but it was empty");
    }

    @Test
    void should_not_throw_when_calling_get_with_InstanceOfAssertFactory_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).get(STRING).startsWith("Fro");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_map_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).map(String::length).contains(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_flatMap_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).flatMap(s -> Optional.of(s.length())).contains(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_chaining_map_across_changing_element_types() {
      // GIVEN / WHEN — VALUE changes from String -> Integer -> Double across the dead chain,
      // which would expose any CCE if deadChain cast were unsafe here.
      softly.assertThat((Optional<String>) null)
            .map(String::length)
            .map(Integer::doubleValue)
            .contains(0.0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }
  }

  @Nested
  class MapNavigations {

    @Test
    void should_not_throw_when_calling_keys_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).keys().contains("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_values_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).values().contains("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_skip_size_assertions_but_not_file_ones_when_map_under_test_is_null() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null)
            .size().isEqualTo(7)
            .returnToMap().describedAs("isNotEmpty on null map").isNotEmpty();
      // THEN
      List<AssertionError> errorsCollected = softly.assertionErrorsCollected();
      then(errorsCollected.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errorsCollected.get(1)).hasMessageContaining("isNotEmpty on null map");
    }

    @Test
    void should_not_throw_when_extractingByKey_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingByKey("k").isEqualTo("v");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingByKey_with_InstanceOfAssertFactory_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingByKey("k", STRING).startsWith("hi");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingByKeys_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingByKeys("a", "b").contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingFromEntries_single_extractor_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingFromEntries(Map.Entry::getKey).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_flatExtracting_by_keys_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).flatExtracting("a", "b").hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingFromEntries_multiple_extractors_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingFromEntries(Map.Entry::getKey, Map.Entry::getValue).hasSize(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class ThrowableNavigations {

    @Test
    void should_not_throw_when_calling_message_on_null_throwable() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).message().isEqualTo("boom");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_cause_on_null_throwable() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).cause().hasMessage("boom");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_cause_on_throwable_with_no_cause() {
      // GIVEN / WHEN
      softly.assertThat(new RuntimeException("boom")).cause().hasMessage("inner");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("cause");
    }

    @Test
    void should_cascade_dead_chain_through_multiple_cause_calls() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).cause().cause().hasMessage("boom");
      // THEN - only the first cause()'s isNotNull collects an error;
      // the second cause() runs on a dead chain and must be skipped (no extra error, no leak).
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_rootCause_on_null_throwable() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).rootCause().hasMessage("boom");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_rootCause_on_throwable_with_no_root_cause() {
      // GIVEN / WHEN
      softly.assertThat(new RuntimeException("boom")).rootCause().hasMessage("inner");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("cause");
    }

    @Test
    void should_not_throw_when_calling_throwablesChain_on_null_throwable() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).throwablesChain().hasSize(3);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class FileNavigation {

    @Test
    void should_not_throw_when_calling_content_on_null_file() {
      // GIVEN / WHEN
      softly.assertThat((java.io.File) null).content().contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_content_with_charset_on_null_file() {
      // GIVEN / WHEN
      softly.assertThat((java.io.File) null).content(java.nio.charset.StandardCharsets.UTF_8).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_binaryContent_on_null_file() {
      // GIVEN / WHEN
      softly.assertThat((java.io.File) null).binaryContent().isEqualTo(new byte[0]);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_skip_size_assertions_but_not_file_ones_when_file_under_test_is_null() {
      // GIVEN / WHEN
      softly.assertThat((java.io.File) null)
            .size().isEqualTo(7)
            .returnToFile().describedAs("hasName on null file").hasName("foo");
      // THEN
      List<AssertionError> errorsCollected = softly.assertionErrorsCollected();
      then(errorsCollected.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errorsCollected.get(1)).hasMessageContaining("hasName on null file");
    }
  }

  @Nested
  class PathNavigation {

    @Test
    void should_not_throw_when_calling_content_on_null_path() {
      // GIVEN / WHEN
      softly.assertThat((java.nio.file.Path) null).content().contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_content_with_charset_on_null_path() {
      // GIVEN / WHEN
      softly.assertThat((java.nio.file.Path) null).content(java.nio.charset.StandardCharsets.UTF_8).contains("x");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_binaryContent_on_null_path() {
      // GIVEN / WHEN
      softly.assertThat((java.nio.file.Path) null).binaryContent().isEqualTo(new byte[0]);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class AsStringAndAsHexString {

    @Test
    void should_not_throw_when_calling_asString_on_null_object() {
      // GIVEN / WHEN
      softly.assertThatObject(null).asString().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asString_on_null_byte_array() {
      // GIVEN / WHEN
      softly.assertThat((byte[]) null).asString().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asHexString_on_null_byte_array() {
      // GIVEN / WHEN
      softly.assertThat((byte[]) null).asHexString().isEqualTo("00");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asString_with_charset_on_null_byte_array() {
      // GIVEN / WHEN
      softly.assertThat((byte[]) null).asString(java.nio.charset.StandardCharsets.UTF_8).isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asBase64Encoded_on_null_byte_array() {
      // GIVEN / WHEN
      softly.assertThat((byte[]) null).asBase64Encoded().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asBase64UrlEncoded_on_null_byte_array() {
      // GIVEN / WHEN
      softly.assertThat((byte[]) null).asBase64UrlEncoded().isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asString_with_charset_on_null_input_stream() {
      // GIVEN / WHEN
      softly.assertThat((java.io.InputStream) null).asString(java.nio.charset.StandardCharsets.UTF_8).isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_cascade_dead_chain_through_asString_and_bytes() {
      // GIVEN / WHEN - cross-type chain: byte[] -> String -> byte[]
      softly.assertThat((byte[]) null).asString().bytes().isEqualTo(new byte[0]);
      // THEN - only the first navigation's isNotNull collects an error;
      // the second navigation runs on a dead chain and must be skipped.
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class MatcherAndIterator {

    @Test
    void should_not_throw_when_calling_group_by_index_on_null_matcher() {
      // GIVEN / WHEN
      softly.assertThat((java.util.regex.Matcher) null).group(1).isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_group_by_name_on_null_matcher() {
      // GIVEN / WHEN
      softly.assertThat((java.util.regex.Matcher) null).group("g1").isEqualTo("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_groups_on_null_matcher() {
      // GIVEN / WHEN
      softly.assertThat((java.util.regex.Matcher) null).groups().contains("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_toIterable_on_null_iterator() {
      // GIVEN / WHEN
      softly.assertThat((java.util.Iterator<String>) null).toIterable().contains("foo");
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class StringNavigations {

    @Test
    void should_not_throw_when_calling_bytes_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).bytes().isEqualTo(new byte[0]);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_bytes_with_charset_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).bytes(java.nio.charset.StandardCharsets.UTF_8).isEqualTo(new byte[0]);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_bytes_with_charset_name_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).bytes("UTF-8").isEqualTo(new byte[0]);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asBase64Decoded_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asBase64Decoded().isEqualTo(new byte[0]);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asBase64UrlDecoded_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asBase64UrlDecoded().isEqualTo(new byte[0]);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asByte_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asByte().isEqualTo((byte) 0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("to be a valid byte");
    }

    @Test
    void should_not_throw_when_calling_asShort_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asShort().isEqualTo((short) 0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("to be a valid short");
    }

    @Test
    void should_not_throw_when_calling_asInt_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asInt().isEqualTo(0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("to be a valid int");
    }

    @Test
    void should_not_throw_when_calling_asLong_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asLong().isEqualTo(0L);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("to be a valid long");
    }

    @Test
    void should_not_throw_when_calling_asFloat_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asFloat().isEqualTo(0.0f);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("to be a valid float");
    }

    @Test
    void should_not_throw_when_calling_asDouble_on_null_string() {
      // GIVEN / WHEN
      softly.assertThat((String) null).asDouble().isEqualTo(0.0);
      // THEN
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("to be a valid double");
    }

    @Test
    void should_cascade_dead_chain_through_bytes_and_asHexString() {
      // GIVEN / WHEN - cross-type chain: String -> byte[] -> String
      softly.assertThat((String) null).bytes().asHexString().isEqualTo("00");
      // THEN - only the first navigation's isNotNull collects an error;
      // the second navigation runs on a dead chain and must be skipped.
      then(softly.errorsCollected()).singleElement(THROWABLE)
                                    .hasMessageContaining("Expecting actual not to be null");
    }

  }

  /**
   * Regression guard for the dead-chain path of both {@link AbstractAssert}
   * {@link AssertFactory}-based {@code extracting} overloads. If the implementation returned
   * {@code (ASSERT) myself} (a "same-type" dead chain), a subclass navigating to a different
   * assert type via its own {@link AssertFactory} would blow up with
   * {@link ClassCastException} at the call site, because {@code myself} is not assignable to the
   * caller-requested {@code ASSERT}.
   */
  @Test
  void extracting_with_factory_returning_different_assert_type_should_not_throw_class_cast() {
    // GIVEN a custom assert whose myself is NOT assignable to StringAssert
    CustomAssert custom = new CustomAssert(null);
    custom.assertionErrorHandler = softly;
    // WHEN extracting via Function factory and by-name factory, both producing StringAssert (incompatible with CustomAssert)
    custom.extractingAsString().startsWith("foo");
    custom.extractingValueAsString().startsWith("bar");
    // THEN only the two isNotNull errors are collected, and no ClassCastException leaks
    List<AssertionError> errors = softly.assertionErrorsCollected();
    then(errors).hasSize(2);
    then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    then(errors.get(1)).hasMessageContaining("Expecting actual not to be null");
  }

  private record Person(String name) {
  }

  private record Box(String value) {
  }

  private static final class CustomAssert extends AbstractAssert<CustomAssert, Box> {
    CustomAssert(Box actual) {
      super(actual, CustomAssert.class);
    }

    StringAssert extractingAsString() {
      AssertFactory<String, StringAssert> factory = StringAssert::new;
      return extracting(Box::value, factory);
    }

    StringAssert extractingValueAsString() {
      AssertFactory<Object, StringAssert> factory = value -> new StringAssert((String) value);
      return extracting("value", factory);
    }
  }

}
