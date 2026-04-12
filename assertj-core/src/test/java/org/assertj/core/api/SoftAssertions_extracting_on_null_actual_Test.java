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

import java.util.function.Function;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies that navigation methods in the {@code extracting} family stop the chain
 * instead of propagating {@link RuntimeException} when the source {@code actual} is
 * {@code null} under soft assertions (see assertj/assertj#4213).
 */
class SoftAssertions_extracting_on_null_actual_Test {

  private SoftAssertions softly;

  @BeforeEach
  void setup() {
    softly = new SoftAssertions();
  }

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
    // THEN - only the extracting's internal isNotNull collect errors;
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

  private record Person(String name) {
  }

}
