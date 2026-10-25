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

import static org.assertj.core.api.Assertions.useRepresentation;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenNullPointerException;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.assertj.core.api.throwing.ThrowingSupplier;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class SoftAssertions_assertThatThrownBySupplier_Test {

  private final SoftAssertions softly = new SoftAssertions();

  @ParameterizedTest
  @MethodSource("returned_values")
  void should_collect_failure_with_returned_value(Object value, String valueRepresentation) {
    // GIVEN
    ThrowingSupplier<?> supplier = () -> value;
    // WHEN
    softly.assertThatThrownBySupplier(supplier);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement()
                                           .satisfies(error -> then(error).hasMessage("%nExpecting code to raise a throwable, but it returned %s instead.",
                                                                                      valueRepresentation));
  }

  static Stream<Arguments> returned_values() {
    return Stream.of(arguments(42, "42"),
                     arguments(null, "null"),
                     arguments("hello", "\"hello\""),
                     arguments(new int[] { 1, 2 }, "[1, 2]"));
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void should_collect_failure_and_continue_when_supplier_returns(boolean described) {
    // GIVEN
    ThrowingSupplier<?> supplier = () -> 42;
    // WHEN
    invokeAssertThatThrownBySupplier(supplier, described);
    softly.assertThat(1).isEqualTo(2);
    // THEN
    List<AssertionError> errorsCollected = softly.assertionErrorsCollected();
    then(errorsCollected).hasSize(2);
    then(errorsCollected.get(0)).hasMessageContaining("Expecting code to raise a throwable, but it returned 42 instead.");
    then(errorsCollected.get(1)).hasMessageContainingAll("expected: 2", " but was: 1");
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void should_capture_the_original_throwable(boolean described) {
    // GIVEN
    var expected = new Throwable("boom");
    ThrowingSupplier<?> supplier = () -> {
      throw expected;
    };
    // WHEN
    var throwableAssert = invokeAssertThatThrownBySupplier(supplier, described);
    // THEN
    throwableAssert.isSameAs(expected);
    then(softly.assertionErrorsCollected()).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void should_invoke_supplier_once_when_it_returns(boolean described) {
    // GIVEN
    var calls = new AtomicInteger();
    // WHEN
    invokeAssertThatThrownBySupplier(calls::incrementAndGet, described);
    // THEN
    then(calls).hasValue(1);
    then(softly.assertionErrorsCollected()).singleElement()
                                           .satisfies(error -> then(error).hasMessageContaining("returned 1 instead"));
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void should_invoke_supplier_once_when_it_throws(boolean described) {
    // GIVEN
    var calls = new AtomicInteger();
    ThrowingSupplier<?> supplier = () -> {
      calls.incrementAndGet();
      throw new IOException("boom");
    };
    // WHEN
    var throwableAssert = invokeAssertThatThrownBySupplier(supplier, described);
    // THEN
    then(calls).hasValue(1);
    throwableAssert.isInstanceOf(IOException.class);
    then(softly.assertionErrorsCollected()).isEmpty();
  }

  @Test
  void should_include_formatted_description_when_supplier_returns() {
    // GIVEN
    ThrowingSupplier<?> supplier = () -> 42;
    // WHEN
    softly.assertThatThrownBySupplier(supplier, "computing %s", "answer");
    // THEN
    then(softly.assertionErrorsCollected()).singleElement()
                                           .satisfies(error -> then(error).hasMessage("[computing answer] %nExpecting code to raise a throwable, but it returned 42 instead.".formatted()));
  }

  @Test
  void should_preserve_description_for_chained_assertions() {
    // GIVEN
    ThrowingSupplier<?> supplier = () -> {
      throw new IOException("boom");
    };
    // WHEN
    softly.assertThatThrownBySupplier(supplier, "computing %s", "answer").hasMessage("different");
    // THEN
    then(softly.assertionErrorsCollected()).singleElement()
                                           .satisfies(error -> then(error).hasMessageStartingWith("[computing answer]"));
  }

  @Test
  void should_use_custom_representation_for_returned_value() {
    // GIVEN
    var previousRepresentation = AbstractAssert.customRepresentation;
    useRepresentation(new StandardRepresentation() {
      @Override
      public String toStringOf(Object value) {
        return value instanceof Integer ? "the answer" : super.toStringOf(value);
      }
    });
    try {
      // WHEN
      softly.assertThatThrownBySupplier(() -> 42);
      // THEN
      then(softly.assertionErrorsCollected()).singleElement()
                                             .satisfies(error -> then(error).hasMessage("%nExpecting code to raise a throwable, but it returned the answer instead.".formatted()));
    } finally {
      useRepresentation(previousRepresentation);
    }
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void should_reject_null_supplier(boolean described) {
    // GIVEN
    ThrowingSupplier<?> supplier = null;
    // WHEN/THEN
    thenNullPointerException().isThrownBy(() -> invokeAssertThatThrownBySupplier(supplier, described))
                              .withMessage("The supplier must not be null");
  }

  @Test
  void should_accept_varargs_method_reference() {
    // GIVEN
    int expectedCount = 0;
    // WHEN
    softly.assertThatThrownBySupplier(SoftAssertions_assertThatThrownBySupplier_Test::return_count);
    // THEN
    then(softly.assertionErrorsCollected()).singleElement()
                                           .satisfies(error -> then(error).hasMessage("%nExpecting code to raise a throwable, but it returned %s instead.",
                                                                                      expectedCount));
  }

  private static int return_count(Object... values) {
    return values.length;
  }

  private AbstractThrowableAssert<?, ? extends Throwable> invokeAssertThatThrownBySupplier(ThrowingSupplier<?> supplier,
                                                                                           boolean addDescription) {
    return addDescription
        ? softly.assertThatThrownBySupplier(supplier, "computing %s", "answer")
        : softly.assertThatThrownBySupplier(supplier);
  }
}
