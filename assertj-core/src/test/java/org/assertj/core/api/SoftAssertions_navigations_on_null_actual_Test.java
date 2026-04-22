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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
  class Extracting {

    @Test
    void should_not_throw_when_chaining_extracting_by_name_after_failed_isNotNull() {
      // GIVEN / WHEN
      softly.assertThat((Object) null).isNotNull().extracting("foo").isEqualTo("bar");
      // THEN - no RuntimeException, only soft-collected AssertionErrors
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(2); // explicit isNotNull() + extracting's internal isNotNull()
      then(errors).allMatch(e -> e instanceof AssertionError);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errors.get(1)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_noop_chain_after_navigation_on_null_so_subsequent_independent_chains_run() {
      // GIVEN / WHEN
      softly.assertThat((Object) null).isNotNull().extracting("foo").isEqualTo("bar");
      softly.assertThat("THIS IS").isEqualTo("BUG");
      // THEN - the second chain MUST run and add its own error
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(3); // 2 from the first chain + 1 from the second
      then(errors.get(2)).hasMessageContaining("BUG");
    }

    @Test
    void should_not_throw_when_chaining_extracting_by_function_after_failed_isNotNull() {
      // GIVEN
      Function<Object, Object> extractor = Object::toString;
      // WHEN
      softly.assertThat((Object) null).isNotNull().extracting(extractor).isEqualTo("bar");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(2);
      then(errors).allMatch(e -> e instanceof AssertionError);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errors.get(1)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_by_name_without_explicit_isNotNull() {
      // GIVEN / WHEN
      softly.assertThat((Object) null).extracting("foo").isEqualTo("bar");
      // THEN - extracting's internal isNotNull collects one error; downstream is noop
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_cascade_dead_chain_through_multiple_extracting_calls() {
      // GIVEN / WHEN
      softly.assertThat((Object) null)
            .extracting("foo")
            .extracting("bar")
            .isEqualTo("baz");
      // THEN - only the first extracting's internal isNotNull collects an error;
      // the second extracting is on a dead chain and must not add an error or throw.
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_chaining_extracting_with_InstanceOfAssertFactory_after_null() {
      // GIVEN / WHEN
      softly.assertThat((Object) null).isNotNull().extracting("foo", STRING).startsWith("Fro");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).allMatch(e -> e instanceof AssertionError);
      then(errors).hasSize(2);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
      then(errors.get(1)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_properties_on_null() {
      // GIVEN / WHEN
      softly.assertThat((Object) null).extracting("a", "b", "c").contains("x");
      // THEN - extracting's isNotNull collects one error; contains on dead chain noops.
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extracting_multiple_functions_on_null() {
      // GIVEN / WHEN
      softly.assertThat((Object) null).extracting(Object::toString, Object::hashCode).contains("x");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
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
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_first_on_empty_iterable() {
      // GIVEN / WHEN
      softly.assertThat(List.of()).first().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("not to be empty");
    }

    @Test
    void should_not_throw_when_calling_first_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).first(STRING).startsWith("Fro");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_last_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).last(STRING).startsWith("Fro");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_element_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).element(0, STRING).startsWith("Fro");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_singleElement_with_InstanceOfAssertFactory_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).singleElement(STRING).startsWith("Fro");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_last_on_empty_iterable() {
      // GIVEN / WHEN
      softly.assertThat(List.of()).last().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("not to be empty");
    }

    @Test
    void should_not_throw_when_calling_singleElement_on_empty_iterable() {
      // GIVEN / WHEN
      softly.assertThat(List.of()).singleElement().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
    }

    @Test
    void should_not_throw_when_chaining_navigation_on_dead_iterable_chain() {
      // GIVEN / WHEN - elements() returns dead chain; chaining first() on it must not leak
      // NoSuchElementException and must not re-collect a duplicate error
      softly.assertThat((Iterable<String>) null).elements(0, 1).first().isEqualTo("x");
      // THEN - only the initial isNotEmpty error is collected
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_last_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).last().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_element_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).element(0).isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_singleElement_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).singleElement().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_elements_on_null_iterable() {
      // GIVEN / WHEN
      softly.assertThat((Iterable<String>) null).elements(0, 1).containsExactly("foo", "bar");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
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

  }

  @Nested
  class OptionalNavigations {

    @Test
    void should_not_throw_when_calling_get_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).get().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_get_on_empty_optional() {
      // GIVEN / WHEN
      softly.assertThat(Optional.<String> empty()).get().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("to contain a value but it was empty");
    }

    @Test
    void should_not_throw_when_calling_get_with_InstanceOfAssertFactory_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).get(STRING).startsWith("Fro");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_map_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).map(String::length).contains(0);
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_flatMap_on_null_optional() {
      // GIVEN / WHEN
      softly.assertThat((Optional<String>) null).flatMap(s -> Optional.of(s.length())).contains(0);
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_chaining_map_across_changing_element_types() {
      // GIVEN / WHEN — VALUE changes from String -> Integer -> Double across the dead chain,
      // which would expose any CCE if deadChainSameType's (A) myself cast were unsafe here.
      softly.assertThat((Optional<String>) null)
            .map(String::length)
            .map(Integer::doubleValue)
            .contains(0.0);
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class MapNavigations {

    @Test
    void should_not_throw_when_extractingByKey_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingByKey("k").isEqualTo("v");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingByKey_with_InstanceOfAssertFactory_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingByKey("k", STRING).startsWith("hi");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingByKeys_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingByKeys("a", "b").contains("x");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingFromEntries_single_extractor_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingFromEntries(Map.Entry::getKey).contains("x");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_extractingFromEntries_multiple_extractors_on_null_map() {
      // GIVEN / WHEN
      softly.assertThat((Map<String, String>) null).extractingFromEntries(Map.Entry::getKey, Map.Entry::getValue).hasSize(0);
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class ThrowableNavigations {

    @Test
    void should_not_throw_when_calling_cause_on_null_throwable() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).cause().hasMessage("boom");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_cause_on_throwable_with_no_cause() {
      // GIVEN / WHEN
      softly.assertThat(new RuntimeException("boom")).cause().hasMessage("inner");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("cause");
    }

    @Test
    void should_not_throw_when_calling_rootCause_on_null_throwable() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).rootCause().hasMessage("boom");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_rootCause_on_throwable_with_no_root_cause() {
      // GIVEN / WHEN
      softly.assertThat(new RuntimeException("boom")).rootCause().hasMessage("inner");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("cause");
    }

    @Test
    void should_not_throw_when_calling_throwablesChain_on_null_throwable() {
      // GIVEN / WHEN
      softly.assertThat((Throwable) null).throwablesChain().hasSize(3);
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

  }

  @Nested
  class AsStringAndAsHexString {

    @Test
    void should_not_throw_when_calling_asString_on_null_object() {
      // GIVEN / WHEN
      softly.assertThat((Object) null).asString().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asString_on_null_byte_array() {
      // GIVEN / WHEN
      softly.assertThat((byte[]) null).asString().isEqualTo("foo");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
    }

    @Test
    void should_not_throw_when_calling_asHexString_on_null_byte_array() {
      // GIVEN / WHEN
      softly.assertThat((byte[]) null).asHexString().isEqualTo("00");
      // THEN
      List<Throwable> errors = softly.errorsCollected();
      then(errors).hasSize(1);
      then(errors.get(0)).hasMessageContaining("Expecting actual not to be null");
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
    List<Throwable> errors = softly.errorsCollected();
    then(errors).hasSize(2);
    then(errors).allMatch(e -> e instanceof AssertionError);
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
