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
import static org.assertj.core.api.BDDAssertions.thenThrownBySupplier;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.assertj.core.api.throwing.ThrowingSupplier;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class BDDAssertions_thenThrownBySupplier_Test {

  @ParameterizedTest
  @MethodSource("returned_values")
  void should_fail_with_returned_value(Object value, String valueRepresentation) {
    // GIVEN
    ThrowingSupplier<?> supplier = () -> value;
    // WHEN
    var error = expectAssertionError(() -> thenThrownBySupplier(supplier));
    // THEN
    then(error).hasMessage("%nExpecting code to raise a throwable, but it returned %s instead.", valueRepresentation);
  }

  static Stream<Arguments> returned_values() {
    return Stream.of(arguments(42, "42"),
                     arguments(null, "null"),
                     arguments("hello", "\"hello\""),
                     arguments(new int[] { 1, 2 }, "[1, 2]"));
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
    var throwableAssert = invokeThenThrownBySupplier(supplier, described);
    // THEN
    throwableAssert.isSameAs(expected);
  }

  @ParameterizedTest
  @ValueSource(booleans = { false, true })
  void should_invoke_supplier_once_when_it_returns(boolean described) {
    // GIVEN
    var calls = new AtomicInteger();
    // WHEN
    var error = expectAssertionError(() -> invokeThenThrownBySupplier(calls::incrementAndGet, described));
    // THEN
    then(calls).hasValue(1);
    then(error).hasMessageContaining("returned 1 instead");
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
    var throwableAssert = invokeThenThrownBySupplier(supplier, described);
    // THEN
    then(calls).hasValue(1);
    throwableAssert.isInstanceOf(IOException.class);
  }

  @Test
  void should_include_formatted_description_when_supplier_returns() {
    // GIVEN
    ThrowingSupplier<?> supplier = () -> 42;
    // WHEN
    var error = expectAssertionError(() -> thenThrownBySupplier(supplier, "computing %s", "answer"));
    // THEN
    then(error).hasMessage("[computing answer] %nExpecting code to raise a throwable, but it returned 42 instead.".formatted());
  }

  @Test
  void should_preserve_description_for_chained_assertions() {
    // GIVEN
    ThrowingSupplier<?> supplier = () -> {
      throw new IOException("boom");
    };
    // WHEN
    var error = expectAssertionError(() -> thenThrownBySupplier(supplier, "computing %s", "answer").hasMessage("different"));
    // THEN
    then(error).hasMessageStartingWith("[computing answer]");
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
      var error = expectAssertionError(() -> thenThrownBySupplier(() -> 42));
      // THEN
      then(error).hasMessage("%nExpecting code to raise a throwable, but it returned the answer instead.".formatted());
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
    thenNullPointerException().isThrownBy(() -> invokeThenThrownBySupplier(supplier, described))
                              .withMessage("The supplier must not be null");
  }

  @Test
  void should_accept_varargs_method_reference() {
    // GIVEN
    int expectedCount = 0;
    // WHEN
    var error = expectAssertionError(() -> thenThrownBySupplier(BDDAssertions_thenThrownBySupplier_Test::return_count));
    // THEN
    then(error).hasMessage("%nExpecting code to raise a throwable, but it returned %s instead.", expectedCount);
  }

  private static int return_count(Object... values) {
    return values.length;
  }

  private static AbstractThrowableAssert<?, ? extends Throwable> invokeThenThrownBySupplier(ThrowingSupplier<?> supplier,
                                                                                            boolean addDescription) {
    return addDescription ? thenThrownBySupplier(supplier, "computing %s", "answer") : thenThrownBySupplier(supplier);
  }
}
